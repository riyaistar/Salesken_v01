package ai.salesken.v1.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ai.salesken.v1.R;
import ai.salesken.v1.activity.disposition.NoResponseActivity;
import ai.salesken.v1.adapter.LanguageAdapter;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;
import ai.salesken.v1.pojo.SaleskenResponse;
import ai.salesken.v1.pojo.User;
import ai.salesken.v1.utils.CustomSpinnerAdapter;
import ai.salesken.v1.utils.MediaSaver;
import ai.salesken.v1.utils.PictureUploadUtil;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import ai.salesken.v1.viewholder.LanguageViewHolder;
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

public class EditAccountActivity extends SaleskenActivity implements SaleskenActivityImplementation,EditText.OnEditorActionListener {
    private static final String TAG = "EditAccountActivity";



    private User user;

    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.number)
    EditText number;
    @BindView(R.id.sip_username)
    EditText sip_username;
    @BindView(R.id.sippassword)
    EditText sip_password;
    @BindView(R.id.sipdomain)
    EditText sip_domain;
    @BindView(R.id.sipprovider)
    EditText sip_provider;

    @BindView(R.id.progress)
    ConstraintLayout progress;
    @BindView(R.id.container)
    ConstraintLayout container;

    @BindView(R.id.language)
    EditText language;
    AlertDialog dialog;
    private HashMap<String,String> language_map=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        language_map.put("en-IN","English - IN");
        language_map.put("en-US","English - US");
        language_map.put("hi-IN","Hindi");

        user=getCurrentUser();
        name.setText(user.getName());
        number.setText(user.getMobile());

        if(user.getSipUserName() != null){
            sip_username.setText(user.getSipUserName());
        }
        if(user.getSipPassword() != null){
            sip_password.setText(user.getSipPassword());
        }
        if(user.getSipURL() != null){
            sip_domain.setText(user.getSipURL());
        }
        if(user.getSipProvider() != null){
            sip_provider.setText(user.getSipProvider());
        }
        if(user.getLanguage() !=null){
            String display_text="";
            for(String lang:user.getLanguage().split(",")){
                if(language_map.get(lang)!=null){
                    display_text+=language_map.get(lang)+",";
                }

            }
            language.setText(display_text.substring(0,display_text.length()-1));

        }





    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_edit_account);
        ButterKnife.bind(this);
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
    @OnClick(R.id.submit)
    public void submitProfile(){
        if(isInternetAvailable()) {
            hideKeyboard();


            if (name.getText() == null || name.getText().toString().equalsIgnoreCase("")) {
                showToast("Name cannot be empty");
                return;
            }
        /*if(number.getText() == null || number.getText().toString().equalsIgnoreCase("")){
            showToast("Number cannot be empty");
            return;
        }*/
            if (sip_username.getText() == null || sip_username.getText().toString().equalsIgnoreCase("")) {
                showToast("Sip Username cannot be empty");
                return;
            }
            if (sip_password.getText() == null || sip_password.getText().toString().equalsIgnoreCase("")) {
                showToast("Sip Password cannot be empty");
                return;
            }
            if (sip_domain.getText() == null || sip_domain.getText().toString().equalsIgnoreCase("")) {
                showToast("Sip Domain cannot be empty");
                return;
            }
            if (sip_provider.getText() == null || sip_provider.getText().toString().equalsIgnoreCase("")) {
                showToast("Sip Provider cannot be empty");
                return;
            }
            if (language.getText() == null || language.getText().toString().equalsIgnoreCase("")) {
                showToast("Language cannot be empty");
                return;
            }

            user.setName(name.getText().toString());
            user.setSipUserName(sip_username.getText().toString());
            user.setSipProvider(sip_provider.getText().toString());
            user.setSipPassword(sip_password.getText().toString());
            String set_language = "";
            showProgressBar();
            for (String selected_lang : language.getText().toString().split(",")) {
                for (String key : language_map.keySet()) {
                    if (language_map.get(key).equalsIgnoreCase(selected_lang)) {
                        set_language += key + ",";
                    }
                }
            }
            user.setLanguage(set_language.substring(0, set_language.length() - 1));
            Call<SaleskenResponse> update_user = restUrlInterface.updateUser(sharedpreferences.getString(SaleskenSharedPrefKey.TOKEN, ""), user);
            update_user.enqueue(new Callback<SaleskenResponse>() {
                @Override
                public void onResponse(Call<SaleskenResponse> call, Response<SaleskenResponse> response) {
                    switch (response.code()) {
                        case 200:
                            SaleskenResponse saleskenResponse = response.body();
                            if (saleskenResponse.getResponseCode() == 200) {
                                if((Boolean)saleskenResponse.getResponse()){
                                    updateUser(user);
                                    showToast("Profile Successfully changed.");

                                }else{
                                    showToast("Profile can't updated from server");
                                }
                            }
                            break;
                        default:
                            try {
                                SaleskenResponse saleskenResponse1 = gson.fromJson(response.errorBody().string(),SaleskenResponse.class);
                                showToast(saleskenResponse1.getResponseMessage());

                            } catch (JsonSyntaxException e) {
                                showToast("Server Couldn't Process the request. Please try again later.");
                                e.printStackTrace();
                            } catch (IOException e) {
                                showToast("Server Couldn't Process the request. Please try again later.");
                                e.printStackTrace();
                            }
                    }
                    hideProgressBar();
                }

                @Override
                public void onFailure(Call<SaleskenResponse> call, Throwable t) {
                    hideProgressBar();
                    showToast("Connection Refuse.");

                }
            });
        }else{
            showToast("Please Check your Internet Connection.");
        }

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

    @OnClick(R.id.language)
    public void openLanguage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditAccountActivity.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.language_layout, null);
        ImageButton close = customLayout.findViewById(R.id.close);
        Button submit_language = customLayout.findViewById(R.id.submit_language);
        RecyclerView language_recycler= customLayout.findViewById(R.id.language_recycler);
        ArrayList<String> languages = new ArrayList<>();
        languages.add("English - US");
        languages.add("English - IN");
        languages.add("Hindi");
        LanguageAdapter languageAdapter =new LanguageAdapter(languages,language.getText().toString());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(EditAccountActivity.this);
        language_recycler.setLayoutManager(mLayoutManager);
        language_recycler.setAdapter(languageAdapter);
        builder.setView(customLayout);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        submit_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String display_language="";
                for (int x = language_recycler.getChildCount(), i = 0; i < x; ++i) {
                    LanguageViewHolder holder = (LanguageViewHolder) language_recycler.getChildViewHolder(language_recycler.getChildAt(i));
                    TextView lang_text = holder.itemView.findViewById(R.id.lang_text);
                    CheckBox checkBox = holder.itemView.findViewById(R.id.checkBox);
                    if(checkBox.isChecked()){
                        display_language+=lang_text.getText()+",";
                    }
                    Log.d(TAG,"Text "+lang_text.getText()+" check "+checkBox.isChecked());
                }
                language.setText(display_language.substring(0,display_language.length()-1));

                dialog.dismiss();

            }
        });
      dialog= builder.create();
       dialog.show();
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
           /* if(v.getId() == current_password.getId()){
                new_password.requestFocus();
            }else if(v.getId() == new_password.getId()){
                confirm_password.requestFocus();
            }else if(v.getId() == confirm_password.getId()){
                changePassword();
            }*/
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(dialog!=null){
            dialog.dismiss();
        }
    }
}