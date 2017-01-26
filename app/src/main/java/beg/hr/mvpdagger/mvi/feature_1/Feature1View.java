package beg.hr.mvpdagger.mvi.feature_1;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import beg.hr.mvpdagger.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/** Created by juraj on 26/01/2017. */
public class Feature1View extends LinearLayout {

  @BindView(R.id.btn)
  Button btn;

  @BindView(R.id.et)
  EditText et;

  public Feature1View(Context context) {
    this(context, null);
  }

  public Feature1View(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public Feature1View(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  public void setText(String text) {
    et.setText(text);
    et.setSelection(text.length());
  }

  public Button getButton() {
    return btn;
  }

  public TextView getTextInput() {
    return et;
  }
}
