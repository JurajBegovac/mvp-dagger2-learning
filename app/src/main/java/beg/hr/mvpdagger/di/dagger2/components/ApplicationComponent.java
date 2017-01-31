package beg.hr.mvpdagger.di.dagger2.components;

import android.content.Context;

import javax.inject.Singleton;

import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.di.dagger2.modules.ApplicationModule;
import beg.hr.mvpdagger.di.dagger2.qualifiers.ApplicationContext;
import dagger.Component;

/** Created by juraj on 27/06/16. */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
  @ApplicationContext
  Context context();

  ActivityComponent plus(ActivityModule module);
}
