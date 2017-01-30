package beg.hr.mvpdagger.mvi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import beg.hr.mvpdagger.mvi.feature_1.Feature1ViewDriver;
import beg.hr.mvpdagger.util.mvp.Bundleable;
import beg.hr.mvpdagger.util.mvp.FlowActivity;
import beg.hr.mvpdagger.util.view.ViewComponent;
import beg.hr.mvpdagger.util.view.ViewComponentFactory;
import flow.Direction;
import flow.State;
import flow.TraversalCallback;

import static beg.hr.mvpdagger.util.view.ViewComponentFactory.FEATURE1_COMPONENT;

public class Feature1Flow extends FlowActivity {

  private ViewComponentFactory viewComponentFactory;

  public static Intent getStartIntent(Context context) {
    return new Intent(context, Feature1Flow.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    viewComponentFactory = new ViewComponentFactory(this);
    super.onCreate(savedInstanceState);
  }

  @Override
  protected Object initScreen() {
    return FEATURE1_COMPONENT;
  }

  @Nullable
  @Override
  protected Bundleable bundleable() {
    return new Bundleable() {
      @Override
      public Bundle createBundle(Object key, @Nullable View view, @Nullable Dialog dialog) {
        if (view == null) return Bundle.EMPTY;
        Bundle bundle = new Bundle();
        if (FEATURE1_COMPONENT.equals(key)) {
          Object state = view.getTag();
          if (state != null && state instanceof Feature1ViewDriver.State) {
            bundle.putParcelable(Feature1ViewDriver.State.TAG, (Feature1ViewDriver.State) state);
          }
        }
        return bundle;
      }

      @Override
      public Object initViewState(State state) {
        Object key = state.getKey();
        Bundle bundle = state.getBundle();
        if (FEATURE1_COMPONENT.equals(key)) {
          if (bundle != null && bundle.containsKey(Feature1ViewDriver.State.TAG)) {
            return bundle.getParcelable(Feature1ViewDriver.State.TAG);
          }
          return Feature1ViewDriver.State.defaultState();
        }
        return null;
      }
    };
  }

  @Override
  protected void changeDialogKey(Object dialogKey) {}

  @Override
  protected boolean changeMainKey(Object mainKey, Direction direction, TraversalCallback callback) {
    View view = null;
    ViewComponent viewComponent =
        viewComponentFactory.create(mainKey, flowDispatcher.initViewState(mainKey));
    if (viewComponent != null) view = viewComponent.view();
    if (view != null) {
      showMainView(view, direction);
      return true;
    }
    return false;
  }
}
