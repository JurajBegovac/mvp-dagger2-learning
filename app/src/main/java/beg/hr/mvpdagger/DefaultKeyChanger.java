package beg.hr.mvpdagger;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import javax.inject.Provider;

import beg.hr.mvpdagger.util.Utils;
import beg.hr.mvpdagger.util.flow.DialogKey;
import beg.hr.mvpdagger.util.flow.FlowViewState2;
import beg.hr.mvpdagger.util.flow.NewFlowKey;
import beg.hr.mvpdagger.util.flow.TransitionManager;
import beg.hr.mvpdagger.util.view.ViewComponent;
import beg.hr.mvpdagger.util.view.ViewComponentFactory;
import flow.Direction;
import flow.KeyChanger;
import flow.State;
import flow.TraversalCallback;

import static beg.hr.mvpdagger.util.transitions.DefaultTransitionManager.IN_LEFT_OUT_RIGHT;
import static beg.hr.mvpdagger.util.transitions.DefaultTransitionManager.IN_RIGHT_OUT_LEFT;

/** Created by juraj on 07/02/2017. */
public class DefaultKeyChanger implements KeyChanger {

  public static final Object FLOW_EMPTY_SIGNAL = "flow_empty_signal";
  public static final Object FLOW_FINISH_SIGNAL = "flow_finish_signal";

  protected final ViewComponentFactory viewComponentFactory;
  protected final TransitionManager transitionManager;
  private final Provider<ViewGroup> rootProvider;

  private Dialog dialog;
  private Object in;
  private Object out;

  public DefaultKeyChanger(
      Provider<ViewGroup> rootProvider,
      TransitionManager transitionManager,
      ViewComponentFactory viewComponentFactory) {
    this.rootProvider = rootProvider;
    this.transitionManager = transitionManager;
    this.viewComponentFactory = viewComponentFactory;
  }

  @Override
  public void changeKey(
      @Nullable State outgoingState,
      @NonNull State incomingState,
      @NonNull Direction direction,
      @NonNull Map<Object, Context> incomingContexts,
      @NonNull TraversalCallback callback) {

    ViewGroup root = rootProvider.get();

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

    if (shouldReverse(outKey, mainKey)) {
      transitionManager.reverse();
      callback.onTraversalCompleted();
      return;
    }

    if (mainKey.equals(FLOW_EMPTY_SIGNAL)) {
      // this is first empty signal and  it'll be replaced
      TextView view = new TextView(root.getContext());
      view.setText("Empty view");
      root.removeAllViews();
      root.addView(view);
      callback.onTraversalCompleted();
      return;
    }
    if (mainKey.equals(FLOW_FINISH_SIGNAL)) {
      // TODO: 07/02/2017
      //      finish();
      callback.onTraversalCompleted();
      return;
    }
    if (mainKey instanceof NewFlowKey) {
      // TODO: 07/02/2017
      //      startAnotherFlow(callback, (NewFlowKey) mainKey);
      callback.onTraversalCompleted();
      return;
    }

    View view = null;
    ViewComponent viewComponent =
        viewComponentFactory.create(mainKey, null, new FlowViewState2(incomingState));
    if (viewComponent != null) {
      view = viewComponent.view();
    }
    if (view != null) {
      showMainContent(view, outKey, mainKey, direction);
    }

    dismissOldDialog();
    if (dialogKey != null) {
      // TODO: 07/02/2017 dialog
      //      ViewComponent dialogComponent = viewComponentFactory.create(dialogKey, null, new FlowViewState2(incomingState))
    }
    callback.onTraversalCompleted();
  }

  private boolean shouldReverse(Object out, Object in) {
    boolean retValue = transitionManager.inProgress() && this.in.equals(out) && this.out.equals(in);
    this.out = out;
    this.in = in;
    return retValue;
  }

  private void dismissOldDialog() {
    if (dialog != null) {
      dialog.dismiss();
      dialog = null;
    }
  }

  //  private Object getMainKey(Object parent) {
  //    if (parent instanceof DialogKey) return ((DialogKey) parent).mainContent();
  //    return parent;
  //  }
  //
  //  private boolean shouldAnimate(State outgoingState) {
  //    return outgoingState == null || !isOutgoingStateDialog(outgoingState);
  //  }

  //  private boolean isOutgoingStateDialog(State outgoingState) {
  //    return outgoingState.getKey() instanceof DialogKey;
  //  }
  //
  //  private void startAnotherFlow(TraversalCallback callback, NewFlowKey newFlowKey) {
  //    callback.onTraversalCompleted();
  //    Builder historyBuilder = Flow.get(this).getHistory().buildUpon();
  //    historyBuilder.pop();
  //    Flow.get(this).setHistory(historyBuilder.build(), REPLACE);
  //
  //    startActivity(newFlowKey.intent());
  //    overridePendingTransition(newFlowKey.enterAnim(), newFlowKey.exitAnim());
  //  }
  //

  protected void showMainContent(
      View newView, Object oldState, Object newState, Direction direction) {
    ViewGroup root = rootProvider.get();
    View current = Utils.getCurrentView(root);

    if (oldState instanceof DialogKey) {
      Object mainKey = ((DialogKey) oldState).mainContent();
      if (mainKey.equals(newState)) {
        if (Utils.rootContainsView(root, newView)) {
          Utils.removeAllOtherViews(root, newView);
          return;
        }
        root.removeAllViews();
        root.addView(newView);
        return;
      }
    }

    switch (direction) {
      case REPLACE:
        if (Utils.rootContainsView(root, newView)) {
          Utils.removeAllOtherViews(root, newView);
          return;
        }
        root.removeAllViews();
        root.addView(newView);
        break;
      case FORWARD:
        transitionManager.animate(current, newView, IN_RIGHT_OUT_LEFT);
        break;
      case BACKWARD:
        transitionManager.animate(current, newView, IN_LEFT_OUT_RIGHT);
        break;
      default:
        throw new IllegalStateException("Don't know how to handle this direction: " + direction);
    }
  }

  protected void showDialog(Dialog dialog) {
    this.dialog = dialog;
    this.dialog.show();
  }

  //  protected void mainKeyToDialogKey(Object mainKey, TraversalCallback callback) {
  //    callback.onTraversalCompleted();
  //    Builder historyBuilder = Flow.get(this).getHistory().buildUpon();
  //    historyBuilder.pop();
  //    Object main = getMainKey(historyBuilder.peek());
  //    historyBuilder.push(new DialogKey(main, mainKey));
  //    Flow.get(this).setHistory(historyBuilder.build(), REPLACE);
  //  }

}
