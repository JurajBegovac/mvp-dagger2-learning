package beg.hr.mvpdagger.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;

public class HomeActivity extends AppCompatActivity {

    @Inject HomeScreen.Presenter m_presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeScreen.Component component = DaggerHomeScreen_Component.builder()
                .activityModule(new ActivityModule(this))
                .module(new HomeScreen.Module())
                .build();
        component.inject(this);
        HomeView view = component.view();
        component.inject(view);
        setContentView(view);
    }
}
