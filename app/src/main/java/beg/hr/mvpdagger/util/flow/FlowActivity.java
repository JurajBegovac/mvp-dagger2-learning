package beg.hr.mvpdagger.util.flow;

import com.google.gson.Gson;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import beg.hr.mvpdagger.DefaultKeyChanger;
import beg.hr.mvpdagger.util.Utils;
import beg.hr.mvpdagger.util.transitions.DefaultTransitionManager;
import beg.hr.mvpdagger.util.transitions.TransitionManager;
import beg.hr.mvpdagger.util.view.ViewComponentFactory;
import flow.Dispatcher;
import flow.Flow;
import flow.KeyChanger;

/** Created by juraj on 19/01/2017. */
public abstract class FlowActivity extends AppCompatActivity {

  protected abstract Object initScreen();

  protected TransitionManager transitionManager() {
    return new DefaultTransitionManager(this::rootView);
  }

  protected KeyChanger keyChanger() {
    return new DefaultKeyChanger(
        this::rootView, transitionManager(), viewComponentFactory(), redirect());
  }

  protected ViewComponentFactory viewComponentFactory() {
    return new ViewComponentFactory(this);
  }

  protected Redirect redirect() {
    return new DefaultRedirect();
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    Dispatcher flowDispatcher = new BaseDispatcher(this, keyChanger());
    newBase =
        Flow.configure(newBase, this)
            .dispatcher(flowDispatcher)
            .defaultKey(initScreen())
            .keyParceler(new GsonParceler(new Gson()))
            .install();
    super.attachBaseContext(newBase);
  }

  @Override
  public void onBackPressed() {
    final View view = Utils.getCurrentView(rootView());
    if (!BackSupport.onBackPressed(view)) {
      super.onBackPressed();
    }
  }

  protected ViewGroup rootView() {
    return (ViewGroup) findViewById(android.R.id.content);
  }
}
