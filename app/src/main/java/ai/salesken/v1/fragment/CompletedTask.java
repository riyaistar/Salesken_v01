package ai.salesken.v1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ai.salesken.v1.R;
import ai.salesken.v1.activity.SaleskenActivity;
import ai.salesken.v1.adapter.CompletedAdapter;
import ai.salesken.v1.adapter.UpcomingAdapter;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;
import ai.salesken.v1.pojo.SaleskenResponse;
import ai.salesken.v1.pojo.Task;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @BindView(R.id.constraintLayout2)
    ConstraintLayout constraintLayout2;

    @BindView(R.id.no_result)
    ConstraintLayout no_result;

    @BindView(R.id.completed_progress)
    ConstraintLayout progress;
    @BindView(R.id.no_data)
    ConstraintLayout no_data;
    @BindView(R.id.error_subtitle)
    TextView error_subtitle;
    SaleskenActivity saleskenActivity;
    List<Task> tasks;
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
        error_subtitle.setText("No Completed Task Found.");

        fetchData();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
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
               filterTaskList(newText);
                return false;
            }
        });
        return view;
    }



    public void fetchData(){
        saleskenActivity=((SaleskenActivity)getActivity());
        Call<SaleskenResponse> login_call = saleskenActivity.restUrlInterface.completed(saleskenActivity.sharedpreferences.getString(SaleskenSharedPrefKey.TOKEN,null));
        showProgress();
        login_call.enqueue(new Callback<SaleskenResponse>() {
            @Override
            public void onResponse(Call<SaleskenResponse> call, Response<SaleskenResponse> response) {

                switch (response.code()) {
                    case 200:
                        SaleskenResponse saleskenResponse = response.body();
                        if (saleskenResponse.getResponseCode() == 200) {
                            try {
                                Type type = new TypeToken<List<Task>>() {
                                }.getType();
                                tasks = saleskenActivity.gson.fromJson(saleskenActivity.gson.toJson(saleskenResponse.getResponse()), type);
                                if(tasks !=null && tasks.size() >0) {
                                    result.setText(tasks.size()+"");
                                    completedAdapter = new CompletedAdapter(saleskenActivity, tasks);
                                    recyclerView.setAdapter(completedAdapter);
                                }else {
                                    showNodata();
                                }
                            }catch (JsonSyntaxException jse){
                                ((SaleskenActivity)getActivity()).showToast("Bad response recieved from server.");
                                showNodata();
                            }catch (Exception e){
                                ((SaleskenActivity)getActivity()).showToast("Bad response recieved from server.");
                                showNodata();
                            }
                        }else{
                            ((SaleskenActivity)getActivity()).showToast(saleskenResponse.getResponseMessage());
                        }
                        hideProgress();
                        break;
                    default:
                        ((SaleskenActivity)getActivity()).showToast("Bad request recieved from server.");
                        hideProgress();
                        showNodata();
                }
            }

            @Override
            public void onFailure(Call<SaleskenResponse> call, Throwable t) {
                ((SaleskenActivity)getActivity()).showToast("Server Unreachable.");
                hideProgress();
                showNodata();
            }
        });
    }


    private void showProgress(){
        progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        constraintLayout2.setVisibility(View.GONE);

    }
    private void hideProgress(){
        progress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        constraintLayout2.setVisibility(View.VISIBLE);

    }

    private void showNodata(){
        recyclerView.setVisibility(View.GONE);
        constraintLayout2.setVisibility(View.GONE);
        no_data.setVisibility(View.VISIBLE);

    }
    private void showNoResultFound(){
        recyclerView.setVisibility(View.GONE);
        no_result.setVisibility(View.VISIBLE);
    }

    private void hideNoResultFound(){
        recyclerView.setVisibility(View.VISIBLE);
        no_result.setVisibility(View.GONE);
    }
    private void filterTaskList(String search) {
        if(tasks != null && tasks.size()>0){
            if(search != null && search.trim().length()>0) {
                List<Task> filtered_tasks = new ArrayList<>();
                for (Task task : tasks) {
                    if (task.getContactName().trim().toLowerCase().contains(search.trim().toLowerCase())
                    ) {
                        filtered_tasks.add(task);
                    }

                }
                if(filtered_tasks.size() >0) {
                    hideNoResultFound();
                    completedAdapter.updateList(filtered_tasks);
                }else{
                    showNoResultFound();
                }
            }else{
                completedAdapter.updateList(tasks);
                hideNoResultFound();

            }

        }
    }
}
