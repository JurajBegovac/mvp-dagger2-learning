package beg.hr.mvpdagger.screen_1;

import com.google.auto.value.AutoValue;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;

import javax.inject.Inject;

import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.di.dagger2.components.ActivityComponent;
import beg.hr.mvpdagger.di.dagger2.qualifiers.ActivityContext;
import beg.hr.mvpdagger.di.dagger2.scopes.PerScreen;
import beg.hr.mvpdagger.screen_2.Screen2;
import beg.hr.mvpdagger.util.mvp.Mvp;
import beg.hr.mvpdagger.util.mvp.ViewPresenter;
import dagger.Provides;
import dagger.Subcomponent;
import flow.Flow;

/** Created by juraj on 17/01/2017. */
@AutoValue
public abstract class Screen1 implements Parcelable {

  public static Screen1 create() {
    return new AutoValue_Screen1();
  }

  public Component component(ActivityComponent activityComponent, State initState) {
    return activityComponent.plus(new Module(initState));
  }

  @PerScreen
  @Subcomponent(modules = Module.class)
  public interface Component {
    Mvp.Link<Presenter, View1> mvp();
  }

  @dagger.Module
  public static class Module {

    private State initState;

    public Module(State initState) {
      this.initState = initState;
    }

    @PerScreen
    @Provides
    State state() {
      return initState;
    }

    @PerScreen
    @Provides
    View1 view(@ActivityContext Context p_context) {
      return (View1) View.inflate(p_context, R.layout.view_1, null);
    }

    @PerScreen
    @Provides
    Mvp.Link<Presenter, View1> mvp(Presenter p, View1 v) {
      return Mvp.Link.create(p, v);
    }
  }

  @PerScreen
  public static class Presenter extends ViewPresenter<View1> {

    private State state;

    @Inject
    public Presenter(State state) {
      this.state = state;
    }

    @Override
    protected void onLoad() {
      super.onLoad();
      getView().setText(state.editedText());
    }

    void backPressed() {
      Flow.get(getView()).goBack();
    }

    void dialogPressed() {
      Flow.get(getView()).set(Screen2.create());
    }
  }

  @AutoValue
  public abstract static class State implements Parcelable {

    public static final String KEY = "key_screen_1";

    public static State create(String editedText) {
      return new AutoValue_Screen1_State(editedText);
    }

    public static State defaultState() {
      return State.create("");
    }

    abstract String editedText();
  }
}
