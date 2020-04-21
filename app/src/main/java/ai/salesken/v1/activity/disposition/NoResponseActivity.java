package ai.salesken.v1.activity.disposition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import ai.salesken.v1.R;
import ai.salesken.v1.activity.DialerActivity;
import ai.salesken.v1.activity.SaleskenActivity;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;
import ai.salesken.v1.pojo.SaleskenResponse;
import ai.salesken.v1.pojo.TaskSubmission;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoResponseActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    private static final String TAG = "NoResponseActivity" ;
    @BindView(R.id.datetxt)
    EditText datetxt;
    @BindView(R.id.timetxt)
    EditText timetxt;
    @BindView(R.id.input_layout_msg)
    TextInputLayout input_layout_msg;
    @BindView(R.id.msg)
    EditText msg;
    @BindView(R.id.sendSMS)
    CheckBox sendSMS;
    @BindView(R.id.progress)
    ConstraintLayout progress;
    @BindView(R.id.content)
    ConstraintLayout content;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String date, time;
    SaleskenActivity saleskenActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        sendSMS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    if(isChecked){
                        input_layout_msg.setVisibility(View.VISIBLE);
                        msg.requestFocus();
                    }else{
                        input_layout_msg.setVisibility(View.GONE);
                    }
              }
          }
        );
    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_no_response);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.datetxt)
    public void dateClick(){
        selectDate();
    }

    @OnClick(R.id.date_picker)
    public void datePickerClick(){
        selectDate();
    }

    public void selectDate(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String day= dayOfMonth+"", month= (monthOfYear+1)+"";
                        if (dayOfMonth<10){
                            day = "0"+dayOfMonth;
                        }
                        if(monthOfYear<10){
                            month = "0"+ (monthOfYear + 1);
                        }
                        datetxt.setText(day + "-" + month + "-" + year);
                        date = year+ "-" + month + "-" + day ;
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    @OnClick(R.id.timetxt)
    public void timeClick(){
        selectTime();
    }

    @OnClick(R.id.time_picker)
    public void timePickerClick(){
        selectTime();
    }

    private void selectTime() {
// Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String dataHour = hourOfDay+"";
                        String hour = hourOfDay+"", minutes = minute+"";
                        if(hourOfDay >= 12){
                            hourOfDay = hourOfDay -12;
                            if(hourOfDay!=12) {
                                hour = hourOfDay+ "";
                            }
                            if(hourOfDay<10){
                                hour = "0"+hourOfDay;
                            }
                            if(minute<10){
                                minutes = "0"+minute;
                            }
                            if(hourOfDay == 0){
                                hour = "12";
                            }
                            timetxt.setText(hour + ":" + minutes + " PM");
                        }else {
                            if(hourOfDay == 0){
                                hour = "12";
                            }
                            if(hourOfDay<10 && hourOfDay != 0){
                                hour = "0"+hourOfDay;
                            }
                            if(minute<10){
                                minutes = "0"+minute;
                            }
                            timetxt.setText(hour + ":" + minutes + " AM");
                        }
                        time = dataHour + ":" + minutes;
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @OnClick(R.id.writeSMS)
    public void writeSMS(){
        if(sendSMS.isChecked()){
            input_layout_msg.setVisibility(View.GONE);
            sendSMS.setChecked(false);
        }else{
            input_layout_msg.setVisibility(View.VISIBLE);
            sendSMS.setChecked(true);
            msg.requestFocus();
        }
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
        Intent i = new Intent(NoResponseActivity.this,DispositionActivity.class);
        startActivity(i);
        finish();
    }
    @OnClick(R.id.submit_btn)
    public void submitClick(){
        TaskSubmission taskSubmission = new TaskSubmission();
        taskSubmission.setDisposition("NoResponse");
        taskSubmission.setId(18433872);
        taskSubmission.setIsFollowup(true);
        taskSubmission.setFollowupActor(getCurrentUser().getId());
        taskSubmission.setIsSendSMS(sendSMS.isChecked());
        if(sendSMS.isChecked()){
            taskSubmission.setSmsContent(msg.getText().toString());
        }
        if(date != null && !date.equalsIgnoreCase("")){
            taskSubmission.setFollowupDate(date);
            if(time != null && !time.equalsIgnoreCase("")){
                taskSubmission.setFollowupTime(time);
                Log.d(TAG,gson.toJson(taskSubmission));
                showProgress();
                Call<SaleskenResponse> disposition_call = restUrlInterface.disposition(sharedpreferences.getString(SaleskenSharedPrefKey.TOKEN,null),taskSubmission);
                disposition_call.enqueue(new Callback<SaleskenResponse>() {
                    @Override
                    public void onResponse(Call<SaleskenResponse> call, Response<SaleskenResponse> response) {
                        switch (response.code()) {
                            case 200:
                                SaleskenResponse saleskenResponse = response.body();
                                if(saleskenResponse.getResponseCode() == 200){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(NoResponseActivity.this);
                                    final View customLayout = getLayoutInflater().inflate(R.layout.feedback_layout, null);
                                    ImageButton close = customLayout.findViewById(R.id.close);
                                    Button skip = customLayout.findViewById(R.id.skip);
                                    Button save = customLayout.findViewById(R.id.save_contact);

                                    builder.setView(customLayout);

                                    AlertDialog dialog = builder.create();
                                    hideProgress();
                                    save.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(NoResponseActivity.this, DialerActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    });
                                    skip.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(NoResponseActivity.this, DialerActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    });
                                    close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                } else {
                                    showToast(saleskenResponse.getResponseMessage());
                                    hideProgress();

                                }
                                break;
                            default:
                                showToast("Bad Request");
                                hideProgress();

                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<SaleskenResponse> call, Throwable t) {
                        showToast("Connection refused");
                        hideProgress();

                    }
                });
            }else{
                showToast("Please pick a time");
            }
        }else{
            showToast("Please pick a date");
        }
    }


    public void showProgress(){
        progress.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
    }
    public void hideProgress(){
        progress.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
    }
}
