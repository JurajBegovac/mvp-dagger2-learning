package beg.hr.mvpdagger.di.dagger2.modules;

import android.app.Activity;

import beg.hr.mvpdagger.RxBleObservable;
import beg.hr.mvpdagger.di.dagger2.scopes.PerActivity;
import dagger.Module;
import dagger.Provides;

/**
 * Created by juraj on 10/08/16.
 */
@Module
public class BTModule {

    private final Activity m_activity;

    public BTModule(Activity p_activity) {
        m_activity = p_activity;
    }

    @PerActivity
    @Provides
    public RxBleObservable provideRxBleObservable() {
        return new RxBleObservable(m_activity);
    }
}
