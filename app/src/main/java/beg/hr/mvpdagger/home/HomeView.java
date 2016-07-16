package beg.hr.mvpdagger.home;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import beg.hr.mvpdagger.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by juraj on 16/07/16.
 */
public class HomeView extends RelativeLayout {

    @Inject HomeScreen.Presenter m_presenter;

    @BindView(R.id.button) Button m_button;

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
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        m_presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        m_presenter.dropView(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void onClickButton() {
        m_presenter.dummy();
    }

    public void setButtonText(String p_text) {
        m_button.setText(p_text);
    }
}
