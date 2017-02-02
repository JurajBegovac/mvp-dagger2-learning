package beg.hr.mvpdagger.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import flow.Direction;

/** Created by juraj on 02/02/2017. */
public class DefaultTransitionsFactory implements TransitionsFactory {

  @Override
  public void execute(
      @NonNull ViewGroup root,
      @Nullable View current,
      @NonNull View newView,
      @Nullable Object oldState,
      @NonNull Object newState,
      Direction direction) {
    Transitions.Config config =
        Transitions.Config.builder().root(root).current(current).newView(newView).build();
    switch (direction) {
      case REPLACE:
        root.removeAllViews();
        root.addView(newView);
        break;
      case FORWARD:
        Transitions.animateEnterRightExitLeft(config);
        break;
      case BACKWARD:
        Transitions.animateEnterLeftExitRight(config);
        break;
    }
  }
}
