package ai.salesken.v1.activity.disposition;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import ai.salesken.v1.R;
import ai.salesken.v1.activity.SaleskenActivity;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoResponseActivity extends SaleskenActivity implements SaleskenActivityImplementation {
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
    private int mYear, mMonth, mDay, mHour, mMinute;
    String date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
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
                        time = hour + ":" + minutes;
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @OnClick(R.id.writeSMS)
    public void writeSMS(){
        if(sendSMS.isChecked()){
            input_layout_msg.setVisibility(View.VISIBLE);
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
}
