package beg.hr.mvpdagger.di.dagger2.components;

import android.content.Context;

import beg.hr.mvpdagger.db.UserRepository;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.di.dagger2.qualifiers.ActivityContext;
import beg.hr.mvpdagger.di.dagger2.scopes.PerActivity;
import dagger.Component;

/**
 * Created by juraj on 16/07/16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    //     provide to components that depend on this component
    @ActivityContext
    Context context();

    UserRepository userRepository();
}
