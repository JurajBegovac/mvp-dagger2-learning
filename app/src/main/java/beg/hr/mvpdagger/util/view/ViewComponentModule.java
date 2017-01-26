package beg.hr.mvpdagger.util.view;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * TODO: Add a class header comment!
 */
@dagger.Module
public abstract class ViewComponentModule<T, V extends View> {

  private final T config;
  private final V view;

  protected ViewComponentModule(@Nullable T config, @Nullable V view) {
    this.config = config;
    this.view = view;
  }

  @Nullable
  public T config() {
    return config;
  }

  @Nullable
  public V view() {
    return view;
  }
}
