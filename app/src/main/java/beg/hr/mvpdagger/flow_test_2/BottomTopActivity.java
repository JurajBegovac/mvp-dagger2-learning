package beg.hr.mvpdagger.flow_test_2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

import beg.hr.mvpdagger.MvpDaggerApplication;
import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.di.dagger2.components.ActivityComponent;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.home.HomeScreen;
import beg.hr.mvpdagger.screen_1.Screen1;
import beg.hr.mvpdagger.screen_2.Screen2;
import beg.hr.mvpdagger.util.mvp.ViewStateManager;
import beg.hr.mvpdagger.util.mvp.FlowActivity;
import flow.Direction;
import flow.Flow;
import flow.TraversalCallback;

public class BottomTopActivity extends FlowActivity {
  public static final Object KEY = "new_flow";

  private static final String INIT_SCREEN = "init_screen";

  private ActivityComponent component;

  public static Intent getStartIntent(Context context, Parcelable screen) {
    Intent intent = new Intent(context, BottomTopActivity.class);
    intent.putExtra(INIT_SCREEN, screen);
    return intent;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    component = MvpDaggerApplication.component().plus(new ActivityModule(this));
    super.onCreate(savedInstanceState);
  }

  @Override
  protected Object initScreen() {
    return getIntent().getParcelableExtra(INIT_SCREEN);
  }

  @Override
  protected ViewStateManager viewStateManager() {
    return MvpDaggerApplication.component().viewStateManager();
  }

  @Override
  protected void changeDialogKey(Object dialogKey) {
    Dialog dialog = null;
    if (dialogKey instanceof Screen2) {
      dialog = new BottomSheetDialog(this);
      dialog.setContentView(((Screen2) dialogKey).component(component).mvp().view());
      dialog.setOnCancelListener(dialog1 -> Flow.get(this).goBack());
    }
    if (dialog != null) showDialog(dialog);
  }

  @Override
  protected boolean changeMainKey(Object mainKey, Direction direction, TraversalCallback callback) {
    View view = null;
    if (mainKey instanceof HomeScreen) {
      view =
          ((HomeScreen) mainKey)
              .component(component, (HomeScreen.State) initViewState(mainKey))
              .mvp()
              .view();
    } else if (mainKey instanceof Screen1) {
      view =
          ((Screen1) mainKey)
              .component(component, (Screen1.State) initViewState(mainKey))
              .mvp()
              .view();
    } else if (mainKey instanceof Screen2) {
      mainKeyToDialogKey(mainKey, callback);
    }
    if (view != null) {
      showMainView(view, direction);
      return true;
    }
    return false;
  }

  @Override
  public void finish() {
    super.finish();
    overridePendingTransition(R.anim.nothing, R.anim.slide_out_bottom);
  }
}
