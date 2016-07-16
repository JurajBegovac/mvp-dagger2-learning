package beg.hr.mvpdagger;

import android.app.Application;

import beg.hr.mvpdagger.di.dagger2.components.ApplicationComponent;
import beg.hr.mvpdagger.di.dagger2.components.DaggerApplicationComponent;
import beg.hr.mvpdagger.di.dagger2.modules.ApplicationModule;

/**
 * Created by juraj on 16/07/16.
 */
public class MvpDaggerApplication extends Application {

    private static ApplicationComponent s_applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        s_applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static ApplicationComponent applicationComponent() {
        return s_applicationComponent;
    }
}
