package beg.hr.mvpdagger.profile;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import beg.hr.mvpdagger.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by juraj on 17/10/16.
 */

public class ProfileView2 extends ProfileView {

    @BindView(R.id.name)      TextView m_name;
    @BindView(R.id.presenter) TextView m_presenterName;

    public ProfileView2(Context context) {
        this(context, null);
    }
    public ProfileView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ProfileView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    public void setName(String p_name) {
        m_name.setText(p_name);
    }

    @Override
    public void setPresenterName(String p_name) {
        m_presenterName.setText(p_name);
    }

    @OnClick(R.id.back)
    public void onClickBack() {
        m_presenter.onBackPressed();
    }
}
