package beg.hr.mvpdagger.util.flow;

import android.os.Bundle;
import android.support.annotation.Nullable;

/** Created by juraj on 01/02/2017. */
public class FlowViewState implements ViewState {

  private final Object key;
  private final BaseDispatcher flowDispatcher;

  public FlowViewState(Object key, BaseDispatcher flowDispatcher) {
    this.key = key;
    this.flowDispatcher = flowDispatcher;
  }

  private static ViewState child(ViewState parent, String key) {
    return new ViewState() {
      @Nullable
      @Override
      public Bundle get() {
        if (parent.get() == null) return null;
        return parent.get().getBundle(key);
      }

      @Override
      public void set(Bundle bundle) {
        if (parent.get() == null) {
          parent.set(new Bundle());
        }
        parent.get().putBundle(key, bundle);
      }

      @Override
      public ViewState child(String key) {
        return FlowViewState.child(this, key);
      }
    };
  }

  @Nullable
  @Override
  public Bundle get() {
    return flowDispatcher.getBundle(key);
  }

  @Override
  public void set(Bundle bundle) {
    flowDispatcher.save(key, bundle);
  }

  @Override
  public ViewState child(String key) {
    return child(this, key);
  }
}
