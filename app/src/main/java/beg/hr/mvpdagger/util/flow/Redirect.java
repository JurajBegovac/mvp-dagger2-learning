package beg.hr.mvpdagger.util.flow;

import android.content.Context;

import flow.TraversalCallback;

/** Created by juraj on 08/02/2017. */
public interface Redirect {

  boolean shouldRedirect(Object key);

  void redirect(TraversalCallback callback, Object mainKey, Context context);
}
