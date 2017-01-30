package beg.hr.mvpdagger.util.mvp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Use this for storing state of current view / dialog when configuration changes, android kills
 * process etc.
 *
 * <p>Created by juraj on 19/01/2017.
 */
public interface ViewStateManager {
  // todo remove this method
  @Deprecated
  Bundle createBundle(Object key, @Nullable View view, @Nullable Dialog dialog);

  Bundle createBundle();

  Object initialViewState(Object key, Bundle bundle);
}
