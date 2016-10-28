package beg.hr.mvpdagger.di.dagger2.components;

import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.di.dagger2.scopes.PerActivity;
import beg.hr.mvpdagger.home.HomeScreen;
import beg.hr.mvpdagger.profile.ProfileScreen;
import dagger.Subcomponent;

/**
 * Created by juraj on 16/07/16.
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    HomeScreen.Component plus(HomeScreen.Module p_module);
    ProfileScreen.Component plus(ProfileScreen.Module p_module);
}
