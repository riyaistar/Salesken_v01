package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import ai.salesken.v1.R;
import ai.salesken.v1.activity.disposition.WrongPersonActivity;
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

public class AddLeadActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    private static final String TAG = "AddLeadActivity";
    @BindView(R.id.company_name)
    EditText company_name;
    @BindView(R.id.first_name)
    EditText first_name;
    @BindView(R.id.last_name)
    EditText last_name;
    @BindView(R.id.designation)
    EditText designation;
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.progress)
    ConstraintLayout progress;
    @BindView(R.id.container)
    ConstraintLayout container;
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();

        mobile.setText(getIntent().getStringExtra("contact"));
        if(getIntent().getStringExtra(SaleskenIntent.MOBILE)!=null){
            mobile.setText(getIntent().getStringExtra(SaleskenIntent.MOBILE));
        }
        if(!getIntent().getBooleanExtra(SaleskenIntent.IS_MOBILE_ENABLED,true)){
            mobile.setEnabled(false);
        }
    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_add_lead);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.save_contact)
    public void save_contact(){
        if(getIntent().getStringExtra(SaleskenIntent.MOBILE)!=null){
            editlead();
        }
    }

    private void editlead() {
        hideKeyboard();
        TaskSubmission taskSubmission = new TaskSubmission();
        taskSubmission.setDisposition("WrongPerson");
        taskSubmission.setId(18433872);
        taskSubmission.setFollowupActor(getCurrentUser().getId());
        if(company_name.getText().toString()!=null && !company_name.getText().toString().equalsIgnoreCase("")){
            taskSubmission.setCompanyName(company_name.getText().toString());
        }else{
            showToast("Please enter a Company name");
            return;
        }
        if((first_name.getText().toString()!=null && !first_name.getText().toString().equalsIgnoreCase("")) ||( last_name.getText().toString()!=null && !last_name.getText().toString().equalsIgnoreCase(""))){
            String name="";
            if((first_name.getText().toString()!=null && !first_name.getText().toString().equalsIgnoreCase("")) ){
                name = first_name.getText().toString() + " ";
            }
            if((last_name.getText().toString()!=null && !last_name.getText().toString().equalsIgnoreCase("")) ){
                name = name + last_name.getText().toString();
            }
            taskSubmission.setContactName(name);
        }else{
            showToast("Please enter a name");
            return;
        }
        if(designation.getText().toString()!=null && !designation.getText().toString().equalsIgnoreCase("")){
            taskSubmission.setContactDesignation(designation.getText().toString());
        }else{
            showToast("Please enter a designation");
            return;
        }
        if(email.getText().toString()!=null && !email.getText().toString().equalsIgnoreCase("")){
            if(isValidEmail(email.getText().toString())){
                taskSubmission.setContactEmail(email.getText().toString());
            }else{
                showToast("Please enter a valid email address");
                return;
            }
        }
        showProgress();
        Call<SaleskenResponse> disposition_call = restUrlInterface.disposition(sharedpreferences.getString(SaleskenSharedPrefKey.TOKEN,null),taskSubmission);
        disposition_call.enqueue(new Callback<SaleskenResponse>() {
            @Override
            public void onResponse(Call<SaleskenResponse> call, Response<SaleskenResponse> response) {
                switch (response.code()) {
                    case 200:
                        SaleskenResponse saleskenResponse = response.body();
                        if(saleskenResponse.getResponseCode() == 200){
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddLeadActivity.this);
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
                                    Intent i = new Intent(AddLeadActivity.this, DialerActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            });
                            skip.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    Intent i = new Intent(AddLeadActivity.this, DialerActivity.class);
                                    startActivity(i);
                                    finish();
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

    public void showProgress(){
        progress.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
    }

    public void hideProgress(){
        progress.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(dialog != null){
            dialog.dismiss();
        }
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getIntent().getStringExtra(SaleskenIntent.MOBILE)!=null){
            Intent i = new Intent(AddLeadActivity.this, WrongPersonActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent(AddLeadActivity.this, DialerActivity.class);
            startActivity(i);
            finish();
        }
    }
}
