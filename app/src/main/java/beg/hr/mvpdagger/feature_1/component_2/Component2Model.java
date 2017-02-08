package beg.hr.mvpdagger.feature_1.component_2;

import javax.annotation.Nullable;
import javax.inject.Inject;

import beg.hr.mvpdagger.atom.Transformations.Transform;
import beg.hr.mvpdagger.feature_1.component_2.Component2ViewDriver.State;
import beg.hr.mvpdagger.util.view.Event;
import beg.hr.mvpdagger.util.view.ViewComponentFactory;
import rx.Observable;

import static beg.hr.mvpdagger.feature_1.component_2.Component2ViewDriver.State.TEXT;
import static beg.hr.mvpdagger.feature_1.component_2.Component2ViewDriver.TYPE_BUTTON_BACK_PRESSED;
import static beg.hr.mvpdagger.feature_1.component_2.Component2ViewDriver.TYPE_BUTTON_PRESSED;
import static beg.hr.mvpdagger.feature_1.component_2.Component2ViewDriver.TYPE_TEXT_CHANGED;

/** Created by juraj on 31/01/2017. */
public class Component2Model {

  @Inject
  public Component2Model() {}

  Output from(Input input) {
    Observable<State> state = getState(input).share();
    Observable<Object> navigation = getNavigation(input).share();
    return new Output() {
      @Override
      public Observable<State> state() {
        return state;
      }

      @Override
      public Observable<Object> navigation() {
        return navigation;
      }
    };
  }

  private Observable<Object> getNavigation(Input input) {
    // TODO: 31/01/2017
    Observable<String> buttonEvent =
        input.events(TYPE_BUTTON_PRESSED).map(event -> ViewComponentFactory.FEATURE1_COMPONENT1);

    // TODO: 31/01/2017 This now maps this event just to type of event - map it to some navigation command
    Observable<String> goBackEvent = input.events(TYPE_BUTTON_BACK_PRESSED).map(Event::type);

    return Observable.merge(buttonEvent, goBackEvent);
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
    return new Transform<State>() {
      @Nullable
      @Override
      public State apply(@Nullable State state) {
        return State.create(text);
      }
    };
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
