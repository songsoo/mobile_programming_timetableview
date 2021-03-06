package com.gcsw.teamone;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalTime;
import java.util.Calendar;

public class addPlan extends Activity {

    Button startTimeBtn, endTimeBtn,startDateBtn, cancelBtn, addBtn;
    EditText planName;
    TextView startTimeTxt, endTimeTxt, startDateTxt;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference planRef = database.getReference("plan");


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_add_plan);


        startTimeBtn = findViewById(R.id.startTimeBtn_plan);
        endTimeBtn = findViewById(R.id.endTimeBtn_plan);
        startDateBtn = findViewById(R.id.startDateBtn_plan);
        cancelBtn = findViewById(R.id.addPlanCancel);
        addBtn = findViewById(R.id.addPlanAdd);
        planName = findViewById(R.id.planNameTxt);
        startTimeTxt = findViewById(R.id.startTimeTxt_plan);
        endTimeTxt = findViewById(R.id.endTimeTxt_plan);
        startDateTxt = findViewById(R.id.startDateTxt_plan);

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        /*
        ?????? ?????? ????????? ???????????????.
        TimePickerDialog??? ?????? ????????? ????????????
        3?????? 03?????? ??????, 3????????? 03????????? ????????? ??? ????????? ??????????????????.
        ??????????????? 03:03?????? ???????????? ????????????.
         */
        /*
        Button that set the start time of plan
        Read time with TimePickerDialog, and show time (alway shows Number of digits in 10)
         */
        startTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(addPlan.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            String hour, minute;

                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                if (i < 10) {
                                    hour = "0" + Integer.toString(i);
                                } else {
                                    hour = Integer.toString(i);
                                }
                                if (i1 < 10) {
                                    minute = "0" + Integer.toString(i1);
                                } else {
                                    minute = Integer.toString(i1);
                                }
                                startTimeTxt.setText(hour + ":" + minute);
                            }

                        }, 0, 0, true);
                timePickerDialog.show();

            }
        });

        /*
        ????????? ????????? ????????? ???????????????.
        ???????????? ????????? ???????????????.
         */
        /*
        Button that set end time of plan
        same with startTimeBtn
         */
        endTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(addPlan.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            String hour, minute;

                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                if (i < 10) {
                                    hour = "0" + Integer.toString(i);
                                } else {
                                    hour = Integer.toString(i);
                                }
                                if (i1 < 10) {
                                    minute = "0" + Integer.toString(i1);
                                } else {
                                    minute = Integer.toString(i1);
                                }

                                endTimeTxt.setText(hour + ":" + minute);

                            }

                        }, 0, 0, true);
                timePickerDialog.show();
            }
        });


        /*
        ????????? ????????? ???????????????.
        DatePickDialog??? ?????? ????????? ????????????
        ?????? ???????????? ????????? 1 ?????? ????????? 3????????? 2??? ????????? ?????? +1??? ????????????.
        ????????? ????????? ?????? 3?????? 03??? 3?????? 03??? ?????? ?????? ????????? ??????????????????.
        ???/???/??? ???????????? ???????????? ??????????????????.
         */
        /*
        Button that set date of plan
        read date with DatePickDialog, increase the month by one (It shows the month less by one)
        alway shows Number of digits in 10 (month, day)
        Set the text "year"/"month"/"day"
         */
        startDateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(addPlan.this,
                        new DatePickerDialog.OnDateSetListener() {
                            String month, day;

                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                i1 += 1;
                                if (i1 < 10) {
                                    month = "0" + Integer.toString(i1);
                                } else {
                                    month = Integer.toString(i1);
                                }
                                if (i2 < 10) {
                                    day = "0" + Integer.toString(i2);
                                } else {
                                    day = Integer.toString(i2);
                                }
                                startDateTxt.setText(i + "/" + month + "/" + day);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        /*
        ?????? ?????? ????????? ?????? ??????????????? ???????????????.
         */
        /*
        If the cancel button is clicked, finish the activity
         */
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*
        ?????? ?????? ????????? ???????????? ??????????????????.
        ????????? ?????? ???, ?????? ????????? ?????? ????????? ???????????? ????????????,
        ??????, ?????? ?????? ???????????? ???????????? ????????? ????????? ??????x
        ?????? ????????? ????????? ?????? ????????? ????????? ????????? ???????????? ?????? boolean ????????? correct ????????? ???????????? true??? ??????????????????.
         */
        /*
        Button that add plan with given information
        Read information such as schedule name, start time, etc...
        If any of them are not entered, do not add plan
         */
        addBtn.setOnClickListener(new View.OnClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String planNameV = planName.getText().toString();
                String startTimeTxtV = startTimeTxt.getText().toString();
                String endTimeTxtV = endTimeTxt.getText().toString();
                String startDateTxtV = startDateTxt.getText().toString();

                if (planNameV.length() != 0 && !startTimeTxtV.equals("????????????") && !endTimeTxtV.equals("????????????") && !startDateTxtV.equals("?????????")&&!startDateTxtV.equals("?????? ???")) {
                    int startHour, startMinute, endHour, endMinute;

                    String[] startTimeSplit = startTimeTxtV.split(":");
                    String[] endTimeSplit = endTimeTxtV.split(":");
                    String[] startDateSplit = startDateTxtV.split("/");

                    boolean correct = true;
                    int i = 0;

                    /*
                    ?????? ????????? ?????????????????? ?????? ?????? correct??? false??? ???????????????.
                     */
                    /*
                    if start time is later than endtime, make correct false
                     */
                    if (startTimeSplit[0].compareTo(endTimeSplit[0]) > 0) {
                        correct = false;
                    } else if(startTimeSplit[0].compareTo(endTimeSplit[0]) == 0){
                        if (startTimeSplit[1].compareTo(endTimeSplit[1]) > 0) {
                            correct = false;
                        }
                    }

                    if (correct) {

                        startHour = Integer.parseInt(startTimeSplit[0]);
                        startMinute = Integer.parseInt(startTimeSplit[1]);
                        endHour = Integer.parseInt(endTimeSplit[0]);
                        endMinute = Integer.parseInt(endTimeSplit[1]);

                        LocalTime start = LocalTime.of(startHour,startMinute);
                        LocalTime end = LocalTime.of(endHour,endMinute);

                        /*
                        ????????? ????????? ????????? ???????????? ?????? ???????????????????????? ????????? ?????? ???????????????.
                        ???????????? ???????????? ????????? ?????? ????????? ????????????????????? ???????????? ????????? ?????????.
                        ?????? ????????? ????????? ???????????? ????????? ????????? ???????????? ???????????????.
                         */
                        /*
                        Read every plan of the user to check if there are any overlapping plans.
                        If there is any overlapping plan, do not add this plan
                         */
                        planRef.child(FirstAuthActivity.getMyID()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                            boolean isOverlap = false;

                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onComplete(Task<DataSnapshot> task) {

                                String titleG,dateG,timeG;
                                int startHourG,startMinuteG,endHourG,endMinuteG;

                                for (DataSnapshot plan : task.getResult().getChildren()) {

                                    titleG = plan.child("title").getValue().toString();
                                    dateG = plan.child("date").getValue().toString();
                                    timeG = plan.child("time").getValue().toString();

                                    String[] timeSplitG = timeG.split("~");
                                    startHourG = Integer.parseInt(timeSplitG[0].substring(0,2));
                                    startMinuteG = Integer.parseInt(timeSplitG[0].substring(3,5));
                                    endHourG = Integer.parseInt(timeSplitG[1].substring(0,2));
                                    endMinuteG = Integer.parseInt(timeSplitG[1].substring(3,5));

                                    LocalTime startG = LocalTime.of(startHourG,startMinuteG);
                                    LocalTime endG = LocalTime.of(endHourG,endMinuteG);

                                    if(isOverlapDate(dateG,startDateTxtV)){
                                        if(isOverlapTime(start,end,startG,endG)){
                                            isOverlap = true;
                                            break;
                                        }
                                    }
                                }

                                /*
                                ???????????? ????????? ????????? ?????????, ????????????????????? ???????????? ?????????
                                ?????????????????? ??????????????? ??? ?????? ????????? ????????? ????????? ?????? ?????????
                                ????????? ???????????? ?????????????????? ???????????? ?????? ??????????????? ???????????? ????????? ????????????.
                                 */
                                /*
                                if there is not any overlapping plans, add this plan.
                                finish this activity after the last data of this plan is entered.
                                 */
                                if(!isOverlap) {
                                    planRef.child(FirstAuthActivity.getMyID()).child(startDateSplit[0] + startDateSplit[1] + startDateSplit[2] + "_" + startTimeTxtV + "~" + endTimeTxtV + "_" + planNameV).child("title").setValue(planNameV);
                                    planRef.child(FirstAuthActivity.getMyID()).child(startDateSplit[0] + startDateSplit[1] + startDateSplit[2] + "_" + startTimeTxtV + "~" + endTimeTxtV + "_" + planNameV).child("date").setValue(startDateTxtV);
                                    planRef.child(FirstAuthActivity.getMyID()).child(startDateSplit[0] + startDateSplit[1] + startDateSplit[2] + "_" + startTimeTxtV + "~" + endTimeTxtV + "_" + planNameV).child("time").setValue(startTimeTxtV + "~" + endTimeTxtV).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            finish();
                                        }
                                    });
                                }else{
                                    Toast.makeText(getApplicationContext(), "????????? plan??? ????????????.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "????????? ??????/?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "?????? ????????? ???????????????", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }

    /*
    ????????? ????????? ?????? ?????? ?????? ???????????? ???????????? ?????????
    ????????? ????????? true??? ????????? false??? ???????????????.
     */
    /*
    If the date is same, return true
    or false.
     */
    private boolean isOverlapDate(String start,String startG){
        if (start.equals(startG)){
            return true;
        }else{
            return false;
        }
    }

    /*
    ????????? ???????????? ???????????? ????????????
    A??? B?????? ????????? ??????????????? ?????????????????????

    A??? B?????? ??????????????? ???????????? ???????????? ??????????????? B?????? ????????? ?????? ??? (A??? ????????? B??? ?????? ???)
    A??? ??????????????? B??? ?????????????????? ???????????? A??? ??????????????? B??? ?????????????????? ?????? ???
    (?????? ???????????? ?????? ???????????? A??? ??? ?????? ?????? ???)

    ????????? ???????????? ???????????? A??? B??? ????????? ????????? ????????? ????????? ????????? ????????? ????????????.
     */
    /*
    function that check if the time is overlapping
    1. if A's start time is same or earlier than B's start time and A's end time is same or later than B's end time
    2. if A's start time is earlier than B's end time and A's end time is earlier than B's end time
    3,4. The situation in which A and B have changed.
    if one of the four situation is true, return true
     */
    private boolean isOverlapTime(LocalTime start,LocalTime end, LocalTime startG,LocalTime endG){
        if(start.compareTo(startG)<=0&&end.compareTo(endG)>=0){
            return true;
        }
        if(startG.compareTo(start)<=0&&endG.compareTo(end)>=0){
            return true;
        }
        if(start.compareTo(endG)<0&&end.compareTo(endG)>0){
            return true;
        }
        if(startG.compareTo(end)<0&&endG.compareTo(end)>0){
            return true;
        }
        return false;
    }


}