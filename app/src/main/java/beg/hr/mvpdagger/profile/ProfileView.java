package beg.hr.mvpdagger.profile;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import beg.hr.mvpdagger.util.mvp.Mvp.HasPresenter;

/**
 * Created by juraj on 17/10/16.
 */
public abstract class ProfileView extends RelativeLayout implements HasPresenter<ProfileScreen.Presenter> {

    protected ProfileScreen.Presenter m_presenter;

    public ProfileView(Context context) {
        this(context, null);
    }
    public ProfileView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ProfileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract void setName(String p_name);
    public abstract void setPresenterName(String p_name);

    @Override
    public void setPresenter(ProfileScreen.Presenter p_presenter) {
        m_presenter = p_presenter;
    }
}
