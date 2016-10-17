package beg.hr.mvpdagger.profile;

import android.content.Context;
import android.view.View;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.auto.value.AutoValue;

import java.io.Serializable;
import java.util.Random;

import javax.inject.Provider;

import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.db.User;
import beg.hr.mvpdagger.db.UserRepository;
import beg.hr.mvpdagger.di.dagger2.components.ActivityComponent;
import beg.hr.mvpdagger.di.dagger2.qualifiers.ActivityContext;
import beg.hr.mvpdagger.di.dagger2.scopes.PerScreen;
import beg.hr.mvpdagger.util.mvp.Mvp;
import beg.hr.mvpdagger.util.mvp.ViewPresenter;
import dagger.Provides;
import flow.Flow;

/**
 * Created by juraj on 17/10/16.
 */
@AutoValue
public abstract class ProfileScreen implements Serializable {

    public static ProfileScreen create(String p_id) {
        return new AutoValue_ProfileScreen(p_id);
    }

    public abstract String id();

    public Component getComponent(ActivityComponent p_activityComponent) {
        return DaggerProfileScreen_Component.builder()
                .activityComponent(p_activityComponent)
                .module(new Module())
                .build();
    }

    @PerScreen
    @dagger.Component(modules = Module.class, dependencies = ActivityComponent.class)
    public interface Component {
        Mvp.Link<Presenter, ProfileView> mvp();
    }

    @dagger.Module
    public class Module {

        @Provides
        @PerScreen
        public ProfileView view(@ActivityContext Context p_context) {
            Random random = new Random();
            if (random.nextInt() % 2 == 0)
                return (ProfileView1) View.inflate(p_context, R.layout.screen_profile_1, null);
            else
                return (ProfileView2) View.inflate(p_context, R.layout.screen_profile_2, null);
        }

        @Provides
        @PerScreen
        public PresenterFactory presenterFactory(Provider<UserRepository> p_userRepositoryProvider) {
            Random random = new Random();
            if (random.nextInt() % 2 == 0) return new ProfileScreen_Presenter1Factory(p_userRepositoryProvider);
            else return new ProfileScreen_Presenter2Factory(p_userRepositoryProvider);
        }

        @Provides
        @PerScreen
        public Mvp.Link<Presenter, ProfileView> mvp(PresenterFactory p_factory, ProfileView p_view) {
            return Mvp.Link.create(p_factory.createPresenter(id()), p_view);
        }
    }

    public interface PresenterFactory {
        Presenter createPresenter(String p_id);
    }

    public interface Presenter extends Mvp.Presenter<ProfileView> {
        void onBackPressed();
    }

    @AutoFactory(implementing = PresenterFactory.class)
    public static class Presenter1 extends ViewPresenter<ProfileView> implements Presenter {

        protected String         m_userId;
        protected UserRepository m_userRepository;

        public Presenter1(String p_userId, @Provided UserRepository p_userRepository) {
            m_userId = p_userId;
            m_userRepository = p_userRepository;
        }

        @Override
        protected void onLoad() {
            super.onLoad();
            User user = m_userRepository.getUser(m_userId);
            getView().setName(user.name());
            getView().setPresenterName("I am presenter 1");
        }

        @Override
        public void onBackPressed() {
            Flow.get(getView()).goBack();
        }
    }

    @AutoFactory(implementing = PresenterFactory.class)
    public static class Presenter2 extends Presenter1 {

        public Presenter2(String p_userId, @Provided UserRepository p_userRepository) {
            super(p_userId, p_userRepository);
        }

        @Override
        protected void onLoad() {
            super.onLoad();
            getView().setPresenterName("I am presenter 2");
        }

        @Override
        public void onBackPressed() {
            Flow.get(getView()).goBack();
        }
    }
}
