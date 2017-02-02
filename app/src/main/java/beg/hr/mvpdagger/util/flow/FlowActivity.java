package beg.hr.mvpdagger.util.flow;

import com.google.gson.Gson;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import beg.hr.mvpdagger.util.DefaultTransitionsFactory;
import beg.hr.mvpdagger.util.TransitionsFactory;
import flow.Direction;
import flow.Flow;
import flow.History;
import flow.History.Builder;
import flow.KeyChanger;
import flow.State;
import flow.TraversalCallback;

import static flow.Direction.REPLACE;

/** Created by juraj on 19/01/2017. */
public abstract class FlowActivity extends AppCompatActivity implements KeyChanger {

  protected static final Object FLOW_EMPTY_SIGNAL = "flow_empty_signal";
  protected static final Object FLOW_FINISH_SIGNAL = "flow_finish_signal";

  protected BaseDispatcher flowDispatcher;
  private Dialog dialog;
  private TransitionsFactory transitionsFactory;

  protected abstract Object initScreen();

  protected abstract void changeKey(
      @Nullable Object previousKey,
      Object mainKey,
      @Nullable Object dialogKey,
      Direction direction,
      TraversalCallback callback);

  protected TransitionsFactory transitionsFactory() {
    return new DefaultTransitionsFactory();
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    transitionsFactory = transitionsFactory();
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    if (savedInstanceState == null) {
      History history = History.emptyBuilder().push(FLOW_FINISH_SIGNAL).push(initScreen()).build();
      Flow.get(this).setHistory(history, Direction.REPLACE);
    }
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    flowDispatcher = new BaseDispatcher(this, this);
    newBase =
        Flow.configure(newBase, this)
            .dispatcher(flowDispatcher)
            .defaultKey(FLOW_EMPTY_SIGNAL)
            .keyParceler(new GsonParceler(new Gson()))
            .install();
    super.attachBaseContext(newBase);
  }

  @Override
  public void onBackPressed() {
    final View view = getCurrentView();
    if (!BackSupport.onBackPressed(view)) {
      super.onBackPressed();
    }
  }

  @Override
  public void changeKey(
      @Nullable State outgoingState,
      @NonNull State incomingState,
      @NonNull Direction direction,
      @NonNull Map<Object, Context> incomingContexts,
      @NonNull TraversalCallback callback) {
    Object outKey = outgoingState != null ? outgoingState.getKey() : null;
    Object inKey = incomingState.getKey();
    Object mainKey;
    Object dialogKey;

    if (inKey instanceof DialogKey) {
      mainKey = ((DialogKey) inKey).mainContent();
      dialogKey = ((DialogKey) inKey).dialogContent();
    } else {
      mainKey = inKey;
      dialogKey = null;
    }

    if (mainKey.equals(FLOW_EMPTY_SIGNAL)) {
      // this is first empty signal and  it'll be replaced
      TextView view = new TextView(this);
      view.setText("Empty view");
      showMainView(outKey, mainKey, view, direction);
      callback.onTraversalCompleted();
      return;
    }
    if (mainKey.equals(FLOW_FINISH_SIGNAL)) {
      finish();
      return;
    }
    if (mainKey instanceof NewFlowKey) {
      startAnotherFlow(callback, (NewFlowKey) mainKey);
      return;
    }

    dismissOldDialog();
    changeKey(outKey, mainKey, dialogKey, direction, callback);
  }

  private ViewGroup getRootView() {
    return (ViewGroup) findViewById(android.R.id.content);
  }

  @Nullable
  private View getCurrentView() {
    ViewGroup rootView = getRootView();
    int childCount = rootView.getChildCount();
    if (childCount > 0) return rootView.getChildAt(childCount - 1);
    return null;
  }

  private void dismissOldDialog() {
    if (dialog != null) {
      dialog.dismiss();
      dialog = null;
    }
  }

  private Object getMainKey(Object parent) {
    if (parent instanceof DialogKey) return ((DialogKey) parent).mainContent();
    return parent;
  }

  private boolean shouldAnimate(State outgoingState) {
    return outgoingState == null || !isOutgoingStateDialog(outgoingState);
  }

  private boolean isOutgoingStateDialog(State outgoingState) {
    return outgoingState.getKey() instanceof DialogKey;
  }

  private void startAnotherFlow(TraversalCallback callback, NewFlowKey newFlowKey) {
    callback.onTraversalCompleted();
    Builder historyBuilder = Flow.get(this).getHistory().buildUpon();
    historyBuilder.pop();
    Flow.get(this).setHistory(historyBuilder.build(), REPLACE);

    startActivity(newFlowKey.intent());
    overridePendingTransition(newFlowKey.enterAnim(), newFlowKey.exitAnim());
  }

  private void animate(@Nullable Object oldKey, Object key, View view, Direction direction) {
    transitionsFactory.execute(getRootView(), getCurrentView(), view, oldKey, key, direction);
  }

  protected void showMainView(@Nullable Object oldKey, Object key, View view, Direction direction) {
    State outgoingState = flowDispatcher.getOutgoingState();
    if (shouldAnimate(outgoingState)) animate(oldKey, key, view, direction);
  }

  protected void showDialog(Dialog dialog) {
    this.dialog = dialog;
    this.dialog.show();
  }

  protected void mainKeyToDialogKey(Object mainKey, TraversalCallback callback) {
    callback.onTraversalCompleted();
    Builder historyBuilder = Flow.get(this).getHistory().buildUpon();
    historyBuilder.pop();
    Object main = getMainKey(historyBuilder.peek());
    historyBuilder.push(new DialogKey(main, mainKey));
    Flow.get(this).setHistory(historyBuilder.build(), REPLACE);
  }

  protected ViewState viewStateManager(Object key) {
    return new FlowViewState(key, flowDispatcher);
  }
}
