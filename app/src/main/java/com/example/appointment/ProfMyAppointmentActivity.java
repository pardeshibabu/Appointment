package com.example.appointment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class ProfMyAppointmentActivity extends AppCompatActivity {
    ListView chooselistViewdataAppt,prof2profListview;
    List<DataApptClass> choosedates_array1,choosedates_array2;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String Email;
    private String Uid;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_my_appointment);
        setTitle("My Appointment");
        FirebaseApp.initializeApp(this);
        chooselistViewdataAppt = (ListView)findViewById(R.id.myappt_Stu_Prof_Listview);
        prof2profListview = (ListView)findViewById(R.id.myapptListview);
        choosedates_array1 =new ArrayList<DataApptClass>();
        choosedates_array2 =new ArrayList<DataApptClass>();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=getIntent();
        String ci=intent.getStringExtra("CollegeId");
        fetchBookeddata(ci);
    }
    public void fetchBookeddata(String pci){

        final SimpleDateFormat geek = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        final String cuurentdate = geek.format(c.getTime());
        SharedPreferences psharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final String v = psharedPref.getString("PcollegeId", "Not Available");
        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference("DATABASE").child("AppointmentDATA").child(v);

//        DatabaseReference mr= database.getRef().getParent();
//        Log.e("!_@@<DATA>",mr.toString());
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    DataApptClass obj = ds.getValue(DataApptClass.class);
//                    DataApptClass dateobject = new DataApptClass();
//                    dateobject.setTime_date(obj.getApptDay());
//                    dateobject.setTime_From(obj.getApptStartTime());
//                    dateobject.setTime_To(obj.getApptEndTime());
                    String d=obj.getApptDay();
//                    try {
//                        if(!(geek.parse(d).before(geek.parse(cuurentdate))))
//                            choosedates_array1.add(obj);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
                    try {
                        Date date2 = geek.parse(cuurentdate);
                        Date date1 = geek.parse(d);
//                        Log.d("<data>int",String.valueOf(date2.compareTo(date1)));
                        Log.d("<data>int",obj.getUserType());

                        String st="Student";
                        String pt="Professor";
                        //  &&
                        if(obj.getUserType().equals(pt) && date2.compareTo(date1) <=0){
                            Log.e("<data>hello",obj.getUserType());
                            choosedates_array2.add(obj);
                        }
                        if(obj.getUserType().equals(st) && date2.compareTo(date1) <=0){
                            Log.e("<data>nhi",obj.getUserType());
                            choosedates_array1.add(obj);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

//                            Toast.makeText(Prof_ProfileActivity.this,"hi"+dateobject.getTime_date(),Toast.LENGTH_SHORT).show();

                }
                dataappt_adapter_class adapter1 = new dataappt_adapter_class(ProfMyAppointmentActivity.this,choosedates_array1);
                dataappt_adapter_class adapter2 = new dataappt_adapter_class(ProfMyAppointmentActivity.this,choosedates_array2);
                chooselistViewdataAppt.setAdapter(adapter1);
                prof2profListview.setAdapter(adapter2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfMyAppointmentActivity.this,"oops :)",Toast.LENGTH_SHORT).show();
            }
        });

    }

}
