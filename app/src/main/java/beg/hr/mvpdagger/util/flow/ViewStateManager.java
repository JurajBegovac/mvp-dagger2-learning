package beg.hr.mvpdagger.util.flow;

import android.os.Bundle;
import android.support.annotation.Nullable;

/** Created by juraj on 31/01/2017. */
public interface ViewStateManager {

  @Nullable
  Bundle getInitState();

  void saveState(Bundle bundle);


}
