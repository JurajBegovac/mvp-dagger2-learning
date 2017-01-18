package beg.hr.flowlesstest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import beg.hr.flowlesstest.dagger2.modules.ActivityModule;
import beg.hr.flowlesstest.home.HomeScreen;
import beg.hr.flowlesstest.home.HomeView;
import flowless.Flow;
import flowless.KeyManager;
import flowless.State;
import flowless.preset.SingleRootDispatcher;

public class FlowlessActivity extends AppCompatActivity {

  private SingleRootDispatcher flowDispatcher;

  @Override
  protected void attachBaseContext(Context newBase) {
    flowDispatcher =
        new MyDispatcher(MvpDaggerApplication.component().plus(new ActivityModule(this)));
    newBase =
        Flow.configure(newBase, this) //
            .defaultKey(HomeScreen.create()) //
            .dispatcher(flowDispatcher) //
            .install(); //
    flowDispatcher.setBaseContext(this);
    super.attachBaseContext(newBase);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    flowDispatcher.getRootHolder().setRoot((ViewGroup) findViewById(android.R.id.content));
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    //    flowDispatcher.preSaveViewState(); // optional
    Flow flow = Flow.get(this);
    Object key = Flow.get(this).getHistory().top();
    KeyManager states = flow.getStates();
    State state = states.getState(key);
    state.setBundle(outgoingBundle(state));
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    flowDispatcher.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void onBackPressed() {
    if (!flowDispatcher.onBackPressed()) {
      super.onBackPressed();
    }
  }

  private Bundle outgoingBundle(State outgoingState) {

    ViewGroup view = flowDispatcher.getRootHolder().getRoot();
    Bundle bundle = new Bundle();
    if (outgoingState.getKey() instanceof HomeScreen) {
      String text = ((HomeView) view.getChildAt(0)).getText();
      bundle.putParcelable(HomeScreen.State.KEY, HomeScreen.State.create(text));
    }
    return bundle;
  }
}
