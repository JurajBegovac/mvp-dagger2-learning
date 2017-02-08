package beg.hr.mvpdagger.feature_1;

import android.content.Context;
import android.content.Intent;

import beg.hr.mvpdagger.util.flow.DefaultRedirect;
import beg.hr.mvpdagger.util.flow.FlowActivity;
import beg.hr.mvpdagger.util.flow.Redirect;
import flow.TraversalCallback;

import static beg.hr.mvpdagger.util.view.ViewComponentFactory.FEATURE1_COMPONENT1;
import static beg.hr.mvpdagger.util.view.ViewComponentFactory.FEATURE1_COMPOSITE_COMPONENT;

public class Feature1Flow extends FlowActivity {

  public static Intent getStartIntent(Context context) {
    return new Intent(context, Feature1Flow.class);
  }

  @Override
  protected Object initScreen() {
    return FEATURE1_COMPOSITE_COMPONENT;
  }

  @Override
  protected Redirect redirect() {
    return new DefaultRedirect() {

      @Override
      public boolean shouldRedirect(Object key) {
        return FEATURE1_COMPONENT1.equals(key) || super.shouldRedirect(key);
      }

      @Override
      public void redirect(TraversalCallback callback, Object mainKey, Context context) {
        if (FEATURE1_COMPONENT1.equals(mainKey)) {
          toDialogKey(callback, mainKey, context);
          return;
        }
        super.redirect(callback, mainKey, context);
      }
    };
  }
}
