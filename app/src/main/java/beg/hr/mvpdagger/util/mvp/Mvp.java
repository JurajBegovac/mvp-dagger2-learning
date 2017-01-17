package beg.hr.mvpdagger.util.mvp;

import com.google.auto.value.AutoValue;

import android.support.v4.view.ViewCompat;
import android.view.View;

/** TODO: Add a class header comment! */
public class Mvp {

  public interface Presenter<V> {
    void takeView(V view);

    void dropView(V view);
  }

  public interface HasPresenter<P extends Presenter<?>> {
    void setPresenter(P value);
  }

  @AutoValue
  public abstract static class Link<P extends Presenter<V>, V extends View & HasPresenter<P>> {

    public static <P extends Presenter<V>, V extends View & HasPresenter<P>> Link<P, V> create(
        final P presenter, final V view) {
      view.setPresenter(presenter);
      view.addOnAttachStateChangeListener(
          new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
              presenter.takeView(view);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
              presenter.dropView(view);
            }
          });

      if (ViewCompat.isAttachedToWindow(view)) presenter.takeView(view);

      return new AutoValue_Mvp_Link<>(presenter, view);
    }

    public abstract P presenter();

    public abstract V view();
  }
}
