package beg.hr.mvpdagger.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import beg.hr.mvpdagger.MvpDaggerApplication;
import beg.hr.mvpdagger.di.dagger2.components.ActivityComponent;
import beg.hr.mvpdagger.di.dagger2.components.DaggerActivityComponent;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityComponent activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(MvpDaggerApplication.applicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
        HomeScreen.Component component = DaggerHomeScreen_Component.builder()
                .activityComponent(activityComponent)
                .module(new HomeScreen.Module())
                .build();
        HomeView view = component.view();
        component.inject(view);
        setContentView(view);
    }
}
