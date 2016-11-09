package beg.hr.mvpdagger.home;

import android.content.Context;
import android.view.View;

import com.google.auto.value.AutoValue;

import java.io.Serializable;

import javax.inject.Inject;

import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.di.dagger2.components.ActivityComponent;
import beg.hr.mvpdagger.di.dagger2.components.ApplicationComponent;
import beg.hr.mvpdagger.di.dagger2.qualifiers.ActivityContext;
import beg.hr.mvpdagger.di.dagger2.qualifiers.ApplicationContext;
import beg.hr.mvpdagger.di.dagger2.scopes.CustomScope3;
import beg.hr.mvpdagger.profile.ProfileScreen;
import beg.hr.mvpdagger.util.mvp.Mvp;
import beg.hr.mvpdagger.util.mvp.ViewPresenter;
import dagger.Provides;
import flow.Flow;

/**
 * Created by juraj on 29/09/16.
 */
@AutoValue
public abstract class HomeScreen implements Serializable {

    public static HomeScreen create() {
        return new AutoValue_HomeScreen();
    }

    public Component component(ActivityComponent p_activityComponent) {
//        DaggerHomeScreen_Component.builder()
//                .activityComponent(p_activityComponent)
//                .build();
        return null;
    }

    @CustomScope3
    @dagger.Component(modules = Module.class, dependencies = ApplicationComponent.class)
    public interface Component {
        Mvp.Link<Presenter, HomeView> mvp();
    }

    @dagger.Module
    public static class Module {

        @Provides
        @CustomScope3
        public HomeView view(@ActivityContext Context p_context) {
            return (HomeView) View.inflate(p_context, R.layout.screen_home, null);
        }

        @Provides
        @CustomScope3
        public Mvp.Link<Presenter, HomeView> mvp(Presenter p_presenter, HomeView p_view) {
            return Mvp.Link.create(p_presenter, p_view);
        }

    }

    public static class Presenter extends ViewPresenter<HomeView> {

        @Inject
        public Presenter() {
        }

        public void onButtonPressed() {
            Flow.get(getView()).set(ProfileScreen.create("Id 1"));
        }
    }
}
