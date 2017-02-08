package beg.hr.mvpdagger.util.flow;

import android.content.Context;

import flow.Flow;
import flow.History.Builder;
import flow.TraversalCallback;

import static flow.Direction.REPLACE;

/** Created by juraj on 08/02/2017. */
public class DefaultRedirect implements Redirect {

  @Override
  public boolean shouldRedirect(Object key) {
    return false;
  }

  @Override
  public void redirect(TraversalCallback callback, Object mainKey, Context context) {
    // per default do nothing
  }

  protected void toNewFlowKey(TraversalCallback callback, Context context, NewFlowKey key) {
    callback.onTraversalCompleted();
    Builder historyBuilder = Flow.get(context).getHistory().buildUpon();
    historyBuilder.pop();
    historyBuilder.push(key);
    Flow.get(context).setHistory(historyBuilder.build(), REPLACE);
  }

  protected void toDialogKey(TraversalCallback callback, Object mainKey, Context context) {
    callback.onTraversalCompleted();
    Builder historyBuilder = Flow.get(context).getHistory().buildUpon();
    historyBuilder.pop();
    Object main = getMainKey(historyBuilder.peek());
    historyBuilder.push(new DialogKey(main, mainKey));
    Flow.get(context).setHistory(historyBuilder.build(), REPLACE);
  }

  private Object getMainKey(Object parent) {
    if (parent instanceof DialogKey) return ((DialogKey) parent).mainContent();
    return parent;
  }
}
