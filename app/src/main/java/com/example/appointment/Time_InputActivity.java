package com.example.appointment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Time_InputActivity extends AppCompatActivity {

    EditText dateInput,from,to;
    Button addtime;
    TimePickerDialog timePickerDialog;
    DatePickerDialog picker;


    String strDate,strDatefrom,strDateto;

    FirebaseDatabase database;
    DatabaseReference ref;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String Email;
    private String Uid;
    private String td="TIME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time__input);
        Intent intent1 = getIntent();
        final String str = intent1.getStringExtra("Value");
        setTitle(str);
        dateInput = (EditText)findViewById(R.id.InputFreeDate);
        from = (EditText)findViewById(R.id.InputFreetimeFrom);
        to = (EditText)findViewById(R.id.InputFreetimeTo);
        addtime = (Button) findViewById(R.id.AddTime);


        FirebaseApp.initializeApp(this);






// set time....................................
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Time_InputActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateInput.setText(dayOfMonth+"/"+(monthOfYear + 1)+"/"+year);
                                int m = (monthOfYear+1);
                                strDate=Integer.toString(dayOfMonth)+"/"+Integer.toString(m)+"/"+Integer.toString(year);
                                //obj.setTime_date(dateInput.getText().toString());


                            }
                        }, year, month, day);
                picker.show();
            }
        });

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Time_InputActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        from.setText(String.format("%02d:%02d",selectedHour,selectedMinute));
                        strDatefrom=Integer.toString(selectedHour)+":"+Integer.toString(selectedMinute);
//                        obj.setTime_From(from.getText().toString());

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Time_InputActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        to.setText(String.format("%02d:%02d",selectedHour,selectedMinute));
                        strDateto=Integer.toString(selectedHour)+":"+Integer.toString(selectedMinute);
//                        obj.setTime_To(to.getText().toString());
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


//        add time to firebase database.........................

        addtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final date n =new date();
                n.setTime_date(strDate);
                n.setTime_From(strDatefrom);
                n.setTime_To(strDateto);
                SimpleDateFormat geek = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat geek1 = new SimpleDateFormat("hh:mm");
                Calendar cinstance = Calendar.getInstance();
                String day =geek.format(cinstance.getTime());
                String timefrom =geek1.format(cinstance.getTime());
                Date d1 = new Date(day);
                Date d2 = new Date(dateInput.getText().toString());


                Log.e("<DATA>day",day);
                Log.e("<DATA>timefrom",timefrom);
                Log.e("<DATA>daytoinput",Integer.toString(dateInput.getText().toString().compareTo(day)));
                Log.e("<DATA>timetoinput",Integer.toString(from.getText().toString().compareTo(timefrom) ));

                if(TextUtils.isEmpty(n.getTime_date())){
                    Toast.makeText(Time_InputActivity.this,"Please enter valid Date:",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(n.getTime_From())){
                    Toast.makeText(Time_InputActivity.this,"Please enter valid time:",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(n.getTime_To())){
                    Toast.makeText(Time_InputActivity.this,"Please enter valid time:",Toast.LENGTH_SHORT).show();
                }
                else if(d2.before(d1)){
                    dateInput.setError("This date has been passed!!");
                }
                else{
                    database = FirebaseDatabase.getInstance();
                    ref = database.getReference();
                    Intent intent = getIntent();
                    final String str = intent.getStringExtra("Value");
                    final String c = intent.getStringExtra("ValueofCI");
//                    Log.e("CI_VALUE",c);
                    String mGroupId = ref.push().getKey();
                    ref.child(td).child(c).child(str).child(mGroupId).setValue(n);
                    Toast.makeText(Time_InputActivity.this,"set :)",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(Time_InputActivity.this, Prof_ProfileActivity.class));

                }
            }
        });

    }
}
