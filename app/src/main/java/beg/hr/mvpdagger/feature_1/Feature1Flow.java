package beg.hr.mvpdagger.feature_1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import beg.hr.mvpdagger.MvpDaggerApplication;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.util.DefaultTransitionsFactory;
import beg.hr.mvpdagger.util.Transitions;
import beg.hr.mvpdagger.util.TransitionsFactory;
import beg.hr.mvpdagger.util.flow.FlowActivity;
import beg.hr.mvpdagger.util.view.ViewComponent;
import beg.hr.mvpdagger.util.view.ViewComponentFactory;
import flow.Direction;
import flow.TraversalCallback;

import static beg.hr.mvpdagger.util.view.ViewComponentFactory.FEATURE1_COMPONENT1;
import static beg.hr.mvpdagger.util.view.ViewComponentFactory.FEATURE1_COMPOSITE_COMPONENT;

public class Feature1Flow extends FlowActivity {

  private ViewComponentFactory viewComponentFactory;

  public static Intent getStartIntent(Context context) {
    return new Intent(context, Feature1Flow.class);
  }

  @Override
  protected Object initScreen() {
    return FEATURE1_COMPOSITE_COMPONENT;
  }

  @Override
  protected void changeKey(
      @Nullable Object previousKey,
      Object mainKey,
      @Nullable Object dialogKey,
      Direction direction,
      TraversalCallback callback) {
    View view = null;

    ViewComponent viewComponent =
        viewComponentFactory.create(mainKey, null, viewStateManager(mainKey));
    if (viewComponent != null) view = viewComponent.view();

    if (view != null) {
      showMainView(previousKey, mainKey, view, direction);
    }

    if (dialogKey != null) {
      // TODO: 01/02/2017 handle dialog
    }
    callback.onTraversalCompleted();
  }

  @Override
  protected TransitionsFactory transitionsFactory() {

    return new DefaultTransitionsFactory() {
      @Override
      public void execute(
          @NonNull ViewGroup root,
          @Nullable View current,
          @NonNull View newView,
          @Nullable Object oldState,
          @NonNull Object newState,
          Direction direction) {
        if (oldState != null) {
          if (FEATURE1_COMPOSITE_COMPONENT.equals(oldState)
              && FEATURE1_COMPONENT1.equals(newState)) {
            Transitions.animateEnterBottomExitNone(
                Transitions.Config.builder().root(root).current(current).newView(newView).build());
            return;
          } else if (FEATURE1_COMPONENT1.equals(oldState)
              && FEATURE1_COMPOSITE_COMPONENT.equals(newState)) {
            Transitions.animateEnterNoneExitBottom(
                Transitions.Config.builder().root(root).current(current).newView(newView).build());
            return;
          }
        }
        super.execute(root, current, newView, oldState, newState, direction);
      }
    };
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    viewComponentFactory =
        MvpDaggerApplication.component().plus(new ActivityModule(this)).viewComponentFactory();
    super.onCreate(savedInstanceState);
  }
}
