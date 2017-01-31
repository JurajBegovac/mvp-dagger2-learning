package beg.hr.mvpdagger.util.view;

import android.app.Activity;

import beg.hr.mvpdagger.MvpDaggerApplication;
import beg.hr.mvpdagger.di.dagger2.components.ActivityComponent;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.mvi.feature_1.Feature1Component;
import beg.hr.mvpdagger.util.mvp.ViewStateManager2;

/** Created by juraj on 26/01/2017. */
public class ViewComponentFactory {

  public static final String FEATURE1_COMPONENT = "feature1-component";

  private final ActivityComponent activityComponent;

  public ViewComponentFactory(Activity activity) {
    this.activityComponent = MvpDaggerApplication.component().plus(new ActivityModule(activity));
  }

  public ViewComponent create(Object key, ViewStateManager2 viewStateManager) {
    if (FEATURE1_COMPONENT.equals(key))
      return activityComponent.plus(new Feature1Component.Module(viewStateManager)).viewComponent();
    return null;
  }
}
