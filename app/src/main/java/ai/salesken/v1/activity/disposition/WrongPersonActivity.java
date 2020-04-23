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
import ai.salesken.v1.activity.AddLeadActivity;
import ai.salesken.v1.activity.DialerActivity;
import ai.salesken.v1.activity.SaleskenActivity;
import ai.salesken.v1.constant.SaleskenIntent;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;
import ai.salesken.v1.pojo.SaleskenResponse;
import ai.salesken.v1.pojo.TaskSubmission;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WrongPersonActivity extends SaleskenActivity implements SaleskenActivityImplementation {

    private static final String TAG = "WrongPersonActivity";
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
        setContentView(R.layout.activity_wrong_person);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.dnd)
    public void dnd_text_click(){
        TaskSubmission taskSubmission = new TaskSubmission();
        taskSubmission.setIsDnd(true);
        Log.d(TAG,gson.toJson(taskSubmission));
        dispositionCall(taskSubmission);

    }

    @OnClick(R.id.lead_lost)
    public void lost_lead_click(){
        TaskSubmission taskSubmission = new TaskSubmission();
        taskSubmission.setIsLeadLost(true);
        Log.d(TAG,gson.toJson(taskSubmission));
        dispositionCall(taskSubmission);

    }

    @OnClick(R.id.edit_lead)
    public void edit_lead_click(){
        Intent i = new Intent(WrongPersonActivity.this, AddLeadActivity.class);
        i.putExtra(SaleskenIntent.IS_MOBILE_ENABLED, false);
        i.putExtra(SaleskenIntent.MOBILE,"918145906972");
        startActivity(i);
        finish();
    }

    @OnClick(R.id.skipDisposition)
    public void skipDisposition(){
        gotoDashboard();
    }

    public void gotoDashboard(){
        Intent i = new Intent(WrongPersonActivity.this, DialerActivity.class);
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
        Intent i = new Intent(WrongPersonActivity.this,DispositionActivity.class);
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
        taskSubmission.setDisposition("WrongPerson");
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(WrongPersonActivity.this);
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
