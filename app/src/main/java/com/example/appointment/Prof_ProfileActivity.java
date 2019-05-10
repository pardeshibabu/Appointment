package com.example.appointment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

public class Prof_ProfileActivity extends AppCompatActivity {

    Dialog myDialog;
    Button myschedule;


    private ProgressDialog loadingbar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String Email;
    private String Uid;
    DatabaseReference database;
    private String ci="";
    private String td="TIME";
    Button DeleteBtn;

    ListView listViewfrreDate,listViewOfficeDate;
    List<date> dates_array,dates_array1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof__profile);
        setTitle("My Profile");
        myDialog = new Dialog(this);

        loadingbar = new ProgressDialog(this);

        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance().getReference("Professor");

        listViewfrreDate = (ListView)findViewById(R.id.TimeFreeList);
        listViewOfficeDate =(ListView)findViewById(R.id.TimeOfficeList);
        DeleteBtn=(Button)findViewById(R.id.ProfUpdateBtn);
        dates_array =new ArrayList<date>();
        dates_array1 =new ArrayList<date>();

        // must run before all function..
        myOnStart();

        myschedule = (Button) findViewById(R.id.ProfMySchedule);
        myschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(Prof_ProfileActivity.this,"Hello",Toast.LENGTH_SHORT).show();
                TextView txtclose, FreeHour, Officehour;

                myDialog.setContentView(R.layout.pop_layout);

                txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
                FreeHour = (TextView) myDialog.findViewById(R.id.FreeHour);
                Officehour = (TextView) myDialog.findViewById(R.id.OfficeHour);
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });

                FreeHour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        freeDateSet();
                    }
                });

                Officehour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OfiiceDateSet();
                    }
                });

                myDialog.show();

            }
        });
        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Prof_ProfileActivity.this,DeleteTimeActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(Prof_ProfileActivity.this,ProfessorHomePageActivity.class));
        super.onBackPressed();
    }

    public void myOnStart() {
            super.onStart();

            user = mAuth.getInstance().getCurrentUser();
            loadingbar.setTitle("Fetching Schedule!!");
            loadingbar.setMessage("Please wait while we are fetchin the schedule");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            if(user != null){
                Email = user.getEmail();
                Uid = user.getUid();

                final TextView profName =(TextView)findViewById(R.id.ProfName);
                final TextView collegeId =(TextView)findViewById(R.id.ProfCollegeId);
                final TextView profEmail =(TextView)findViewById(R.id.ProfEmail);



                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot professorsnapshot : dataSnapshot.getChildren()){
                            RegisterSaveData r = professorsnapshot.getValue(RegisterSaveData.class);

                            if(r.getRegister_email().equals(Email)){
                                profName.setText(r.getRegister_name());
                                collegeId.setText(r.getRegister_collegeid());
                                profEmail.setText(r.getRegister_email());
                                 ci =r.getRegister_collegeid();
//                                loadingbar.dismiss();
//                                 Toast.makeText(Prof_ProfileActivity.this,"oops :)",Toast.LENGTH_SHORT).show();
                                set_date_free_class(r.getRegister_collegeid());
                                set_date_Office_class(r.getRegister_collegeid());
//                                Email=Email+1;
                                loadingbar.dismiss();
                                break;
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Prof_ProfileActivity.this,"oops :)",Toast.LENGTH_SHORT).show();
                    }
                });
            }





        }
        public void set_date_free_class(String register_collegeid){

            final SimpleDateFormat geek = new SimpleDateFormat("dd/MM/yyyy");
            Calendar c = Calendar.getInstance();
            final String cuurentdate = geek.format(c.getTime());
            DatabaseReference database;
            final String cid=register_collegeid;
            database = FirebaseDatabase.getInstance().getReference("TIME");
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.child(cid).child("Free").getChildren()){
                        date dateobject = ds.getValue(date.class);
//                        Log.e("<data!!>",dateobject.getTime_date());
//                        date obj = ds.getValue(date.class);
                        String exiendtime=dateobject.getTime_date();
                        try {
                            Date date2 = geek.parse(cuurentdate);
                            Date date1 = geek.parse(exiendtime);

                            Log.e("<data!!>cd",cuurentdate);
                            Log.e("<data!!>innt",Integer.toString(date2.compareTo(date1)));
                            Log.e("<data!!>ext",exiendtime);
                            if(date2.compareTo(date1) <= 0){
                                Log.e("<data!!>",dateobject.getTime_date());
                                dates_array.add(dateobject);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


//                            Toast.makeText(Prof_ProfileActivity.this,"hi"+dateobject.getTime_date(),Toast.LENGTH_SHORT).show();

                    }
                    time_list_adapter adapter = new time_list_adapter(Prof_ProfileActivity.this,dates_array);
                    listViewfrreDate.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Prof_ProfileActivity.this,"successfuly added",Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void set_date_Office_class(String register_ci){
            final SimpleDateFormat geek = new SimpleDateFormat("dd/MM/yyyy");
            Calendar c = Calendar.getInstance();
            final String cuurentdate = geek.format(c.getTime());
            DatabaseReference database1;
            final String v=register_ci;
            database1 = FirebaseDatabase.getInstance().getReference("TIME");

            database1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.child(v).child("Office").getChildren()){
//                        Log.e("HelloLOgcat", ds.toString());
                        date dateobject = ds.getValue(date.class);
                        String exiendtime=dateobject.getTime_date();
                        try {
                            Date date2 = geek.parse(cuurentdate);
                            Date date1 = geek.parse(exiendtime);
                            if(date2.compareTo(date1) <= 0){
                                dates_array1.add(dateobject);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

//                            Toast.makeText(Prof_ProfileActivity.this,"hi"+dateobject.getTime_date(),Toast.LENGTH_SHORT).show();

                    }
                    time_list_adapter adapter = new time_list_adapter(Prof_ProfileActivity.this,dates_array1);
                    listViewOfficeDate.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Prof_ProfileActivity.this,"successfuly added",Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void freeDateSet(){

            String Value="Free";
            Intent intent = new Intent(Prof_ProfileActivity.this, Time_InputActivity.class);
            intent.putExtra("Value", Value);
            intent.putExtra("ValueofCI", ci);
            startActivity(intent);
        }

    public void OfiiceDateSet(){

        String Value="Office";
        Intent intent = new Intent(Prof_ProfileActivity.this, Time_InputActivity.class);
        intent.putExtra("Value", Value);
        intent.putExtra("ValueofCI", ci);
        startActivity(intent);
    }


}
