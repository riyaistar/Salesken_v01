package ai.salesken.v1.activity.disposition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ai.salesken.v1.R;
import ai.salesken.v1.activity.DialerActivity;
import ai.salesken.v1.activity.SaleskenActivity;
import ai.salesken.v1.constant.SaleskenIntent;
import ai.salesken.v1.constant.SaleskenSharedPrefKey;
import ai.salesken.v1.pojo.PipelineStage;
import ai.salesken.v1.pojo.SaleskenResponse;
import ai.salesken.v1.pojo.TaskSubmission;
import ai.salesken.v1.utils.CustomSpinnerAdapter;
import ai.salesken.v1.utils.SaleskenActivityImplementation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ScheduleActivity extends SaleskenActivity implements SaleskenActivityImplementation {
    private static final String TAG = "ScheduleActivity";
    @BindView(R.id.status)
    Spinner status;
    @BindView(R.id.stage)
    Spinner stage;
    @BindView(R.id.datetxt)
    EditText datetxt;
    @BindView(R.id.timetxt)
    EditText timetxt;
    @BindView(R.id.progress)
    ConstraintLayout progress;
    @BindView(R.id.content)
    ConstraintLayout content;
    @BindView(R.id.deal_value)
    TextView deal_value;
    @BindView(R.id.edit_deal_value)
    ImageButton edit_deal_value;
    @BindView(R.id.deal_value_txt)
    EditText deal_value_txt;
    @BindView(R.id.done_edit)
    ImageButton done_edit;
    @BindView(R.id.msg)
    EditText msg;
    @BindView(R.id.count_character)
    TextView count_character;
    AlertDialog dialog;
    Integer task_id;
    private int mYear, mMonth, mDay, mHour, mMinute, selectedStageId;
    String date, time;
    ArrayList<PipelineStage> stages = new ArrayList<>();
    ArrayList<Integer> stageids = new ArrayList<>();
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    private SimpleDateFormat sdf =new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat sdf1 =new SimpleDateFormat("yyyy-MM-dd");

    DateFormat amPmFormat = new SimpleDateFormat("hh:mm a");
    DateFormat hour_24_Format = new SimpleDateFormat("hh:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
        if(getIntent()!=null){
            task_id = getIntent().getIntExtra(SaleskenIntent.TASK_ID,-1);
        }
        ArrayList<String> status_list = new ArrayList<>();
        status_list.add("Confident");
        status_list.add("Angry");

        CustomSpinnerAdapter statusAdapter = new CustomSpinnerAdapter(this, R.layout.customspinner, status_list);
        status.setAdapter(statusAdapter);

        Call<SaleskenResponse> stage_call = restUrlInterface.stage_list(sharedpreferences.getString(SaleskenSharedPrefKey.TOKEN,null),task_id);
        stage_call.enqueue(new Callback<SaleskenResponse>() {
            @Override
            public void onResponse(Call<SaleskenResponse> call, Response<SaleskenResponse> response) {
                switch (response.code()) {
                    case 200:
                        SaleskenResponse saleskenResponse = response.body();
                        if (saleskenResponse.getResponseCode() == 200) {

                            Type stagesType = new TypeToken<ArrayList<PipelineStage>>(){}.getType();
                            stages = gson.fromJson(gson.toJson(saleskenResponse.getResponse()), stagesType);

                            ArrayList<String> stagenames = new ArrayList<>();
                            stageids = new ArrayList<>();
                            stagenames.add("Select Stage");
                            for(PipelineStage stage: stages){
                                stagenames.add(stage.getStageName());
                                stageids.add(stage.getId());
                            }
                            Log.d(TAG,stageids+"");
                            CustomSpinnerAdapter stageAdapter = new CustomSpinnerAdapter(ScheduleActivity.this, R.layout.customspinner, stagenames);
                            stage.setAdapter(stageAdapter);
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Call<SaleskenResponse> call, Throwable t) {

            }
        });
        stage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(stageids!=null && stageids.size()>0){
                    if(position!=0){
                        selectedStageId = stageids.get(position-1);
                    }
                }
                Log.d(TAG, "selected Stage Id"+ selectedStageId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                count_character.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        datePickerDialog = new DatePickerDialog(this,
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

        timePickerDialog = new TimePickerDialog(this,
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

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        Calendar calendar = Calendar.getInstance();
        datetxt.setText(sdf.format(calendar.getTime()));
        date = sdf1.format(calendar.getTime());
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        timetxt.setText(amPmFormat.format(calendar.getTime()).toUpperCase());
        time=hour_24_Format.format(calendar.getTime());
    }

    @Override
    public void getView() {
        setContentView(R.layout.activity_schedule);
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

        timePickerDialog.show();
    }

    @OnClick(R.id.back)
    public void goBack(){
        Intent i = new Intent(ScheduleActivity.this, CallAnsweredActivity.class);
        i.putExtra(SaleskenIntent.TASK_ID,task_id);
        startActivity(i);
        finish();
    }

    @OnClick(R.id.skip)
    public void skipDisposition(){
        Intent i = new Intent(ScheduleActivity.this, DialerActivity.class);
        startActivity(i);
        finish();
    }
    @OnClick(R.id.submit)
    public void dispositionCall(){
        TaskSubmission taskSubmission = new TaskSubmission();
        taskSubmission.setDisposition("CallAnswered");
        taskSubmission.setId(18433872);
        taskSubmission.setIsFollowup(true);
        taskSubmission.setFollowupActor(getCurrentUser().getId());
        taskSubmission.setIsScheduleActivity(true);
        if(deal_value.getText().toString()!=null && !deal_value.getText().toString().equalsIgnoreCase("")){
            float f=Float.parseFloat("23.6");
            taskSubmission.setDealValue(f);
        }
        if(!msg.getText().toString().equalsIgnoreCase("")){
            taskSubmission.setCallNotes(msg.getText().toString());
        }
            taskSubmission.setScheduleDate(date);
                taskSubmission.setScheduletime(time);
                showProgress();
                Call<SaleskenResponse> disposition_call = restUrlInterface.disposition(sharedpreferences.getString(SaleskenSharedPrefKey.TOKEN,null),taskSubmission);
                disposition_call.enqueue(new Callback<SaleskenResponse>() {
                    @Override
                    public void onResponse(Call<SaleskenResponse> call, Response<SaleskenResponse> response) {
                        switch (response.code()) {
                            case 200:
                                SaleskenResponse saleskenResponse = response.body();
                                if (saleskenResponse.getResponseCode() == 200) {
                                    createAlertDialog();
                                    dialog.show();
                                } else {
                                    showToast(saleskenResponse.getResponseMessage());
                                    hideProgress();

                                }
                                break;
                            default:
                                try {
                                    SaleskenResponse saleskenResponse1 = gson.fromJson(response.errorBody().string(),SaleskenResponse.class);
                                    showToast(saleskenResponse1.getResponseMessage());

                                } catch (JsonSyntaxException e) {
                                    showToast("Bad request from the server");
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    showToast("Bad request from the server");
                                    e.printStackTrace();
                                }
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


    }



    public void showProgress(){
        progress.setVisibility(VISIBLE);
        content.setVisibility(GONE);
    }
    public void hideProgress(){
        progress.setVisibility(GONE);
        content.setVisibility(VISIBLE);
    }

    @OnClick(R.id.edit_deal_value)
    public void edit_deal_value(){
        deal_value.setVisibility(GONE);
        deal_value_txt.setVisibility(VISIBLE);
        done_edit.setVisibility(VISIBLE);
        edit_deal_value.setVisibility(GONE);
        deal_value_txt.setText(deal_value.getText());
    }

    @OnClick(R.id.done_edit)
    public void done_edit(){
        deal_value.setVisibility(VISIBLE);
        deal_value_txt.setVisibility(View.GONE);
        done_edit.setVisibility(View.GONE);
        edit_deal_value.setVisibility(VISIBLE);
        deal_value.setText(deal_value_txt.getText().toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(dialog != null){
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    public void createAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleActivity.this);
        final View customLayout = getLayoutInflater().inflate(R.layout.feedback_layout, null);
        ImageButton close = customLayout.findViewById(R.id.close);
        Button skip = customLayout.findViewById(R.id.skip);
        Button save = customLayout.findViewById(R.id.save_contact);

        builder.setView(customLayout);

        dialog = builder.create();
        hideProgress();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(ScheduleActivity.this, DialerActivity.class);
                startActivity(i);
                finish();
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(ScheduleActivity.this, DialerActivity.class);
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
    }
}
