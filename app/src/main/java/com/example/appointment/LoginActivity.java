package com.example.appointment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    private EditText loginemail,loginPassword;
    CheckBox check1,check2;
    private Button loginBtn,loginRegisterBtn;
    private ProgressDialog loadingbar;

    private DatabaseReference Rootref;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    String parrentdb=null;
    SharedPreferences profsp,stusp;
    FirebaseDatabase database;
    DatabaseReference ref;

    String[] title = new String[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle("Login");


        database = FirebaseDatabase.getInstance();
        ref =database.getReference();

        loginBtn=(Button)findViewById(R.id.Loginbutton);
        loginRegisterBtn=(Button)findViewById(R.id.LoginSignupBtn);
        loginemail=(EditText) findViewById(R.id.Login_Email);
        loginPassword=(EditText) findViewById(R.id.Login_password);
        loadingbar = new ProgressDialog(this);
        Intent in = getIntent();
        parrentdb=in.getStringExtra("arg");



        profsp = getSharedPreferences("plogin",MODE_PRIVATE);
        stusp = getSharedPreferences("slogin",MODE_PRIVATE);

//        profsp = getSharedPreferences("proflogin",MODE_PRIVATE);
//        profsp = getSharedPreferences("Stulogin",MODE_PRIVATE);



        //      Method for check box......................
        addListenerOnChkIos();




        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if(parrentdb=="Professor"){
                    ProfessorLogin(parrentdb);
                }
                else{
                    StudentLogin(parrentdb);
                }
            }
        });

        loginRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


