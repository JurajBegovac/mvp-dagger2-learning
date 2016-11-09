package beg.hr.mvpdagger.di.dagger2.components;

import javax.inject.Singleton;

import beg.hr.mvpdagger.db.DbModule;
import beg.hr.mvpdagger.db.UserRepository;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.di.dagger2.modules.ApplicationModule;
import beg.hr.mvpdagger.di.dagger2.scopes.PerActivity;
import dagger.Component;

/**
 * Created by juraj on 27/06/16.
 */
@PerActivity
@Component(modules = {ApplicationModule.class, DbModule.class})
public interface ApplicationComponent {
//    ActivityComponent plus(ActivityModule p_module);

    UserRepository userRepository();
}
