package ai.salesken.v1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ai.salesken.v1.R;
import ai.salesken.v1.adapter.CompletedAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CompletedTask extends Fragment {
    private ViewGroup container;
    private LayoutInflater inflater;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    CompletedAdapter completedAdapter;

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

        completedAdapter = new CompletedAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(completedAdapter);
        return view;
    }
}
