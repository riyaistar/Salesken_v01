package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ai.salesken.v1.R;

public class SaleskenActivity extends AppCompatActivity {
    private static final String TAG ="SaleskenActivity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesken);
    }
}
