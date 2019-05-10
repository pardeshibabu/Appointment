package com.example.appointment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class StudentProfileActivity extends AppCompatActivity {
    TextView name,email,ci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        name = (TextView)findViewById(R.id.stdprofilename);
        email = (TextView)findViewById(R.id.stdprofileEmail);
        ci = (TextView)findViewById(R.id.stdprofileCi);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(StudentProfileActivity.this);
        final String sname = sharedPref.getString("Sname", "Not Available");
        final String scollegeid = sharedPref.getString("scollegeid", "Not Available");
        final String semail = sharedPref.getString("Semail", "Not Available");

        name.setText(sname);
        email.setText(semail);
        ci.setText(scollegeid);



    }
}
