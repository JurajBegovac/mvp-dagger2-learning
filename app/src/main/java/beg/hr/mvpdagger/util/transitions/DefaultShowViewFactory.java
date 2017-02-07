package beg.hr.mvpdagger.util.transitions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import beg.hr.mvpdagger.util.Utils;
import beg.hr.mvpdagger.util.flow.DialogKey;
import flow.Direction;

import static beg.hr.mvpdagger.util.transitions.TransitionManager.IN_LEFT_OUT_RIGHT;
import static beg.hr.mvpdagger.util.transitions.TransitionManager.IN_RIGHT_OUT_LEFT;

/** Created by juraj on 02/02/2017. */
public class DefaultShowViewFactory implements ShowViewFactory {

  private final TransitionManager transitionManager;

  private Object out;
  private Object in;

  public DefaultShowViewFactory(TransitionManager transitionManager) {
    this.transitionManager = transitionManager;
  }

  @Override
  public boolean shouldReverse(Object out, Object in) {
    boolean retValue = transitionManager.inProgress() && this.in.equals(out) && this.out.equals(in);
    this.out = out;
    this.in = in;
    return retValue;
  }

  @Override
  public void execute(
      @NonNull ViewGroup root,
      @Nullable View current,
      @NonNull View newView,
      @Nullable Object oldState,
      @NonNull Object newState,
      Direction direction) {

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
