package beg.hr.mvpdagger.util.view;

import android.app.Activity;

import beg.hr.mvpdagger.MvpDaggerApplication;
import beg.hr.mvpdagger.di.dagger2.components.ActivityComponent;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.feature_1.component_1.Component1ViewComponent;
import beg.hr.mvpdagger.feature_1.component_2.Component2ViewComponent;
import beg.hr.mvpdagger.util.flow.ViewStateManager;

/** Created by juraj on 26/01/2017. */
public class ViewComponentFactory {

  public static final String FEATURE1_COMPONENT1 = "feature1-component1";
  public static final String FEATURE1_COMPONENT2 = "feature1-component2";

  private final ActivityComponent activityComponent;

  public ViewComponentFactory(Activity activity) {
    this.activityComponent = MvpDaggerApplication.component().plus(new ActivityModule(activity));
  }

  public ViewComponent create(Object key, ViewStateManager viewStateManager) {
    if (FEATURE1_COMPONENT1.equals(key))
      return activityComponent
          .plus(new Component1ViewComponent.Module(viewStateManager))
          .viewComponent();
    if (FEATURE1_COMPONENT2.equals(key))
      return activityComponent
          .plus(new Component2ViewComponent.Module(viewStateManager))
          .viewComponent();
    return null;
  }
}