//        final String m_szAndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//        String colid = loginemail.getText().toString().replaceAll("[-+.^:,@]","");
//        final String[] parredb = new String[1];
//        DatabaseReference m = ref.child("UniqueLogin").child(m_szAndroidID).child(colid).child("usertype").getRef();
//        Log.e("<data!!>title", m.toString());
//        ref.child("UniqueLogin").child(m_szAndroidID).child(colid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds : dataSnapshot.getChildren()){
//                    if(ds.getKey().equals("usertype")){
//                        title[0] = ds.getValue(String.class);
//                        Toast.makeText(LoginActivity.this,title[0]+"="+parrentdb,Toast.LENGTH_LONG).show();
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("<data!!>",databaseError.toString());
//            }
//        });

    }//End of onCreate(Bundle savedInstanceState)............................


    public void ProfessorLogin(final String pd){
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        final String email = loginemail.getText().toString();
        String Loginpassword = loginPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            loginemail.setError("fill email");
        }
        else if (TextUtils.isEmpty(Loginpassword)) {
            loginPassword.setError("fill password");
        }
        else {
            loadingbar.setTitle("Login Account");
            loadingbar.setMessage("Please wait while we are checking the credential!");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            if(pd.equals("Professor")){
                mAuth.signInWithEmailAndPassword(email, Loginpassword)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
//                                    SharedPreferences sharedPreferences = PreferenceManager
//                                            .getDefaultSharedPreferences(LoginActivity.this);
//                                    SharedPreferences.Editor editor1 = sharedPreferences.edit();
//
//                                    editor1.putBoolean("plogged",true).apply();
//                                    editor1.putBoolean("slogged",false).apply();
//                                    editor1.apply();
                                    stusp.edit().putBoolean("slogged",false).apply();
                                    profsp.edit().putBoolean("plogged",true).apply();
//                                    profsp = getSharedPreferences("proflogin",MODE_PRIVATE);
//                                    profsp = getSharedPreferences("Stulogin",MODE_PRIVATE);
//                                    profsp.edit().putBoolean("plogged",true).apply();
//                                    stusp.edit().putBoolean("slogged",false).apply();

//   --------------- for unique login ----------------------------------------------------------
                                    currentUser = mAuth.getCurrentUser();


//                                    SharedPreferences psharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
//                                    final String curruser = psharedPref.getString("pcollegeid", "Not Available");
                                    String m_szAndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                    String t="t";
                                    String colid = loginemail.getText().toString().replaceAll("[-+.^:,@]","");
                                    unique_login_class obj=new unique_login_class();
                                    obj.setFlag(t);
                                    obj.setEmail(loginemail.getText().toString());
                                    obj.setUsertype(parrentdb);

//                                    Log.e("<data!!>log",Boolean.toString(t));
//                                    Log.e("<data!!>log",email);
//                                    Log.e("<data!!>log",m_szAndroidID);
//                                    Log.e("<data!!>log",ref.child("UniqueLogin").child(m_szAndroidID).toString());
                                    ref.child("UniqueLogin").child(m_szAndroidID).child(colid).setValue(obj);

                                    //Going to Admin area............
                                    Toast.makeText(LoginActivity.this,"Loged in successfully..",Toast.LENGTH_LONG).show();
                                    loadingbar.dismiss();
//                                    finish();
                                    startActivity(new Intent(LoginActivity.this,ProfessorHomePageActivity.class));
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "couldn't login",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                    loadingbar.dismiss();
                                }
                            }
                        });
            }
            else{
                Toast.makeText(LoginActivity.this, "Sorry this id doesn't exist as professor create new account",
                        Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));

            }
        }

    }

    public void StudentLogin(final String pd){

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        final String email = loginemail.getText().toString();
        String Loginpassword = loginPassword.getText().toString();


        SharedPreferences psharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        final String ci = psharedPref.getString("register_collegeId", "Not Available");
        final String m_szAndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        if (TextUtils.isEmpty(email)) {
            loginemail.setError("fill email");
        } else if (TextUtils.isEmpty(Loginpassword)) {
            loginPassword.setError("fill password");
        }
        else {
            loadingbar.setTitle("Login Account");
            loadingbar.setMessage("Please wait while we are checking the credential!");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();


            if(pd.equals("Student")){

                mAuth.signInWithEmailAndPassword(email, Loginpassword)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    currentUser = mAuth.getCurrentUser();


//   --------------- for unique login ----------------------------------------------------------
                                    currentUser = mAuth.getCurrentUser();
                                    String m_szAndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                    String t="t";
                                    String colid = loginemail.getText().toString().replaceAll("[-+.^:,@]","");
                                    unique_login_class obj=new unique_login_class();
                                    obj.setFlag(t);
                                    obj.setUsertype(pd);
                                    obj.setEmail(email);
                                    ref.child("UniqueLogin").child(m_szAndroidID).child(colid).setValue(obj);

// -----------------Keep login always -----------------------------------------------------------
//                                    SharedPreferences sharedPreferences = PreferenceManager
//                                            .getDefaultSharedPreferences(LoginActivity.this);
//                                    SharedPreferences.Editor editor2 = sharedPreferences.edit();
//
//                                    editor2.putBoolean("plogged",false).apply();
//                                    editor2.putBoolean("slogged",true).apply();
//                                    editor2.apply();
                                    stusp.edit().putBoolean("slogged",true).apply();
                                    profsp.edit().putBoolean("plogged",false).apply();
//                                    sp.edit().putBoolean("logged",true).apply();
//                                    stusp.edit().putBoolean("slogged",true).apply();
//                                    profsp.edit().putBoolean("plogged",false).apply();

// ------------------Going to Student Area...................................................................

                                    mAuth = FirebaseAuth.getInstance();
                                    currentUser = mAuth.getCurrentUser();
                                    String colid1 = currentUser.getEmail().replaceAll("[-+.^:,@]","");
                                    Rootref= FirebaseDatabase.getInstance().getReference("Student").child(colid1);

                                    Rootref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            RegisterSaveData obj1 = dataSnapshot.getValue(RegisterSaveData.class);
                                            String sci=obj1.getRegister_collegeid();
                                            String name=obj1.getRegister_name();
                                            String tok = FirebaseInstanceId.getInstance().getToken();
                                            Log.d("tok",tok);
                                            // Create object of SharedPreferences.
                                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                            //now get Editor
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            //put your value
                                            editor.putString("Sname",name);
                                            editor.putString("Semail",obj1.getRegister_email());
                                            editor.putString("scollegeid",sci);
                                            editor.putString("Stoken",tok);
                                            //commits your edits
                                            editor.commit();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    Toast.makeText(LoginActivity.this,"Loged in successfully..",Toast.LENGTH_LONG).show();
                                    loadingbar.dismiss();
                                    finish();
                                    startActivity(new Intent(LoginActivity.this,Student_HomePageActivity.class));


                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "couldn't login"+task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                    loadingbar.dismiss();
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                }
                            }
                        });

            }
            else{
                Toast.makeText(LoginActivity.this, "Sorry this id doesn't exist as Student create new account",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                loadingbar.dismiss();
            }

        }


    }

    public void addListenerOnChkIos() {
        check1 = (CheckBox) findViewById(R.id.ProfcheckBox);
        check2 = (CheckBox) findViewById(R.id.StucheckBox);

        check1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    parrentdb = "Professor";
//                    check2.setVisibility(View.INVISIBLE);
                }
            }
        });
        check2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
//                    check1.setVisibility(View.INVISIBLE);
                    parrentdb = "Student";
                    Log.e("<DATA>",parrentdb);
                }
            }
        });
    }


}
