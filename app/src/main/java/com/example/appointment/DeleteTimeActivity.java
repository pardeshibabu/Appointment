package com.example.appointment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

public class DeleteTimeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference ref,ref1;

    private ProgressDialog loadingbar;

    ListView listViewfrreDate,listViewOfficeDate;
    List<date> dates_array1,dates_array2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_time);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        loadingbar = new ProgressDialog(DeleteTimeActivity.this);

        listViewfrreDate = (ListView)findViewById(R.id.DeleteFreeListview1);
        listViewOfficeDate =(ListView)findViewById(R.id.DeleteOfficeListview2);
        dates_array1 =new ArrayList<date>();
        dates_array2 =new ArrayList<date>();

        final String colid = currentUser.getEmail().replaceAll("[-+.^:,@]","");
        database = FirebaseDatabase.getInstance();
        ref =database.getReference();
        ref1 =database.getReference();

        freetimeDelete();
        officeTimeDelete();


    }

    public void freetimeDelete(){
        loadingbar.show();
        final String colid = currentUser.getEmail().replaceAll("[-+.^:,@]","");
        ref.child("Professor").child(colid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final RegisterSaveData obj =dataSnapshot.getValue(RegisterSaveData.class);

                final SimpleDateFormat geek = new SimpleDateFormat("dd/MM/yyyy");
                Calendar c = Calendar.getInstance();
                final String cuurentdate = geek.format(c.getTime());
                DatabaseReference database;

                ref1.child("TIME").child(obj.getRegister_collegeid()).child("Free").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dates_array1.clear();
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            date dateobject = ds.getValue(date.class);
                            String exiendtime=dateobject.getTime_date();
                            try {
                                Date date2 = geek.parse(cuurentdate);
                                Date date1 = geek.parse(exiendtime);

//                                Log.e("<data!!>cd",cuurentdate);
//                                Log.e("<data!!>innt",Integer.toString(date2.compareTo(date1)));
//                                Log.e("<data!!>ext",exiendtime);
                                if(date2.compareTo(date1) <= 0){
                                    Log.e("<data!!>",dateobject.getTime_date());
                                    dates_array1.add(dateobject);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                        time_list_adapter adapter = new time_list_adapter(DeleteTimeActivity.this,dates_array1);
                        listViewfrreDate.setAdapter(adapter);
                        loadingbar.dismiss();
                        listViewfrreDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final date obj1;
                                obj1=dates_array1.get(position);
//                                Log.e("<DATE>day",obj1.getTime_date());
//                                Log.e("<DATE>from",obj1.getTime_From());
//                                Log.e("<DATE>to",obj1.getTime_To());
//                            dates_array1.remove(position);

                                ref1.child("TIME").child(obj.getRegister_collegeid()).child("Free").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot ds1:dataSnapshot.getChildren()){
                                            date obj3 =ds1.getValue(date.class);
                                            DatabaseReference delref1=ds1.getRef();
                                            if(obj1.getTime_date().equals(obj3.getTime_date()) &&
                                                    obj1.getTime_From().equals(obj3.getTime_From()) &&
                                                    obj1.getTime_To().equals(obj3.getTime_To())){
                                                delref1.removeValue();
                                                Toast.makeText(DeleteTimeActivity.this,"Deleted",Toast.LENGTH_SHORT).show();
                                                finish();
                                                startActivity(new Intent(DeleteTimeActivity.this,Prof_ProfileActivity.class));
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        finish();
                                        startActivity(new Intent(DeleteTimeActivity.this,Prof_ProfileActivity.class));
                                        Toast.makeText(DeleteTimeActivity.this,databaseError.toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        });



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        finish();
                        startActivity(new Intent(DeleteTimeActivity.this,Prof_ProfileActivity.class));
                        Toast.makeText(DeleteTimeActivity.this,databaseError.toString(),Toast.LENGTH_SHORT).show();
                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                finish();
                startActivity(new Intent(DeleteTimeActivity.this,Prof_ProfileActivity.class));
                Toast.makeText(DeleteTimeActivity.this,databaseError.toString(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void officeTimeDelete(){

        final String colid = currentUser.getEmail().replaceAll("[-+.^:,@]","");
        loadingbar.show();
        ref.child("Professor").child(colid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final RegisterSaveData obj =dataSnapshot.getValue(RegisterSaveData.class);

                final SimpleDateFormat geek = new SimpleDateFormat("dd/MM/yyyy");
                Calendar c = Calendar.getInstance();
                final String cuurentdate = geek.format(c.getTime());
                DatabaseReference database;

                ref1.child("TIME").child(obj.getRegister_collegeid()).child("Office").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dates_array2.clear();
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            date dateobject = ds.getValue(date.class);
                            String exiendtime=dateobject.getTime_date();
                            try {
                                Date date2 = geek.parse(cuurentdate);
                                Date date1 = geek.parse(exiendtime);

//                                Log.e("<data!!>cd",cuurentdate);
//                                Log.e("<data!!>innt",Integer.toString(date2.compareTo(date1)));
//                                Log.e("<data!!>ext",exiendtime);
                                if(date2.compareTo(date1) <= 0){
                                    Log.e("<data!!>",dateobject.getTime_date());
                                    dates_array2.add(dateobject);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                        time_list_adapter adapter = new time_list_adapter(DeleteTimeActivity.this,dates_array2);
                        listViewOfficeDate.setAdapter(adapter);
                        loadingbar.dismiss();
                        listViewOfficeDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final date obj1;
                                obj1=dates_array2.get(position);
//                                Log.e("<DATE>day",obj1.getTime_date());
//                                Log.e("<DATE>from",obj1.getTime_From());
//                                Log.e("<DATE>to",obj1.getTime_To());
//                            dates_array1.remove(position);

                                ref1.child("TIME").child(obj.getRegister_collegeid()).child("Office").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot ds1:dataSnapshot.getChildren()){
                                            date obj3 =ds1.getValue(date.class);
                                            DatabaseReference delref1=ds1.getRef();
                                            if(obj1.getTime_date().equals(obj3.getTime_date()) &&
                                                    obj1.getTime_From().equals(obj3.getTime_From()) &&
                                                    obj1.getTime_To().equals(obj3.getTime_To())){
                                                delref1.removeValue();
                                                Toast.makeText(DeleteTimeActivity.this,"Deleted",Toast.LENGTH_SHORT).show();
                                                finish();
                                                startActivity(new Intent(DeleteTimeActivity.this,Prof_ProfileActivity.class));
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        finish();
                                        startActivity(new Intent(DeleteTimeActivity.this,Prof_ProfileActivity.class));
                                        Toast.makeText(DeleteTimeActivity.this,databaseError.toString(),Toast.LENGTH_SHORT).show();

                                    }
                                });


                            }
                        });



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        finish();
                        startActivity(new Intent(DeleteTimeActivity.this,Prof_ProfileActivity.class));
                        Toast.makeText(DeleteTimeActivity.this,databaseError.toString(),Toast.LENGTH_SHORT).show();
                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                finish();
                startActivity(new Intent(DeleteTimeActivity.this,Prof_ProfileActivity.class));
                Toast.makeText(DeleteTimeActivity.this,databaseError.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
