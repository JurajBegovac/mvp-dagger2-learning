package beg.hr.flowlesstest.dagger2.modules;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import beg.hr.flowlesstest.dagger2.qualifiers.ActivityContext;
import beg.hr.flowlesstest.dagger2.scopes.PerActivity;

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
    public Context context() {
        return m_activity;
    }
}
