package beg.hr.mvpdagger.home;

import android.content.Context;
import android.view.View;

import java.util.Random;

import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.di.dagger2.qualifiers.ActivityContext;
import beg.hr.mvpdagger.di.dagger2.scopes.PerActivity;
import beg.hr.mvpdagger.util.mvp.ViewPresenter;
import dagger.Provides;

/**
 * Created by juraj on 16/07/16.
 */
public class HomeScreen {

    @PerActivity
    @dagger.Component(dependencies = ActivityModule.class, modules = Module.class)
    public interface Component {
        //targets
        void inject(HomeActivity p_target);
        void inject(HomeView p_target);

        // provides
        HomeView view();

    }

    @dagger.Module
    public static class Module {

        @PerActivity
        @Provides
        public HomeView provideView(@ActivityContext Context p_context) {
            return (HomeView) View.inflate(p_context, R.layout.activity_main, null);
        }

        @PerActivity
        @Provides
        public Presenter providePresenter() {
            return new Presenter();
        }
    }

    public static class Presenter extends ViewPresenter<HomeView> {

        public void dummy() {
            // do nothing
            getView().setButtonText("Hello "+ new Random().nextInt(100));
        }
    }
}
