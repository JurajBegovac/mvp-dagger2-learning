package beg.hr.mvpdagger.home;

import com.google.auto.value.AutoValue;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;

import java.util.Random;

import javax.inject.Inject;

import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.di.dagger2.components.ActivityComponent;
import beg.hr.mvpdagger.di.dagger2.qualifiers.ActivityContext;
import beg.hr.mvpdagger.di.dagger2.scopes.PerScreen;
import beg.hr.mvpdagger.flow_test_2.BottomTopActivity;
import beg.hr.mvpdagger.screen_1.Screen1;
import beg.hr.mvpdagger.util.mvp.Mvp;
import beg.hr.mvpdagger.util.mvp.ViewPresenter;
import dagger.Provides;
import dagger.Subcomponent;
import flow.Flow;

/** Created by juraj on 16/07/16. */
@AutoValue
public abstract class HomeScreen implements Parcelable {

  public static HomeScreen create() {
    return new AutoValue_HomeScreen();
  }

  public Component component(ActivityComponent activityComponent, State initState) {
    return activityComponent.plus(new Module(initState));
  }

  @PerScreen
  @Subcomponent(modules = Module.class)
  public interface Component {
    Mvp.Link<Presenter, HomeView> mvp();
  }

  @PerScreen
  public static class Presenter extends ViewPresenter<HomeView> {

    private final State initState;

    @Inject
    public Presenter(State initState) {
      this.initState = initState;
    }

    @Override
    protected void onLoad() {
      super.onLoad();
      getView().setButtonText(initState.text());
    }

    void randomPressed() {
      // do nothing
      getView().setButtonText("Hello " + new Random().nextInt(100));
    }

    void button2Pressed() {
      Flow.get(getView()).set(Screen1.create());
    }

    public void backPressed() {
      Flow.get(getView()).goBack();
    }

    public void anotherFlowPressed() {
      Flow.get(getView()).set(BottomTopActivity.KEY);
    }
  }

  @dagger.Module
  public static class Module {

    private final State initState;

    public Module(State initState) {
      this.initState = initState;
    }

    @PerScreen
    @Provides
    HomeView view(@ActivityContext Context p_context) {
      return (HomeView) View.inflate(p_context, R.layout.screen_home, null);
    }

    @PerScreen
    @Provides
    Mvp.Link<Presenter, HomeView> mvp(Presenter p, HomeView v) {
      return Mvp.Link.create(p, v);
    }

    @PerScreen
    @Provides
    State initState() {
      return initState;
    }
  }

  @AutoValue
  public abstract static class State implements Parcelable {

    public static final String KEY = "HomeKey";

    public static State create(String text) {
      return new AutoValue_HomeScreen_State(text);
    }

    public static State defaultState() {
      return State.create("Default state");
    }

    abstract String text();
  }
}
