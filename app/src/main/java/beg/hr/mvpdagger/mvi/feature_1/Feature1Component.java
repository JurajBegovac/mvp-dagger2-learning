package beg.hr.mvpdagger.mvi.feature_1;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.jakewharton.rxbinding.view.ViewAttachEvent;

import javax.inject.Inject;

import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.di.dagger2.qualifiers.ActivityContext;
import beg.hr.mvpdagger.di.dagger2.scopes.PerScreen;
import beg.hr.mvpdagger.mvi.feature_1.Feature1Model.Input;
import beg.hr.mvpdagger.mvi.feature_1.Feature1Model.Output;
import beg.hr.mvpdagger.mvi.feature_1.Feature1ViewDriver.State;
import beg.hr.mvpdagger.util.mvp.ViewStateManager2;
import beg.hr.mvpdagger.util.view.Event;
import beg.hr.mvpdagger.util.view.ViewDriverComponent;
import dagger.Provides;
import dagger.Subcomponent;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static beg.hr.mvpdagger.util.view.ViewComponentFactory.FEATURE1_COMPONENT;

/** Created by juraj on 26/01/2017. */
public class Feature1Component extends ViewDriverComponent<Feature1ViewDriver> implements Input {

  private final Feature1Model model;
  private final ViewStateManager2 viewStateManager;

  @Inject
  public Feature1Component(
      ViewStateManager2 viewStateManager, Feature1ViewDriver driver, Feature1Model model) {
    super(driver);
    this.model = model;
    this.viewStateManager = viewStateManager;

    driver.lifecycle().filter(driver::isAttach).subscribe(this::onAttach);
    driver.lifecycle().filter(driver::isDetach).subscribe(this::onDetach);
  }

  @Override
  public State initState() {
    Bundle bundle = viewStateManager.getBundle(FEATURE1_COMPONENT);
    if (bundle != null && bundle.containsKey(State.TAG)) return bundle.getParcelable(State.TAG);
    return State.defaultState();
  }

  @Override
  public Observable<Event> events(String type) {
    return driver().bind(type);
  }

  private Bundle bundleToSave() {
    State state = driver().currentState();
    if (state == null) return Bundle.EMPTY;

    Bundle bundle = new Bundle();
    bundle.putParcelable(State.TAG, state);
    return bundle;
  }

  private void onDetach(ViewAttachEvent e) {
    viewStateManager.saveState(FEATURE1_COMPONENT, bundleToSave());
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
    Feature1Component viewComponent();
  }

  @dagger.Module
  public static class Module {

    private final ViewStateManager2 viewStateManager;

    public Module(ViewStateManager2 viewStateManager) {
      this.viewStateManager = viewStateManager;
    }

    @PerScreen
    @Provides
    public ViewStateManager2 viewStateManager() {
      return viewStateManager;
    }

    @PerScreen
    @Provides
    public Feature1View view(@ActivityContext Context context) {
      return (Feature1View) View.inflate(context, R.layout.screen_feature_1, null);
    }
  }
}
