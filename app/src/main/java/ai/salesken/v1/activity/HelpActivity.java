package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ai.salesken.v1.R;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;
import ai.salesken.v1.pojo.SaleskenIssue;
import ai.salesken.v1.pojo.SaleskenResponse;
import ai.salesken.v1.utils.CustomSpinnerAdapter;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpActivity extends SaleskenActivity implements SaleskenActivityImplementation {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.container)
    ConstraintLayout container;
    @BindView(R.id.progress)
    ConstraintLayout progress;
    @BindView(R.id.reason)
    Spinner reason;
    @BindView(R.id.description)
    EditText description;
    private List<String> reason_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        setNavigationView(drawer, navigationView, 2);
        reason_list.add("Not able to Call");
        reason_list.add("Background Noise in the Call");
        reason_list.add("Client can't hear me");
        reason_list.add("Noise Distortion");
        reason_list.add("Other Reason");
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,reason_list);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reason.setAdapter(aa);
        description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                submitIssue();
                return false;
            }
        });
    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.openDrawer)
    public void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
        unlockDrawer(drawer);
    }

    @OnClick(R.id.submit)
    public void submitIssue(){
        hideKeyboard();
            if(description.getText() != null && !description.getText().toString().equalsIgnoreCase("")){
                if(isInternetAvailable()) {
                    showProgressBar();
                    SaleskenIssue saleskenIssue = new SaleskenIssue(reason.getSelectedItem().toString(), description.getText().toString());
                    Call<SaleskenResponse> issue_call = restUrlInterface.issues(sharedpreferences.getString(SaleskenSharedPrefKey.TOKEN, ""), saleskenIssue);
                    issue_call.enqueue(new Callback<SaleskenResponse>() {
                        @Override
                        public void onResponse(Call<SaleskenResponse> call, Response<SaleskenResponse> response) {
                            switch (response.code()) {
                                case 200:
                                    SaleskenResponse saleskenResponse = response.body();
                                    if (saleskenResponse.getResponseCode() == 200) {
                                                if((Boolean)saleskenResponse.getResponse()){
                                                    showToast("Issue Submitted Successfully.");
                                                    description.setText("");
                                                    reason.setSelection(0,true);
                                                }else{
                                                    showToast("Issue not Submitted. Please try after sometime");

                                                }
                                    }
                                    break;
                                default:
                                    try {
                                        SaleskenResponse saleskenResponse1 = gson.fromJson(response.errorBody().string(), SaleskenResponse.class);
                                        showToast(saleskenResponse1.getResponseMessage());

                                    } catch (JsonSyntaxException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                            }
                            hideProgressBar();

                        }

                        @Override
                        public void onFailure(Call<SaleskenResponse> call, Throwable t) {
                            showToast("Connection Refuse.");
                            hideProgressBar();
                        }
                    });
                }else{
                    showToast("Please Check your Internet Connection.");
                }
            }else{
                showToast("Please enter a description about your issue.");

            }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(HelpActivity.this, DialerActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private void showProgressBar(){
        progress.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
    }
    private void hideProgressBar(){
        progress.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);


    }
}
