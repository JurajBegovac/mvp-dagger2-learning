package beg.hr.flowlesstest.dagger2.components;

import android.content.Context;

import dagger.Subcomponent;
import beg.hr.flowlesstest.dagger2.modules.ActivityModule;
import beg.hr.flowlesstest.dagger2.qualifiers.ActivityContext;
import beg.hr.flowlesstest.dagger2.scopes.PerActivity;
import beg.hr.flowlesstest.home.HomeScreen;

/** Created by juraj on 16/07/16. */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
  @ActivityContext
  Context context();

  HomeScreen.Component plus(HomeScreen.Module module);
}
