package beg.hr.mvpdagger.mvi.feature_1;

import javax.inject.Inject;

import beg.hr.mvpdagger.mvi.atom.Transformations.Transform;
import beg.hr.mvpdagger.mvi.feature_1.Feature1ViewDriver.State;
import beg.hr.mvpdagger.util.view.Event;
import rx.Observable;

/** Created by juraj on 26/01/2017. */
public class Feature1Model {

  @Inject
  public Feature1Model() {}

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
        return null;
      }
    };
  }

  private Observable<State> getState(Input input) {
    Observable<Transform<State>> textReducers =
        input
            .events(Feature1ViewDriver.TYPE_TEXT_CHANGED)
            .distinctUntilChanged()
            .map(event -> event.data().get(State.TEXT))
            .cast(String.class)
            .map(this::toTextReducer);

    return textReducers.scan(input.initState(), (state, event) -> event.apply(state));
  }

  private Transform<State> toTextReducer(String text) {
    return state -> State.create(text);
  }

  interface Output {
    Observable<State> state();

    Observable<Object> navigation();
  }

  interface Input {
    State initState();

    Observable<Event> events(String type);
  }
}
