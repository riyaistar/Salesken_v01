package ai.salesken.v1.activity.disposition;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import ai.salesken.v1.R;
import ai.salesken.v1.activity.DialerActivity;
import ai.salesken.v1.activity.SaleskenActivity;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DispositionActivity extends SaleskenActivity implements SaleskenActivityImplementation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_disposition);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.no_response)
    public void gotoNoResponse(){
        Intent i = new Intent(DispositionActivity.this, NoResponseActivity.class);
        startActivity(i);
        finish();
    }
    @OnClick(R.id.call_dropped)
    public void gotoCallDropped(){
        Intent i = new Intent(DispositionActivity.this, CallDroppedActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.wrong_person)
    public void gotoWrongPerson(){
        Intent i = new Intent(DispositionActivity.this, WrongPersonActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.wrong_number)
    public void gotoWrongNumber(){
        Intent i = new Intent(DispositionActivity.this, WrongNumberActivity.class);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.call_answered)
    public void gotoCallAnswered(){
        Intent i = new Intent(DispositionActivity.this, CallAnsweredActivity.class);
        startActivity(i);
        finish();
    }


    @OnClick(R.id.skipDisposition)
    public void skipDisposition(){
        Intent i = new Intent(DispositionActivity.this, DialerActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
