package beg.hr.mvpdagger.util.mvp.flow;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import flow.Flow;
import flow.Flow.Direction;
import flow.Flow.Dispatcher;
import flow.Flow.Traversal;
import flow.Flow.TraversalCallback;
import flow.FlowDelegate;
import flow.History;
import flow.StateParceler;

public abstract class FlowActivity extends AppCompatActivity implements Dispatcher {

    public static final Object FLOW_FINISH_SIGNAL = "flow_finish_signal";

    /**
     * Flow initializer for this scope
     */
    protected FlowDelegate m_flowDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StateParceler parceler = new GsonParceler(new Gson());
        FlowDelegate.NonConfigurationInstance nonConfig =
                (FlowDelegate.NonConfigurationInstance) getLastCustomNonConfigurationInstance();
        m_flowDelegate = FlowDelegate.onCreate(nonConfig, getIntent(), null, parceler, getHistory(), this);
    }

    abstract public History getHistory();

    @Override
    protected void onResume() {
        super.onResume();
        m_flowDelegate.onResume();
    }

    @Override
    protected void onPause() {
        m_flowDelegate.onPause();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        final View view = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        if (!BackSupport.onBackPressed(view)) {
            super.onBackPressed();
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return m_flowDelegate.onRetainNonConfigurationInstance();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        m_flowDelegate.onSaveInstanceState(outState);
    }

    /**
     * Service provider for this scope
     */
    @Override
    public Object getSystemService(String name) {
        if (m_flowDelegate != null) {
            Object flowService = m_flowDelegate.getSystemService(name);
            if (flowService != null) return flowService;
        }

        return super.getSystemService(name);
    }

    private Object m_currentState;

    protected void setCurrentState(Object p_state) {
        m_currentState = p_state;
    }

    protected boolean isReplacingSameState(Traversal p_traversal) {
        Object newState = p_traversal.destination.top();
        return p_traversal.direction == Direction.REPLACE
                && m_currentState != null && m_currentState.equals(newState);
    }

//    protected void animate(View p_view, Direction p_direction) {
//        switch (p_direction) {
//            case REPLACE:
//                // no animation
//                break;
//            case FORWARD:
//                animate(p_view, R.anim.slide_in_right, R.anim.slide_out_left);
//                break;
//            case BACKWARD:
//                animate(p_view, R.anim.slide_in_left, R.anim.slide_out_right);
//                break;
//        }
//    }

//    protected void animate(View p_view, @AnimRes int p_enter, @AnimRes int p_exit) {
//        final View currentView = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
//        if (currentView != null) {
//            Animation exitAnimation = AnimationUtils.loadAnimation(this, p_exit);
//            currentView.startAnimation(exitAnimation);
//        }
//        Animation enterAnimation = AnimationUtils.loadAnimation(this, p_enter);
//        p_view.startAnimation(enterAnimation);
//    }

    protected void onTraversalSkipped(TraversalCallback p_callback) {
        p_callback.onTraversalCompleted();
        History.Builder history = Flow.get(this).getHistory().buildUpon();
        history.pop();
        Flow.get(this).setHistory(history.build(), Direction.REPLACE);
    }

    public static void replaceTop(Context p_context, Object p_newState, Direction p_direction) {
        History.Builder history = Flow.get(p_context).getHistory().buildUpon();
        history.pop();
        history.push(p_newState);
        Flow.get(p_context).setHistory(history.build(), p_direction);
    }
}
