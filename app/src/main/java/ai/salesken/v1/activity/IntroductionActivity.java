package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import ai.salesken.v1.R;
import ai.salesken.v1.adapter.IntroductionAdapter;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntroductionActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tab)
    TabLayout tab;
    private IntroductionAdapter introductionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();

        introductionAdapter = new IntroductionAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(introductionAdapter);
        tab.setupWithViewPager(viewPager, true);
    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_introduction);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.next)
    public void changePager(){
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
    }
}
