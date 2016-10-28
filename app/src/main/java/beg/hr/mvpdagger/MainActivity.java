package beg.hr.mvpdagger;

import android.os.Bundle;
import android.view.View;

import beg.hr.mvpdagger.di.dagger2.components.ActivityComponent;
import beg.hr.mvpdagger.di.dagger2.modules.ActivityModule;
import beg.hr.mvpdagger.home.HomeScreen;
import beg.hr.mvpdagger.profile.ProfileScreen;
import beg.hr.mvpdagger.util.mvp.flow.FlowActivity;
import flow.Flow.Traversal;
import flow.Flow.TraversalCallback;
import flow.History;

public class MainActivity extends FlowActivity {

    private ActivityComponent m_activityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        m_activityComponent = MvpDaggerApplication.applicationComponent().plus(new ActivityModule(this));
        super.onCreate(savedInstanceState);
    }
    @Override
    public History getHistory() {
        return History.emptyBuilder()
                .push(FLOW_FINISH_SIGNAL)
                .push(HomeScreen.create())
                .build();
    }

    @Override
    public void dispatch(Traversal traversal, TraversalCallback callback) {
        Object newState = traversal.destination.top();

        if (isReplacingSameState(traversal)) {
            callback.onTraversalCompleted();
            return;
        }

        View view = null;
        if (FLOW_FINISH_SIGNAL.equals(newState)) {
            finish();
        } else if (newState instanceof HomeScreen) {
            view = ((HomeScreen) newState).component(m_activityComponent).mvp().view();
        } else if (newState instanceof ProfileScreen) {
            view = ((ProfileScreen) newState).component(m_activityComponent).mvp().view();
        }

        if (view != null) {
            setCurrentState(newState);
//            animate(view, traversal.direction);
            setContentView(view);
        }
        callback.onTraversalCompleted();
    }
}
