package com.example.appointment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class ProfessorHomePageActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    NavigationView navigationView;
    SharedPreferences sp,stusp,profsp;
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private ProgressDialog loadingbar;
    static String GlobalPNmae=null,GlobalPEmail=null,GlobalPCid=null;
    Dialog CnfmyDialog;
    TextView leavePopUpClose;
    EditText leavePopUpdate;
    Button leavePopUpBtn;
    static String strDate;
    String tkn,notititle;
    Editable notibody;
    DatePickerDialog picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_home_page);
        setTitle("Homepage");

        CnfmyDialog = new Dialog(this);

        loadingbar = new ProgressDialog(ProfessorHomePageActivity.this);

        database = FirebaseDatabase.getInstance();
        ref =database.getReference();

        final Calendar cldr = Calendar.getInstance();
        final int day = cldr.get(Calendar.DAY_OF_MONTH);
        final int month = cldr.get(Calendar.MONTH);
        final int year = cldr.get(Calendar.YEAR);



//--------------------------Share value of professor to fetch informatio----------------------------------------------------
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        final String colid = currentUser.getEmail().replaceAll("[-+.^:,@]","");

        ref.child("Professor").child(colid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegisterSaveData obj = dataSnapshot.getValue(RegisterSaveData.class);
                if(obj.getRegister_name() != null)
                    GlobalPNmae=obj.getRegister_name();
                if(obj.getRegister_email() != null)
                    GlobalPEmail =obj.getRegister_email();
                if(obj.getRegister_collegeid() != null)
                    GlobalPCid = obj.getRegister_collegeid();

//                Log.e("<DATA>e",obj.getRegister_email());
//                Log.e("<DATA>nam",obj.getRegister_name());
//                Log.e("<DATA>ci",obj.getRegister_collegeid());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ProfessorHomePageActivity.this);
        //now get Editor
        SharedPreferences.Editor editor = sharedPref.edit();
        //put your value
        editor.putString("PEmail",GlobalPEmail);
        editor.putString("Pname",GlobalPNmae);
        editor.putString("PcollegeId",GlobalPCid);
        //commits your edits
        editor.commit();

//------------using-------Semafor--for keep login--always------------------------------------------------------------------------



        profsp = getSharedPreferences("plogin",MODE_PRIVATE);
        stusp = getSharedPreferences("slogin",MODE_PRIVATE);


//----------- navigation drawer and header-------------------------------------------
        Toolbar toolbar = findViewById(R.id.ProfHomepageToolBar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.ProfHomePageDrawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView =(NavigationView)findViewById(R.id.Profnav_view);



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.prof_homepage_nav_home:
//                        Toast.makeText(ProfessorHomePageActivity.this,"Home",Toast.LENGTH_SHORT).show();
                        Intent home=new Intent(ProfessorHomePageActivity.this,ProfessorHomePageActivity.class);
                        startActivity(home);
                        break;
                    case R.id.prof_homepage_nav_profile:
                        Intent profile=new Intent(ProfessorHomePageActivity.this,Prof_ProfileActivity.class);
                        startActivity(profile);
                        break;
                    case R.id.prof_homepage_nav_message:
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("text/plain");
                        startActivity(emailIntent);
                        break;
                    case R.id.prof_homepage_nav_leave:
                        CnfmyDialog = new Dialog(ProfessorHomePageActivity.this);
                        CnfmyDialog.setContentView(R.layout.leave_popup_layout);
                        leavePopUpClose = (TextView) CnfmyDialog.findViewById(R.id.ClosPopUp);
                        leavePopUpdate = (EditText)CnfmyDialog.findViewById(R.id.LeaveDate);
                        leavePopUpBtn=(Button)CnfmyDialog.findViewById(R.id.LeaveBtn);
//                        final String strDate;
                        leavePopUpClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CnfmyDialog.dismiss();
//                                    Intent intent = new Intent(SetDescriptionAptActivity.this,Student_HomePageActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);
                            }
                        });

                        leavePopUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // date picker dialog
                                picker = new DatePickerDialog(CnfmyDialog.getContext(),
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                                leavePopUpdate.setText(dayOfMonth+"/"+(monthOfYear + 1)+"/"+year);
                                                int m = (monthOfYear+1);
                                                strDate=Integer.toString(dayOfMonth)+"/"+Integer.toString(m)+"/"+Integer.toString(year);
                                                //obj.setTime_date(dateInput.getText().toString());


                                            }
                                        }, year, month, day);
                                picker.show();

                            }
                        });
                        leavePopUpBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ref.child("Professor").child(colid).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        RegisterSaveData obj = dataSnapshot.getValue(RegisterSaveData.class);
                                        if(obj.getRegister_name() != null)
                                            GlobalPNmae=obj.getRegister_name();
                                        if(obj.getRegister_email() != null)
                                            GlobalPEmail =obj.getRegister_email();
                                        if(obj.getRegister_collegeid() != null)
                                            GlobalPCid = obj.getRegister_collegeid();
                                        if(TextUtils.isEmpty(leavePopUpdate.getText())){
                                            leavePopUpdate.setError("fill date here");
                                        }
