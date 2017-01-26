package beg.hr.mvpdagger.util.view;

import android.view.View;

/** A view component that gets access to the driver and the view */
public abstract class ViewDriverComponent<T extends ViewDriver> implements ViewComponent {

  private final T driver;

  public ViewDriverComponent(T driver) {
    this.driver = driver;
  }

  protected T driver() {
    return driver;
  }

  @Override
  public View view() {
    return driver.view();
  }
}
