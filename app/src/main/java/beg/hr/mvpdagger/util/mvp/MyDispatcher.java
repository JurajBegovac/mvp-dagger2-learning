package beg.hr.mvpdagger.util.mvp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

import flow.Direction;
import flow.Dispatcher;
import flow.KeyChanger;
import flow.KeyDispatcher;
import flow.State;
import flow.Traversal;
import flow.TraversalCallback;

/** Created by juraj on 18/01/2017. */
public class MyDispatcher implements Dispatcher, KeyChanger {
  private final KeyDispatcher keyDispatcher;
  private final KeyChanger keyChanger;

  private Traversal traversal;

  public MyDispatcher(Activity activity, KeyChanger keyChanger) {
    this.keyChanger = keyChanger;
    this.keyDispatcher = (KeyDispatcher) KeyDispatcher.configure(activity, this).build();
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
}
