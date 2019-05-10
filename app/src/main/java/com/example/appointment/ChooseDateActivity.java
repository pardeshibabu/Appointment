package com.example.appointment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChooseDateActivity extends AppCompatActivity {

    ListView chooselistViewfrreDate,chooselistViewOfficeDate,chooseListviewBookdate;
    List<date> choosedates_array1,choosedates_array2,choosedates_array3;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String Email;
    private String Uid;
    DatabaseReference database;
    Button cdBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date);
        setTitle("Request Appointment");

        FirebaseApp.initializeApp(this);
        //database = FirebaseDatabase.getInstance().getReference("TIME");

        chooselistViewfrreDate = (ListView)findViewById(R.id.ChooseTimeFreeList);
        chooselistViewOfficeDate =(ListView)findViewById(R.id.ChooseTimeOfficeList);
        chooseListviewBookdate=(ListView)findViewById(R.id.ChooseTimeBookedList);
        cdBtn = (Button)findViewById(R.id.BtnTxtview) ;
        choosedates_array1 =new ArrayList<date>();
        choosedates_array2 =new ArrayList<date>();
        choosedates_array3 =new ArrayList<date>();

        cdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                String ci=intent.getStringExtra("CollegeId");
                Intent i = new Intent(ChooseDateActivity.this, SetDescriptionAptActivity.class);
                i.putExtra("CollegeId", ci);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=getIntent();
        String ci=intent.getStringExtra("CollegeId");
        choose_set_freetime(ci);
        choose_set_officetime(ci);
        choose_set_Booktime(ci);
    }


    public void choose_set_freetime(String register_collegeid){
        final SimpleDateFormat geek = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        final String cuurentdate = geek.format(c.getTime());
        DatabaseReference database;

        SharedPreferences psharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final String v = psharedPref.getString("pcollegeid", "Not Available");
//        final String v=register_collegeid;
        database = FirebaseDatabase.getInstance().getReference("TIME");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                choosedates_array1.clear();
                for(DataSnapshot ds : dataSnapshot.child(v).child("Free").getChildren()){
                    date dateobject = ds.getValue(date.class);
//                    String exiendtime=dateobject.getTime_To();
                    String exiendtime=dateobject.getTime_date();
                    try {
                        Date date2 = geek.parse(cuurentdate);
                        Date date1 = geek.parse(exiendtime);
                        if(date2.compareTo(date1) <= 0){
                            Log.e("<data!!>hello",dateobject.getTime_date());
                            choosedates_array1.add(dateobject);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
//                            Toast.makeText(Prof_ProfileActivity.this,"hi"+dateobject.getTime_date(),Toast.LENGTH_SHORT).show();

                }
                time_list_adapter adapter = new time_list_adapter(ChooseDateActivity.this,choosedates_array1);
                chooselistViewfrreDate.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChooseDateActivity.this,"oops :)",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void choose_set_officetime(String register_collegeid){
        final SimpleDateFormat geek = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        final String cuurentdate = geek.format(c.getTime());
        DatabaseReference database;
        final String v=register_collegeid;
        database = FirebaseDatabase.getInstance().getReference("TIME");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.child(v).child("Office").getChildren()){
                    date dateobject = ds.getValue(date.class);
                    String exiendtime=dateobject.getTime_date();
                    try {
                        Date date2 = geek.parse(cuurentdate);
                        Date date1 = geek.parse(exiendtime);
                        if(date2.compareTo(date1) <= 0){
                            Log.e("<data!!>",dateobject.getTime_date());
                            choosedates_array2.add(dateobject);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
//                            Toast.makeText(Prof_ProfileActivity.this,"hi"+dateobject.getTime_date(),Toast.LENGTH_SHORT).show();

                }
                time_list_adapter adapter = new time_list_adapter(ChooseDateActivity.this,choosedates_array2);
                chooselistViewOfficeDate.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChooseDateActivity.this,"oops :)",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void choose_set_Booktime(String register_collegeid){

        final SimpleDateFormat geek = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        final String cuurentdate = geek.format(c.getTime());
        SharedPreferences psharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final String v = psharedPref.getString("pcollegeid", "Not Available");
//        final String v=register_collegeid;
        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference("DATABASE").child("AppointmentDATA").child(v);

//        DatabaseReference mr= database.getRef().getParent();
//        Log.e("!_@@<DATA>",mr.toString());
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                choosedates_array3.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    DataApptClass obj = ds.getValue(DataApptClass.class);
                    date dateobject = new date();
                    dateobject.setTime_date(obj.getApptDay());
                    dateobject.setTime_From(obj.getApptStartTime());
                    dateobject.setTime_To(obj.getApptEndTime());
                    String d=obj.getApptDay();
                    String exiendtime=obj.getApptDay();
                    try {
                        Date date2 = geek.parse(cuurentdate);
                        Date date1 = geek.parse(exiendtime);
                        if(date2.compareTo(date1) <= 0){
                            Log.e("<data!!>",dateobject.getTime_date());
                            choosedates_array3.add(dateobject);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
//                            Toast.makeText(Prof_ProfileActivity.this,"hi"+dateobject.getTime_date(),Toast.LENGTH_SHORT).show();

                }
                time_list_adapter adapter = new time_list_adapter(ChooseDateActivity.this,choosedates_array3);
                chooseListviewBookdate.setAdapter(adapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChooseDateActivity.this,"oops :)",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
