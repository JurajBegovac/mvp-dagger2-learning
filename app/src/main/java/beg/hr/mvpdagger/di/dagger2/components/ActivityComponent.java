package beg.hr.mvpdagger.di.dagger2.components;

import android.content.Context;

import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.di.dagger2.qualifiers.ActivityContext;
import beg.hr.mvpdagger.di.dagger2.scopes.PerActivity;
import beg.hr.mvpdagger.feature_1.component_1.Component1ViewComponent;
import beg.hr.mvpdagger.feature_1.component_2.Component2ViewComponent;
import beg.hr.mvpdagger.feature_1.composite_component.CompositeViewComponent;
import beg.hr.mvpdagger.feature_1.composite_component.CompositeViewComponent.ObjectGraph;
import beg.hr.mvpdagger.util.view.ViewComponentFactory;
import dagger.Subcomponent;

/** Created by juraj on 16/07/16. */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
  @ActivityContext
  Context context();

  Component1ViewComponent.ObjectGraph plus(Component1ViewComponent.Module module);

  Component2ViewComponent.ObjectGraph plus(Component2ViewComponent.Module module);

  ViewComponentFactory viewComponentFactory();

  ObjectGraph plus(CompositeViewComponent.Module module);
}
