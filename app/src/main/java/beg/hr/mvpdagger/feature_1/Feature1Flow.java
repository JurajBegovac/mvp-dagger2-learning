package beg.hr.mvpdagger.feature_1;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import beg.hr.mvpdagger.DefaultKeyChanger;
import beg.hr.mvpdagger.util.Utils;
import beg.hr.mvpdagger.util.flow.FlowActivity;
import flow.Direction;
import flow.KeyChanger;

import static beg.hr.mvpdagger.util.transitions.DefaultTransitionManager.IN_BOTTOM_OUT_NONE;
import static beg.hr.mvpdagger.util.transitions.DefaultTransitionManager.IN_NONE_OUT_BOTTOM;
import static beg.hr.mvpdagger.util.view.ViewComponentFactory.FEATURE1_COMPONENT1;
import static beg.hr.mvpdagger.util.view.ViewComponentFactory.FEATURE1_COMPOSITE_COMPONENT;

public class Feature1Flow extends FlowActivity {

  public static Intent getStartIntent(Context context) {
    return new Intent(context, Feature1Flow.class);
  }

  @Override
  protected Object initScreen() {
    return FEATURE1_COMPOSITE_COMPONENT;
  }

  @Override
  protected KeyChanger keyChanger() {
    return new DefaultKeyChanger(() -> rootView(), transitionManager(), viewComponentFactory()) {
      @Override
      protected void showMainContent(
          View newView, Object oldState, Object newState, Direction direction) {

        if (FEATURE1_COMPONENT1.equals(newState) && FEATURE1_COMPOSITE_COMPONENT.equals(oldState)) {
          this.transitionManager.animate(
              Utils.getCurrentView(rootView()), newView, IN_BOTTOM_OUT_NONE);
          return;
        }
        if (FEATURE1_COMPOSITE_COMPONENT.equals(newState) && FEATURE1_COMPONENT1.equals(oldState)) {
          this.transitionManager.animate(
              Utils.getCurrentView(rootView()), newView, IN_NONE_OUT_BOTTOM);
          return;
        }
        super.showMainContent(newView, oldState, newState, direction);
      }
    };
  }
}
