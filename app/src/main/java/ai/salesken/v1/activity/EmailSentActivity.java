package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import ai.salesken.v1.R;
import ai.salesken.v1.utils.SaleskenActivityImplementation;

public class EmailSentActivity  extends SaleskenActivity implements SaleskenActivityImplementation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();

    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_email_sent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(EmailSentActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
