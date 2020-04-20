package ai.salesken.v1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import ai.salesken.v1.R;
import ai.salesken.v1.adapter.TaskAdapter;
import ai.salesken.v1.utils.BottomBarUtil;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    private TaskAdapter taskAdapter;

    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        new BottomBarUtil().setupBottomBar(navigation, TaskActivity.this, R.id.tasks);
        setNavigationView(drawer, navigationView, 0);
        String[] tabTitles = new String[]{"UPCOMING", "RECENT", "COMPLETED"};
        taskAdapter= new TaskAdapter(this,getSupportFragmentManager(), tabTitles);
        viewPager.setAdapter(taskAdapter);
        tabLayout.setupWithViewPager(viewPager);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_task);
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



}
