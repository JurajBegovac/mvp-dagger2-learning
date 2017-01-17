package beg.hr.mvpdagger.screen_2;

import com.google.auto.value.AutoValue;

import android.content.Context;
import android.view.View;

import javax.inject.Inject;

import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.di.dagger2.components.ActivityComponent;
import beg.hr.mvpdagger.di.dagger2.qualifiers.ActivityContext;
import beg.hr.mvpdagger.di.dagger2.scopes.PerScreen;
import beg.hr.mvpdagger.util.mvp.Mvp;
import beg.hr.mvpdagger.util.mvp.ViewPresenter;
import dagger.Provides;
import dagger.Subcomponent;
import flow.Flow;

/** Created by juraj on 17/01/2017. */
@AutoValue
public abstract class Screen2 {

  public static Screen2 create() {
    return new AutoValue_Screen2();
  }

  public Component component(ActivityComponent activityComponent) {
    return activityComponent.plus(new Module());
  }

  @PerScreen
  @Subcomponent(modules = Module.class)
  public interface Component {
    Mvp.Link<Presenter, View2> mvp();
  }

  @dagger.Module
  public static class Module {

    @PerScreen
    @Provides
    View2 provideView(@ActivityContext Context p_context) {
      return (View2) View.inflate(p_context, R.layout.view_2, null);
    }

    @PerScreen
    @Provides
    Mvp.Link<Presenter, View2> mvp(Presenter p, View2 v) {
      return Mvp.Link.create(p, v);
    }
  }

  @PerScreen
  public static class Presenter extends ViewPresenter<View2> {

    @Inject
    public Presenter() {}

    public void backPressed() {
      Flow.get(getView()).goBack();
    }
  }
}
