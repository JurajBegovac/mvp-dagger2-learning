package beg.hr.mvpdagger.util.view;

import com.google.auto.value.AutoValue;

import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ViewAnimator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.LinkedList;
import java.util.Queue;

import beg.hr.mvpdagger.R;

/** Created by petar on 17/02/2017. */
public class BetterViewAnimator extends ViewAnimator {

  private boolean isAnimationRunning;
  private Queue<Config> queue = new LinkedList<>();

  public BetterViewAnimator(Context context) {
    this(context, null);
  }

  public BetterViewAnimator(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void showView(Config config) {
    if (isAnimationRunning) {
      queue.add(config);
      return;
    }

    View view = config.view();
    if (getChildAt(getDisplayedChild()) == view) return;

    addView(view, config.newViewIndex());

    Animation in = AnimationUtils.loadAnimation(getContext(), config.inAnimRes());
    in.setAnimationListener(inAnimationListener(config.oldViewIndex()));
    setInAnimation(in);
    setOutAnimation(getContext(), config.outAnimRes());

    for (int i = 0, count = getChildCount(); i < count; i++) {
      if (getChildAt(i) == view) {
        isAnimationRunning = true;
        setDisplayedChild(i);
        return;
      }
    }

    throw new IllegalArgumentException("No view " + view.toString());
  }

  @NonNull
  private AnimationListener inAnimationListener(int oldViewIndex) {
    return new AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {}

      @Override
      public void onAnimationEnd(Animation animation) {
        setInAnimation(null);
        setOutAnimation(null);
        removeViewAt(oldViewIndex);
        isAnimationRunning = false;
        if (!queue.isEmpty()) showView(queue.poll());
      }

      @Override
      public void onAnimationRepeat(Animation animation) {}
    };
  }

  @AutoValue
  public abstract static class Config {

    public static final String IN_RIGHT_OUT_LEFT = "in_right_out_left";
    public static final String IN_LEFT_OUT_RIGHT = "in_left_out_right";
    public static final String IN_BOTTOM_OUT_NOTHING = "in_bottom_out_nothing";
    public static final String IN_NOTHING_OUT_BOTTOM = "in_nothing_out_bottom";

    public static Config create(View view, @AnimationDirection String animationDirection) {
      return new AutoValue_BetterViewAnimator_Config(view, animationDirection);
    }

    public abstract View view();

    @AnimationDirection
    public abstract String animationDirection();

    private boolean isSpecialCase() {
      return animationDirection().equals(Config.IN_NOTHING_OUT_BOTTOM);
    }

    public int newViewIndex() {
      return isSpecialCase() ? 0 : 1;
    }

    public int oldViewIndex() {
      return isSpecialCase() ? 1 : 0;
    }

    @AnimRes
    public int inAnimRes() {
      switch (animationDirection()) {
        case IN_RIGHT_OUT_LEFT:
          return R.anim.slide_in_right;
        case IN_LEFT_OUT_RIGHT:
          return R.anim.slide_in_left;
        case IN_BOTTOM_OUT_NOTHING:
          return R.anim.slide_in_bottom;
        case IN_NOTHING_OUT_BOTTOM:
          return R.anim.nothing;
        default:
          return R.anim.nothing;
      }
    }

    @AnimRes
    public int outAnimRes() {
      switch (animationDirection()) {
        case IN_RIGHT_OUT_LEFT:
          return R.anim.slide_out_left;
        case IN_LEFT_OUT_RIGHT:
          return R.anim.slide_out_right;
        case IN_BOTTOM_OUT_NOTHING:
          return R.anim.nothing;
        case IN_NOTHING_OUT_BOTTOM:
          return R.anim.slide_out_bottom;
        default:
          return R.anim.nothing;
      }
    }

    @StringDef({IN_RIGHT_OUT_LEFT, IN_LEFT_OUT_RIGHT, IN_BOTTOM_OUT_NOTHING, IN_NOTHING_OUT_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationDirection {}
  }
}
