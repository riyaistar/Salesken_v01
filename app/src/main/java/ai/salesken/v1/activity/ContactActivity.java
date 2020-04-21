package ai.salesken.v1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;

import ai.salesken.v1.R;
import ai.salesken.v1.adapter.ContactAdapter;
import ai.salesken.v1.async.FetchContactAsync;
import ai.salesken.v1.constant.SaleskenIntent;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;
import ai.salesken.v1.pojo.ContactPojo;
import ai.salesken.v1.utils.AppDatabase;
import ai.salesken.v1.utils.BottomBarUtil;
import ai.salesken.v1.utils.ContactObserver;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

public class ContactActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    private static final String TAG = "ContactActivity";
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.searchview)
    SearchView searchview;
    @BindView(R.id.result)
    TextView result;
    @BindView(R.id.found)
    TextView found;
    @BindView(R.id.contact_list)
    public IndexFastScrollRecyclerView contact_list;
    private ContactAdapter contactAdapter;
    private List<ContactPojo> contactPojos = new ArrayList<>();
    @BindView(R.id.nocontactpermission)
    ConstraintLayout nocontactpermission;
    @BindView(R.id.progress)
    ConstraintLayout progress;
    @BindView(R.id.container)
    ConstraintLayout container;
    private ContactBroadcast contactBroadcast = new ContactBroadcast();
    private boolean isReceiverRegistered = false;
    private AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(ContactActivity.this);
        getView();
        registerReceiver();
        new BottomBarUtil().setupBottomBar(navigation, ContactActivity.this, R.id.contact);
        setNavigationView(drawer, navigationView, 0);


        if(checkContactPermission()){
            nocontactpermission.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            container.setVisibility(View.GONE);
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    contactPojos=db.contactDao().getAll();
                    if(contactPojos!= null && contactPojos.size()>0){
                        showContacts(contactPojos);
                    }else {
                        new FetchContactAsync(ContactActivity.this).execute();
                    }
                }
            });


        }else{
            String[] perms = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
            ActivityCompat.requestPermissions(this, perms, 200);
            nocontactpermission.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);

        }

    }

    public void showContacts(List<ContactPojo> contactPojos){
        container.setVisibility(View.VISIBLE);


        contactPojos=db.contactDao().getAll();
        if(contactPojos.size()>0) {
            result.setText(contactPojos.size()+"");
            searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterContactList(newText);
                    return false;
                }
            });
            contactPojos=sortStringThenNumber(contactPojos);
            //Collections.sort(contactPojos, String.CASE_INSENSITIVE_ORDER);

            contactAdapter = new ContactAdapter(ContactActivity.this, contactPojos);


            contact_list.setAdapter(contactAdapter);
            contact_list.setLayoutManager(new LinearLayoutManager(ContactActivity.this));
            // contact_list.addItemDecoration(sectionItemDecoration);
            contact_list.setIndexBarColor(R.color.white);
            contact_list.setIndexTextSize(14);
            contact_list.setIndexBarTextColor(R.color.greyishBrown);
            //contact_list.setIndexbarHighLateTextColor(R.color.black_theme_text);
            contact_list.setIndexbarHighLightTextColor(R.color.white);
            contact_list.setIndexBarVisibility(true);
            contact_list.setIndexBarStrokeColor("#00000000");

            //contact_list.setPreviewVisibility(false);
            // contact_list.setIndexBarHighLateTextVisibility(true);
            contact_list.setIndexBarTransparentValue((float) 1);
            contact_list.setIndexbarWidth(70);
            contact_list.setIndexbarMargin(14);
            searchview.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        result.setVisibility(View.GONE);
                        found.setVisibility(View.GONE);
                    } else {
                        result.setVisibility(View.VISIBLE);
                        found.setVisibility(View.VISIBLE);
                    }
                }
            });
        }else{
            contact_list.setIndexBarVisibility(false);
            searchview.setVisibility(View.GONE);
        }

    }


    private static List<ContactPojo> sortStringThenNumber(List<ContactPojo> contactPojos) {

        List<ContactPojo> numbers = new ArrayList<ContactPojo>();
        List<ContactPojo> strings = new ArrayList<ContactPojo>();

        for (ContactPojo contactPojo : contactPojos) {
            if (contactPojo.getName().trim().matches("^\\d.*")|| contactPojo.getName().startsWith("+")) {
                numbers.add(contactPojo);
            } else {
                strings.add(contactPojo);
            }
        }
        contactPojos = null;
        Collections.sort(numbers, new Comparator<ContactPojo>() {
            @Override
            public int compare(ContactPojo contactPojo, ContactPojo contactPojo1) {
                try
                {
                    int i1 = Integer.parseInt(contactPojo.getName());
                    int i2 = Integer.parseInt(contactPojo1.getName());
                    return i1 - i2;
                }
                catch (NumberFormatException e)
                {
                    return contactPojo.getName().toLowerCase().compareTo(contactPojo1.getName().toLowerCase());
                }


            }

        });
        Collections.sort(strings, new Comparator<ContactPojo>() {
            @Override
            public int compare(ContactPojo contactPojo, ContactPojo contactPojo1) {
                return contactPojo.getName().toLowerCase().compareTo(contactPojo1.getName().toLowerCase());



            }

        });


        List<ContactPojo> all = new ArrayList<ContactPojo>();
        all.addAll(strings);
        for(ContactPojo contactPojo:numbers){
            contactPojo.setName("#"+contactPojo.getName());
        }
        all.addAll(numbers);

        return all;
    }

    private void filterContactList(String newText) {
        if(newText.trim().length()>0) {
            List<ContactPojo> tempContactPojos = new ArrayList();
            for (ContactPojo contactPojo : contactPojos) {
                if (contactPojo.getName().trim().toLowerCase().contains(newText.toLowerCase().trim())) {
                    tempContactPojos.add(contactPojo);
                }
            }
            contactAdapter.updateList(tempContactPojos);
        }else{
            contactAdapter.updateList(contactPojos);

        }

    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

    }
    @OnClick(R.id.imageButton)
    public void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
        unlockDrawer(drawer);
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           /* Intent intent = new Intent(TaskDetailActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();*/
        }
    }


    @OnClick(R.id.allowcontact)
    public void allowContact(){
        if(checkPermissionDenied(Manifest.permission.READ_CONTACTS)) {
            String[] perms = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
            ActivityCompat.requestPermissions(this, perms, 200);
        }else{
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);

            // startActivity(intent);
            startActivityForResult(intent,200);
            finish();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG,requestCode+" *******");
        if (requestCode == 200) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equalsIgnoreCase(Manifest.permission.READ_CONTACTS)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        new FetchContactAsync(ContactActivity.this).execute();
                    }
                } else {

                }
            }
        }
    }

    public void showProgressBar() {

        progress.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progress.setVisibility(View.GONE);

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "The onResume() event");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "The onDestroy() event");

    unregisterReceiver();
    }


    private void registerReceiver() {

        if(!isReceiverRegistered){
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SaleskenIntent.CONTACT_BROADCAST);

            LocalBroadcastManager.getInstance(this).registerReceiver(contactBroadcast,intentFilter);
            isReceiverRegistered = true;
            //fetchToken();
        }
    }
    private void unregisterReceiver() {
        if(contactBroadcast != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(contactBroadcast);
            isReceiverRegistered = false;

        }
    }


    private class ContactBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Intent already filtered by the LocalBroadcastManager in onResume()
            switch (intent.getAction()){
                case SaleskenIntent.CONTACT_BROADCAST:

                    contactPojos=db.contactDao().getAll();
                        showContacts(contactPojos);
                    break;

            }
        }
    }
}
