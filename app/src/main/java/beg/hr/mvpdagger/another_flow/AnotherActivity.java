package beg.hr.mvpdagger.another_flow;

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
import beg.hr.mvpdagger.screen_1.Screen1;
import beg.hr.mvpdagger.screen_1.View1;
import beg.hr.mvpdagger.screen_2.Screen2;
import beg.hr.mvpdagger.util.mvp.FlowActivity;
import flow.Direction;
import flow.Flow;
import flow.History;

public class AnotherActivity extends FlowActivity {

  private static final String INIT_SCREEN = "init_screen";
  private ActivityComponent component;

  public static Intent getStartIntent(Context context, Parcelable screen) {
    Intent intent = new Intent(context, AnotherActivity.class);
    intent.putExtra(INIT_SCREEN, screen);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    component = MvpDaggerApplication.component().plus(new ActivityModule(this));
    super.onCreate(savedInstanceState);
  }

  @Override
  protected Object defaultScreen() {
    return null;
  }

  @Override
  protected void dispatch(Object mainKey, @Nullable Object dialogKey, Direction direction) {
    if (mainKey instanceof Screen1) {
      View1 view =
          ((Screen1) mainKey).component(component, Screen1.State.defaultState()).mvp().view();
      displayMainView(view, direction);
    }
    if (mainKey instanceof Screen2) {
      showAsDialog(mainKey);
    }

    if (dialogKey != null) {
      Dialog dialog = null;
      if (dialogKey instanceof Screen2) {
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(((Screen2) dialogKey).component(component).mvp().view());
        dialog.setOnCancelListener(dialog1 -> Flow.get(this).goBack());
      }
      displayDialog(dialog);
    }
  }

  @Override
  protected Bundle bundleToSave(View currentView) {
    return new Bundle();
  }

  @Override
  protected History defaultHistory() {
    return History.emptyBuilder()
        .push(FLOW_FINISH_SIGNAL)
        .push(getIntent().getParcelableExtra(INIT_SCREEN))
        .build();
  }

  @Override
  public void finish() {
    super.finish();
    overridePendingTransition(R.anim.nothing, R.anim.slide_out_bottom);
  }
}
