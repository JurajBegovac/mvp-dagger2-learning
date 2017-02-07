package beg.hr.mvpdagger.feature_1.component_2;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.jakewharton.rxbinding.view.ViewAttachEvent;

import javax.inject.Inject;

import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.di.dagger2.qualifiers.ActivityContext;
import beg.hr.mvpdagger.di.dagger2.scopes.PerScreen;
import beg.hr.mvpdagger.feature_1.component_2.Component2Model.Input;
import beg.hr.mvpdagger.feature_1.component_2.Component2Model.Output;
import beg.hr.mvpdagger.feature_1.component_2.Component2ViewDriver.State;
import beg.hr.mvpdagger.util.flow.ViewState;
import beg.hr.mvpdagger.util.view.Event;
import beg.hr.mvpdagger.util.view.ViewDriverComponent;
import dagger.Provides;
import dagger.Subcomponent;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static beg.hr.mvpdagger.feature_1.component_2.Component2ViewDriver.TYPE_BUTTON_BACK_PRESSED;
import static beg.hr.mvpdagger.util.view.ViewComponentFactory.FEATURE1_COMPONENT1;

/** Created by juraj on 31/01/2017. */
public class Component2ViewComponent extends ViewDriverComponent<Component2ViewDriver>
    implements Input {

  private final Component2Model model;
  private final ViewState viewState;

  @Inject
  public Component2ViewComponent(
      Component2ViewDriver driver, Component2Model model, ViewState viewState) {
    super(driver);
    this.model = model;
    this.viewState = viewState;

    driver.lifecycle().filter(driver::isAttach).subscribe(this::onAttach);
    driver.lifecycle().filter(driver::isDetach).subscribe(this::onDetach);
  }

  @Override
  public State initState() {
    Bundle initBundle = viewState.get();
    if (initBundle != null && initBundle.containsKey(State.TAG))
      return initBundle.getParcelable(State.TAG);
    return State.defaultState();
  }

  @Override
  public Observable<Event> events(String type) {
    return driver().bind(type);
  }

  private void onDetach(ViewAttachEvent e) {
    viewState.set(bundleToSave());
  }

  private Bundle bundleToSave() {
    State state = driver().currentState();
    if (state == null) return new Bundle();

    Bundle bundle = new Bundle();
    bundle.putParcelable(State.TAG, state);
    return bundle;
  }

  private void onAttach(ViewAttachEvent e) {
    Output out = model.from(this);

    out.state()
        .observeOn(AndroidSchedulers.mainThread())
        .compose(driver().bindUntilDetach())
        .subscribe(driver()::render);

    out.navigation()
        .observeOn(AndroidSchedulers.mainThread())
        .compose(driver().bindUntilDetach())
        .filter(o -> o.equals(TYPE_BUTTON_BACK_PRESSED))
        .subscribe(o -> driver().goBack());

    // TODO: 31/01/2017 Handle other navigation
    out.navigation()
        .observeOn(AndroidSchedulers.mainThread())
        .compose(driver().bindUntilDetach())
        .filter(FEATURE1_COMPONENT1::equals)
        .subscribe(o -> driver().goTo(o));
  }

  @PerScreen
  @Subcomponent(modules = Module.class)
  public interface ObjectGraph {
    Component2ViewComponent viewComponent();
  }

  @dagger.Module
  public static class Module {

    private final View view;
    private final ViewState viewState;

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
    public Component2View view(@ActivityContext Context context) {
      if (view != null) return (Component2View) view;
      return (Component2View) View.inflate(context, R.layout.screen_component_2, null);
    }
  }
}
