package beg.hr.mvpdagger.di.dagger2.components;

import android.content.Context;

import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.di.dagger2.qualifiers.ActivityContext;
import beg.hr.mvpdagger.di.dagger2.scopes.PerActivity;
import beg.hr.mvpdagger.home.HomeScreen;
import beg.hr.mvpdagger.mvi.feature_1.Feature1Component;
import beg.hr.mvpdagger.screen_1.Screen1;
import beg.hr.mvpdagger.screen_2.Screen2;
import dagger.Subcomponent;

/** Created by juraj on 16/07/16. */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
  @ActivityContext
  Context context();

  HomeScreen.Component plus(HomeScreen.Module module);

  Screen1.Component plus(Screen1.Module module);

  Screen2.Component plus(Screen2.Module module);

  Feature1Component.ObjectGraph plus(Feature1Component.Module module);
}
