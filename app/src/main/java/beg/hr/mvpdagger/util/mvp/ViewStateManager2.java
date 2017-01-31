package beg.hr.mvpdagger.util.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

/** Created by juraj on 31/01/2017. */
public interface ViewStateManager2 {

  @Nullable
  Bundle getBundle(Object key);

  void saveState(Object key, Bundle bundle);
}
