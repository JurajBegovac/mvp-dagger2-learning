package beg.hr.flowlesstest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import beg.hr.flowlesstest.dagger2.components.ActivityComponent;
import beg.hr.flowlesstest.home.HomeScreen;
import beg.hr.flowlesstest.home.HomeView;
import flowless.State;
import flowless.Traversal;
import flowless.TraversalCallback;
import flowless.preset.DispatcherUtils;
import flowless.preset.SingleRootDispatcher;

/** Created by juraj on 18/01/2017. */
public class MyDispatcher extends SingleRootDispatcher {

  private final ActivityComponent component;

  public MyDispatcher(ActivityComponent component) {
    this.component = component;
  }

  @Override
  public void dispatch(@NonNull Traversal traversal, @NonNull TraversalCallback callback) {
    if (DispatcherUtils.isPreviousKeySameAsNewKey(traversal.origin, traversal.destination)) {
      callback.onTraversalCompleted();
      return;
    }

    State outState = traversal.origin == null ? null : traversal.getState(traversal.origin.top());
    Object outKey = outState == null ? null : outState.getKey();

    State inState = traversal.getState(traversal.destination.top());
    Object inKey = inState.getKey();

    if (outState != null) {
      saveOutgoingState(outState);
    }

    final ViewGroup root = rootHolder.getRoot();

    View view = null;
    if (inKey instanceof HomeScreen) {
      view = ((HomeScreen) inKey).component(component, getInitState(inState)).mvp().view();
    }

    if (view != null) {
      root.removeAllViews();
      root.addView(view);
    }
  }

  private HomeScreen.State getInitState(State incomingState) {
    if (incomingState.getBundle() != null
        && incomingState.getBundle().getParcelable(HomeScreen.State.KEY) != null) {
      return incomingState.getBundle().getParcelable(HomeScreen.State.KEY);
    }
    return HomeScreen.State.defaultState();
  }

  private void saveOutgoingState(State outgoingState) {
    ViewGroup view = getRootHolder().getRoot();
    if (outgoingState.getKey() instanceof HomeScreen) {
      String text = ((HomeView) view.getChildAt(0)).getText();
      Bundle bundle = new Bundle();
      bundle.putParcelable(HomeScreen.State.KEY, HomeScreen.State.create(text));
      outgoingState.setBundle(bundle);
    }
  }
}
