package ai.salesken.v1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import ai.salesken.v1.R;
import butterknife.ButterKnife;

public class IntroductionOne extends Fragment {
    private ViewGroup container;
    private LayoutInflater inflater;
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        this.inflater = inflater;
        return initializeView();
    }
    private View initializeView() {

        final View view;
        view = inflater.inflate(
                R.layout.introduction_one, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}

