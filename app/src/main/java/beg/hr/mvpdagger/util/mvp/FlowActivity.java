package beg.hr.mvpdagger.util.mvp;

import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.AnimRes;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import beg.hr.mvpdagger.R;
import flow.Direction;
import flow.Flow;
import flow.History;
import flow.KeyChanger;
import flow.KeyDispatcher;
import flow.Traversal;
import flow.TraversalCallback;

public abstract class FlowActivity extends AppCompatActivity implements KeyChanger {

  protected static final int FROM_BOTTOM = 1;
  protected static final int FROM_RIGHT = 2;
  protected static final int REPLACE = 3;

  private static final int REQUEST_CODE_NEW_FLOW = 112;

  private Object currentState;

  public static void replaceTop(Context context, Object newState, Direction direction) {
    History.Builder history = Flow.get(context).getHistory().buildUpon();
    history.pop();
    history.push(newState);
    Flow.get(context).setHistory(history.build(), direction);
  }

  @Override
  protected void attachBaseContext(Context baseContext) {
    baseContext =
        Flow.configure(baseContext, this)
            .dispatcher(KeyDispatcher.configure(this, this).build())
            .defaultKey(defaultScreen())
            .keyParceler(new GsonParceler(new Gson()))
            .install();
    super.attachBaseContext(baseContext);
  }

  private Flow getFlow() {
    return Flow.get(this);
  }

  protected abstract Object defaultScreen();

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
    final View view = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
    if (!BackSupport.onBackPressed(view)) {
      super.onBackPressed();
    }
  }

  protected void setCurrentState(Object state) {
    currentState = state;
  }

  protected boolean isReplacingSameState(Traversal traversal) {
    Object newState = traversal.destination.top();
    return traversal.direction == Direction.REPLACE
        && currentState != null
        && currentState.equals(newState);
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
    final View currentView = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
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

  private void popHistory() {
    History.Builder history = Flow.get(this).getHistory().buildUpon();
    history.pop();
    Flow.get(this).setHistory(history.build(), Direction.REPLACE);
  }

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({FROM_RIGHT, FROM_BOTTOM, REPLACE})
  protected @interface NewFlowDirection {}
}
