package ai.salesken.v1.activity.disposition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import ai.salesken.v1.R;
import ai.salesken.v1.activity.DialerActivity;
import ai.salesken.v1.activity.SaleskenActivity;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CallAnsweredActivity extends SaleskenActivity implements SaleskenActivityImplementation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_call_answered);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.dnd_text_click)
    public void dnd_text_click(){
        gotoDashboard();
    }

    @OnClick(R.id.lost_lead_click)
    public void lost_lead_click(){
        gotoDashboard();
    }

    @OnClick(R.id.follow_up_click)
    public void follow_up_click(){
        gotoDashboard();
    }

    @OnClick(R.id.lead_won_click)
    public void lead_won_click(){
        gotoDashboard();
    }


    @OnClick(R.id.skipDisposition)
    public void skipDisposition(){
        gotoDashboard();
    }

    public void gotoDashboard(){
        Intent i = new Intent(CallAnsweredActivity.this, DialerActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    @OnClick(R.id.back)
    public void backClick(){
        goBack();
    }

    private void goBack() {
        Intent i = new Intent(CallAnsweredActivity.this,DispositionActivity.class);
        startActivity(i);
        finish();
    }

}
