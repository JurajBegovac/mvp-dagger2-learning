package beg.hr.mvpdagger.di.dagger2.modules;

import android.app.Activity;
import android.content.Context;

import beg.hr.mvpdagger.di.dagger2.qualifiers.ActivityContext;
import beg.hr.mvpdagger.di.dagger2.scopes.PerActivity;
import dagger.Module;
import dagger.Provides;

/**
 * Created by juraj on 16/07/16.
 */
@Module
public class ActivityModule {
    protected final Activity m_activity;

    public ActivityModule(Activity p_activity) {
        m_activity = p_activity;
    }

    @PerActivity
    @Provides
    @ActivityContext
    public Context provideContext() {
        return m_activity;
    }
}
