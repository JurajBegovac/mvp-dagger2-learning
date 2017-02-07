package beg.hr.mvpdagger.feature_1;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;

import beg.hr.mvpdagger.MvpDaggerApplication;
import beg.hr.mvpdagger.di.dagger2.components.ActivityComponent;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.util.flow.FlowActivity;
import beg.hr.mvpdagger.util.transitions.DefaultShowViewFactory;
import beg.hr.mvpdagger.util.transitions.ShowViewFactory;
import beg.hr.mvpdagger.util.view.ViewComponent;
import beg.hr.mvpdagger.util.view.ViewComponentFactory;
import flow.Direction;
import flow.Flow;
import flow.TraversalCallback;

import static beg.hr.mvpdagger.util.transitions.TransitionManager.IN_BOTTOM_OUT_NONE;
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

    if (mainKey.equals(FEATURE1_COMPONENT1)) {
      mainKeyToDialogKey(mainKey, callback);
      return;
    }

    View view = null;
    ViewComponent viewComponent = viewComponentFactory.create(mainKey, null, viewState(mainKey));
    if (viewComponent != null) view = viewComponent.view();
    if (view != null) {
      showMain(view, previousKey, mainKey, direction);
    }

    if (dialogKey != null) {
      ViewComponent dialogComponent =
          viewComponentFactory.create(dialogKey, null, viewState(dialogKey));
      if (dialogComponent != null) {
        Dialog dialog =
            new AlertDialog.Builder(this)
                .setView(dialogComponent.view())
                .setOnCancelListener(dialog1 -> Flow.get(Feature1Flow.this).goBack())
                .create();
        showDialog(dialog);
      }
    }
    callback.onTraversalCompleted();
  }

  @Override
  protected ShowViewFactory showFactory() {
    return new DefaultShowViewFactory(transitionManager) {
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
            transitionManager.animate(current, newView, IN_BOTTOM_OUT_NONE);
            return;
          }
          //          else if (FEATURE1_COMPONENT1.equals(oldState)
          //              && FEATURE1_COMPOSITE_COMPONENT.equals(newState)) {
          //            transitionManager.animate(current, newView, IN_NONE_OUT_BOTTOM);
          //            return;
          //          }
        }
        super.execute(root, current, newView, oldState, newState, direction);
      }
    };
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    ActivityComponent component = MvpDaggerApplication.component().plus(new ActivityModule(this));
    viewComponentFactory = component.viewComponentFactory();
    super.onCreate(savedInstanceState);
  }
}
