package beg.hr.mvpdagger.feature_1.composite_component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.feature_1.component_1.Component1View;
import beg.hr.mvpdagger.feature_1.component_2.Component2View;
import butterknife.BindView;
import butterknife.ButterKnife;

/** Created by juraj on 01/02/2017. */
public class CompositeView extends LinearLayout {

  @BindView(R.id.component1)
  public Component1View component1View;

  @BindView(R.id.component2)
  public Component2View component2View;

  public CompositeView(Context context) {
    this(context, null);
  }

  public CompositeView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CompositeView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);
  }
}
