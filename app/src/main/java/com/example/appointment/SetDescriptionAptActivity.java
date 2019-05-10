package com.example.appointment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SetDescriptionAptActivity extends AppCompatActivity {

    Dialog CnfmyDialog;
    TextView Cnftxtclose,CnfYes,cnftextview,CnfAlter;
    EditText cdateInput,cfrom,cto,subject;
    CheckBox gen,impo,check1,check2,ruProf;
    TimePickerDialog timePickerDialog;
    DatePickerDialog picker;
    FirebaseDatabase database;
    DatabaseReference ref;
    Button appointmentBtn;
    String userType="Student", strDate,strDatefrom,datasetname="AppointmentDATA",tkn,notititle;
    boolean isAppointment = false;
    Editable notibody;
    private ProgressDialog loadingbar;
    static List<date> choosedates_array1;

//    FirebaseAuth mAuth;
//    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_description_apt);

        FirebaseApp.initializeApp(this);
//        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();


        cdateInput = (EditText)findViewById(R.id.DateInput1);
        cfrom = (EditText)findViewById(R.id.DateInput2);
        cto = (EditText)findViewById(R.id.DateInput3);
        subject = (EditText)findViewById(R.id.SubjectInput);
        gen = (CheckBox)findViewById(R.id.General);
        impo = (CheckBox)findViewById(R.id.Important);
        ruProf=(CheckBox)findViewById(R.id.RuProf);
        appointmentBtn = (Button)findViewById(R.id.AppointmentBtn);

        CnfmyDialog = new Dialog(this);
        choosedates_array1 =new ArrayList<date>();
        loadingbar = new ProgressDialog(this);

        CheckBox check3=(CheckBox)findViewById(R.id.RuProf);
        check3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ruProf.isChecked()){
                    userType ="Professor";
                    Log.e("userType",userType);
//                    Toast.makeText(SetDescriptionAptActivity.this,userType,Toast.LENGTH_SHORT).show();
                }
            }
        });



// Checking for General or important
        addListenerOnChkIos();

        appointmentBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(final View v) {
                String date = cdateInput.getText().toString();
                String timefrom = cfrom.getText().toString();
                String timeto = cto.getText().toString();
                checkavailability();
//                CnfmyDialog.dismiss();




            }
        });

        // set time....................................
        cdateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(SetDescriptionAptActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                cdateInput.setText(dayOfMonth+"/"+(monthOfYear + 1)+"/"+year);
                                int m = (monthOfYear+1);
                                strDate=Integer.toString(dayOfMonth)+"/"+Integer.toString(m)+"/"+Integer.toString(year);
                                //obj.setTime_date(dateInput.getText().toString());


                            }
                        }, year, month, day);
                picker.show();
            }
        });

        cfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SetDescriptionAptActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        cfrom.setText(String.format("%02d:%02d",selectedHour,selectedMinute));
                        strDatefrom=Integer.toString(selectedHour)+":"+Integer.toString(selectedMinute);
//                        obj.setTime_From(from.getText().toString());

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        cto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SetDescriptionAptActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        cto.setText(String.format("%02d:%02d",selectedHour,selectedMinute));
                        strDatefrom=Integer.toString(selectedHour)+":"+Integer.toString(selectedMinute);
//                        obj.setTime_From(from.getText().toString());

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }




    public class Notify extends AsyncTask<Void,Void,Void>{


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

    public void addListenerOnChkIos() {
        check1 = (CheckBox) findViewById(R.id.General);
        check2 = (CheckBox) findViewById(R.id.Important);

        check1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    notititle = "General";
//                    check2.setVisibility(View.INVISIBLE);
//                    Toast.makeText(LoginActivity.this,
//                            "Professor "+parrentdb, Toast.LENGTH_LONG).show();
                }
            }
        });
        check2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
//                    check1.setVisibility(View.INVISIBLE);
                    notititle = "Important";
