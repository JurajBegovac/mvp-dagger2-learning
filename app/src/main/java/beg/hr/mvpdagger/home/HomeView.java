package beg.hr.mvpdagger.home;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import beg.hr.mvpdagger.R;
import beg.hr.mvpdagger.home.HomeScreen.Presenter;
import beg.hr.mvpdagger.util.mvp.Mvp.HasPresenter;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by juraj on 17/10/16.
 */

public class HomeView extends LinearLayout implements HasPresenter<HomeScreen.Presenter> {

    private Presenter m_presenter;

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
    public void setPresenter(HomeScreen.Presenter p_presenter) {
        m_presenter = p_presenter;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void onClickButton() {
        m_presenter.onButtonPressed();
    }
}
