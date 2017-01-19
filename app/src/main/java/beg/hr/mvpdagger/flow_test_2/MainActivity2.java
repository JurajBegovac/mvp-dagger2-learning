package beg.hr.mvpdagger.flow_test_2;

import com.google.gson.Gson;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

import beg.hr.mvpdagger.MvpDaggerApplication;
import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.di.dagger2.components.ActivityComponent;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.home.HomeScreen;
import beg.hr.mvpdagger.home.HomeView;
import beg.hr.mvpdagger.screen_1.Screen1;
import beg.hr.mvpdagger.screen_1.View1;
import beg.hr.mvpdagger.screen_2.Screen2;
import beg.hr.mvpdagger.util.mvp.DialogKey;
import beg.hr.mvpdagger.util.mvp.GsonParceler;
import beg.hr.mvpdagger.util.mvp.MyDispatcher;
import flow.Direction;
import flow.Flow;
import flow.History;
import flow.History.Builder;
import flow.KeyChanger;
import flow.State;
import flow.TraversalCallback;

public class MainActivity2 extends AppCompatActivity implements KeyChanger {

  private static final Object FLOW_EMPTY_SIGNAL = "flow_empty_signal";
  private static final Object FLOW_FINISH_SIGNAL = "flow_finish_signal";

  private MyDispatcher flowDispatcher;
  private ActivityComponent component;
  private Dialog dialog;

