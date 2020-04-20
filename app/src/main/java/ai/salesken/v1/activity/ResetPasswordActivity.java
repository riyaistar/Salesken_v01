package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ai.salesken.v1.R;
import ai.salesken.v1.constant.SaleskenIntent;
import ai.salesken.v1.pojo.SaleskenResponse;
import ai.salesken.v1.pojo.User;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    @BindView(R.id.new_password)
    EditText new_password;
    @BindView(R.id.confirm_password)
    EditText confirm_password;
    @BindView(R.id.reset)
    Button reset;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.progress)
    ConstraintLayout progress;
    @BindView(R.id.content)
    ConstraintLayout content;
    String email,otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        if(getIntent()!= null){
            if(getIntent().getStringExtra(SaleskenIntent.EMAIL) != null){
                email = getIntent().getStringExtra(SaleskenIntent.EMAIL);
            }
            if(getIntent().getStringExtra(SaleskenIntent.OTP)!=null){
                otp = getIntent().getStringExtra(SaleskenIntent.OTP);
            }
        }
    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.reset)
    public void resetPassword(){
        String password1 = new_password.getText().toString();
        String password2 = confirm_password.getText().toString();
        Object objStr = password2;
        if(!password1.equalsIgnoreCase("") || !password2.equalsIgnoreCase("") || password1.length()<4 || password2.length()<4){
            if(objStr.equals(password1)){
                User user = new User();
                user.setEmail(email);
                user.setAuthToken(otp);
                user.setPassword(password1);
                showProgress();
                hideKeyboard();
                Call<SaleskenResponse> reset_call = restUrlInterface.reset_password(user);
                reset_call.enqueue(new Callback<SaleskenResponse>() {
                    @Override
                    public void onResponse(Call<SaleskenResponse> call, Response<SaleskenResponse> response) {
                        switch (response.code()) {
                            case 200:
                                SaleskenResponse saleskenResponse = response.body();
                                if (saleskenResponse.getResponseCode() == 200) {
                                        showToast("Password changed successfully!");
                                        login.setVisibility(View.VISIBLE);
                                        hideProgress();
                                }
                                break;
                            default:
                                showToast("Could not reset password. Try again later.");
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
            }else{
                showToast("Passwords do no match");
            }
        } else{
            showToast("New Password cannot be empty and should be more than 4 characters");
        }
    }

    public void showProgress(){
        progress.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
    }

    public void hideProgress(){
        progress.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.login)
    public void gotoLogin(){
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
