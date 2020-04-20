package ai.salesken.v1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import ai.salesken.v1.MainActivity;
import ai.salesken.v1.R;
import ai.salesken.v1.utils.PinEntryEditText;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EmailSentActivity  extends SaleskenActivity implements SaleskenActivityImplementation {
    @BindView(R.id.pinEntryEditText)
    PinEntryEditText txtPinEntry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();

        txtPinEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == "123456".length()) {

                }
            }
        });
    }


    @Override
    public void getView() {
        setContentView(R.layout.activity_email_sent);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(EmailSentActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
