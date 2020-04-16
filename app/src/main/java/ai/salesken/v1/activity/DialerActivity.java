package ai.salesken.v1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import ai.salesken.v1.R;
import ai.salesken.v1.activity.disposition.DispositionActivity;
import ai.salesken.v1.utils.BottomBarUtil;
import ai.salesken.v1.utils.ContactUtil;
import ai.salesken.v1.utils.KeypadlessKeypad;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialerActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;


    @BindView(R.id.button2)
    Button one;
    @BindView(R.id.button3)
    Button two;
    @BindView(R.id.button4)
    Button three;
    @BindView(R.id.button5)
    Button four;
    @BindView(R.id.button6)
    Button five;
    @BindView(R.id.button7)
    Button six;
    @BindView(R.id.button8)
    Button seven;
    @BindView(R.id.button9)
    Button eight;
    @BindView(R.id.button10)
    Button nine;
    @BindView(R.id.button11)
    Button star;
    @BindView(R.id.button12)
    Button zero;
    @BindView(R.id.button13)
    Button hash;
    @BindView(R.id.callbutton)
    ImageButton callButton;
    @BindView(R.id.backslash)
    ImageButton backslash;
    @BindView(R.id.add_contacts)
    Button add_contacts;

    @BindView(R.id.keypad)
    KeypadlessKeypad keypad;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        keypad.setCursorVisible(false);
        keypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(keypad.length() != 0){
                        keypad.setCursorVisible(true);
                    }
            }
        });
        keypad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(keypad.length() != 0){
                    keypad.setCursorVisible(true);
                    add_contacts.setVisibility(View.VISIBLE);
                }else{
                    keypad.setCursorVisible(false);
                    add_contacts.setVisibility(View.GONE);

                }
            }
        });
        keypad.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                keypad.setCursorVisible(true);
                return false;
            }
        });
        keypad.setOnEditTextActionListener(
                new KeypadlessKeypad.OnEditTextActionListener() {
                    @Override
                    public void onPaste() {
                        String mobileNumber = keypad.getText().toString();

                        if (TextUtils.isEmpty(mobileNumber)) {
                            return;
                        }
                    }
                });
        backslash.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                keypad.getText().clear();
                keypad.setCursorVisible(false);
                return true;
            }
        });
        setButtonClickListner(one, two, three, four, five, six, seven, eight, nine, star, hash, zero);
        zero.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String digitToSet="+";
                if (!TextUtils.isEmpty(digitToSet)) {
                    char digit = digitToSet.charAt(0);
                    keypad.getText().insert(keypad.getSelectionStart(), digitToSet);
                    // If the cursor is at the end of the text we hide it.
                    final int length = keypad.length();
                    if (length == keypad.getSelectionStart() && length == keypad.getSelectionEnd()) {
                        keypad.setCursorVisible(false);
                    }
                }

                return true;
            }
        });
        new BottomBarUtil().setupBottomBar(navigation, DialerActivity.this, R.id.dialer);
        setNavigationView(drawer, navigationView, 0);



    }
    @OnClick(R.id.backslash)
    public void delete_number() {
        int pos = keypad.getSelectionStart();
        if (pos > 0) {
            keypad
                    .getText().delete(pos - 1, pos);
        }
    }

    @OnClick(R.id.add_contacts)
    public void addNewContact(){
        Intent intent = new Intent(DialerActivity.this,AddLeadActivity.class);
        intent.putExtra("contact",keypad.getText().toString());
        startActivity(intent);
        finish();
    }


    public void setButtonClickListner(Button... buttons) {
        for (final TextView button : buttons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String digitToSet=button.getText().toString();
                    if (!TextUtils.isEmpty(digitToSet)) {
                        char digit = digitToSet.charAt(0);
                        keypad.getText().insert(keypad.getSelectionStart(), digitToSet);
                        // If the cursor is at the end of the text we hide it.
                        final int length = keypad.length();
                        if (length == keypad.getSelectionStart() && length == keypad.getSelectionEnd()) {
                            keypad.setCursorVisible(false);
                        }
                    }
                }
            });
        }
    }



    @OnClick(R.id.imageButton)
    public void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
        unlockDrawer(drawer);
    }
    @Override
    public void getView() {
        setContentView(R.layout.activity_dialer);
        ButterKnife.bind(this);
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

    @OnClick(R.id.callbutton)
    public void call(){
        startActivity(new Intent(DialerActivity.this, CallConnectedActivity.class));
        finish();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 200){
            for(int i=0;i<permissions.length;i++){
                if(permissions[i].equalsIgnoreCase(Manifest.permission.READ_CONTACTS)){
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        fetchContact();
                    }
                }

            }
        }
    }
}
