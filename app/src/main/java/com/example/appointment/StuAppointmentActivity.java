package com.example.appointment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
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

public class StuAppointmentActivity extends AppCompatActivity {
    ListView chooselistViewdataAppt,DetaillistViewdataAppt;
    List<RegisterSaveData> choosedates_array1;


    FirebaseDatabase database;
    DatabaseReference ref;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_appointment);
        setTitle("My Appointment");

        FirebaseApp.initializeApp(this);
        chooselistViewdataAppt = (ListView)findViewById(R.id.myappt_Stu_Listview1);
        choosedates_array1 =new ArrayList<RegisterSaveData>();
        loadingbar = new ProgressDialog(this);

        SharedPreferences psharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final String v = psharedPref.getString("PcollegeId", "Not Available");
        DatabaseReference dbr;
        dbr = FirebaseDatabase.getInstance().getReference("DATABASE").child("AppointmentDATA").child(v);

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
        loadingbar.show();
        ref = FirebaseDatabase.getInstance().getReference("DATABASE").child("AppointmentDATA");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                choosedates_array1.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    String id=ds.getKey();
                    Log.e("dikhaore",id);
                    addprofessorList(id);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    public void addprofessorList(String pci){
        final SimpleDateFormat geek = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        final String cuurentdate = geek.format(c.getTime());
        SharedPreferences psharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final String v = psharedPref.getString("PcollegeId", "Not Available");
        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference("DATABASE").child("AppointmentDATA").child(pci);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    DataApptClass obj = ds.getValue(DataApptClass.class);

                    RegisterSaveData obj1 =new RegisterSaveData();
                    obj1.setRegister_name(obj.getResponderName());
                    obj1.setRegister_collegeid(obj.getResponderId());
                    obj1.setToken(obj.responderDeviceToken);
                    String d=obj.getApptDay();

                    try {
                        Date date2 = geek.parse(cuurentdate);
                        Date date1 = geek.parse(d);
                        Log.d("<data>int",ds.getRef().toString());

                        String st="Student";

                        if(obj.getUserType().equals(st) && date2.compareTo(date1) <=0){
                            Log.e("<data>nhi",obj.getUserType());
                            choosedates_array1.add(obj1);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

//                            Toast.makeText(Prof_ProfileActivity.this,"hi"+dateobject.getTime_date(),Toast.LENGTH_SHORT).show();

                }

                StuApptProfList_AdapterClass adapter1 = new StuApptProfList_AdapterClass(StuAppointmentActivity.this,choosedates_array1);
                chooselistViewdataAppt.setAdapter(adapter1);
                loadingbar.dismiss();


                chooselistViewdataAppt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        RegisterSaveData o;
                        o = choosedates_array1.get(position);
                        String pci=o.getRegister_collegeid();
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(StuAppointmentActivity.this);
                        //now get Editor
                        SharedPreferences.Editor editor = sharedPref.edit();
                        //put your value
                        editor.putString("ShowPCi",o.getRegister_collegeid());
                        //commits your edits
                        editor.commit();

                        Intent i = new Intent(StuAppointmentActivity.this, ShowStuApptDetailActivity.class);
//                        Log.e("!_@@<DATA>",name);
                        startActivity(i);

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StuAppointmentActivity.this,"oops :)",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
