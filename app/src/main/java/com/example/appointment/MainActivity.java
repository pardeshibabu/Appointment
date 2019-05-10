package com.example.appointment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
    SharedPreferences stusp,profsp;


    public static final String CHANNEL_ID = "Notify Channel_id";
    public static final String CHANNEL_NAME = "Notify Channel_id";
    public static final String CHANNEL_DESC = "Notify Channel_id";

    private Handler handler = new Handler();
    Button TextView, txtviews,txtviews2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(MainActivity.this);

        txtviews =(Button) findViewById(R.id.ProfessorTextview);
        txtviews2 =(Button) findViewById(R.id.StudetTextview);

        txtviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                profsp = getSharedPreferences("plogin",MODE_PRIVATE);
//                Log.e("hith",sp.getBoolean());
//                SharedPreferences sharedPreferences = PreferenceManager
//                        .getDefaultSharedPreferences(MainActivity.this);
                 profsp = getSharedPreferences("plogin",MODE_PRIVATE);
                 stusp = getSharedPreferences("slogin",MODE_PRIVATE);
//                boolean sp = sharedPreferences.getBoolean("plogged",false);
                if(profsp.getBoolean("plogged",false)){
                    startActivity(new Intent(MainActivity.this,ProfessorHomePageActivity.class));
                }
                else{
                    String pro="Professor";
                    OpenProfessorModule(pro);
                }
//
            }
        });
        txtviews2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                stusp = getSharedPreferences("slogin",MODE_PRIVATE);
//                Log.e("hith",sp.getBoolean());
//                SharedPreferences sharedPreferences = PreferenceManager
//                        .getDefaultSharedPreferences(MainActivity.this);
//                boolean sp1 = sharedPreferences.getBoolean("slogged",false);

//                SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);
                profsp = getSharedPreferences("plogin",MODE_PRIVATE);
                stusp = getSharedPreferences("slogin",MODE_PRIVATE);
                if(stusp.getBoolean("slogged",false)){
                    startActivity(new Intent(MainActivity.this,Student_HomePageActivity.class));
                }
                else{
                    String stu="Student";
                    OpenStudentModule(stu);
                }
            }
        });



    }

    public void OpenProfessorModule(String pro){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);

        intent.putExtra("arg",pro); // getText() SHOULD NOT be static!!!
        startActivity(intent);
    }

    public void OpenStudentModule(String stu){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("arg1",stu); // getText() SHOULD NOT be static!!!
        startActivity(intent);
    }

}
