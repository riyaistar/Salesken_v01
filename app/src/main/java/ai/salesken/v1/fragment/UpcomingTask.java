package ai.salesken.v1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ai.salesken.v1.R;
import ai.salesken.v1.adapter.RecentAdapter;
import ai.salesken.v1.adapter.UpcomingAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UpcomingTask extends Fragment {
    private ViewGroup container;
    private LayoutInflater inflater;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    UpcomingAdapter upcomingAdapter;
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        this.inflater = inflater;
        return initializeView();
    }
    private View initializeView() {

        final View view;
        view = inflater.inflate(
                R.layout.task_recycler_view, container, false);
        ButterKnife.bind(this, view);
        upcomingAdapter = new UpcomingAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(upcomingAdapter);
        return view;
    }
}
