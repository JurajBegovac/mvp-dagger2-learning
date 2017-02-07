package beg.hr.mvpdagger.util.transitions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.transitionseverywhere.Transition;
import com.transitionseverywhere.Transition.TransitionListenerAdapter;

import beg.hr.mvpdagger.util.Utils;
import beg.hr.mvpdagger.util.transitions.Transitions.Config;
import flow.Direction;

/** Created by juraj on 03/02/2017. */
public abstract class TransitionManager {

  // animation types
  public static final int IN_RIGHT_OUT_LEFT = 200;
  public static final int IN_LEFT_OUT_RIGHT = 201;
  public static final int IN_BOTTOM_OUT_NONE = 202;
  public static final int IN_NONE_OUT_BOTTOM = 203;

  // status
  private static final int IDLE = 100;
  private static final int IN_PROGRESS = 101;

  private final ViewGroup root;
  protected Object out;
  protected Object in;
  // mutable properties
  private View outView;
  private View inView;
  private int status = IDLE;
  private int type = 0;

  public TransitionManager(ViewGroup root) {
    this.root = root;
  }

  private void reverse(View outView, View inView) {
    switch (type) {
      case IN_RIGHT_OUT_LEFT:
        animate(outView, inView, IN_LEFT_OUT_RIGHT);
        break;
      case IN_LEFT_OUT_RIGHT:
        animate(outView, inView, IN_RIGHT_OUT_LEFT);
        break;
      case IN_BOTTOM_OUT_NONE:
        animate(outView, inView, IN_NONE_OUT_BOTTOM);
        break;
      case IN_NONE_OUT_BOTTOM:
        animate(outView, inView, IN_BOTTOM_OUT_NONE);
        break;
      default:
        throw new IllegalStateException("Wrong animation type");
    }
  }

  private boolean shouldAddNewView(ViewGroup root, View newView) {
    // TODO: 02/02/2017 check also if new view is under current view
    return !Utils.rootContainsView(root, newView);
  }

  public void animate(View outView, View inView, int type) {
    this.type = type;
    this.outView = outView;
    this.inView = inView;

    Config config = Config.builder().root(root).current(outView).newView(inView).build();
    TransitionListenerAdapter listener =
        new MyListener() {
          @Override
          public void onTransitionStart(Transition transition) {
            status = IN_PROGRESS;
          }

          @Override
          public void onTransitionEnd(Transition transition) {
            status = IDLE;
            if (!isCanceled()) {
              root.removeView(outView);
            }
          }
        };

    switch (type) {
      case IN_RIGHT_OUT_LEFT:
        if (inView.getParent() == null) root.addView(inView);
        Transitions.animateEnterRightExitLeft(config, listener);
        break;
      case IN_LEFT_OUT_RIGHT:
        if (inView.getParent() == null) root.addView(inView);
        Transitions.animateEnterLeftExitRight(config, listener);
        break;
      case IN_BOTTOM_OUT_NONE:
        if (inView.getParent() == null) root.addView(inView);
        Transitions.animateEnterBottomExitNone(config, listener);
        break;
      case IN_NONE_OUT_BOTTOM:
        if (shouldAddNewView(root, config.newView())) {
          root.removeView(outView);
          root.addView(inView);
          root.addView(outView);
        }
        Transitions.animateEnterNoneExitBottom(config, listener);
        break;
      default:
        throw new IllegalStateException("Wrong animation type");
    }
  }

  public boolean inProgress() {
    return status == IN_PROGRESS;
  }

  public void reverse() {
    reverse(inView, outView);
  }

  public abstract boolean shouldReverse(Object out, Object in);

  public abstract void animate(
      @NonNull ViewGroup root,
      @Nullable View current,
      @NonNull View newView,
      @Nullable Object oldState,
      @NonNull Object newState,
      Direction direction);

  private static class MyListener extends TransitionListenerAdapter {
    private boolean canceled;

    public boolean isCanceled() {
      return canceled;
    }

    @Override
    public void onTransitionPause(Transition transition) {
      canceled = true;
    }

    @Override
    public void onTransitionCancel(Transition transition) {
      canceled = true;
    }
  }
}
