package beg.hr.mvpdagger.feature_1.component_1;

import javax.inject.Inject;

import beg.hr.mvpdagger.atom.Transformations.Transform;
import beg.hr.mvpdagger.feature_1.component_1.Component1ViewDriver.State;
import beg.hr.mvpdagger.util.view.Event;
import rx.Observable;

import static beg.hr.mvpdagger.feature_1.component_1.Component1ViewDriver.State.TEXT;
import static beg.hr.mvpdagger.feature_1.component_1.Component1ViewDriver.State.create;
import static beg.hr.mvpdagger.feature_1.component_1.Component1ViewDriver.TYPE_TEXT_CHANGED;
import static beg.hr.mvpdagger.util.view.ViewComponentFactory.FEATURE1_COMPONENT2;

/** Created by juraj on 26/01/2017. */
public class Component1Model {

  @Inject
  public Component1Model() {}

  Output from(Input input) {
    Observable<State> state = getState(input).share();
    return new Output() {
      @Override
      public Observable<State> state() {
        return state;
      }

      @Override
      public Observable<Object> navigation() {

        // TODO: 26/01/2017
        return input
            .events(Component1ViewDriver.TYPE_BUTTON_PRESSED)
            .map(event -> FEATURE1_COMPONENT2);
      }
    };
  }

  private Observable<State> getState(Input input) {
    Observable<Transform<State>> textReducers =
        input
            .events(TYPE_TEXT_CHANGED)
            .distinctUntilChanged()
            .map(event -> event.data().get(TEXT))
            .cast(String.class)
            .map(this::toTextReducer);

    return textReducers.scan(
        input.initState(), (state, stateTransform) -> stateTransform.apply(state));
  }

  private Transform<State> toTextReducer(String text) {
    return state -> create(text);
  }

  public interface Output {
    Observable<State> state();

    Observable<Object> navigation();
  }

  public interface Input {
    State initState();

    Observable<Event> events(String type);
  }
}
