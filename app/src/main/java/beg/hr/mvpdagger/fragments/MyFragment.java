package beg.hr.mvpdagger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import beg.hr.mvpdagger.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** A placeholder fragment containing a simple view. */
public class MyFragment extends Fragment {

  private static final String NUMBER = "NUMBER";

  @BindView(R.id.text)
  TextView text;

  private int number;

  public MyFragment() {}

  public static MyFragment create(int number) {
    MyFragment fragment = new MyFragment();
    Bundle bundle = new Bundle();
    bundle.putInt(NUMBER, number);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    number = getArguments().getInt(NUMBER);
    this.text.setText("This is fragment: " + number);
  }

  @OnClick(R.id.btn)
  public void onClick() {
    ((MainActivity) getActivity()).goTo(create(++number));
  }
}
