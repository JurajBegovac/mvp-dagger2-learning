package beg.hr.mvpdagger.util.view;

import android.support.v4.view.ViewCompat;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.view.ViewAttachEvent;
import com.jakewharton.rxbinding.view.ViewAttachEvent.Kind;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import rx.Observable;

/**
 * Driver that binds to the view, inherits view lifecycle, and knows how to render state onto the
 * view
 */
public abstract class ViewDriver<V extends View, S> implements LifecycleProvider<ViewAttachEvent> {

  private final V view;
  private final ViewAttachEvent attachEvent;
  private final ViewAttachEvent detachEvent;

  protected ViewDriver(V view) {
    this.view = view;
    this.attachEvent = ViewAttachEvent.create(view, Kind.ATTACH);
    this.detachEvent = ViewAttachEvent.create(view, Kind.DETACH);
  }

  public V view() {
    return view;
  }

  public boolean isAttach(ViewAttachEvent e) {
    return e.equals(attachEvent);
  }

  public boolean isDetach(ViewAttachEvent e) {
    return e.equals(detachEvent);
  }

  @Nonnull
  @CheckReturnValue
  public Observable<ViewAttachEvent> lifecycle() {
    Observable<ViewAttachEvent> events = RxView.attachEvents(this.view);
    if (ViewCompat.isAttachedToWindow(view)) events = events.startWith(attachEvent);
    return events;
  }

  @Nonnull
  @CheckReturnValue
  public <T> LifecycleTransformer<T> bindUntilEvent(@Nonnull ViewAttachEvent event) {
    return RxLifecycle.bindUntilEvent(lifecycle(), event);
  }

  @Nonnull
  @CheckReturnValue
  public <T> LifecycleTransformer<T> bindToLifecycle() {
    return RxLifecycleAndroid.bindView(this.view);
  }

  @Nonnull
  @CheckReturnValue
  public <T> LifecycleTransformer<T> bindUntilDetach() {
    return bindUntilEvent(ViewAttachEvent.create(view, Kind.DETACH));
  }

  public abstract Observable<Event> bind(String type);

  public abstract void render(S state);
}
