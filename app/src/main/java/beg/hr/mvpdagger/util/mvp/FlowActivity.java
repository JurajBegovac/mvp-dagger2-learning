package beg.hr.mvpdagger.util.mvp;

import com.google.gson.Gson;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

import beg.hr.mvpdagger.R;
import flow.Direction;
import flow.Flow;
import flow.History;
import flow.History.Builder;
import flow.KeyChanger;
import flow.State;
import flow.TraversalCallback;

public abstract class FlowActivity extends AppCompatActivity implements KeyChanger {

  protected static final int FROM_BOTTOM = 1;
  protected static final int FROM_RIGHT = 2;
  protected static final int REPLACE = 3;

  private static final int REQUEST_CODE_NEW_FLOW = 112;

  private MyDispatcher flowDispatcher;
  private Dialog dialog;

  protected abstract Object defaultScreen();

  protected abstract void dispatch(Object mainKey, @Nullable Object dialogKey);

  protected abstract Bundle bundleToSave(View currentView);

  @Override
  protected void attachBaseContext(Context baseContext) {
    flowDispatcher = new MyDispatcher(this, this);
    baseContext =
        Flow.configure(baseContext, this)
            .dispatcher(flowDispatcher)
            .defaultKey(defaultScreen())
            .keyParceler(new GsonParceler(new Gson()))
            .install();
    super.attachBaseContext(baseContext);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE_NEW_FLOW) {
      popHistory();
      return;
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void onBackPressed() {
    final View view = getCurrentView();
    if (!BackSupport.onBackPressed(view)) {
      super.onBackPressed();
    }
  }

  @Override
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    dismissOldDialog();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    Object key = Flow.get(this).getHistory().top();
    getState(key).setBundle(bundleToSave(getCurrentView()));
    super.onSaveInstanceState(outState);
  }

  @Override
  public void changeKey(
      @Nullable State outgoingState,
      @NonNull State incomingState,
      @NonNull Direction direction,
      @NonNull Map<Object, Context> incomingContexts,
      @NonNull TraversalCallback callback) {

    if (outgoingState != null) {
      outgoingState.setBundle(bundleToSave(getCurrentView()));
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
    dismissOldDialog();
    dispatch(mainKey, dialogKey);
    callback.onTraversalCompleted();
  }

  private ViewGroup getRootView() {
    return (ViewGroup) findViewById(android.R.id.content);
  }

  private void popHistory() {
    History.Builder history = Flow.get(this).getHistory().buildUpon();
    history.pop();
    Flow.get(this).setHistory(history.build(), Direction.REPLACE);
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

  private View getCurrentView() {
    ViewGroup view = getRootView();
    return view.getChildAt(0);
  }

  protected void showAsDialog(Object key) {
    Builder builder = Flow.get(this).getHistory().buildUpon();
    Object top = builder.peek();
    Flow.get(this)
        .setHistory(builder.push(new DialogKey(getMainKey(top), key)).build(), Direction.REPLACE);
  }

  protected void displayMainView(View view) {
    if (view != null) {
      //        animate(view, direction);
      getRootView().removeAllViews();
      getRootView().addView(view);
    }
  }

  protected void displayDialog(Dialog dialog) {
    if (dialog != null) {
      this.dialog = dialog;
      this.dialog.show();
    }
  }

  protected void animate(View view, Direction direction) {
    switch (direction) {
      case REPLACE:
        // no animation
        break;
      case FORWARD:
        animate(view, R.anim.slide_in_right, R.anim.slide_out_left);
        break;
      case BACKWARD:
        animate(view, R.anim.slide_in_left, R.anim.slide_out_right);
        break;
      default:
        // noop
    }
  }

  protected void animate(View view, @AnimRes int enter, @AnimRes int exit) {
    final View currentView = getCurrentView();
    if (currentView != null) {
      Animation exitAnimation = AnimationUtils.loadAnimation(this, exit);
      currentView.startAnimation(exitAnimation);
    }
    Animation enterAnimation = AnimationUtils.loadAnimation(this, enter);
    view.startAnimation(enterAnimation);
  }

  protected void onTraversalSkipped(TraversalCallback callback) {
    callback.onTraversalCompleted();
    popHistory();
  }

  protected void startAnotherFlow(Intent startIntent, @NewFlowDirection int direction) {
    startActivityForResult(startIntent, REQUEST_CODE_NEW_FLOW);
    if (direction == FROM_BOTTOM) {
      overridePendingTransition(R.anim.slide_in_bottom, R.anim.nothing);
    }
    if (direction == FROM_RIGHT) {
      overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
  }

  protected State getState(Object key) {
    return flowDispatcher.getState(key);
  }

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({FROM_RIGHT, FROM_BOTTOM, REPLACE})
  protected @interface NewFlowDirection {}
}
