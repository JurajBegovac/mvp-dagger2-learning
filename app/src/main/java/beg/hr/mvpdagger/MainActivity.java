package beg.hr.mvpdagger;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.view.View;

import beg.hr.mvpdagger.di.dagger2.components.ActivityComponent;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.home.HomeScreen;
import beg.hr.mvpdagger.home.HomeView;
import beg.hr.mvpdagger.screen_1.Screen1;
import beg.hr.mvpdagger.screen_2.Screen2;
import beg.hr.mvpdagger.util.mvp.FlowActivity;
import flow.Flow;
import flow.State;

public class MainActivity extends FlowActivity {

  private ActivityComponent component;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    component = MvpDaggerApplication.component().plus(new ActivityModule(this));
    super.onCreate(savedInstanceState);
  }

  @Override
  protected Object defaultScreen() {
    return HomeScreen.create();
  }

  @Override
  protected void dispatch(Object mainKey, @Nullable Object dialogKey) {
    if (mainKey instanceof HomeScreen) {
      HomeView view =
          ((HomeScreen) mainKey).component(component, getInitState(getState(mainKey))).mvp().view();
      displayMainView(view);
    } else if (mainKey instanceof Screen1) {
      showAsDialog(mainKey);
    } else if (mainKey instanceof Screen2) {
      showAsDialog(mainKey);
    }

    if (dialogKey != null) {
      Dialog dialog = null;
      if (dialogKey instanceof Screen1) {
        dialog =
            new AlertDialog.Builder(this)
                .setView(((Screen1) dialogKey).component(component).mvp().view())
                .setOnCancelListener(dialog1 -> Flow.get(MainActivity.this).goBack())
                .create();
      }
      if (dialogKey instanceof Screen2) {
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(((Screen2) dialogKey).component(component).mvp().view());
        dialog.setOnCancelListener(dialog1 -> Flow.get(MainActivity.this).goBack());
      }
      displayDialog(dialog);
    }
  }

  @Override
  protected Bundle bundleToSave(View currentView) {
    Bundle bundle = new Bundle();
    if (currentView instanceof HomeView) {
      String text = ((HomeView) currentView).getText();
      bundle.putParcelable(HomeScreen.State.KEY, HomeScreen.State.create(text));
    }
    return bundle;
  }

  private HomeScreen.State getInitState(State incomingState) {
    if (incomingState.getBundle() != null
        && incomingState.getBundle().getParcelable(HomeScreen.State.KEY) != null) {
      return incomingState.getBundle().getParcelable(HomeScreen.State.KEY);
    }
    return HomeScreen.State.defaultState();
  }
}
