package beg.hr.mvpdagger.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import beg.hr.mvpdagger.MvpDaggerApplication;
import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.di.dagger2.components.ActivityComponent;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.util.flow.FlowActivity;
import flow.Direction;
import flow.TraversalCallback;

public class BottomTopActivity extends FlowActivity {

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
  protected void changeKey(
      Object mainKey,
      @Nullable Object dialogKey,
      Direction direction,
      TraversalCallback callback) {}

  @Override
  public void finish() {
    super.finish();
    overridePendingTransition(R.anim.nothing, R.anim.slide_out_bottom);
  }
}
