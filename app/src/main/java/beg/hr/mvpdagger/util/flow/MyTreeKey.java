package beg.hr.mvpdagger.util.flow;

import android.support.annotation.NonNull;

import flow.TreeKey;

/** Created by juraj on 17/01/2017. */
public class MyTreeKey implements TreeKey {

  private final Object parentKey;
  private final Object key;

  public MyTreeKey(Object parentKey, Object key) {
    this.parentKey = parentKey;
    this.key = key;
  }

  public Object key() {
    return key;
  }

  @NonNull
  @Override
  public Object getParentKey() {
    return parentKey;
  }
}
