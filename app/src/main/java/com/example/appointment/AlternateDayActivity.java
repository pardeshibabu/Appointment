package com.example.appointment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

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

public class AlternateDayActivity extends AppCompatActivity {

    ListView chooselistViewfrreDate;
    List<date> choosedates_array1;
    DatabaseReference database;
    private ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternate_day);
        loadingbar = new ProgressDialog(this);

        loadingbar.show();

        chooselistViewfrreDate = (ListView)findViewById(R.id.alterListview);
        choosedates_array1 =new ArrayList<date>();
        database = FirebaseDatabase.getInstance().getReference("TIME");
        SharedPreferences psharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final String pci = psharedPref.getString("pcollegeid", "Not Available");
        final SimpleDateFormat geek = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        final String cuurentdate = geek.format(c.getTime());

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.child(pci).child("Free").getChildren()){
                    date dateobject = ds.getValue(date.class);
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

                }
                time_list_adapter adapter = new time_list_adapter(AlternateDayActivity.this,choosedates_array1);
                chooselistViewfrreDate.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
