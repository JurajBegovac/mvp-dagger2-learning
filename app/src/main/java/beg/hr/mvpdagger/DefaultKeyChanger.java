package beg.hr.mvpdagger;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import javax.inject.Provider;

import beg.hr.mvpdagger.util.Utils;
import beg.hr.mvpdagger.util.flow.DialogKey;
import beg.hr.mvpdagger.util.flow.FlowViewState2;
import beg.hr.mvpdagger.util.flow.NewFlowKey;
import beg.hr.mvpdagger.util.flow.Redirect;
import beg.hr.mvpdagger.util.transitions.TransitionManager;
import beg.hr.mvpdagger.util.view.ViewComponent;
import beg.hr.mvpdagger.util.view.ViewComponentFactory;
import flow.Direction;
import flow.Flow;
import flow.History.Builder;
import flow.KeyChanger;
import flow.State;
import flow.TraversalCallback;

import static beg.hr.mvpdagger.util.transitions.DefaultTransitionManager.IN_LEFT_OUT_RIGHT;
import static beg.hr.mvpdagger.util.transitions.DefaultTransitionManager.IN_RIGHT_OUT_LEFT;
import static flow.Direction.REPLACE;

/** Created by juraj on 07/02/2017. */
public class DefaultKeyChanger implements KeyChanger {

  public static final Object FLOW_EMPTY_SIGNAL = "flow_empty_signal";

  protected final ViewComponentFactory viewComponentFactory;
  protected final TransitionManager transitionManager;

  private final Provider<ViewGroup> rootProvider;
  private final Redirect redirect;

  private Dialog dialog;
  private Object in;
  private Object out;

  public DefaultKeyChanger(
      Provider<ViewGroup> rootProvider,
      TransitionManager transitionManager,
      ViewComponentFactory viewComponentFactory,
      Redirect redirect) {
    this.rootProvider = rootProvider;
    this.transitionManager = transitionManager;
    this.viewComponentFactory = viewComponentFactory;
    this.redirect = redirect;
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

    if (redirect.shouldRedirect(mainKey)) {
      redirect.redirect(callback, mainKey, incomingContexts.get(mainKey));
      return;
    }

    if (mainKey.equals(FLOW_EMPTY_SIGNAL)) {
      // this is first empty signal and  it'll be replaced
      TextView view = new TextView(incomingContexts.get(mainKey));
      view.setText("Empty view");
      root.removeAllViews();
      root.addView(view);
      callback.onTraversalCompleted();
      return;
    }

    if (mainKey instanceof NewFlowKey) {
      startAnotherFlow(callback, (NewFlowKey) mainKey, incomingContexts.get(mainKey));
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
      // TODO: 07/02/2017 handle dialog state ?!
      ViewComponent dialogComponent =
          viewComponentFactory.create(dialogKey, null, new FlowViewState2(incomingState));
      View dialogView = null;
      if (dialogComponent != null) dialogView = dialogComponent.view();
      if (dialogView != null) {
        dialog = dialogContent(dialogKey, dialogView, incomingContexts.get(dialogKey));
        dialog.show();
      }
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

  private void startAnotherFlow(
      TraversalCallback callback, NewFlowKey newFlowKey, Context context) {
    callback.onTraversalCompleted();
    Builder historyBuilder = Flow.get(context).getHistory().buildUpon();
    historyBuilder.pop();
    Flow.get(context).setHistory(historyBuilder.build(), REPLACE);
    context.startActivity(newFlowKey.intent());
  }

  /**
   * Default dialog - Alert dialog.
   *
   * <p>Override this to show some other dialog (e.g. BottomSheets dialog)
   */
  protected Dialog dialogContent(Object dialogKey, View view, Context context) {
    return new AlertDialog.Builder(context)
        .setView(view)
        .setOnCancelListener(dialog1 -> Flow.get(context).goBack())
        .create();
  }

  /**
   * Shows main content - default behavior: REPLACE - just replaces current view, FORWARD - go
   * right, BACK - go left
   *
   * <p>Override this if you want to show main view in some another way (e.g. from bottom to top
   * etc.)
   */
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
}