//                    Toast.makeText(LoginActivity.this,
//                            "Student"+parrentdb, Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    public void checkavailability(){
        loadingbar.show();
        if (TextUtils.isEmpty(cdateInput.getText())){
            cdateInput.setError("fill correct date");

        }
        else if (TextUtils.isEmpty(cfrom.getText())){
            cfrom.setError("fill correct time");

        }
        else if(TextUtils.isEmpty(cto.getText())){
            cto.setError("fill correct time");
        }
        else if(TextUtils.isEmpty(subject.getText())){
            subject.setError("fill reasone");
        }
        else if((!gen.isChecked()) && (!impo.isChecked())){
            gen.setError("fill click here");
            impo.setError("Please click here");
        }
        else{
            final float[] rs = {Float.parseFloat(cfrom.getText().toString().replace(":", "."))};
            final float[] re = {Float.parseFloat(cto.getText().toString().replace(":", "."))};



            SharedPreferences psharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            final String pci = psharedPref.getString("pcollegeid", "Not Available");
            final DatabaseReference database;
            database = FirebaseDatabase.getInstance().getReference("TIME");
            final int[] size1 = {0};

            final float finalRs = rs[0];
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int i=0;
                    for(DataSnapshot ds : dataSnapshot.child(pci).child("Free").getChildren()){
                        date dateobject = ds.getValue(date.class);

                        choosedates_array1.add(dateobject);
                        size1[0]++;
                    }

//                Log.e("!_@@<DATA>",Integer.toString(size1[0]));
                    //        size =choosedates_array1.size();
                    if(choosedates_array1.size()== 0){

                        Toast.makeText(SetDescriptionAptActivity.this,"database empty choose another date",Toast.LENGTH_SHORT).show();
//                Log.e("!_@@<DATA>",Integer.toString(size));
                        Intent intent = new Intent(SetDescriptionAptActivity.this,ProfessorHomePageActivity.class);
                        startActivity(intent);

//            t=false;
                    }
                    else {
                        boolean t=true;
                        for(int i =0;i<choosedates_array1.size();i++){
                            float es = Float.parseFloat(choosedates_array1.get(i).getTime_From().replace(":","."));
                            float ee = Float.parseFloat(choosedates_array1.get(i).getTime_To().replace(":","."));
                            String ed = choosedates_array1.get(i).getTime_date().toString();

                            String rd = cdateInput.getText().toString();
                            String res=cfrom.getText().toString();
                            String ree =cto.getText().toString();
                            String eday=choosedates_array1.get(i).getTime_date();
                            String est =choosedates_array1.get(i).getTime_From();
                            String eet =choosedates_array1.get(i).getTime_To();

                            if (rd.compareTo(eday) ==0) {
                                if ((est.compareTo(res) <= 0) && (ree.compareTo(eet) <= 0)) {
                                    confirmationfn();
                                    t=false;
                                    break;
                                }
                            }

                            Log.d("<es!!>rd",rd);
                            Log.d("<es!!>res",res);
                            Log.d("<es!!>ree",ree);
                            Log.d("<es!!>eday",eday);
                            Log.d("<es!!>est",est);
                            Log.d("<es!!>es",Float.toString(es));
                            Log.d("<es!!>eet",eet);

                        }
                        if(t){
                            CnfmyDialog = new Dialog(SetDescriptionAptActivity.this);
                            CnfmyDialog.setContentView(R.layout.confirm_pop_layout);
                            CnfAlter = (TextView) CnfmyDialog.findViewById(R.id.CnfAlterTextview);
                            Cnftxtclose = (TextView) CnfmyDialog.findViewById(R.id.CnfPoptxtclose);
                            CnfYes = (TextView)CnfmyDialog.findViewById(R.id.CnfYesTextview);
                            cnftextview=(TextView)CnfmyDialog.findViewById(R.id.CnfTitleTextview);
                            CnfYes.setText("Choose");
                            cnftextview.setText("Choose correct free Date and time!!");
                            Cnftxtclose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CnfmyDialog.dismiss();
//                                    Intent intent = new Intent(SetDescriptionAptActivity.this,Student_HomePageActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);
                                }
                            });

                            CnfYes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    CnfmyDialog.cancel();
                                    Intent intent = new Intent(SetDescriptionAptActivity.this,Student_HomePageActivity.class);
//                                    finish();
                                    CnfmyDialog.dismiss();
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });

                            CnfAlter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SetDescriptionAptActivity.this,AlternateDayActivity.class);
//                                    finish();
                                    CnfmyDialog.dismiss();
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });

                            CnfmyDialog.show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SetDescriptionAptActivity.this,"database error occured",Toast.LENGTH_SHORT).show();
                }
            });

        }

        loadingbar.dismiss();
    }


