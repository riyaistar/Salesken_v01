package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import ai.salesken.v1.R;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;
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
                fetchContact();
            } } ).start();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                if(sharedpreferences.getBoolean(SaleskenSharedPrefKey.FIRST_TIME,false)){
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    editor.putBoolean(SaleskenSharedPrefKey.FIRST_TIME,true);
                    editor.commit();
                    editor.apply();
                    Intent i = new Intent(SplashScreenActivity.this, IntroductionActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 1000);





    }
}
