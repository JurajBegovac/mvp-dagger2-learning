package beg.hr.mvpdagger.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import beg.hr.mvpdagger.MvpDaggerApplication;
import beg.hr.mvpdagger.RxBleObservable;
import beg.hr.mvpdagger.di.dagger2.components.DaggerHomeActivityComponent;
import beg.hr.mvpdagger.di.dagger2.components.HomeActivityComponent;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.di.dagger2.modules.BTModule;

public class HomeActivity extends AppCompatActivity {

    @Inject
    RxBleObservable m_bleObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeActivityComponent activityComponent = DaggerHomeActivityComponent.builder()
                .applicationComponent(MvpDaggerApplication.applicationComponent())
                .activityModule(new ActivityModule(this))
                .bTModule(new BTModule(this))
                .build();
        activityComponent.inject(this);
        HomeScreen.Component screenComponent = DaggerHomeScreen_Component.builder()
                .homeActivityComponent(activityComponent)
                .module(new HomeScreen.Module())
                .build();
        HomeView view = screenComponent.view();
        HomeScreen.Presenter presenter = screenComponent.presenter();
        screenComponent.inject(view);
        screenComponent.inject(presenter);
        setContentView(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        m_bleObservable.onActivityResult(requestCode, resultCode, data);
    }
}
