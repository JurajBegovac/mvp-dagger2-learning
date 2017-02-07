package beg.hr.mvpdagger.util.flow;

import android.os.Bundle;
import android.support.annotation.Nullable;

import flow.State;

/** Created by juraj on 07/02/2017. */
public class FlowViewState2 implements ViewState {

  private final State state;

  public FlowViewState2(State state) {
    this.state = state;
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
        return FlowViewState2.child(this, key);
      }
    };
  }

  @Nullable
  @Override
  public Bundle get() {
    return state.getBundle();
  }

  @Override
  public void set(Bundle bundle) {
    state.setBundle(bundle);
  }

  @Override
  public ViewState child(String key) {
    return child(this, key);
  }
}
