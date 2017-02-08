package beg.hr.mvpdagger.util.flow;

import com.google.auto.value.AutoValue;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.AnimRes;

/** Created by juraj on 02/02/2017. */
@AutoValue
public abstract class NewFlowKey implements Parcelable {

  public static NewFlowKey create(Intent intent, int enterAnim, int exitAnim) {
    return new AutoValue_NewFlowKey(intent, enterAnim, exitAnim);
  }

  public abstract Intent intent();

  @AnimRes
  abstract int enterAnim();

  @AnimRes
  abstract int exitAnim();
}
