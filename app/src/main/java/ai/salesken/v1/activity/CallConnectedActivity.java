package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ai.salesken.v1.R;
import ai.salesken.v1.activity.disposition.DispositionActivity;
import ai.salesken.v1.adapter.CompletedAdapter;
import ai.salesken.v1.adapter.CueAdapter;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CallConnectedActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    private CueAdapter cueAdapter;
    @BindView(R.id.cuerecycler)
    RecyclerView cuerecycler;
    @BindView(R.id.welcome_layout)
    ConstraintLayout welcome_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        welcome_layout.setVisibility(View.GONE);
        List<String> cues= new ArrayList<>();
        cues.add("cue1");
        cues.add("cue2");
        cueAdapter = new CueAdapter(this, cues);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager).setReverseLayout(true);
        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);
        cuerecycler.setLayoutManager(mLayoutManager);
        cuerecycler.setAdapter(cueAdapter);
    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_call_connected);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.hangup)
    public void hangUp(){
        startActivity(new Intent(CallConnectedActivity.this, DispositionActivity.class));
        finish();
    }

    @OnClick(R.id.hold)
    public void holdCall(){

    }


    @OnClick(R.id.back)
    public void back(){
        startActivity(new Intent(CallConnectedActivity.this,DialerActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }
}
