package ai.salesken.v1.activity;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import ai.salesken.v1.R;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;
import ai.salesken.v1.pojo.SaleskenResponse;
import ai.salesken.v1.pojo.User;
import ai.salesken.v1.utils.MediaSaver;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import ai.salesken.v1.utils.SaveMediaAsync;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.login)
     Button login;
    @BindView(R.id.progress)
    public ConstraintLayout progress;
    @BindView(R.id.login_content)
    public ConstraintLayout login_content;

    @BindView(R.id.username)
    EditText username;
        @BindView(R.id.password)
    EditText password;
    @BindView(R.id.showpassword)
    ImageButton show_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                login.performClick();
                return false;
            }
        });

        if(sharedpreferences.getString(SaleskenSharedPrefKey.EMAIL,null) != null){
            username.setText(sharedpreferences.getString(SaleskenSharedPrefKey.EMAIL,null));
        }
        if(sharedpreferences.getString(SaleskenSharedPrefKey.PASSWORD,null) != null){
            password.setText(sharedpreferences.getString(SaleskenSharedPrefKey.PASSWORD,null));
        }
        if(sharedpreferences.getString(SaleskenSharedPrefKey.USER,null) !=null){
            startActivity(new Intent(LoginActivity.this,DialerActivity.class));
            finish();
        }else{
            requestAllpermission();
            show_password.setTag(true);
        }
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
        hideKeyboard();
        if(username.getText() == null || username.getText().toString().trim().equalsIgnoreCase("")){
            showToast("Username cannot be empty");
            username.requestFocus();
            return;
        }
        if(password.getText() == null || password.getText().toString().trim().equalsIgnoreCase("")) {
            showToast("Password cannot be empty");
            password.requestFocus();
            return;
        }

        if(username.getText().toString().trim().length()<3){
            showToast("Username is too short");
            username.requestFocus();
            return;

        }
        if(password.getText().toString().trim().length()<3){
            showToast("Password is too short");
            password.requestFocus();
            return;

        }
        User user = new User();
        editor.putString(SaleskenSharedPrefKey.EMAIL,username.getText().toString());
        editor.putString(SaleskenSharedPrefKey.PASSWORD,password.getText().toString());
        editor.commit();
        editor.apply();
        user.setEmail(username.getText().toString());
        user.setPassword(password.getText().toString());
        if(isInternetAvailable()) {
            showProgressbar();
            Call<SaleskenResponse> login_call = restUrlInterface.autenticate(user);
            login_call.enqueue(new Callback<SaleskenResponse>() {
                @Override
                public void onResponse(Call<SaleskenResponse> call, Response<SaleskenResponse> response) {
                    User serveruser = new User();
                    switch (response.code()) {
                        case 200:
                            SaleskenResponse saleskenResponse = response.body();
                            if (saleskenResponse.getResponseCode() == 200) {
                                try {
                                    Log.d(TAG, gson.toJson(saleskenResponse.getResponse()));
                                    serveruser = gson.fromJson(gson.toJson(saleskenResponse.getResponse()), User.class);
                                } catch (JsonSyntaxException jse) {
                                    showToast("Couldn't Process the Response recieved from Server");
                                    jse.printStackTrace();
                                } catch (Exception e) {
                                    showToast("Couldn't Process the Response recieved from Server");
                                }
                                editor.putString(SaleskenSharedPrefKey.TOKEN,response.headers().get("authorization"));
                                editor.putString(SaleskenSharedPrefKey.USER,gson.toJson(saleskenResponse.getResponse()));
                                editor.commit();
                                editor.apply();
                                startActivity(new Intent(LoginActivity.this,DialerActivity.class));
                                finish();
                            } else {
                                showToast(saleskenResponse.getResponseMessage());
                                hideProgressbar();
                            }
                            break;
                        default:
                            try {
                                SaleskenResponse saleskenResponse1 = gson.fromJson(response.errorBody().string(),SaleskenResponse.class);
                                showToast(saleskenResponse1.getResponseMessage());

                            } catch (JsonSyntaxException e) {
                                showToast("Bad request from the server");
                                e.printStackTrace();
                            } catch (IOException e) {
                                showToast("Bad request from the server");
                                e.printStackTrace();
                            }
                            hideProgressbar();


                    }
                }

                @Override
                public void onFailure(Call<SaleskenResponse> call, Throwable t) {
                    showToast("Connection Refuse.");
                    hideProgressbar();
                }
            });
        }else{
            showToast("Please Check your Internet Connection.");

        }
        Log.d(TAG,"login clicked");
    }

    @OnClick(R.id.forgot_password)
    public void forgot_pass(){
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
        finish();
        Log.d(TAG,"forgot_pass clicked");
    }

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();

    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 200){
            for(int i=0;i<permissions.length;i++){
                if(permissions[i].equalsIgnoreCase(Manifest.permission.READ_CONTACTS)){
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        fetchContact();

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

    private void showProgressbar(){
        progress.setVisibility(View.VISIBLE);
        login_content.setVisibility(View.GONE);
    }
    private void hideProgressbar(){
        progress.setVisibility(View.GONE);
        login_content.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.showpassword)
    public void showPassword() {
        if (show_password.getTag() != null && (Boolean) show_password.getTag()) {
            show_password.setTag(false);
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            show_password.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.theme_color));
            password.setSelection(password.getText().length());
        } else {
            show_password.setTag(true);
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            show_password.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.greyishBrown));
            password.setSelection(password.getText().length());

        }
    }
}
