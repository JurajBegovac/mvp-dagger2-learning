package beg.hr.mvpdagger.util.transitions;

import com.google.auto.value.AutoValue;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.Transition.TransitionListener;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

/** Created by juraj on 02/02/2017. */
public class Transitions {

  public static void animateEnterRightExitLeft(Config config, TransitionListener listener) {
    ViewGroup root = config.root();
    View currentView = config.current();
    View newView = config.newView();

    newView.setVisibility(View.GONE);

    Transition exit = new Slide(Gravity.START);
    exit.addTarget(currentView);

    Transition enter = new Slide(Gravity.END);
    enter.addTarget(newView);

    TransitionSet set = buildTransitionSet(exit, enter, config.duration(), config.interpolator());
    set.addListener(listener);

    TransitionManager.beginDelayedTransition(root, set);
    currentView.setVisibility(View.GONE);
    newView.setVisibility(View.VISIBLE);
  }

  public static void animateEnterLeftExitRight(Config config, TransitionListener listener) {
    ViewGroup root = config.root();
    View currentView = config.current();
    View newView = config.newView();

    newView.setVisibility(View.GONE);

    Transition exit = new Slide(Gravity.END);
    exit.addTarget(currentView);

    Transition enter = new Slide(Gravity.START);
    enter.addTarget(newView);

    TransitionSet set = buildTransitionSet(exit, enter, config.duration(), config.interpolator());
    set.addListener(listener);

    TransitionManager.beginDelayedTransition(root, set);
    currentView.setVisibility(View.GONE);
    newView.setVisibility(View.VISIBLE);
  }

  /**
   * Animates new view from bottom to top without animating current view. Adds another view on top
   * of current view hierarchy and because of that onDetach from current view is not called. (That's
   * okay)
   */
  public static void animateEnterBottomExitNone(Config config, TransitionListener listener) {
    ViewGroup root = config.root();
    View newView = config.newView();

    newView.setVisibility(View.GONE);

    Transition transition = new Slide(Gravity.BOTTOM);
    transition.addTarget(newView);
    transition.setDuration(config.duration());
    transition.setInterpolator(config.interpolator());
    transition.addListener(listener);
    TransitionManager.beginDelayedTransition(root, transition);
    newView.setVisibility(View.VISIBLE);
  }

  /**
   * Exit bottom animation - current view is animated from top to bottom with newView behind current
   * view. If root doesn't contain new view - new view is added
   */
  public static void animateEnterNoneExitBottom(Config config, TransitionListener listener) {
    ViewGroup root = config.root();
    View current = config.current();
    Transition transition = new Slide(Gravity.BOTTOM);
    transition.addTarget(current);
    transition.setDuration(config.duration());
    transition.setInterpolator(config.interpolator());
    transition.addListener(listener);
    TransitionManager.beginDelayedTransition(root, transition);
    current.setVisibility(View.GONE);
  }

  private static TransitionSet buildTransitionSet(
      Transition exit, Transition enter, long duration, Interpolator interpolator) {
    return buildTransitionSet(exit, enter, duration, interpolator, TransitionSet.ORDERING_TOGETHER);
  }

  private static TransitionSet buildTransitionSet(
      Transition exit, Transition enter, long duration, Interpolator interpolator, int ordering) {
    TransitionSet transitionSet = new TransitionSet();
    transitionSet.addTransition(exit);
    transitionSet.addTransition(enter);
    transitionSet.setOrdering(ordering);
    transitionSet.setDuration(duration);
    transitionSet.setInterpolator(interpolator);
    return transitionSet;
  }

  @AutoValue
  public abstract static class Config {

    private static final long DEFAULT_DURATION = 400; // 400 is android medium anim time

    public static Builder builder() {
      AutoValue_Transitions_Config.Builder builder = new AutoValue_Transitions_Config.Builder();
      builder.duration(DEFAULT_DURATION);
      builder.interpolator(new LinearInterpolator());
      return builder;
    }

    abstract ViewGroup root();

    abstract View newView();

    abstract View current();

    abstract long duration();

    abstract Interpolator interpolator();

    @AutoValue.Builder
    public abstract static class Builder {
      public abstract Builder root(ViewGroup root);

      public abstract Builder newView(View newView);

      public abstract Builder current(View current);

      public abstract Builder duration(long duration);

      public abstract Builder interpolator(Interpolator interpolator);

      public abstract Config build();
    }
  }
}
