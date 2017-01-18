package beg.hr.mvpdagger;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import beg.hr.mvpdagger.di.dagger2.components.ActivityComponent;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.home.HomeScreen;
import beg.hr.mvpdagger.home.HomeView;
import beg.hr.mvpdagger.screen_1.Screen1;
import beg.hr.mvpdagger.screen_2.Screen2;
import beg.hr.mvpdagger.util.mvp.DialogKey;
import beg.hr.mvpdagger.util.mvp.FlowActivity;
import flow.Direction;
import flow.Flow;
import flow.History.Builder;
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

    if (outgoingState != null) {
      saveOutgoingState(outgoingState);
    }

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
      view = ((HomeScreen) mainKey).component(component, getInitState(incomingState)).mvp().view();
    } else if (mainKey instanceof Screen1) {
      callback.onTraversalCompleted();
      Builder builder = Flow.get(this).getHistory().buildUpon();
      builder.pop();
      Flow.get(this)
          .setHistory(
              builder.push(new DialogKey(getMainKey(outgoingState.getKey()), mainKey)).build(),
              Direction.REPLACE);
      return;
    } else if (mainKey instanceof Screen2) {
      callback.onTraversalCompleted();
      Builder builder = Flow.get(this).getHistory().buildUpon();
      builder.pop();
      Flow.get(this)
          .setHistory(
              builder.push(new DialogKey(getMainKey(outgoingState.getKey()), mainKey)).build(),
              Direction.REPLACE);
      return;
    }

    if (view != null) {
      if (outgoingState != null && !(outgoingState.getKey() instanceof DialogKey))
        animate(view, direction);
      incomingState.restore(view);
      setContentView(view);
    }

    dismissOldDialog();
    if (dialogKey != null) {
      if (dialogKey instanceof Screen1) {
        visibleDialog =
            new AlertDialog.Builder(this)
                .setView(((Screen1) dialogKey).component(component).mvp().view())
                .setOnCancelListener(dialog -> Flow.get(MainActivity.this).goBack())
                .show();
      }
      if (dialogKey instanceof Screen2) {
        visibleDialog = new BottomSheetDialog(this);
        visibleDialog.setContentView(((Screen2) dialogKey).component(component).mvp().view());
        visibleDialog.setOnCancelListener(dialog -> Flow.get(MainActivity.this).goBack());
        visibleDialog.show();
      }
      //       Prevent logging of android.view.WindowLeaked.
      //                  getApplication()
      //                      .registerActivityLifecycleCallbacks(
      //                          new ActivityLifecycleCallbackAdapter() {
      //                            @Override
      //                            public void onActivityDestroyed(Activity activity) {
      //                              getApplication().unregisterActivityLifecycleCallbacks(this);
      //                              dismissOldDialog();
      //                            }
      //                          });
    }
    callback.onTraversalCompleted();
  }

  private HomeScreen.State getInitState(State incomingState) {
    if (incomingState.getBundle() != null
        && incomingState.getBundle().getParcelable(HomeScreen.State.KEY) != null) {
      return incomingState.getBundle().getParcelable(HomeScreen.State.KEY);
    }
    return HomeScreen.State.defaultState();
  }

  private void saveOutgoingState(State outgoingState) {
    ViewGroup view = (ViewGroup) findViewById(android.R.id.content);
    if (outgoingState.getKey() instanceof HomeScreen) {
      String text = ((HomeView) view.getChildAt(0)).getText();
      Bundle bundle = new Bundle();
      bundle.putParcelable(HomeScreen.State.KEY, HomeScreen.State.create(text));
      outgoingState.setBundle(bundle);
    }
  }

  private Object getMainKey(Object parent) {
    if (parent instanceof DialogKey) return ((DialogKey) parent).mainContent();
    return parent;
  }

  private void dismissOldDialog() {
    if (visibleDialog != null) {
      visibleDialog.dismiss();
      visibleDialog = null;
    }
  }
}
