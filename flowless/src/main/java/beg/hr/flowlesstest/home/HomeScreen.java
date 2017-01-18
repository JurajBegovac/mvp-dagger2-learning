package beg.hr.flowlesstest.home;

import com.google.auto.value.AutoValue;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;

import java.util.Random;

import javax.inject.Inject;

import beg.hr.flowlesstest.R;
import beg.hr.flowlesstest.dagger2.components.ActivityComponent;
import beg.hr.flowlesstest.dagger2.qualifiers.ActivityContext;
import beg.hr.flowlesstest.dagger2.scopes.PerScreen;
import beg.hr.flowlesstest.mvp.Mvp;
import beg.hr.flowlesstest.mvp.ViewPresenter;
import dagger.Provides;
import dagger.Subcomponent;

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
      //      Flow.get(getView()).set(Screen1.create());
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