//----------------    Booke Apooinment class Define here--------------------------------
    public void Bookappointment(){


//  -----------------------      Student Dataset ----------------------

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final String sname = sharedPref.getString("Sname", "Not Available");
        final String scollegeid = sharedPref.getString("scollegeid", "Not Available");
        final String stok1 = sharedPref.getString("Stoken", "Not Available");

//   ---------------------------     Professor dataset ----------------------------
        SharedPreferences psharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final String pname = psharedPref.getString("pname", "Not Available");
        final String pci = psharedPref.getString("pcollegeid", "Not Available");
        final String ptok = psharedPref.getString("Ptoken", "Not Available");
        tkn= ptok;
        if (TextUtils.isEmpty(cfrom.getText())){
            subject.setError("fill reason");

        }
        notibody = subject.getText();


        long millis=System.currentTimeMillis();
        final java.util.Date currentdate=new java.util.Date(millis);
        final String appointmentid = String.valueOf(System.currentTimeMillis());
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("DATABASE");
        final DataApptClass dataset= new DataApptClass();

        dataset.setResponderId(pci);
        dataset.setResponderName(pname);
        dataset.setRequestedId(scollegeid);
        dataset.setRequesterName(sname);
        dataset.setApptId(appointmentid);
        dataset.setRequesterDeviceToken(stok1);
        dataset.setResponderDeviceToken(ptok);
        dataset.setApptDay(cdateInput.getText().toString());
        dataset.setApptStartTime(cfrom.getText().toString());
        dataset.setApptEndTime(cto.getText().toString());
        dataset.setApptType(notititle);
        dataset.setRequestedTime(currentdate.toString());
        dataset.setMessagebody(subject.getText().toString());
        dataset.setUserType(userType);

//                Log.e("!_@@<DATA>",aptype.toString());




        String mGroupId = ref.push().getKey();
        ref.child(datasetname).child(pci).child(mGroupId).setValue(dataset);
        Toast.makeText(SetDescriptionAptActivity.this,"confirmed :)",Toast.LENGTH_LONG).show();

        // ----------------- notification function----------------------------

        new Notify().execute();

    }

    public void confirmationfn(){
//            loadingbar.dismiss();
            CnfmyDialog.setContentView(R.layout.confirm_pop_layout);
            Cnftxtclose = (TextView) CnfmyDialog.findViewById(R.id.CnfPoptxtclose);
            CnfYes = (TextView)CnfmyDialog.findViewById(R.id.CnfYesTextview);

            Cnftxtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { CnfmyDialog.dismiss();
                }
            });


            CnfYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingbar.show();
                    final float rs = Float.parseFloat(cfrom.getText().toString().replace(":", "."));
                    final float re = Float.parseFloat(cto.getText().toString().replace(":", "."));
                    final String rd=cdateInput.getText().toString();

//                    Log.e("ee",cto.getText().toString());
//                    Log.e("rd",rd);
                    SharedPreferences psharedPref1 = PreferenceManager.getDefaultSharedPreferences(SetDescriptionAptActivity.this);
                    final String pci = psharedPref1.getString("pcollegeid", "Not Available");
                    final DatabaseReference database;
                    database = FirebaseDatabase.getInstance().getReference("TIME").child(pci).child("Free");
//                    database.removeValue();
                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds: dataSnapshot.getChildren()){
                                date obj=ds.getValue(date.class);
                                final float es=Float.parseFloat(obj.getTime_From().toString().replace(":", "."));
                                 final float ee=Float.parseFloat(obj.getTime_To().toString().replace(":", "."));
                                 final String ed=obj.getTime_date().toString();

                                String rd = cdateInput.getText().toString();
                                String res=cfrom.getText().toString();
                                String ree =cto.getText().toString();
                                String eday=obj.getTime_date();
                                String est =obj.getTime_From();
                                String eet =obj.getTime_To();

                                 float requestingSlot=re-rs;
                                 float existingSlot =ee-es;

                                if (rd.compareTo(eday) ==0) {
                                    if ((est.compareTo(res) <= 0) && (ree.compareTo(eet) <= 0)) {
                                        DatabaseReference ref =ds.child("time_To").getRef().getParent();
                                        Log.e("<ed>",ref.toString());
                                        ref.removeValue();


                                        Bookappointment();

                                        if(res.compareTo(est) > 0)
                                            setDate(est,res,rd);

                                        if(eet.compareTo(ree) > 0 )
                                            setDate(ree,eet,rd);

                                        if(userType=="Professor"){
                                            Intent intent =new Intent(SetDescriptionAptActivity.this,ProfessorHomePageActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                        else{
                                            Intent intent =new Intent(SetDescriptionAptActivity.this,Student_HomePageActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                        break;

                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("databaseError",databaseError.toString());

                        }
                    });

//                loadingbar.dismiss();

                }

            });
//            finishAffinity();
            CnfmyDialog.show();
            loadingbar.dismiss();
        }

        public void setDate(String nstTime,String nendTime,String existDate){
            date obj =new date();
            obj.setTime_date(existDate);
            obj.setTime_From(nstTime);
            obj.setTime_To(nendTime);
            SharedPreferences psharedPref1 = PreferenceManager.getDefaultSharedPreferences(SetDescriptionAptActivity.this);
            final String pci = psharedPref1.getString("pcollegeid", "Not Available");
            final DatabaseReference database;
            database = FirebaseDatabase.getInstance().getReference("TIME").child(pci).child("Free");
            String mGroupId = ref.push().getKey();
            database.child(mGroupId).setValue(obj);
        }

}
