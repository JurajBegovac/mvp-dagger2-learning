package beg.hr.mvpdagger.mvi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import beg.hr.mvpdagger.mvi.feature_1.Feature1InitialViewStateFactory;
import beg.hr.mvpdagger.util.mvp.FlowActivity;
import beg.hr.mvpdagger.util.mvp.ViewStateManager;
import beg.hr.mvpdagger.util.view.ViewComponent;
import beg.hr.mvpdagger.util.view.ViewComponentFactory;
import flow.Direction;
import flow.TraversalCallback;

import static beg.hr.mvpdagger.util.view.ViewComponentFactory.FEATURE1_COMPONENT;

public class Feature1Flow extends FlowActivity {

  private ViewComponentFactory viewComponentFactory;
  private ViewComponent viewComponent;

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
  protected ViewStateManager viewStateManager() {
    return new ViewStateManager() {
      @Override
      public Bundle createBundle(Object key, @Nullable View view, @Nullable Dialog dialog) {
        // deprecated
        if (viewComponent == null) return Bundle.EMPTY;
        return viewComponent.saveState();
      }

      @Override
      public Bundle createBundle() {
        if (viewComponent == null) return Bundle.EMPTY;
        return viewComponent.saveState();
      }

      @Override
      public Object initialViewState(Object key, Bundle bundle) {
        return Feature1InitialViewStateFactory.state(key, bundle);
      }
    };
  }

  @Override
  protected void changeDialogKey(Object dialogKey) {}

  @Override
  protected boolean changeMainKey(Object mainKey, Direction direction, TraversalCallback callback) {
    View view = null;
    viewComponent = viewComponentFactory.create(mainKey, initialViewState(mainKey));
    if (viewComponent != null) view = viewComponent.view();
    if (view != null) {
      showMainView(view, direction);
      return true;
    }
    return false;
  }
}
