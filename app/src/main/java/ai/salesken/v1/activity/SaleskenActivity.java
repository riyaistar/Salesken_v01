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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    private RestUrlInterface restUrlInterface;
    public RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesken);
        sharedpreferences = getSharedPreferences(getResources().getString(R.string.shared_preference_key), Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        restUrlInterface= RestApiClient.getClient(SaleskenActivity.this).create(RestUrlInterface.class);
        requestManager = Glide.with(this);

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
       // User user = getCurrentUser();
       // CircleImageView circleImageView = headerLayout.findViewById(R.id.profile_image);
       // Glide.with(this).load(user.getProfileImage()).into(circleImageView);

       // TextView first_name = headerLayout.findViewById(R.id.first_name);
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

}
