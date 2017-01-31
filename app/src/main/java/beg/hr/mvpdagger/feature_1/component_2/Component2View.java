package beg.hr.mvpdagger.feature_1.component_2;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import beg.hr.mvpdagger.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/** Created by juraj on 17/01/2017. */
public class Component2View extends LinearLayout {

  @BindView(R.id.editText)
  EditText editText;

  @BindView(R.id.dialog)
  Button startDialog;

  @BindView(R.id.back)
  Button back;

  public Component2View(Context context) {
    this(context, null);
  }

  public Component2View(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public Component2View(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }

  public void setText(String text) {
    editText.setText(text);
    editText.setSelection(text.length());
  }
}
