package beg.hr.mvpdagger.util.mvp;

import android.support.v4.view.ViewCompat;
import android.view.View;

import com.google.auto.value.AutoValue;

/**
 * TODO: Add a class header comment!
 */
public class Mvp {

    public interface Presenter<V> {
        void takeView(V view);

        void dropView(V view);
    }

    public interface HasPresenter<P extends Presenter<?>> {
        void setPresenter(P value);
    }

    @AutoValue
    public static abstract class Link<P extends Presenter<V>, V extends View & HasPresenter<P>> {

        public abstract P presenter();

        public abstract V view();

        public static <P extends Presenter<V>, V extends View & HasPresenter<P>> Link<P, V> create(P p_presenter,
                                                                                                   V p_view) {
            p_view.setPresenter(p_presenter);
            p_view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    p_presenter.takeView(p_view);
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    p_presenter.dropView(p_view);
                }
            });

            if (ViewCompat.isAttachedToWindow(p_view)) p_presenter.takeView(p_view);

            return new AutoValue_Mvp_Link<>(p_presenter, p_view);
        }
    }

}