//                                        Toast.makeText(CnfmyDialog.getContext(), dataSnapshot.getRef().toString(), Toast.LENGTH_SHORT).show();
//                                        Log.e("<REF>",leavePopUpdate.getText().toString());
                                        ref.child("DATABASE").child("AppointmentDATA").child(GlobalPCid).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot ds:dataSnapshot.getChildren()){
                                                    DataApptClass obj = ds.getValue(DataApptClass.class);
                                                    tkn=obj.getRequesterDeviceToken();
                                                    notititle="Cancel appointment";
                                                    notibody=leavePopUpdate.getText();
                                                    new Notify().execute();
                                                    if(obj.getApptDay().equals(strDate)) {
                                                        DatabaseReference ref = ds.getRef();
                                                        Log.e("<REF>TKN",tkn);

                                                        Log.e("<REF>", ref.toString());
                                                        ref.removeValue();
//                                                        new Notify().execute();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toast.makeText(CnfmyDialog.getContext(),"oops some error occured",Toast.LENGTH_SHORT).show();
                                                finish();
                                                startActivity(new Intent(CnfmyDialog.getContext(),ProfessorHomePageActivity.class));
                                            }
                                        });
                                        finish();
                                        Toast.makeText(ProfessorHomePageActivity.this,"All slot is deleted !!",Toast.LENGTH_SHORT).show();
                                        synchronized(obj){
                                            obj.notify();
                                        }
                                        startActivity(new Intent(CnfmyDialog.getContext(),ProfessorHomePageActivity.class));


//                                new SetDescriptionAptActivity.Notify().execute();
//                                notify();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(CnfmyDialog.getContext(),"oops some error occured",Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(CnfmyDialog.getContext(),ProfessorHomePageActivity.class));

                                    }
                                });

                            }

                        });
                        CnfmyDialog.show();


                        break;

                    case R.id.prof_homepage_nav_appointment:

                        Intent profile1=new Intent(ProfessorHomePageActivity.this,ProfMyAppointmentActivity.class);
                        startActivity(profile1);
                        break;


                    case R.id.prof_homepage_nav_RqAppointment:
                        Intent profile2=new Intent(ProfessorHomePageActivity.this,ProfessorListActivity.class);
                        startActivity(profile2);
                        break;
                    case R.id.prof_homepage_nav_logout:

                        String t="f";
                        unique_login_class obj=new unique_login_class();
                        obj.setUsertype("Professor");
                        obj.setFlag(t);
                        obj.setEmail(currentUser.getEmail());
                        String m_szAndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        String colid = currentUser.getEmail().replaceAll("[-+.^:,@]","");

                        ref.child("UniqueLogin").child(m_szAndroidID).child(colid).setValue(obj);

                        profsp.edit().putBoolean("plogged",false).apply();
                        Intent intent = new Intent(ProfessorHomePageActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        break;
                }


                return false;
            }
        });
        ref.child("Professor").child(colid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegisterSaveData obj = dataSnapshot.getValue(RegisterSaveData.class);
                if(obj.getRegister_name() != null)
                    GlobalPNmae=obj.getRegister_name();
                if(obj.getRegister_email() != null)
                    GlobalPEmail =obj.getRegister_email();
                if(obj.getRegister_collegeid() != null)
                    GlobalPCid = obj.getRegister_collegeid();

                View hView =navigationView.getHeaderView(0);
                TextView tv = (TextView)hView.findViewById(R.id.pro_header_email);
                TextView user_name = (TextView)hView.findViewById(R.id.pro_header_name);
                ImageView user_photo = (ImageView)hView.findViewById(R.id.pro_header_img);
                user_name.setText(GlobalPNmae);
                tv.setText(currentUser.getEmail());
                Glide.with(ProfessorHomePageActivity.this).load(currentUser.getPhotoUrl()).into(user_photo);

                Log.e("<DATA>e",obj.getRegister_email());
                Log.e("<DATA>nam",obj.getRegister_name());
                Log.e("<DATA>ci",obj.getRegister_collegeid());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
//------------End of Bundal Save instance-----------------------------------------

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        loadingbar.show();
        drawer.openDrawer(GravityCompat.START);
        loadingbar.dismiss();
        super.onStart();
    }
    public class Notify extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            try {

                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization","key=AIzaSyB7TvPT8Hoo30GaMfg-5lVGWJpHbAvHFR0");
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject json = new JSONObject();

                json.put("to", tkn);


                JSONObject info = new JSONObject();
                info.put("title", notititle);   // Notification title
                info.put("body", notibody); // Notification body

                json.put("notification", info);

                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(json.toString());
                wr.flush();
                conn.getInputStream();

            }
            catch (Exception e)
            {
                Log.d("Error",""+e);
            }


            return null;
        }
    }


}
