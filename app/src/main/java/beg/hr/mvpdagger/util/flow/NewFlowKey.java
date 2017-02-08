package beg.hr.mvpdagger.util.flow;

import com.google.auto.value.AutoValue;

import android.content.Intent;
import android.os.Parcelable;

/** Created by juraj on 02/02/2017. */
@AutoValue
public abstract class NewFlowKey implements Parcelable {

  public static NewFlowKey create(Intent intent) {
    return new AutoValue_NewFlowKey(intent);
  }

  public abstract Intent intent();
}
