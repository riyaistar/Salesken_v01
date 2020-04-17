package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import ai.salesken.v1.R;
import ai.salesken.v1.utils.ContactUtil;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.ButterKnife;

public class SplashScreenActivity extends SaleskenActivity implements SaleskenActivityImplementation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        if(checkContactPermission()){
            new Thread( new Runnable() { @Override public void run() {
                // Run whatever background code you want here.
                new ContactUtil().fetchContacts(SplashScreenActivity.this);
            } } ).start();
        }

            new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent i = new Intent(SplashScreenActivity.this, IntroductionActivity.class);
                startActivity(i);
                finish();
            }
        }, 1000);

    }
}
