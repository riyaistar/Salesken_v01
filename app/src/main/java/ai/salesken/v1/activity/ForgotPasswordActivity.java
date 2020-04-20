package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import ai.salesken.v1.R;
import ai.salesken.v1.constant.SaleskenIntent;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;
import ai.salesken.v1.pojo.SaleskenResponse;
import ai.salesken.v1.pojo.User;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    private static final String TAG = "ForgotPasswordActivity";
    @BindView(R.id.send_email)
    Button send_email;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.progress)
    ConstraintLayout progress;
    @BindView(R.id.content)
    ConstraintLayout content;
    String otp, useremail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        if(sharedpreferences.getString(SaleskenSharedPrefKey.EMAIL,null) != null){
            email.setText(sharedpreferences.getString(SaleskenSharedPrefKey.EMAIL,null));
        }
        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                send_email.performClick();
                return false;
            }
        });
    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.send_email)
    public void sendEmail(){
        showProgress();
        User user = new User();
        useremail = email.getText().toString();
        if(useremail!=null && !useremail.equalsIgnoreCase("")){
            user.setEmail(useremail);
            hideKeyboard();
            Call<SaleskenResponse> forgot_call = restUrlInterface.forgot_password(user);
            forgot_call.enqueue(new Callback<SaleskenResponse>() {
                @Override
                public void onResponse(Call<SaleskenResponse> call, Response<SaleskenResponse> response) {
                    switch (response.code()) {
                        case 200:
                            SaleskenResponse saleskenResponse = response.body();
                            if (saleskenResponse.getResponseCode() == 200) {
                                otp=saleskenResponse.getResponse().toString();
                                Log.d(TAG,otp);
                                Intent intent = new Intent(ForgotPasswordActivity.this, EmailSentActivity.class);
                                intent.putExtra(SaleskenIntent.EMAIL,useremail);
                                intent.putExtra(SaleskenIntent.OTP,otp);
                                startActivity(intent);
                                finish();
                            }else{
                                showToast(saleskenResponse.getResponseMessage());
                                hideProgress();
                            }
                            break;
                        default:
                            showToast("Bad request recieved from server.");
                            hideProgress();
                            break;
                    }
                }

                @Override
                public void onFailure(Call<SaleskenResponse> call, Throwable t) {
                    showToast("Connection Refused");
                    hideProgress();
                }
            });
        }else {
            showToast("Please enter an email.");
            hideProgress();
        }


    }
    @OnClick(R.id.back)
    public void back(){
        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
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
}
