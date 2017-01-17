package beg.hr.mvpdagger;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import java.util.Map;

import beg.hr.mvpdagger.di.dagger2.components.ActivityComponent;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.home.HomeScreen;
import beg.hr.mvpdagger.screen_1.Screen1;
import beg.hr.mvpdagger.screen_2.Screen2;
import beg.hr.mvpdagger.util.mvp.DialogKey;
import beg.hr.mvpdagger.util.mvp.FlowActivity;
import flow.Direction;
import flow.Flow;
import flow.State;
import flow.TraversalCallback;

public class MainActivity extends FlowActivity {

  private ActivityComponent component;
  private Dialog visibleDialog;

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
  public void changeKey(
      @Nullable State outgoingState,
      @NonNull State incomingState,
      @NonNull Direction direction,
      @NonNull Map<Object, Context> incomingContexts,
      @NonNull TraversalCallback callback) {

    final Object mainKey;
    final Object dialogKey;

    Object newState = incomingState.getKey();

    if (newState instanceof DialogKey) {
      mainKey = ((DialogKey) newState).mainContent();
      dialogKey = ((DialogKey) newState).dialogContent();
    } else {
      mainKey = newState;
      dialogKey = null;
    }

    View view = null;

    if (mainKey instanceof HomeScreen) {
      view = ((HomeScreen) mainKey).component(component).mvp().view();
    } else if (mainKey instanceof Screen1) {
      view = ((Screen1) mainKey).component(component).mvp().view();
    } else if (mainKey instanceof Screen2) {
      view = ((Screen2) mainKey).component(component).mvp().view();
    }
    if (view != null) {
      //      setCurrentState(newState);
      //      animate(view, direction);
      setContentView(view);
    }
    dismissOldDialog();
    if (dialogKey != null) {
      if (dialogKey instanceof Screen1) {
        visibleDialog =
            new AlertDialog.Builder(incomingContexts.get(dialogKey))
                .setView(((Screen1) dialogKey).component(component).mvp().view())
                .setOnCancelListener(
                    new OnCancelListener() {
                      @Override
                      public void onCancel(DialogInterface dialog) {
                        Flow.get(MainActivity.this).goBack();
                      }
                    })
                .show();
      }
//       Prevent logging of android.view.WindowLeaked.
//            getApplication()
//                .registerActivityLifecycleCallbacks(
//                    new ActivityLifecycleCallbackAdapter() {
//                      @Override
//                      public void onActivityDestroyed(Activity activity) {
//                        getApplication().unregisterActivityLifecycleCallbacks(this);
//                        dismissOldDialog();
//                      }
//                    });
    }

    callback.onTraversalCompleted();
  }

  private void dismissOldDialog() {
    if (visibleDialog != null) {
      visibleDialog.dismiss();
      visibleDialog = null;
    }
  }
}
