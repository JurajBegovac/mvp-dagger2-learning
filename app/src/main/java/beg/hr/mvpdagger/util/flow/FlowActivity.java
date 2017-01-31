package beg.hr.mvpdagger.util.flow;

import com.google.gson.Gson;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Map;

import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.util.BottomTopActivity;
import flow.Direction;
import flow.Flow;
import flow.History;
import flow.History.Builder;
import flow.KeyChanger;
import flow.State;
import flow.TraversalCallback;

import static flow.Direction.REPLACE;

/** Created by juraj on 19/01/2017. */
public abstract class FlowActivity extends AppCompatActivity
    implements KeyChanger {

  protected static final Object FLOW_EMPTY_SIGNAL = "flow_empty_signal";
  protected static final Object FLOW_FINISH_SIGNAL = "flow_finish_signal";

  protected BaseDispatcher flowDispatcher;

  private Dialog dialog;

  protected abstract Object initScreen();

  protected abstract void changeDialogKey(Object dialogKey);

  protected abstract boolean changeMainKey(
      Object mainKey, Direction direction, TraversalCallback callback);

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
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    if (savedInstanceState == null) {
      History history = History.emptyBuilder().push(FLOW_FINISH_SIGNAL).push(initScreen()).build();
      Flow.get(this).setHistory(history, Direction.REPLACE);
    }
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

    // TODO: 19/01/2017 temporary hack
    boolean shouldComplete = true;

    if (mainKey.equals(FLOW_EMPTY_SIGNAL)) {
      // this is first empty signal and  it'll be replaced
      TextView view = new TextView(this);
      view.setText("Empty view");
      showMainView(view, direction);
    } else if (mainKey.equals(FLOW_FINISH_SIGNAL)) {
      finish();
      return;
    } else {
      // let some other flow implementation handle the key
      shouldComplete = changeMainKey(mainKey, direction, callback);
    }

    if (shouldComplete) {
      dismissOldDialog();
      if (dialogKey != null) {
        changeDialogKey(dialogKey);
      }
      callback.onTraversalCompleted();
    }
  }

  private ViewGroup getRootView() {
    return (ViewGroup) findViewById(android.R.id.content);
  }

  @Nullable
  private View getCurrentView() {
    ViewGroup rootView = getRootView();
    if (rootView.getChildCount() > 0) return rootView.getChildAt(0);
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

  private void removeOldViewIfNeeded() {
    View view = getCurrentView();
    if (view != null) getRootView().removeView(view);
  }

  private boolean shouldAnimate(State outgoingState) {
    return outgoingState == null || !isOutgoingStateDialog(outgoingState);
  }

  private boolean isOutgoingStateDialog(State outgoingState) {
    return outgoingState.getKey() instanceof DialogKey;
  }

  private void animate(View view, @AnimRes int enter, @AnimRes int exit) {
    final View currentView = getCurrentView();
    if (currentView != null) {
      Animation exitAnimation = AnimationUtils.loadAnimation(this, exit);
      currentView.startAnimation(exitAnimation);
    }
    Animation enterAnimation = AnimationUtils.loadAnimation(this, enter);
    view.startAnimation(enterAnimation);
  }

  protected void showMainView(View view, Direction direction) {
    State outgoingState = flowDispatcher.getOutgoingState();
    if (shouldAnimate(outgoingState)) animate(view, direction);
    removeOldViewIfNeeded();
    getRootView().addView(view);
  }

  protected void showDialog(Dialog dialog) {
    this.dialog = dialog;
    this.dialog.show();
  }

  protected void startAnotherFlow(TraversalCallback callback, Parcelable initScreen) {
    callback.onTraversalCompleted();
    Builder historyBuilder = Flow.get(this).getHistory().buildUpon();
    historyBuilder.pop();
    Flow.get(this).setHistory(historyBuilder.build(), REPLACE);

    startActivity(BottomTopActivity.getStartIntent(this, initScreen));
    overridePendingTransition(R.anim.slide_in_bottom, R.anim.nothing);
  }

  protected void mainKeyToDialogKey(Object mainKey, TraversalCallback callback) {
    callback.onTraversalCompleted();
    Builder historyBuilder = Flow.get(this).getHistory().buildUpon();
    historyBuilder.pop();
    Object main = getMainKey(historyBuilder.peek());
    historyBuilder.push(new DialogKey(main, mainKey));
    Flow.get(this).setHistory(historyBuilder.build(), REPLACE);
  }

  protected void animate(View view, Direction direction) {
    switch (direction) {
      case REPLACE:
        // no animation
        break;
      case FORWARD:
        animate(view, R.anim.slide_in_right, R.anim.slide_out_left);
        break;
      case BACKWARD:
        animate(view, R.anim.slide_in_left, R.anim.slide_out_right);
        break;
      default:
        // noop
    }
  }

  protected ViewStateManager viewStateManager(Object key) {
    return new ViewStateManager() {
      @Nullable
      @Override
      public Bundle getInitState() {
        return flowDispatcher.getBundle(key);
      }

      @Override
      public void saveState(Bundle bundle) {
        flowDispatcher.save(key, bundle);
      }
    };
  }
}
