package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import ai.salesken.v1.R;
import ai.salesken.v1.adapter.IntroductionAdapter;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntroductionActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    private static final String TAG = "IntroductionActivity";
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
        if(viewPager.getCurrentItem()==2){
            Intent intent = new Intent(IntroductionActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);

    }

    @OnClick(R.id.skip)
    public void skipPager(){
        Intent intent = new Intent(IntroductionActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
