package beg.hr.mvpdagger.di.dagger2.components;

import beg.hr.mvpdagger.RxBleObservable;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.di.dagger2.modules.BTModule;
import beg.hr.mvpdagger.di.dagger2.scopes.PerActivity;
import beg.hr.mvpdagger.home.HomeActivity;
import dagger.Component;

/**
 * Created by juraj on 10/08/16.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {
        ActivityModule.class, BTModule.class
})
public interface HomeActivityComponent extends ActivityComponent {
    void inject(HomeActivity p_target);

    RxBleObservable rxBleObservable();
}
