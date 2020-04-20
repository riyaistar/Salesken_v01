package ai.salesken.v1.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import ai.salesken.v1.R;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;
import ai.salesken.v1.pojo.SaleskenResponse;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAccountActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    private static final String TAG = "EditAccountActivity";
    @BindView(R.id.spinner)
    Spinner language;
    @BindView(R.id.profile_picture)
    ImageView profile_image;
    private PictureUploadUtil pictureUploadUtil;
    private static final int SELECT_FILE = 101;
    private static final int REQUEST_CAMERA = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            profile_image.setClipToOutline(true);
        }
        pictureUploadUtil = new PictureUploadUtil(this);

        ArrayList<String> languages = new ArrayList<>();
        languages.add("English - US");
        languages.add("English - IN");
        languages.add("Hindi");

        CustomSpinnerAdapter dataAdapter = new CustomSpinnerAdapter(this, R.layout.customspinner, languages);
        language.setAdapter(dataAdapter);
    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_edit_account);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.upload_img)
    public void uploadProfilePic() {
        if (requestAllpermission()) {
            pictureUploadUtil.selectImage();
        } else {
            showToast("Microphone,Camera,Location permissions needed for running this app. Please allow in your application settings.");

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
                    MediaSaver local_profile = new MediaSaver(EditAccountActivity.this).setParentDirectoryName("profile_pic").
                            setFileNameKeepOriginalExtension("profile_pic.jpg").
                            setExternal(MediaSaver.isExternalStorageReadable());
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
                    setFileName("profile_pic.jpg").
                    setExternal(MediaSaver.isExternalStorageReadable());
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
        MediaSaver local_profile = new MediaSaver(this).setParentDirectoryName("profile_pic").
                setFileName("profile_pic.jpg").
                setExternal(MediaSaver.isExternalStorageReadable());
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), local_profile.pathFile());
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", local_profile.pathFile().getName(), fileReqBody);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        Call<SaleskenResponse> call = restUrlInterface.uploadImage(sharedpreferences.getString(SaleskenSharedPrefKey.TOKEN,""),part, description);
        call.enqueue(new Callback<SaleskenResponse>() {
            @Override
            public void onResponse(Call<SaleskenResponse> call, Response<SaleskenResponse> response) {
                SaleskenResponse saleskenResponse = response.body();
                Log.d(TAG,gson.toJson(saleskenResponse));
            }

            @Override
            public void onFailure(Call<SaleskenResponse> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.back)
    public void backClick(){
        goBack();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    private void goBack() {
        startActivity(new Intent(EditAccountActivity.this, AccountActivity.class));
        finish();
    }
}