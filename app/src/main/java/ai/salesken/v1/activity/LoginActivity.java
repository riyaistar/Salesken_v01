package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import ai.salesken.v1.R;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.login)
     Button login;
    @BindView(R.id.progress)
    ConstraintLayout progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
               progress.setVisibility(View.GONE);
            }
        }, 1000);

    }

    @OnClick(R.id.login)
    public void login()
    {
        Intent intent = new Intent(LoginActivity.this, DialerActivity.class);
        startActivity(intent);
        Log.d(TAG,"login clicked");
    }

    @OnClick(R.id.forgot_password)
    public void forgot_pass(){
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
        Log.d(TAG,"forgot_pass clicked");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
