package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ai.salesken.v1.MainActivity;
import ai.salesken.v1.R;
import ai.salesken.v1.constant.SaleskenIntent;
import ai.salesken.v1.pojo.SaleskenResponse;
import ai.salesken.v1.pojo.User;
import ai.salesken.v1.utils.PinEntryEditText;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class EmailSentActivity  extends SaleskenActivity implements SaleskenActivityImplementation {
    private static final String TAG = "EmailSentActivity";
    @BindView(R.id.pinEntryEditText)
    PinEntryEditText txtPinEntry;
    @BindView(R.id.verify)
    Button verify;
    @BindView(R.id.error)
    TextView error;
    @BindView(R.id.progress)
    ConstraintLayout progress;
    String email,otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        txtPinEntry.requestFocus();
        verify.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(txtPinEntry, InputMethodManager.SHOW_IMPLICIT);

        txtPinEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                error.setVisibility(View.GONE);
                if (s.length() == 6) {
                    verify.setVisibility(View.VISIBLE);
                }else{
                    verify.setVisibility(View.GONE);
                }
            }
        });

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
        setContentView(R.layout.activity_email_sent);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(EmailSentActivity.this, ForgotPasswordActivity.class);
        startActivity(i);
        finish();
    }
    @OnClick(R.id.verify)
    public void verify(){
        if(otp.equalsIgnoreCase(txtPinEntry.getText().toString())){
            Intent intent = new Intent(EmailSentActivity.this, ResetPasswordActivity.class);
            intent.putExtra(SaleskenIntent.EMAIL,email);
            intent.putExtra(SaleskenIntent.OTP,otp);
            startActivity(intent);
            finish();
        }else{
            error.setVisibility(View.VISIBLE);
        }
    }
}
