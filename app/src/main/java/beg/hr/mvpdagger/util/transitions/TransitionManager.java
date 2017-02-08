package beg.hr.mvpdagger.util.transitions;

import android.view.View;

/** Created by juraj on 07/02/2017. */
public interface TransitionManager {

  void animate(View outView, View inView, int type);

  boolean inProgress();

  void reverse();
}
