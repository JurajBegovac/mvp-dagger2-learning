package beg.hr.flowlesstest;

import android.app.Application;

import beg.hr.flowlesstest.dagger2.components.ApplicationComponent;
import beg.hr.flowlesstest.dagger2.components.DaggerApplicationComponent;
import beg.hr.flowlesstest.dagger2.modules.ApplicationModule;

/** Created by juraj on 16/07/16. */
public class MvpDaggerApplication extends Application {

  private static ApplicationComponent component;

  public static ApplicationComponent component() {
    return component;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    component =
        DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
  }
}
