package beg.hr.mvpdagger.screen_1;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.screen_1.Screen1.Presenter;
import beg.hr.mvpdagger.util.mvp.Mvp.HasPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** Created by juraj on 17/01/2017. */
public class View1 extends LinearLayout implements HasPresenter<Screen1.Presenter> {

  @BindView(R.id.editText)
  EditText editText;

  private Screen1.Presenter presenter;

  public View1(Context context) {
    this(context, null);
  }

  public View1(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public View1(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  @Override
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }

  @OnClick(R.id.back)
  public void onClickBack() {
    presenter.backPressed();
  }

  @OnClick(R.id.dialog)
  public void onClickDialog() {
    presenter.dialogPressed();
  }

  public void setText(String text) {
    editText.setText(text);
  }

  public String getText() {
    return editText.getText().toString();
  }
}
