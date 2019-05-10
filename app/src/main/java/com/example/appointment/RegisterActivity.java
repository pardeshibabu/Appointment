package com.example.appointment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class RegisterActivity extends AppCompatActivity {

    private static final String MY_PREFS_NAME = "pardeshi"; // for SharedPreferences
    private Button CreateAccountButton;
    private EditText register_email, register_password, register_cnf_password, register_name,register_collegeId;
    private TextView register_login;
    private ProgressDialog loadingbar;
    CheckBox check1, check2;

    private String parrentdb;
    private String token;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference ref;

    RegisterSaveData obj;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.setTitle("Register");

        database = FirebaseDatabase.getInstance();
        ref =database.getReference();

        CreateAccountButton = (Button) findViewById(R.id.Register_buttonSignUp);
        register_login = (TextView) findViewById(R.id.Register_textViewLogin);
        register_collegeId=(EditText) findViewById(R.id.Register_RollNumber);
        register_email = (EditText) findViewById(R.id.Register_email);
        register_password = (EditText) findViewById(R.id.Register_password);
        register_cnf_password = (EditText) findViewById(R.id.Register_Cnf_password);
        register_name = (EditText) findViewById(R.id.Register_name);
        check1 = (CheckBox) findViewById(R.id.checkBox1);
        check2 = (CheckBox) findViewById(R.id.checkBox2);
        bar = (ProgressBar) findViewById(R.id.progressbar);

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("register_name",register_name.getText().toString());
        editor.putString("register_email", register_email.getText().toString());
        editor.putString("register_collegeId",register_collegeId.getText().toString());
        editor.apply();


        loadingbar = new ProgressDialog(this);




//      Method for check box......................
        addListenerOnChkIos();

//      Saving Data in the firebase Database..................................

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            token= task.getResult().getToken();
                            createButton_click(token);
                        }
                    }
                });

            }
        });

        register_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivity = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(loginActivity);
            }
        });
    }

    public void createButton_click(String token1) {


        obj = new RegisterSaveData();
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        String em =register_email.getText().toString().trim();
        String Pass = register_password.getText().toString().trim();

        obj.setRegister_name(register_name.getText().toString());
        obj.setRegister_collegeid(register_collegeId.getText().toString());
        obj.setRegister_email(register_email.getText().toString());
        obj.setRegister_password(register_password.getText().toString());
        obj.setRegister_cnf_password(register_cnf_password.getText().toString());
        obj.setToken(token1);

        if (TextUtils.isEmpty(obj.getRegister_name())) {
            register_name.setError("fill name");
        }
        else if (TextUtils.isEmpty(obj.getRegister_collegeid())) {
            register_collegeId.setError("fill correct college id");
        }
        else if (TextUtils.isEmpty(obj.getRegister_email())) {
            register_email.setError("fill email");
        }
        else if (!(obj.getRegister_email().matches(emailPattern))) {
            register_email.setError("fill correct email");
        }
        else if (TextUtils.isEmpty(obj.getRegister_password())) {
            register_password.setError("fill password");
        }
        else if (TextUtils.isEmpty(obj.getRegister_cnf_password())) {
            register_cnf_password.setError("fill Confirm password");
        }
        else if (!(obj.getRegister_password().equals(obj.getRegister_cnf_password()))) {
            register_cnf_password.setError("fill correct confirm password");
        }
        else{
            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("Please wait while we are saving the credential!");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            // Authenticate user .........................................
            mAuth.createUserWithEmailAndPassword(em, Pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

//            Writing Data in the Database....................
                                if(parrentdb.equals("Professor")){
                                    String colid1 = register_email.getText().toString().replaceAll("[-+.^:,@]","");

                                  ref.child("Professor").child(colid1).setValue(obj);
//---------for unique_login_class------------------------------------------------------------
                                  String m_szAndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                  unique_login_class obj=new unique_login_class();
                                  String g="f";
                                  String colid = register_email.getText().toString().replaceAll("[-+.^:,@]","");
//                                  Log.e("<aata>",colid);
                                  obj.setUsertype(parrentdb);
                                  obj.setEmail(register_email.getText().toString());
                                  obj.setFlag(g);
                                  ref.child("UniqueLogin").child(m_szAndroidID)
                                          .child(colid).setValue(obj);


                                  Toast.makeText(RegisterActivity.this, "Account succesfull register.", Toast.LENGTH_SHORT).show();
                                  finish();
                                  loadingbar.dismiss();
                                  startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                }
                                else{
                                    String colid1 = register_email.getText().toString().replaceAll("[-+.^:,@]","");
                                    ref.child("Student").child(colid1).setValue(obj);
// --------for unique_login_class------------------------------------------------------------
                                    String m_szAndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                    unique_login_class obj=new unique_login_class();
                                    String colid = register_email.getText().toString().replaceAll("[-+.^:,@]","");;
//                                    Log.e("<aata>",colid);
                                    String b="f";
                                    obj.setUsertype(parrentdb);
                                    obj.setEmail(register_email.getText().toString());
                                    obj.setFlag(b);
                                    ref.child("UniqueLogin").child(m_szAndroidID)
                                            .child(colid)
                                            .setValue(obj);

                                    Toast.makeText(RegisterActivity.this, "Account succesfull register.", Toast.LENGTH_SHORT).show();
                                    finish();
                                    loadingbar.dismiss();
                                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                }
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Couldn't register, try again with valid input!",
                                        Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                            }

                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void addListenerOnChkIos() {
        check1 = (CheckBox) findViewById(R.id.checkBox1);
        check2 = (CheckBox) findViewById(R.id.checkBox2);

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
                    parrentdb = "Student ";
                }
            }
        });
    }

}

