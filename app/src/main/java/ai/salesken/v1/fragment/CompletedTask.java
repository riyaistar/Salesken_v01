package ai.salesken.v1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

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
    @BindView(R.id.searchview)
    SearchView searchview;
    @BindView(R.id.result)
    TextView result;
    @BindView(R.id.found)
    TextView found;
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        this.inflater = inflater;
        return initializeView();
    }
    private View initializeView() {

        final View view;
        view = inflater.inflate(
                R.layout.completed_fragment, container, false);
        ButterKnife.bind(this, view);

        completedAdapter = new CompletedAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(completedAdapter);
        searchview.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    result.setVisibility(View.GONE);
                    found.setVisibility(View.GONE);
                } else {
                    result.setVisibility(View.VISIBLE);
                    found.setVisibility(View.VISIBLE);
                }
            }
        });
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               // filterContactList(newText);
                return false;
            }
        });
        return view;
    }
}
