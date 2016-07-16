package beg.hr.mvpdagger.util.mvp;

/**
 * Created by juraj on 07/07/16.
 */
public interface Presenter<V> {
    void takeView(V view);
    void dropView(V view);
}
