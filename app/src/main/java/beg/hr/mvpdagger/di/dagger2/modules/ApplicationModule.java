package beg.hr.mvpdagger.di.dagger2.modules;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import beg.hr.mvpdagger.di.dagger2.qualifiers.ApplicationContext;
import beg.hr.mvpdagger.di.dagger2.scopes.PerActivity;
import dagger.Module;
import dagger.Provides;

/**
 * Created by juraj on 27/06/16.
 */
@Module
public class ApplicationModule {
    private final Application m_application;

    public ApplicationModule(Application p_application) {
        m_application = p_application;
    }

    @PerActivity
    @Provides
    Application application() {
        return m_application;
    }

    @PerActivity
    @Provides
    @ApplicationContext
    Context applicationContext() {
        return m_application.getApplicationContext();
    }
}
