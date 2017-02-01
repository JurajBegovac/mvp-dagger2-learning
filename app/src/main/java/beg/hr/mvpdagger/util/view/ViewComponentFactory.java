package beg.hr.mvpdagger.util.view;

import android.app.Activity;
import android.view.View;

import beg.hr.mvpdagger.MvpDaggerApplication;
import beg.hr.mvpdagger.di.dagger2.components.ActivityComponent;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.feature_1.component_1.Component1ViewComponent;
import beg.hr.mvpdagger.feature_1.component_2.Component2ViewComponent;
import beg.hr.mvpdagger.feature_1.composite_component.CompositeViewComponent;
import beg.hr.mvpdagger.util.flow.ViewState;

/** Created by juraj on 26/01/2017. */
public class ViewComponentFactory {

  public static final String FEATURE1_COMPONENT1 = "feature1-component1";
  public static final String FEATURE1_COMPONENT2 = "feature1-component2";
  public static final String FEATURE1_COMPOSITE_COMPONENT = "feature1-composite-component";

  private final ActivityComponent activityComponent;

  public ViewComponentFactory(Activity activity) {
    this.activityComponent = MvpDaggerApplication.component().plus(new ActivityModule(activity));
  }

  public ViewComponent create(Object key, View view, ViewState viewState) {
    if (FEATURE1_COMPONENT1.equals(key))
      return activityComponent
          .plus(new Component1ViewComponent.Module(view, viewState))
          .viewComponent();
    if (FEATURE1_COMPONENT2.equals(key))
      return activityComponent
          .plus(new Component2ViewComponent.Module(view, viewState))
          .viewComponent();
    if (FEATURE1_COMPOSITE_COMPONENT.equals(key))
      return activityComponent
          .plus(new CompositeViewComponent.Module(viewState))
          .viewComponent();
    return null;
  }
}
