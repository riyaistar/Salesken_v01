package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;

import ai.salesken.v1.R;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;
import ai.salesken.v1.pojo.SaleskenResponse;
import ai.salesken.v1.pojo.User;
import ai.salesken.v1.utils.CustomSpinnerAdapter;
import ai.salesken.v1.utils.MediaSaver;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends SaleskenActivity implements SaleskenActivityImplementation,EditText.OnEditorActionListener {
    @BindView(R.id.edit)
    ImageButton edit;
    @BindView(R.id.profile_picture)
    ImageView profile_image;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.current_password)
    EditText current_password;
    @BindView(R.id.new_password)
    EditText new_password;
    @BindView(R.id.confirm_password)
    EditText confirm_password;

    @BindView(R.id.container)
    ConstraintLayout container;

    @BindView(R.id.progress)
    ConstraintLayout progress;

    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            profile_image.setClipToOutline(true);
        }
        setNavigationView(drawer, navigationView, 1);
        user=getCurrentUser();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        if(checkStoragePermission()) {
            MediaSaver local_profile = new MediaSaver(AccountActivity.this).setParentDirectoryName("profile_pic").
                    setFileNameKeepOriginalExtension("profile_pic.jpg").
                    setExternal(MediaSaver.isExternalStorageReadable());
            requestManager.setDefaultRequestOptions(requestOptions.circleCrop())
                    .load(local_profile.pathFile()).into(profile_image);
        }else{
            requestAllpermission();
            requestManager.setDefaultRequestOptions(requestOptions.circleCrop())
                    .load(user.getProfileImage()).into(profile_image);
        }
        name.setText(user.getName());
        email.setText(user.getEmail());
        phone.setText(user.getMobile());
        onKeyBoardSubmit(current_password,new_password,confirm_password);
    }

    public void onKeyBoardSubmit(EditText ... editTexts){
        for(EditText editText:editTexts){
            editText.setOnEditorActionListener(this);
        }
    }


    @Override
    public void getView() {
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.edit)
    public void editClick(){
        Intent i = new Intent(AccountActivity.this, EditAccountActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.submit)
    public void changePassword(){
        if(current_password.getText() == null || current_password.getText().toString().equalsIgnoreCase("")){
            showToast("Please enter your current password");
            return;
        }
        if(new_password.getText() == null || new_password.getText().toString().equalsIgnoreCase("")){
            showToast("Please enter your new password");
            return;
        }
        if(confirm_password.getText() == null || confirm_password.getText().toString().equalsIgnoreCase("")){
            showToast("Please enter your confirm password");
            return;
        }
        if(new_password.getText().length()<6){
            showToast("New password is too short. It should be atleast 6 character length.");
            return;
        }
        Object objStr = confirm_password.getText().toString();
        if(objStr.equals(new_password.getText().toString())){
            user.setPassword(current_password.getText().toString());
            user.setNewPassword(new_password.getText().toString());
            Call<SaleskenResponse> change_password_call = restUrlInterface.change_password(sharedpreferences.getString(SaleskenSharedPrefKey.TOKEN,""),user);
            showProgressBar();
            change_password_call.enqueue(new Callback<SaleskenResponse>() {
                @Override
                public void onResponse(Call<SaleskenResponse> call, Response<SaleskenResponse> response) {
                    switch (response.code()) {
                        case 200:
                            SaleskenResponse saleskenResponse = response.body();
                            if (saleskenResponse.getResponseCode() == 200) {
                                try{
                                    if((Boolean) saleskenResponse.getResponse()){
                                        editor.putString(SaleskenSharedPrefKey.PASSWORD,new_password.getText().toString());
                                        editor.commit();
                                        editor.apply();
                                        showToast("Password Changed sucessfully.");
                                        new_password.setText("");
                                        confirm_password.setText("");
                                        current_password.setText("");
                                    }
                                }catch (Exception e){

                                }


                            } else {
                                showToast(saleskenResponse.getResponseMessage());
                            }
                            hideProgressBar();

                            break;
                        default:
                            try {
                                SaleskenResponse saleskenResponse1 = gson.fromJson(response.errorBody().string(),SaleskenResponse.class);
                                showToast(saleskenResponse1.getResponseMessage());

                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            hideProgressBar();
                    }
                }

                @Override
                public void onFailure(Call<SaleskenResponse> call, Throwable t) {
                    hideProgressBar();
                    showToast("Server Unreachable.");

                }
            });

        }else{
            showToast("New Password and Confirm Password do not match.");

        }


    }


    @OnClick(R.id.openDrawer)
    public void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
        unlockDrawer(drawer);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(AccountActivity.this, DialerActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private void showProgressBar(){
        progress.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
    }
    private void hideProgressBar(){
        progress.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);


    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if(v.getId() == current_password.getId()){
                new_password.requestFocus();
            }else if(v.getId() == new_password.getId()){
                confirm_password.requestFocus();
            }else if(v.getId() == confirm_password.getId()){
                changePassword();
            }
        }
        return false;
    }
}
