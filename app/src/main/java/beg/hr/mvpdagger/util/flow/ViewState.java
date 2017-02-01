package beg.hr.mvpdagger.util.flow;

import android.os.Bundle;
import android.support.annotation.Nullable;

/** Created by juraj on 31/01/2017. */
public interface ViewState {

  @Nullable
  Bundle get();

  void set(Bundle bundle);

  ViewState child(String key);
}
