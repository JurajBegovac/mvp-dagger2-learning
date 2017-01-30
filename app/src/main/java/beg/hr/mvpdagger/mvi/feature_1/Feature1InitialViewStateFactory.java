package beg.hr.mvpdagger.mvi.feature_1;

import android.os.Bundle;

import static beg.hr.mvpdagger.util.view.ViewComponentFactory.FEATURE1_COMPONENT;

/** Created by juraj on 30/01/2017. */
public class Feature1InitialViewStateFactory {

  public static Object state(Object key, Bundle bundle) {
    if (FEATURE1_COMPONENT.equals(key)) {
      if (bundle != null && bundle.containsKey(Feature1ViewDriver.State.TAG)) {
        return bundle.getParcelable(Feature1ViewDriver.State.TAG);
      }
      return Feature1ViewDriver.State.defaultState();
    }
    return null;
  }
}
