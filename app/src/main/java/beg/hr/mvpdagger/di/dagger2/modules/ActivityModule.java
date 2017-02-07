package beg.hr.mvpdagger.di.dagger2.modules;

import android.app.Activity;
import android.content.Context;

import beg.hr.mvpdagger.di.dagger2.qualifiers.ActivityContext;
import beg.hr.mvpdagger.di.dagger2.scopes.PerActivity;
import beg.hr.mvpdagger.util.view.ViewComponentFactory;
import dagger.Module;
import dagger.Provides;

/** Created by juraj on 16/07/16. */
@Module
public class ActivityModule {
  protected final Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  @PerActivity
  @Provides
  @ActivityContext
  public Context context() {
    return activity;
  }

  @PerActivity
  @Provides
  public ViewComponentFactory viewComponentFactory() {
    return new ViewComponentFactory(activity);
  }
}
