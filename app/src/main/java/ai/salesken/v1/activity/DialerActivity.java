package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import ai.salesken.v1.R;
import ai.salesken.v1.utils.BottomBarUtil;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        new BottomBarUtil().setupBottomBar(navigation, DialerActivity.this, R.id.dialer);
        setNavigationView(drawer, navigationView, 1);

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
}
