package beg.hr.mvpdagger.feature_1.component_1;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.jakewharton.rxbinding.view.ViewAttachEvent;

import javax.inject.Inject;

import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.di.dagger2.qualifiers.ActivityContext;
import beg.hr.mvpdagger.di.dagger2.scopes.PerScreen;
import beg.hr.mvpdagger.feature_1.component_1.Component1Model.Input;
import beg.hr.mvpdagger.feature_1.component_1.Component1Model.Output;
import beg.hr.mvpdagger.feature_1.component_1.Component1ViewDriver.State;
import beg.hr.mvpdagger.util.flow.ViewState;
import beg.hr.mvpdagger.util.view.Event;
import beg.hr.mvpdagger.util.view.ViewDriverComponent;
import dagger.Provides;
import dagger.Subcomponent;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/** Created by juraj on 26/01/2017. */
public class Component1ViewComponent extends ViewDriverComponent<Component1ViewDriver>
    implements Input {

  private final Component1Model model;
  private final ViewState viewState;

  @Inject
  public Component1ViewComponent(
      ViewState viewState, Component1ViewDriver driver, Component1Model model) {
    super(driver);
    this.model = model;
    this.viewState = viewState;

    driver.lifecycle().filter(driver::isAttach).subscribe(this::onAttach);
    driver.lifecycle().filter(driver::isDetach).subscribe(this::onDetach);
  }

  @Override
  public State initState() {
    Bundle bundle = viewState.get();
    if (bundle != null && bundle.containsKey(State.TAG)) return bundle.getParcelable(State.TAG);
    return State.defaultState();
  }

  @Override
  public Observable<Event> events(String type) {
    return driver().bind(type);
  }

  private Bundle bundleToSave() {
    State state = driver().currentState();
    if (state == null) return new Bundle();

    Bundle bundle = new Bundle();
    bundle.putParcelable(State.TAG, state);
    return bundle;
  }

  private void onDetach(ViewAttachEvent e) {
    viewState.set(bundleToSave());
  }

  private void onAttach(ViewAttachEvent e) {
    Output out = model.from(this);

    out.state()
        .observeOn(AndroidSchedulers.mainThread())
        .compose(driver().bindUntilDetach())
        .subscribe(driver()::render);

    // TODO: 26/01/2017 navigation
    out.navigation()
        .observeOn(AndroidSchedulers.mainThread())
        .compose(driver().bindUntilDetach())
        .subscribe(o -> driver().goTo(o));
  }

  @PerScreen
  @Subcomponent(modules = Module.class)
  public interface ObjectGraph {
    Component1ViewComponent viewComponent();
  }

  @dagger.Module
  public static class Module {

    private final ViewState viewState;
    private final View view;

    public Module(View view, ViewState viewState) {
      this.view = view;
      this.viewState = viewState;
    }

    @PerScreen
    @Provides
    public ViewState viewStateManager() {
      return viewState;
    }

    @PerScreen
    @Provides
    public Component1View view(@ActivityContext Context context) {
      if (view != null) return (Component1View) view;
      return (Component1View) View.inflate(context, R.layout.screen_component_1, null);
    }
  }
}
