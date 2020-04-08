package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login)
    public void login(){
        Log.d(TAG,"login clicked");
    }

    @OnClick(R.id.forgot_password)
    public void forgot_pass(){
        Log.d(TAG,"forgot_pass clicked");
    }
}
