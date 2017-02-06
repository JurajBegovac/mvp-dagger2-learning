package beg.hr.mvpdagger.util.transitions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import flow.Direction;

import static beg.hr.mvpdagger.util.transitions.TransitionManager.IN_LEFT_OUT_RIGHT;
import static beg.hr.mvpdagger.util.transitions.TransitionManager.IN_RIGHT_OUT_LEFT;

/** Created by juraj on 02/02/2017. */
public class DefaultTransitionsFactory implements TransitionsFactory {

  private final TransitionManager transitionManager;

  public DefaultTransitionsFactory(TransitionManager transitionManager) {
    this.transitionManager = transitionManager;
  }

  @Override
  public void execute(
      @NonNull ViewGroup root,
      @Nullable View current,
      @NonNull View newView,
      @Nullable Object oldState,
      @NonNull Object newState,
      Direction direction) {
    switch (direction) {
      case REPLACE:
        root.removeAllViews();
        root.addView(newView);
        break;
      case FORWARD:
        transitionManager.animate(current, newView, IN_RIGHT_OUT_LEFT);
        break;
      case BACKWARD:
        transitionManager.animate(current, newView, IN_LEFT_OUT_RIGHT);
        break;
    }
  }
}
