package beg.hr.flowlesstest.home;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

import beg.hr.flowlesstest.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import beg.hr.flowlesstest.mvp.Mvp.HasPresenter;

/** Created by juraj on 16/07/16. */
public class HomeView extends LinearLayout implements HasPresenter<HomeScreen.Presenter> {

  @BindView(R.id.button)
  Button m_button;

  private HomeScreen.Presenter presenter;

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
    presenter.randomPressed();
  }

  @OnClick(R.id.button2)
  public void onClickButton2() {
    presenter.button2Pressed();
  }

  public void setButtonText(String p_text) {
    m_button.setText(p_text);
  }

  @Override
  public void setPresenter(HomeScreen.Presenter presenter) {
    this.presenter = presenter;
  }

  public String getText() {
    return m_button.getText().toString();
  }
}
