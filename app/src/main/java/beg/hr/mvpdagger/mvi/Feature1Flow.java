package beg.hr.mvpdagger.mvi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import beg.hr.mvpdagger.util.mvp.Bundleable;
import beg.hr.mvpdagger.util.mvp.FlowActivity;
import beg.hr.mvpdagger.util.view.ViewComponent;
import beg.hr.mvpdagger.util.view.ViewComponentFactory;
import flow.Direction;
import flow.TraversalCallback;

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
    return ViewComponentFactory.FEATURE1_COMPONENT;
  }

  @Nullable
  @Override
  protected Bundleable bundleable() {
    return null;
  }

  @Override
  protected void changeDialogKey(Object dialogKey) {}

  @Override
  protected boolean changeMainKey(Object mainKey, Direction direction, TraversalCallback callback) {
    View view = null;
    ViewComponent viewComponent = viewComponentFactory.create(mainKey);
    if (viewComponent != null) view = viewComponent.view();
    if (view != null) {
      showMainView(view, direction);
      return true;
    }
    return false;
  }
}
