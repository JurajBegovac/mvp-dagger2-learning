package beg.hr.mvpdagger;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.view.View;

import beg.hr.mvpdagger.di.dagger2.components.ActivityComponent;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.flow_test_2.BottomTopActivity;
import beg.hr.mvpdagger.home.HomeScreen;
import beg.hr.mvpdagger.screen_1.Screen1;
import beg.hr.mvpdagger.screen_2.Screen2;
import beg.hr.mvpdagger.util.mvp.ViewStateManager;
import beg.hr.mvpdagger.util.mvp.FlowActivity;
import flow.Direction;
import flow.Flow;
import flow.TraversalCallback;

public class MainActivity extends FlowActivity {

  private ActivityComponent component;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    component = MvpDaggerApplication.component().plus(new ActivityModule(this));
    super.onCreate(savedInstanceState);
  }

  @Override
  protected Object initScreen() {
    return HomeScreen.create();
  }

//  @Override
//  protected ViewStateManager viewStateManager() {
//    return MvpDaggerApplication.component().viewStateManager();
//  }

  @Override
  protected void changeDialogKey(Object dialogKey) {
//    Dialog dialog = null;
//    if (dialogKey instanceof Screen1) {
//      dialog =
//          new AlertDialog.Builder(this)
//              .setView(
//                  ((Screen1) dialogKey)
//                      .component(component, (Screen1.State) initialViewState(dialogKey))
//                      .mvp()
//                      .view())
//              .setOnCancelListener(dialog1 -> Flow.get(this).goBack())
//              .create();
//    } else if (dialogKey instanceof Screen2) {
//      dialog = new BottomSheetDialog(this);
//      dialog.setContentView(((Screen2) dialogKey).component(component).mvp().view());
//      dialog.setOnCancelListener(dialog1 -> Flow.get(this).goBack());
//    }
//    if (dialog != null) showDialog(dialog);
  }

  @Override
  protected boolean changeMainKey(Object mainKey, Direction direction, TraversalCallback callback) {
    View view = null;
    if (mainKey instanceof HomeScreen) {
//      view =
//          ((HomeScreen) mainKey)
//              .component(component, (HomeScreen.State) initialViewState(mainKey))
//              .mvp()
//              .view();
    } else if (mainKey instanceof Screen1) {
      mainKeyToDialogKey(mainKey, callback);
      //      view =
      //          ((Screen1) mainKey)
      //              .component(component, (Screen1.State) initialViewState(mainKey))
      //              .mvp()
      //              .view();
    } else if (mainKey.equals(BottomTopActivity.KEY)) {
      startAnotherFlow(callback);
    } else if (mainKey instanceof Screen2) {
      mainKeyToDialogKey(mainKey, callback);
    }
    if (view != null) {
      showMainView(view, direction);
      return true;
    }
    return false;
  }
}