  @Override
  protected void attachBaseContext(Context newBase) {
    flowDispatcher = new MyDispatcher(this, this);
    newBase =
        Flow.configure(newBase, this)
            .dispatcher(flowDispatcher)
            .defaultKey(FLOW_EMPTY_SIGNAL)
            .keyParceler(new GsonParceler(new Gson()))
            .install();
    super.attachBaseContext(newBase);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    component = MvpDaggerApplication.component().plus(new ActivityModule(this));
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    if (savedInstanceState == null) {
      History history =
          History.emptyBuilder().push(FLOW_FINISH_SIGNAL).push(HomeScreen.create()).build();
      Flow.get(this).setHistory(history, Direction.REPLACE);
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    saveCurrentState();
    super.onSaveInstanceState(outState);
  }

  private void saveCurrentState() {
    Object currentKey = getCurrentKey();
    getState(currentKey).setBundle(createBundle(currentKey));
  }

  private Bundle createBundle(Object key) {
    View currentView = getCurrentView();
    if (currentView == null) return Bundle.EMPTY;

    Bundle bundle = new Bundle();
    if (key instanceof HomeScreen && currentView instanceof HomeView) {
      String text = ((HomeView) currentView).getText();
      bundle.putParcelable(HomeScreen.State.KEY, HomeScreen.State.create(text));
    }
    if (key instanceof Screen1 && currentView instanceof View1) {
      String text = ((View1) currentView).getText();
      bundle.putParcelable(Screen1.State.KEY, Screen1.State.create(text));
    }
    if (key instanceof DialogKey) {
      Object dialogKey = ((DialogKey) key).dialogContent();

      if (dialogKey instanceof Screen1) {
        String text = ((EditText) (dialog.findViewById(R.id.editText))).getText().toString();
        bundle.putParcelable(Screen1.State.KEY, Screen1.State.create(text));
      }
    }
    return bundle;
  }

  private State getState(Object key) {
    return flowDispatcher.getState(key);
  }

  private Object getCurrentKey() {
    return Flow.get(this).getHistory().top();
  }

  @Override
  public void changeKey(
      @Nullable State outgoingState,
      @NonNull State incomingState,
      @NonNull Direction direction,
      @NonNull Map<Object, Context> incomingContexts,
      @NonNull TraversalCallback callback) {

    if (outgoingState != null) {
      // save state
      outgoingState.setBundle(createBundle(outgoingState.getKey()));
    }

    Object inKey = incomingState.getKey();
    Object mainKey;
    Object dialogKey;

    if (inKey instanceof DialogKey) {
      mainKey = ((DialogKey) inKey).mainContent();
      dialogKey = ((DialogKey) inKey).dialogContent();
    } else {
      mainKey = inKey;
      dialogKey = null;
    }

    View view = null;
    if (mainKey.equals(FLOW_EMPTY_SIGNAL)) {
      // this is first empty signal and  it'll be replaced
      view = new TextView(this);
      ((TextView) view).setText("Empty view");
    } else if (mainKey.equals(FLOW_FINISH_SIGNAL)) {
      finish();
    } else if (mainKey instanceof HomeScreen) {
      view =
          ((HomeScreen) mainKey)
              .component(component, (HomeScreen.State) initState(mainKey))
              .mvp()
              .view();
    } else if (mainKey instanceof Screen1) {
      callback.onTraversalCompleted();
      Builder historyBuilder = Flow.get(this).getHistory().buildUpon();
      historyBuilder.pop();
      Object main = getMainKey(historyBuilder.peek());
      historyBuilder.push(new DialogKey(main, mainKey));
      Flow.get(this).setHistory(historyBuilder.build(), Direction.REPLACE);
      return;
      //      view = ((Screen1) mainKey).component(component).mvp().view();
    } else if (mainKey.equals(FlowWithInitScreenActivity.KEY)) {
      callback.onTraversalCompleted();
      Builder historyBuilder = Flow.get(this).getHistory().buildUpon();
      historyBuilder.pop();
      Flow.get(this).setHistory(historyBuilder.build(), Direction.REPLACE);
      startActivity(FlowWithInitScreenActivity.getStartIntent(this, Screen1.create()));
      return;

    } else if (mainKey instanceof Screen2) {
      callback.onTraversalCompleted();
      Builder historyBuilder = Flow.get(this).getHistory().buildUpon();
      historyBuilder.pop();
      Object main = getMainKey(historyBuilder.peek());
      historyBuilder.push(new DialogKey(main, mainKey));
      Flow.get(this).setHistory(historyBuilder.build(), Direction.REPLACE);
      return;
    } else {
      // unknown state - remove it from history
      callback.onTraversalCompleted();
      Builder historyBuilder = Flow.get(this).getHistory().buildUpon();
      historyBuilder.pop();
      Flow.get(this).setHistory(historyBuilder.build(), Direction.REPLACE);
      return;
    }

    if (view != null) {
      removeOldViewIfNeeded();
      getRootView().addView(view);
    }

    dismissOldDialog();
    if (dialogKey != null) {
      if (dialogKey instanceof Screen1) {
        dialog =
            new AlertDialog.Builder(this)
                .setView(
                    ((Screen1) dialogKey)
                        .component(component, (Screen1.State) initState(dialogKey))
                        .mvp()
                        .view())
                .setOnCancelListener(dialog1 -> Flow.get(this).goBack())
                .create();
        dialog.show();
      } else if (dialogKey instanceof Screen2) {
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(((Screen2) dialogKey).component(component).mvp().view());
        dialog.setOnCancelListener(dialog1 -> Flow.get(this).goBack());
        dialog.show();
      }
    }
    callback.onTraversalCompleted();
  }

  private Object getMainKey(Object parent) {
    if (parent instanceof DialogKey) return ((DialogKey) parent).mainContent();
    return parent;
  }

  private void dismissOldDialog() {
    if (dialog != null) {
      dialog.dismiss();
      dialog = null;
    }
  }

  private Object initState(Object key) {
    Bundle bundle = getState(key).getBundle();
    if (key instanceof HomeScreen) {
      if (bundle != null && bundle.containsKey(HomeScreen.State.KEY)) {
        return bundle.getParcelable(HomeScreen.State.KEY);
      }
      return HomeScreen.State.defaultState();
    } else if (key instanceof Screen1) {
      if (bundle != null && bundle.containsKey(Screen1.State.KEY)) {
        return bundle.getParcelable(Screen1.State.KEY);
      }
      return Screen1.State.defaultState();
    }
    return null;
  }

  private ViewGroup getRootView() {
    return (ViewGroup) findViewById(android.R.id.content);
  }

  private void removeOldViewIfNeeded() {
    View view = getCurrentView();
    if (view != null) getRootView().removeView(view);
  }

  @Nullable
  private View getCurrentView() {
    ViewGroup rootView = getRootView();
    if (rootView.getChildCount() > 0) return rootView.getChildAt(0);
    return null;
  }
}
