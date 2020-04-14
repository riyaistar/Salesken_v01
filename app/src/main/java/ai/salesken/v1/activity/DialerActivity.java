package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ai.salesken.v1.R;
import ai.salesken.v1.utils.BottomBarUtil;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DialerActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        new BottomBarUtil().setupBottomBar(navigation, DialerActivity.this, R.id.dialer);

    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_dialer);
        ButterKnife.bind(this);
    }
}
