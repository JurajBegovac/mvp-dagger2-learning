package beg.hr.mvpdagger.di.dagger2.components;

import android.content.Context;

import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.di.dagger2.qualifiers.ActivityContext;
import beg.hr.mvpdagger.di.dagger2.scopes.PerScreen;
import beg.hr.mvpdagger.profile.ProfileScreen;
import dagger.Component;

/**
 * Created by juraj on 16/07/16.
 */
@PerScreen
@Component(modules = ActivityModule.class, dependencies = ApplicationComponent.class)
public interface ActivityComponent {
    //    HomeScreen.Component plus(HomeScreen.Module p_module);
    ProfileScreen.Component plus(ProfileScreen.Module p_module);

    @ActivityContext
    Context context();
}
