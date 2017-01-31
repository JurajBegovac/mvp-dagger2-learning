package beg.hr.mvpdagger.util.view;

import android.view.View;

import rx.Observable;

/** TODO: Add a class header comment! */
public interface ViewComponent<V extends View> {

  V view();

  Observable<Event> events(String type);
}
