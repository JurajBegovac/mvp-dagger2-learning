package beg.hr.mvpdagger.feature_1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import beg.hr.mvpdagger.MvpDaggerApplication;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.util.flow.FlowActivity;
import beg.hr.mvpdagger.util.view.ViewComponent;
import beg.hr.mvpdagger.util.view.ViewComponentFactory;
import flow.Direction;
import flow.TraversalCallback;

import static beg.hr.mvpdagger.util.view.ViewComponentFactory.FEATURE1_COMPOSITE_COMPONENT;

public class Feature1Flow extends FlowActivity {

  public static final String DUMMY_KEY = "dummy_key";
  private ViewComponentFactory viewComponentFactory;

  public static Intent getStartIntent(Context context) {
    return new Intent(context, Feature1Flow.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    viewComponentFactory =
        MvpDaggerApplication.component().plus(new ActivityModule(this)).viewComponentFactory();
    super.onCreate(savedInstanceState);
  }

  @Override
  protected Object initScreen() {
    return FEATURE1_COMPOSITE_COMPONENT;
  }

  @Override
  protected void changeDialogKey(Object dialogKey) {}

  @Override
  protected boolean changeMainKey(Object mainKey, Direction direction, TraversalCallback callback) {
    View view = null;
    ViewComponent viewComponent =
        viewComponentFactory.create(mainKey, null, viewStateManager(mainKey));
    if (viewComponent != null) view = viewComponent.view();
    else if (mainKey.equals(DUMMY_KEY)) {
      view = new TextView(this);
      ((TextView) view).setText("Dummy");
    }

    if (view != null) {
      showMainView(view, direction);
      return true;
    }
    return false;
  }
}
