package beg.hr.mvpdagger.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import flow.Direction;

/** Created by juraj on 02/02/2017. */
public interface TransitionsFactory {

  void execute(
      @NonNull ViewGroup root,
      @Nullable View current,
      @NonNull View newView,
      @Nullable Object oldState,
      @NonNull Object newState,
      Direction direction);
}
