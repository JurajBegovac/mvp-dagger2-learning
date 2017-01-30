package beg.hr.mvpdagger.mvi.feature_1;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.Arrays;

import javax.inject.Inject;

import beg.hr.mvpdagger.mvi.feature_1.Feature1ViewDriver.State;
import beg.hr.mvpdagger.util.view.Event;
import beg.hr.mvpdagger.util.view.Event.Builder;
import beg.hr.mvpdagger.util.view.ViewDriver;
import rx.Observable;

/** Created by juraj on 26/01/2017. */
public class Feature1ViewDriver extends ViewDriver<Feature1View, State> {

  public static final String BASE = "view:feature1:";
  public static final String TYPE_BUTTON_PRESSED = BASE + "button_pressed";
  public static final String TYPE_TEXT_CHANGED = BASE + "text_changed";

  private static final int FLAG_TEXT = 0;

  private State currentState;
  private Observable<Event> events;

  @Inject
  public Feature1ViewDriver(Feature1View view) {
    super(view);
    events =
        Observable.merge(
                Binder.bind(view, TYPE_BUTTON_PRESSED), Binder.bind(view, TYPE_TEXT_CHANGED))
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
    view().setTag(currentState);
  }

  private void render(boolean[] diff, State state) {
    if (diff[FLAG_TEXT]) view().setText(state.text());
  }

  private boolean[] diff(@Nullable State s0, @NonNull State s1) {
    boolean[] diff = new boolean[1];
    if (s0 == null) {
      Arrays.fill(diff, true);
      return diff;
    }
    if (!s0.text().equals(s1.text())) diff[FLAG_TEXT] = true;
    return diff;
  }

  private static class Binder {
    @NonNull
    static Observable<Event> bind(Feature1View view, String type) {
      Builder builder = Event.builder().origin("feature1_view_driver");
      if (TYPE_BUTTON_PRESSED.equals(type))
        return RxView.clicks(view.getButton()).map(e -> builder.type(TYPE_BUTTON_PRESSED).build());
      if (TYPE_TEXT_CHANGED.equals(type))
        return RxTextView.textChanges(view.getTextInput())
            .skip(1) // FIXME: 30/01/2017 not working without this on orientation changes
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
    public static final String TAG = "state:feature1";
    public static final String TEXT = "text";

    public static State defaultState() {
      return State.create("");
    }

    public static State create(String text) {
      return new AutoValue_Feature1ViewDriver_State(text);
    }

    public abstract String text();
  }
}