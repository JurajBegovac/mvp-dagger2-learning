package beg.hr.mvpdagger.flows;

import android.content.Context;
import android.content.Intent;

import beg.hr.mvpdagger.util.flow.DefaultRedirect;
import beg.hr.mvpdagger.util.flow.FlowActivity;
import beg.hr.mvpdagger.util.flow.Redirect;

import static beg.hr.mvpdagger.util.view.ViewComponentFactory.FEATURE1_COMPOSITE_COMPONENT;

public class Flow1 extends FlowActivity {

  public static Intent getStartIntent(Context context) {
    return new Intent(context, Flow1.class);
  }

  @Override
  protected Object initScreen() {
    return FEATURE1_COMPOSITE_COMPONENT;
  }

  @Override
  protected Redirect redirect() {
    return new DefaultRedirect() {

      //      @Override
      //      public boolean shouldRedirect(Object key) {
      //        return FEATURE1_COMPONENT1.equals(key)
      //            || FEATURE1_COMPONENT2.equals(key)
      //            || super.shouldRedirect(key);
      //      }

      //      @Override
      //      public void redirect(TraversalCallback callback, Object mainKey, Context context) {
      //        if (FEATURE1_COMPONENT1.equals(mainKey)) {
      //          toDialogKey(callback, mainKey, context);
      //          return;
      //        }
      //        if (FEATURE1_COMPONENT2.equals(mainKey)) {
      //          toNewFlowKey(
      //              callback,
      //              context,
      //              NewFlowKey.create(Flow2.getStartIntent(context, FEATURE1_COMPONENT2)));
      //          return;
      //        }
      //        super.redirect(callback, mainKey, context);
      //      }
    };
  }
}
