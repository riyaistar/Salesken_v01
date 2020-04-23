package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Update;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
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
import com.github.abdularis.civ.CircleImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import ai.salesken.v1.R;
import ai.salesken.v1.async.UpdateUserAsync;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;
import ai.salesken.v1.pojo.SaleskenResponse;
import ai.salesken.v1.pojo.User;
import ai.salesken.v1.utils.CustomSpinnerAdapter;
import ai.salesken.v1.utils.MediaSaver;
import ai.salesken.v1.utils.PictureUploadUtil;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends SaleskenActivity implements SaleskenActivityImplementation,EditText.OnEditorActionListener {
    private static final String TAG = "AccountActivity";
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
    private PictureUploadUtil pictureUploadUtil;
    private static final int SELECT_FILE = 101;
    private static final int REQUEST_CAMERA = 100;
    public User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            profile_image.setClipToOutline(true);
        }
        pictureUploadUtil = new PictureUploadUtil(this);
        setNavigationView(drawer, navigationView, 1);
        user=getCurrentUser();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        if(checkStoragePermission()) {
            MediaSaver local_profile = new MediaSaver(AccountActivity.this).setParentDirectoryName("profile_pic").
                    setFileNameKeepOriginalExtension("profile_pic.jpg").
                    setExternal(MediaSaver.isExternalStorageReadable());
            requestManager.setDefaultRequestOptions(requestOptions.centerCrop())
                    .load(local_profile.pathFile()).into(profile_image);
        }else{
            requestAllpermission();
            requestManager.setDefaultRequestOptions(requestOptions.centerCrop())
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

    @OnClick(R.id.upload_img)
    public void uploadProfilePic() {
        if (checkStoragePermission()) {
            pictureUploadUtil.selectImage();
        } else {
            if(checkPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, perms, 200);
            }else {
                showToast("Storage Permission needed to upload Profile Image. Please allow Storage permission in your application settings.");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, 200);
                    }
                }, 3000);
            }


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                //showToast("Press save button to update your profile picture");
                if (requestCode == SELECT_FILE)
                    onSelectFromGalleryResult(data);

                else if (requestCode == REQUEST_CAMERA)
                    // onSelectFromGalleryResult(data);

                    onCaptureImageResult(data);

            }
        } catch (OutOfMemoryError oxy) {
            oxy.printStackTrace();
        }
    }


    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                profile_image.setImageBitmap(bm);
                saveImage(bm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void onCaptureImageResult(Intent data) {
        final MediaSaver temp_profile_pic = new MediaSaver(this).
                setParentDirectoryName("profile_pic").
                setFileNameKeepOriginalExtension("temp_profile_pic.jpg").
                setExternal(MediaSaver.isExternalStorageReadable());
        RequestOptions requestOptions = new RequestOptions();
        //requestOptions.placeholder(R.drawable.no_image_found);
        //requestOptions.error(R.drawable.no_image_found);
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestManager.setDefaultRequestOptions(requestOptions.centerCrop())
                .load(temp_profile_pic.pathFile()).into(profile_image);
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                try {
                    MediaSaver local_profile = new MediaSaver(AccountActivity.this).setParentDirectoryName("profile_pic").
                            setFileNameKeepOriginalExtension("profile_pic.jpg").
                            setExternal(MediaSaver.isExternalStorageReadable());
                    local_profile.pathFile().delete();
                    local_profile.createFile();
                    local_profile.save(new FileInputStream(temp_profile_pic.pathFile()));
                    uploadToServer();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void saveImage(Bitmap bitmap) {

        try {
            MediaSaver local_profile = new MediaSaver(this).setParentDirectoryName("profile_pic").
                    setFileNameKeepOriginalExtension("profile_pic.jpg").
                    setExternal(MediaSaver.isExternalStorageReadable());
            local_profile.pathFile().delete();
            local_profile.createFile();
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(local_profile.pathFile()));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
            os.flush();
            os.close();

            uploadToServer();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadToServer() {
        showProgressBar();
        MediaSaver local_profile = new MediaSaver(this).setParentDirectoryName("profile_pic").
                setFileNameKeepOriginalExtension("profile_pic.jpg").
                setExternal(MediaSaver.isExternalStorageReadable());
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), local_profile.pathFile());
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", local_profile.pathFile().getName(), fileReqBody);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        Call<SaleskenResponse> call = restUrlInterface.uploadImage(sharedpreferences.getString(SaleskenSharedPrefKey.TOKEN,""),part, description);
        call.enqueue(new Callback<SaleskenResponse>() {
            @Override
            public void onResponse(Call<SaleskenResponse> call, Response<SaleskenResponse> response) {
                switch (response.code()) {
                    case 200:
                        SaleskenResponse saleskenResponse = response.body();
                        Log.d(TAG, gson.toJson(saleskenResponse));
                        new UpdateUserAsync(AccountActivity.this).execute((String) saleskenResponse.getResponse());
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
            }
        });
    }
    private void updateUser(User user1){
        Log.d(TAG,"update user url ");
        editor.putString(SaleskenSharedPrefKey.USER,gson.toJson(user1));
        editor.commit();
        editor.apply();
    }

    private void updateUser(String profileImage){
        Log.d(TAG,"update image url ");
        user.setProfileImage(profileImage);
        editor.putString(SaleskenSharedPrefKey.USER,gson.toJson(user));
        editor.commit();
        editor.apply();
    }

    public void updateDrawer() {
        View headerLayout = navigationView.getHeaderView(0);
        CircleImageView profile_image = headerLayout.findViewById(R.id.profile_image);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        if(checkStoragePermission()) {
            MediaSaver local_profile = new MediaSaver(AccountActivity.this).setParentDirectoryName("profile_pic").
                    setFileNameKeepOriginalExtension("profile_pic.jpg").
                    setExternal(MediaSaver.isExternalStorageReadable());
            if (local_profile.pathFile().exists())
                requestManager.setDefaultRequestOptions(requestOptions.circleCrop())
                        .load(local_profile.pathFile()).into(profile_image);
            else
                requestManager.setDefaultRequestOptions(requestOptions.circleCrop())
                        .load(user.getProfileImage()).into(profile_image);
        }else{
            requestManager.setDefaultRequestOptions(requestOptions.circleCrop())
                    .load(user.getProfileImage()).into(profile_image);
        }
        hideProgressBar();

    }
}
