package ai.salesken.v1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import ai.salesken.v1.R;
import ai.salesken.v1.utils.ContactUtil;
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
    @BindView(R.id.login_content)
    ConstraintLayout login_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        requestAllpermission();

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
               login_content.setVisibility(View.VISIBLE);
            }
        }, 1000);

    }

    @OnClick(R.id.login)
    public void login()
    {
        Intent intent = new Intent(LoginActivity.this, DialerActivity.class);
        startActivity(intent);
        finish();
        Log.d(TAG,"login clicked");
    }

    @OnClick(R.id.forgot_password)
    public void forgot_pass(){
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
        finish();
        Log.d(TAG,"forgot_pass clicked");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 200){
            for(int i=0;i<permissions.length;i++){
                if(permissions[i].equalsIgnoreCase(Manifest.permission.READ_CONTACTS)){
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED){

                    }
                }else{
                    if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.READ_CONTACTS)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("Contact permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 200);
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    }
                }

            }
        }
    }
}
