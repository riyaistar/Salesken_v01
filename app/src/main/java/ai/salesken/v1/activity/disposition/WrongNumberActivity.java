package ai.salesken.v1.activity.disposition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import ai.salesken.v1.R;
import ai.salesken.v1.activity.DialerActivity;
import ai.salesken.v1.activity.SaleskenActivity;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;
import ai.salesken.v1.pojo.SaleskenResponse;
import ai.salesken.v1.pojo.Task;
import ai.salesken.v1.pojo.TaskSubmission;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WrongNumberActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    private static final String TAG = "WrongNumberActivity" ;
    @BindView(R.id.progress)
    ConstraintLayout progress;
    @BindView(R.id.content)
    ConstraintLayout content;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_wrong_number);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.dnd_text_click)
    public void dnd_text_click(){
        TaskSubmission taskSubmission = new TaskSubmission();
        taskSubmission.setIsDnd(true);
        Log.d(TAG,gson.toJson(taskSubmission));
        dispositionCall(taskSubmission);
    }

    @OnClick(R.id.lost_lead_click)
    public void lost_lead_click(){
        TaskSubmission taskSubmission = new TaskSubmission();
        taskSubmission.setIsLeadLost(true);
        Log.d(TAG,gson.toJson(taskSubmission));
        dispositionCall(taskSubmission);
        //gotoDashboard();
    }

    @OnClick(R.id.skipDisposition)
    public void skipDisposition(){
        gotoDashboard();
    }

    public void gotoDashboard(){
        Intent i = new Intent(WrongNumberActivity.this, DialerActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    @OnClick(R.id.back)
    public void backClick(){
        goBack();
    }

    private void goBack() {
        Intent i = new Intent(WrongNumberActivity.this,DispositionActivity.class);
        startActivity(i);
        finish();
    }
    public void showProgress(){
        progress.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
    }
    public void hideProgress(){
        progress.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(dialog != null){
            dialog.dismiss();
        }
    }

    public void dispositionCall(TaskSubmission taskSubmission){
        taskSubmission.setDisposition("WrongNumber");
        taskSubmission.setId(18433872);
        taskSubmission.setIsFollowup(false);
        taskSubmission.setFollowupActor(getCurrentUser().getId());
        showProgress();
        Call<SaleskenResponse> disposition_call = restUrlInterface.disposition(sharedpreferences.getString(SaleskenSharedPrefKey.TOKEN,null),taskSubmission);
        disposition_call.enqueue(new Callback<SaleskenResponse>() {

            @Override
            public void onResponse(Call<SaleskenResponse> call, Response<SaleskenResponse> response) {
                switch (response.code()) {
                    case 200:
                        SaleskenResponse saleskenResponse = response.body();
                        if (saleskenResponse.getResponseCode() == 200) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(WrongNumberActivity.this);
                            final View customLayout = getLayoutInflater().inflate(R.layout.feedback_layout, null);
                            ImageButton close = customLayout.findViewById(R.id.close);
                            Button skip = customLayout.findViewById(R.id.skip);
                            Button save = customLayout.findViewById(R.id.save_contact);

                            builder.setView(customLayout);

                            dialog = builder.create();
                            hideProgress();
                            save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    gotoDashboard();
                                }
                            });
                            skip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    gotoDashboard();
                                }
                            });
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        } else {
                            showToast(saleskenResponse.getResponseMessage());
                            hideProgress();

                        }
                        break;
                    default:
                        showToast("Bad Request");
                        hideProgress();

                        break;
                }

            }

            @Override
            public void onFailure(Call<SaleskenResponse> call, Throwable t) {
                showToast("Connection refused");
                hideProgress();
            }
        });
    }
}
