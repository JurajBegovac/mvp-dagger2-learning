package beg.hr.mvpdagger.feature_1.composite_component;

import android.content.Context;
import android.view.View;

import javax.inject.Inject;

import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.di.dagger2.qualifiers.ActivityContext;
import beg.hr.mvpdagger.di.dagger2.scopes.PerScreen;
import beg.hr.mvpdagger.util.flow.ViewState;
import beg.hr.mvpdagger.util.view.Event;
import beg.hr.mvpdagger.util.view.ViewComponent;
import beg.hr.mvpdagger.util.view.ViewComponentFactory;
import dagger.Provides;
import dagger.Subcomponent;
import rx.Observable;

import static beg.hr.mvpdagger.util.view.ViewComponentFactory.FEATURE1_COMPONENT1;
import static beg.hr.mvpdagger.util.view.ViewComponentFactory.FEATURE1_COMPONENT2;

/** Created by juraj on 01/02/2017. */
public class CompositeViewComponent implements ViewComponent<CompositeView> {

  private final CompositeView view;
  //  private ViewState viewState;

  @Inject
  public CompositeViewComponent(
      CompositeView view, ViewComponentFactory viewComponentFactory, ViewState viewState) {
    this.view = view;

    viewComponentFactory.create(
        FEATURE1_COMPONENT1, view.component1View, viewState.child(FEATURE1_COMPONENT1));
    viewComponentFactory.create(
        FEATURE1_COMPONENT2, view.component2View, viewState.child(FEATURE1_COMPONENT2));
  }

  @Override
  public CompositeView view() {
    return view;
  }

  @Override
  public Observable<Event> events(String type) {
    return null;
  }

  @PerScreen
  @Subcomponent(modules = Module.class)
  public interface ObjectGraph {
    CompositeViewComponent viewComponent();
  }

  @dagger.Module
  public static class Module {

    private final ViewState viewState;

    public Module(ViewState viewState) {
      this.viewState = viewState;
    }

    @PerScreen
    @Provides
    public ViewState viewStateManager() {
      return viewState;
    }

    @PerScreen
    @Provides
    public CompositeView view(@ActivityContext Context context) {
      return (CompositeView) View.inflate(context, R.layout.screen_composite, null);
    }
  }
}
