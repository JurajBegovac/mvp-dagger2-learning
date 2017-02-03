package beg.hr.mvpdagger.util;

import com.google.auto.value.AutoValue;

import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.Transition.TransitionListenerAdapter;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

/** Created by juraj on 02/02/2017. */
public class Transitions {

  public static void animateEnterRightExitLeft(Config config) {
    ViewGroup root = config.root();
    View currentView = config.current();
    View newView = config.newView();

    View newViewFromRoot = Utils.getViewFromRoot(root, newView);

    if (newViewFromRoot != null) {
      Transition exit = new Slide(Gravity.START);
      exit.addTarget(currentView);

      Transition enter = new Slide(Gravity.END);
      enter.addTarget(newViewFromRoot);

      TransitionSet set = buildTransitionSet(exit, enter, config.duration(), config.interpolator());
      set.addListener(
          new MyListener() {
            @Override
            public void onTransitionEnd(Transition transition) {
              if (!isPaused) {
                root.removeView(currentView);
                root.removeView(newViewFromRoot);
                root.addView(newView);
              }
            }
          });

      TransitionManager.beginDelayedTransition(root, set);
      currentView.setVisibility(View.GONE);
      newViewFromRoot.setVisibility(View.VISIBLE);
    } else {
      root.addView(newView);
      newView.setVisibility(View.GONE);

      Transition exit = new Slide(Gravity.START);
      exit.addTarget(currentView);

      Transition enter = new Slide(Gravity.END);
      enter.addTarget(newView);

      TransitionSet set = buildTransitionSet(exit, enter, config.duration(), config.interpolator());

      set.addListener(
          new MyListener() {
            @Override
            public void onTransitionEnd(Transition transition) {
              if (!isPaused) root.removeView(currentView);
            }
          });

      TransitionManager.beginDelayedTransition(root, set);
      currentView.setVisibility(View.GONE);
      newView.setVisibility(View.VISIBLE);
    }
  }

  public static void animateEnterLeftExitRight(Config config) {
    ViewGroup root = config.root();
    View currentView = config.current();
    View newView = config.newView();

    View newViewFromRoot = Utils.getViewFromRoot(root, newView);

    if (newViewFromRoot != null) {
      Transition exit = new Slide(Gravity.END);
      exit.addTarget(currentView);

      Transition enter = new Slide(Gravity.START);
      enter.addTarget(newViewFromRoot);

      TransitionSet set = buildTransitionSet(exit, enter, config.duration(), config.interpolator());
      set.addListener(
          new MyListener() {
            @Override
            public void onTransitionEnd(Transition transition) {
              if (!isPaused) {
                root.removeView(currentView);
                root.removeView(newViewFromRoot);
                root.addView(newView);
              }
            }
          });

      TransitionManager.beginDelayedTransition(root, set);
      currentView.setVisibility(View.GONE);
      newViewFromRoot.setVisibility(View.VISIBLE);

    } else {
      root.addView(newView);
      newView.setVisibility(View.GONE);

      Transition exit = new Slide(Gravity.END);
      exit.addTarget(currentView);

      Transition enter = new Slide(Gravity.START);
      enter.addTarget(newView);

      TransitionSet set = buildTransitionSet(exit, enter, config.duration(), config.interpolator());
      set.addListener(
          new MyListener() {
            @Override
            public void onTransitionEnd(Transition transition) {
              if (!isPaused) root.removeView(currentView);
            }
          });

      TransitionManager.beginDelayedTransition(root, set);
      currentView.setVisibility(View.GONE);
      newView.setVisibility(View.VISIBLE);
    }
  }

  /**
   * Animates new view from bottom to top without animating current view. Adds another view on top
   * of current view hierarchy and because of that onDetach from current view is not called. (That's
   * okay)
   */
  public static void animateEnterBottomExitNone(Config config) {
    ViewGroup root = config.root();
    View newView = config.newView();
    Transition transition = new Slide(Gravity.BOTTOM);
    transition.addTarget(newView);
    transition.setDuration(config.duration());
    transition.setInterpolator(config.interpolator());
    TransitionManager.beginDelayedTransition(root, transition);
    root.addView(newView);
  }

  /**
   * Exit bottom animation - current view is animated from top to bottom with newView behind current
   * view. If root doesn't contain new view - new view is added
   */
  public static void animateEnterNoneExitBottom(Config config) {
    ViewGroup root = config.root();
    View current = config.current();
    if (shouldAddNewView(root, config.newView())) {
      root.removeView(current);
      root.addView(config.newView());
      root.addView(current);
    }
    Transition transition;
    transition = new Slide(Gravity.BOTTOM);
    transition.addTarget(current);
    transition.setDuration(config.duration());
    transition.setInterpolator(config.interpolator());
    TransitionManager.beginDelayedTransition(root, transition);
    root.removeView(current);
  }

  private static boolean shouldAddNewView(ViewGroup root, View newView) {
    // TODO: 02/02/2017 check also if new view is under current view
    return !rootContainsView(root, newView);
  }

  private static boolean rootContainsView(ViewGroup root, View view) {
    for (int i = 0; i < root.getChildCount(); i++) {
      View child = root.getChildAt(i);
      Class<? extends View> childClass = child.getClass();
      Class<? extends View> viewClass = view.getClass();
      if (childClass.equals(viewClass)) return true;
    }
    return false;
  }

  private static TransitionSet buildTransitionSet(
      Transition exit, Transition enter, long duration, Interpolator interpolator) {
    return buildTransitionSet(exit, enter, duration, interpolator, TransitionSet.ORDERING_TOGETHER);
  }

  private static TransitionSet buildTransitionSet(
      Transition exit, Transition enter, long duration, Interpolator interpolator, int oredering) {
    TransitionSet transitionSet = new TransitionSet();
    transitionSet.addTransition(exit);
    transitionSet.addTransition(enter);
    transitionSet.setOrdering(oredering);
    transitionSet.setDuration(duration);
    transitionSet.setInterpolator(interpolator);
    return transitionSet;
  }

  private static class MyListener extends TransitionListenerAdapter {
    boolean isPaused = false;

    @Override
    public void onTransitionPause(Transition transition) {
      isPaused = true;
    }
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

    @Nullable
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
