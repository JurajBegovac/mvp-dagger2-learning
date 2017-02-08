package beg.hr.mvpdagger.feature_1.component_2;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import beg.hr.mvpdagger.feature_1.component_2.Component2ViewDriver.State;
import beg.hr.mvpdagger.util.view.Event;
import beg.hr.mvpdagger.util.view.Event.Builder;
import beg.hr.mvpdagger.util.view.ViewDriver;
import flow.Flow;
import rx.Observable;

/** Created by juraj on 31/01/2017. */
public class Component2ViewDriver extends ViewDriver<Component2View, State> {

  public static final String BASE = "view:component2:";
  public static final String TYPE_BUTTON_PRESSED = BASE + "button_dialog_pressed";
  public static final String TYPE_BUTTON_BACK_PRESSED = BASE + "button_back_pressed";
  public static final String TYPE_TEXT_CHANGED = BASE + "text_changed";

  private static final int FLAG_TEXT = 0;

  private State currentState;
  private Observable<Event> events;

  @Inject
  public Component2ViewDriver(Component2View view) {
    super(view);
    events =
        Observable.merge(
                Binder.bind(view, TYPE_BUTTON_PRESSED),
                Binder.bind(view, TYPE_BUTTON_BACK_PRESSED),
                Binder.bind(view, TYPE_TEXT_CHANGED))
            .share();
  }

  @Override
  public Observable<Event> bind(String type) {
    return events.filter(event -> event.type().equals(type));
  }

  @Override
  public void render(State state) {
    boolean[] diff = diff(currentState, state);
    render(diff, state);
    currentState = state;
  }

  private void render(boolean[] diff, State state) {
    if (diff[FLAG_TEXT]) view().setText(state.text());
  }

  private boolean[] diff(State s0, State s1) {
    boolean[] diff = new boolean[1];
    if (s0 == null) {
      Arrays.fill(diff, true);
      return diff;
    }
    if (!s0.text().equals(s1.text())) diff[FLAG_TEXT] = true;
    return diff;
  }

  public State currentState() {
    return currentState;
  }

  public void goBack() {
    // todo move this to some NavigationFactory class
    Flow.get(view()).goBack();
  }

  public void goTo(Object o) {
    // todo move this to some NavigationFactory class
    Flow.get(view()).set(o);
  }

  private static class Binder {
    @NonNull
    static Observable<Event> bind(Component2View view, String type) {
      Builder builder = Event.builder().origin("component2_view_driver");
      if (TYPE_BUTTON_PRESSED.equals(type))
        return RxView.clicks(view.btn).map(e -> builder.type(TYPE_BUTTON_PRESSED).build());
      if (TYPE_BUTTON_BACK_PRESSED.equals(type))
        return RxView.clicks(view.back).map(e -> builder.type(TYPE_BUTTON_BACK_PRESSED).build());
      if (TYPE_TEXT_CHANGED.equals(type))
        return RxTextView.textChanges(view.editText)
            .skip(1) // FIXME: 30/01/2017 not working without this on orientation changes
            .debounce(50, TimeUnit.MILLISECONDS)
            .map(
                text ->
                    builder
                        .type(TYPE_TEXT_CHANGED)
                        .data(ImmutableMap.of(State.TEXT, text.toString()))
                        .build());
      return Observable.empty();
    }
  }

  @AutoValue
  public abstract static class State implements Parcelable {
    public static final String TAG = "state:component2";
    public static final String TEXT = "text";

    public static State defaultState() {
      return State.create("");
    }

    public static State create(String text) {
      return new AutoValue_Component2ViewDriver_State(text);
    }

    public abstract String text();
  }
}
