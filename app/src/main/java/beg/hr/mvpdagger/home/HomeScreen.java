package beg.hr.mvpdagger.home;

import android.content.Context;
import android.view.View;

import java.util.Random;

import javax.inject.Inject;

import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.RxBleObservable;
import beg.hr.mvpdagger.di.dagger2.components.HomeActivityComponent;
import beg.hr.mvpdagger.di.dagger2.qualifiers.ActivityContext;
import beg.hr.mvpdagger.di.dagger2.scopes.PerScreen;
import beg.hr.mvpdagger.util.mvp.ViewPresenter;
import dagger.Provides;

/**
 * Created by juraj on 16/07/16.
 */
public class HomeScreen {

    @PerScreen
    @dagger.Component(dependencies = HomeActivityComponent.class, modules = Module.class)
    public interface Component {
        //targets
        void inject(HomeView p_target);
        void inject(Presenter p_target);

        // provide dependencies
        HomeView view();
        Presenter presenter();
    }

    @dagger.Module
    public static class Module {

        @PerScreen
        @Provides
        public HomeView provideView(@ActivityContext Context p_context) {
            return (HomeView) View.inflate(p_context, R.layout.screen_home, null);
        }

        @PerScreen
        @Provides
        public Presenter providePresenter() {
            return new Presenter();
        }
    }

    public static class Presenter extends ViewPresenter<HomeView> {

        @Inject
        RxBleObservable m_bleObservable;

        public Presenter() {
        }

        public void dummy() {
            // do nothing
            getView().setButtonText("Hello " + new Random().nextInt(100));
            m_bleObservable.enable().first().subscribe();
        }
    }
}
