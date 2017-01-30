package beg.hr.mvpdagger;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import javax.inject.Inject;

import beg.hr.mvpdagger.home.HomeScreen;
import beg.hr.mvpdagger.home.HomeView;
import beg.hr.mvpdagger.screen_1.Screen1;
import beg.hr.mvpdagger.screen_1.View1;
import beg.hr.mvpdagger.util.mvp.DialogKey;
import beg.hr.mvpdagger.util.mvp.ViewStateManager;

/**
 * Custom implementation of ViewStateManager
 *
 * <p>Created by juraj on 19/01/2017.
 */
public class MyViewStateManager implements ViewStateManager {

  @Inject
  public MyViewStateManager() {}

  @Override
  public Bundle createBundle(Object key, @Nullable View view, @Nullable Dialog dialog) {
    if (view == null) return Bundle.EMPTY;

    Bundle bundle = new Bundle();
    if (key instanceof HomeScreen && view instanceof HomeView) {
      String text = ((HomeView) view).getText();
      bundle.putParcelable(HomeScreen.State.KEY, HomeScreen.State.create(text));
    }
    if (key instanceof Screen1 && view instanceof View1) {
      String text = ((View1) view).getText();
      bundle.putParcelable(Screen1.State.KEY, Screen1.State.create(text));
    }
    if (key instanceof DialogKey) {
      // TODO: 19/01/2017 handle dialog
    }
    return bundle;
  }

  @Override
  public Bundle createBundle() {
    // TODO: 30/01/2017 Implement this
    return Bundle.EMPTY;
  }

  @Override
  public Object initialViewState(Object key, Bundle bundle) {
    if (key instanceof HomeScreen) {
      if (bundle != null && bundle.containsKey(HomeScreen.State.KEY)) {
        return bundle.getParcelable(HomeScreen.State.KEY);
      }
      return HomeScreen.State.defaultState();
    } else if (key instanceof Screen1) {
      if (bundle != null && bundle.containsKey(Screen1.State.KEY)) {
        return bundle.getParcelable(Screen1.State.KEY);
      } else {
        return Screen1.State.defaultState();
      }
    }
    // TODO: 19/01/2017 throw some exception that this state is not handled
    return null;
  }
}
