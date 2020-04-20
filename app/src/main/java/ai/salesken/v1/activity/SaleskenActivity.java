package ai.salesken.v1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.github.abdularis.civ.CircleImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import ai.salesken.v1.R;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;
import ai.salesken.v1.pojo.User;
import ai.salesken.v1.pojo.UserRole;
import ai.salesken.v1.utils.ContactObserver;
import ai.salesken.v1.utils.ContactUtil;
import ai.salesken.v1.utils.MediaSaver;
import ai.salesken.v1.utils.RestApiClient;
import ai.salesken.v1.utils.RestUrlInterface;

public class SaleskenActivity extends AppCompatActivity {
    String[] PERMISSIONS = {Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO,Manifest.permission.FOREGROUND_SERVICE,Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS,Manifest.permission.BLUETOOTH};
    private static final int PERMISSION_REQUEST_CODE = 200;
    public Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private static final String TAG ="SaleskenActivity" ;
    public SharedPreferences sharedpreferences;
    public SharedPreferences.Editor editor;
    public RestUrlInterface restUrlInterface;
    public RequestManager requestManager;
    private ContactObserver contactObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesken);
        sharedpreferences = getSharedPreferences(getResources().getString(R.string.shared_preference_key), Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        restUrlInterface= RestApiClient.getClient(SaleskenActivity.this).create(RestUrlInterface.class);
        requestManager = Glide.with(this);
        contactObserver =new ContactObserver(SaleskenActivity.this);
        if(checkContactPermission()){
            Log.d(TAG,"registering contact listener");

            getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true,contactObserver );


        }

    }

    public boolean requestAllpermission() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : PERMISSIONS) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_REQUEST_CODE);
            return false;
        }
        return true;
    }


    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) SaleskenActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(SaleskenActivity.this, message, Toast.LENGTH_SHORT);
        View view = toast.getView();

        view.getBackground().setColorFilter(getResources().getColor(R.color.theme_color), PorterDuff.Mode.SRC_IN);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(getResources().getColor(R.color.white));
        toast.setGravity(Gravity.BOTTOM, 0, 150);
        toast.show();
    }


    public static boolean isServiceRunningInForeground(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }

            }
        }
        return false;
    }

    public void setNavigationView(final DrawerLayout drawer, NavigationView navigationView, int itemno) {
        lockDrawer(drawer);
        drawer.setScrimColor(Color.TRANSPARENT);
        View headerLayout = navigationView.getHeaderView(0); // 0-index header
        User user = getCurrentUser();
        CircleImageView profile_image = headerLayout.findViewById(R.id.profile_image);
        Glide.with(this).load(user.getProfileImage()).into(profile_image);
        TextView name = headerLayout.findViewById(R.id.first_name);

        if(user.getName() != null){
            try {
                name.setText(user.getName().split(" ")[0]);
            }catch (Exception e){
                name.setText(user.getName());
            }
        }

       TextView role = headerLayout.findViewById(R.id.role);

        String roles ="";
       if(user.getRoles() != null) {
           for (UserRole userRole : user.getRoles()) {
               roles += userRole.getName() + "\n";
           }
           role.setText(roles.replaceAll("_", " "));
       }else{
           role.setText("No Role");
       }
      //  first_name.setText(user.getName());
        if (itemno >= 0) {
            navigationView.getMenu().getItem(itemno).setChecked(true);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(SaleskenActivity.this, DialerActivity.class));
                        finish();
                        break;
                    case R.id.account:
                        startActivity(new Intent(SaleskenActivity.this, AccountActivity.class));
                        finish();
                        break;
                    case R.id.help:
                       // startActivity(new Intent(IstarActivity.this, LeadsActivity.class));
                       // finish();
                        break;

                    case R.id.logout:
                        editor.remove(SaleskenSharedPrefKey.USER);
                        editor.commit();
                        editor.apply();
                        Intent intent = new Intent(SaleskenActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        //editor.remove(SaleskenSharedPrefKey.SALES_USER);
                       // editor.remove(SaleskenSharedPrefKey.LEADS);
                        //editor.commit();
                       // editor.apply();
                        /*MediaSaver local_profile = new MediaSaver(IstarActivity.this).setParentDirectoryName("profile_pic").
                                setFileNameKeepOriginalExtension("profile_pic.jpg").
                                setExternal(MediaSaver.isExternalStorageReadable());
                        local_profile.deleteFile();
                        startActivity(intent);
                        finish();*/
                        break;

                }
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                lockDrawer(drawer);
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
    }

    public void unlockDrawer(DrawerLayout drawer) {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void lockDrawer(DrawerLayout drawer) {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }


    public boolean checkContactPermission()
    {
        String permission = Manifest.permission.READ_CONTACTS;
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public void fetchContact(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                new ContactUtil().fetchContacts(SaleskenActivity.this);

            }
        });
    }

    Boolean checkPermissionDenied(String permission){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(SaleskenActivity.this, permission) != PackageManager.PERMISSION_GRANTED
                    && !SaleskenActivity.this.shouldShowRequestPermissionRationale(permission)
                    && PreferenceManager.getDefaultSharedPreferences(SaleskenActivity.this).getBoolean(permission, false);
        }else{
            return true;
        }
    }

    public boolean isInternetAvailable() {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {

            Log.e("isInternetAvailable:",e.toString());
            return false;
        }
    }


    public User getCurrentUser(){
        if(sharedpreferences.getString(SaleskenSharedPrefKey.USER,null) != null ){
            String saved_user = sharedpreferences.getString(SaleskenSharedPrefKey.USER,null);
            User user = gson.fromJson(saved_user,User.class);
            return user;
        }else{
            return new User();
        }

    }

    @Override
    public void onDestroy() {
        getContentResolver().unregisterContentObserver(contactObserver);
        super.onDestroy();
    }
    private String getCapsSentences(String tagName) {
        String[] splits = tagName.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < splits.length; i++) {
            String eachWord = splits[i];
            if (i > 0 && eachWord.length() > 0) {
                sb.append(" ");
            }
            String cap = eachWord.substring(0, 1).toUpperCase()
                    + eachWord.substring(1);
            sb.append(cap);
        }
        return sb.toString();
    }
}
