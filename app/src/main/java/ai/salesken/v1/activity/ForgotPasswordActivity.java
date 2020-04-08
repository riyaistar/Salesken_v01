package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import ai.salesken.v1.R;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    @BindView(R.id.send_email)
    Button send_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.send_email)
    public void sendEmail(){
        Intent intent = new Intent(ForgotPasswordActivity.this, EmailSentActivity.class);
        startActivity(intent);
    }

}
