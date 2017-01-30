package beg.hr.mvpdagger.util.mvp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.Map;

import flow.Direction;
import flow.Dispatcher;
import flow.KeyChanger;
import flow.KeyDispatcher;
import flow.State;
import flow.Traversal;
import flow.TraversalCallback;

/**
 * This is just a link for Flow KeyChanger
 *
 * <p>Created by juraj on 18/01/2017.
 */
public class BaseDispatcher implements Dispatcher, KeyChanger {
  private final KeyDispatcher keyDispatcher;
  private final KeyChanger keyChanger;
  private final ViewStateManager viewStateManager;

  private Traversal traversal;

  public BaseDispatcher(Activity activity, KeyChanger keyChanger, @Nullable ViewStateManager viewStateManager) {
    this.keyChanger = keyChanger;
    this.keyDispatcher = (KeyDispatcher) KeyDispatcher.configure(activity, this).build();
    this.viewStateManager = viewStateManager;
  }

  public State getState(Object key) {
    return traversal.getState(key);
  }

  @Override
  public void dispatch(@NonNull Traversal traversal, @NonNull TraversalCallback callback) {
    this.traversal = traversal;
    keyDispatcher.dispatch(traversal, callback);
  }

  @Override
  public void changeKey(
      @Nullable State outgoingState,
      @NonNull State incomingState,
      @NonNull Direction direction,
      @NonNull Map<Object, Context> incomingContexts,
      @NonNull TraversalCallback callback) {
    keyChanger.changeKey(outgoingState, incomingState, direction, incomingContexts, callback);
  }

  public void saveCurrentState(@Nullable View currentView, @Nullable Dialog dialog) {
    if (viewStateManager == null) return;
    Object currentKey = getCurrentKey();
    getState(currentKey).setBundle(viewStateManager.createBundle(currentKey, currentView, dialog));
  }

  private Object getCurrentKey() {
    return traversal.destination.top();
  }

  public Object initialViewState(Object key) {
    return viewStateManager.initialViewState(getState(key));
  }

  public void saveOutgoingState(
      State outgoingState, @Nullable View outgoingView, @Nullable Dialog dialog) {
    if (viewStateManager == null) return;
    outgoingState.setBundle(viewStateManager.createBundle(outgoingState.getKey(), outgoingView, dialog));
  }

  @Nullable
  public State getOutgoingState() {
    return traversal.origin == null ? null : traversal.getState(traversal.origin.top());
  }
}
