package ai.salesken.v1.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ai.salesken.v1.R;
import ai.salesken.v1.activity.SaleskenActivity;
import ai.salesken.v1.adapter.RecentAdapter;
import ai.salesken.v1.adapter.UpcomingAdapter;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;
import ai.salesken.v1.pojo.ContactPojo;
import ai.salesken.v1.pojo.SaleskenResponse;
import ai.salesken.v1.pojo.Task;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingTask extends Fragment {
    private static final String TAG = "UpcomingTask" ;
    private ViewGroup container;
    private LayoutInflater inflater;
    @BindView(R.id.upcomig_recycler_view)
    RecyclerView recyclerView;
    UpcomingAdapter upcomingAdapter;
    SaleskenActivity saleskenActivity;
    @BindView(R.id.upcoming_progress)
    ConstraintLayout progress;
    @BindView(R.id.no_data)
    ConstraintLayout no_data;
    @BindView(R.id.error_subtitle)
    TextView error_subtitle;

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
        error_subtitle.setText("No Upcoming Task Found.");
        fetchData();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        return view;
    }



    public void fetchData(){
        saleskenActivity=((SaleskenActivity)getActivity());
        Call<SaleskenResponse> login_call = saleskenActivity.restUrlInterface.upcoming(saleskenActivity.sharedpreferences.getString(SaleskenSharedPrefKey.TOKEN,null));
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
                                List<Task> tasks = saleskenActivity.gson.fromJson(saleskenActivity.gson.toJson(saleskenResponse.getResponse()), type);
                                if(tasks !=null && tasks.size() >0) {
                                    upcomingAdapter = new UpcomingAdapter(saleskenActivity, tasks);
                                    Log.d(TAG,"UPCOMING>>>> "+ saleskenActivity.gson.toJson(tasks));
                                    recyclerView.setAdapter(upcomingAdapter);
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

    }
    private void hideProgress(){
        progress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showNodata(){
        recyclerView.setVisibility(View.GONE);
        no_data.setVisibility(View.VISIBLE);

    }
}
