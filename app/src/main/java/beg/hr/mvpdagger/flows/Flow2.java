package beg.hr.mvpdagger.flows;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import beg.hr.mvpdagger.util.flow.FlowActivity;
import flow.Direction;
import flow.Flow;
import flow.History;

import static beg.hr.mvpdagger.DefaultKeyChanger.FLOW_EMPTY_SIGNAL;

public class Flow2 extends FlowActivity {

  private static final String INIT_KEY = "init_key";

  public static Intent getStartIntent(Context context, String initKey) {
    Intent intent = new Intent(context, Flow2.class);
    intent.putExtra(INIT_KEY, initKey);
    return intent;
  }

  @Override
  protected Object initScreen() {
    return FLOW_EMPTY_SIGNAL;
  }

  @Override
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    // this is done like this because init screen is called before we have incoming intent so we have to replace history
    if (savedInstanceState == null) {
      History history = History.emptyBuilder().push(getIntent().getStringExtra(INIT_KEY)).build();
      Flow.get(this).setHistory(history, Direction.REPLACE);
    }
  }
}
