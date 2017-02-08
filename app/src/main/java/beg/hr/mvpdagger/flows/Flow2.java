package beg.hr.mvpdagger.flows;

import android.content.Context;
import android.content.Intent;

import beg.hr.mvpdagger.util.flow.FlowActivity;

public class Flow2 extends FlowActivity {

  private static final String INIT_KEY = "init_key";

  public static Intent getStartIntent(Context context, String initKey) {
    Intent intent = new Intent(context, Flow2.class);
    intent.putExtra(INIT_KEY, initKey);
    return intent;
  }

  @Override
  protected Object initScreen() {
    return getIntent().getStringExtra(INIT_KEY);
  }
}
