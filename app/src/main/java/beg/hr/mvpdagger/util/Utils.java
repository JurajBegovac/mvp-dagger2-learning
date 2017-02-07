package beg.hr.mvpdagger.util;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

/** Created by juraj on 03/02/2017. */
public class Utils {

  @Nullable
  public static View getViewFromRoot(ViewGroup root, Class<? extends View> clazz) {
    for (int i = 0; i < root.getChildCount(); i++) {
      View child = root.getChildAt(i);
      Class<? extends View> childClass = child.getClass();
      if (childClass.equals(clazz)) return child;
    }
    return null;
  }

  @Nullable
  public static View getViewFromRoot(ViewGroup root, View view) {
    for (int i = 0; i < root.getChildCount(); i++) {
      View child = root.getChildAt(i);
      Class<? extends View> childClass = child.getClass();
      Class<? extends View> viewClass = view.getClass();
      if (childClass.equals(viewClass)) return child;
    }
    return null;
  }

  public static boolean rootContainsView(ViewGroup root, View view) {
    for (int i = 0; i < root.getChildCount(); i++) {
      View child = root.getChildAt(i);
      Class<? extends View> childClass = child.getClass();
      Class<? extends View> viewClass = view.getClass();
      if (childClass.equals(viewClass)) return true;
    }
    return false;
  }

  public static void removeAllOtherViews(ViewGroup root, View view) {
    for (int i = 0; i < root.getChildCount(); i++) {
      View child = root.getChildAt(i);
      Class<? extends View> childClass = child.getClass();
      Class<? extends View> viewClass = view.getClass();
      if (childClass.equals(viewClass)) continue;
      root.removeView(child);
    }
  }
}
