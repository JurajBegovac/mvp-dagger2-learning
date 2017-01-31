package beg.hr.mvpdagger.feature_2;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import beg.hr.mvpdagger.R;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** Created by juraj on 17/01/2017. */
public class View2 extends LinearLayout {

  public View2(Context context) {
    this(context, null);
  }

  public View2(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public View2(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  @OnClick(R.id.back)
  public void onClickBack() {
    //    presenter.backPressed();
  }
}
