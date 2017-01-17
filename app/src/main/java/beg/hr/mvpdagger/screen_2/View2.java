package beg.hr.mvpdagger.screen_2;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.util.mvp.Mvp.HasPresenter;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** Created by juraj on 17/01/2017. */
public class View2 extends LinearLayout implements HasPresenter<Screen2.Presenter> {

  private Screen2.Presenter presenter;

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

  @Override
  public void setPresenter(Screen2.Presenter presenter) {
    this.presenter = presenter;
  }

  @OnClick(R.id.back)
  public void onClickBack() {
    presenter.backPressed();
  }
}
