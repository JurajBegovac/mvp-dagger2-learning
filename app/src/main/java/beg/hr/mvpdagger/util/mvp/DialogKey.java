package beg.hr.mvpdagger.util.mvp;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

import flow.MultiKey;

/** Created by juraj on 17/01/2017. */
public class DialogKey implements MultiKey {
  private final Object mainContent;
  private final Object dialogContent;

  public DialogKey(Object mainContent, Object dialogContent) {
    this.mainContent = mainContent;
    this.dialogContent = dialogContent;
  }

  public Object mainContent() {
    return mainContent;
  }

  public Object dialogContent() {
    return dialogContent;
  }

  @NonNull
  @Override
  public List<Object> getKeys() {
    return Arrays.asList(mainContent, dialogContent);
  }
}
