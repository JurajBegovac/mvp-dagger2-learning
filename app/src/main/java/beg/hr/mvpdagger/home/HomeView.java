package beg.hr.mvpdagger.home;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

import beg.hr.mvpdagger.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** Created by juraj on 16/07/16. */
public class HomeView extends LinearLayout {

  @BindView(R.id.button)
  Button m_button;

  public HomeView(Context context) {
    this(context, null);
  }

  public HomeView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public HomeView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  @OnClick(R.id.button)
  public void onClickButton() {
    //    presenter.randomPressed();
  }

  @OnClick(R.id.button2)
  public void onClickButton2() {
    //    presenter.button2Pressed();
  }

  @OnClick(R.id.back)
  public void onClickBack() {
    //    presenter.backPressed();
  }

  @OnClick(R.id.another_flow)
  public void onClickAnotherFlow() {
    //    presenter.anotherFlowPressed();
  }

  public void setButtonText(String p_text) {
    m_button.setText(p_text);
  }
}
