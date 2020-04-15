package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import ai.salesken.v1.R;
import ai.salesken.v1.utils.CustomSpinnerAdapter;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    @BindView(R.id.edit)
    ImageButton edit;
    @BindView(R.id.profile_picture)
    ImageView profile_image;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            profile_image.setClipToOutline(true);
        }
        setNavigationView(drawer, navigationView, 1);

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
}
