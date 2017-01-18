package beg.hr.flowlesstest.dagger2.components;

import android.content.Context;

import javax.inject.Singleton;

import beg.hr.flowlesstest.dagger2.modules.ActivityModule;
import beg.hr.flowlesstest.dagger2.modules.ApplicationModule;
import beg.hr.flowlesstest.dagger2.qualifiers.ApplicationContext;
import dagger.Component;

/** Created by juraj on 27/06/16. */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
  @ApplicationContext
  Context context();

  ActivityComponent plus(ActivityModule module);
}
