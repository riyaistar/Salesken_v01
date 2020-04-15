package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import ai.salesken.v1.R;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddLeadActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    @BindView(R.id.company_name)
    EditText company_name;
    @BindView(R.id.first_name)
    EditText first_name;
    @BindView(R.id.last_name)
    EditText last_name;
    @BindView(R.id.designation)
    EditText designation;
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.email)
    EditText email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();

        mobile.setText(getIntent().getStringExtra("contact"));
    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_add_lead);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.save_contact)
    public void save_contact(){

    }
}
