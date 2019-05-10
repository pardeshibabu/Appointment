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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfessorListActivity extends AppCompatActivity {
    DatabaseReference database;

    ListView listViewProfessor;
    List<RegisterSaveData> fetchprofessorList;
    private ProgressDialog loadingbar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_list);
        this.setTitle("Professor");
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance().getReference();
        listViewProfessor = (ListView)findViewById(R.id.Professorlistview);
        fetchprofessorList =new ArrayList<RegisterSaveData>();
        loadingbar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadingbar.show();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                fetchprofessorList.clear();
                for(DataSnapshot professorsnapshot : dataSnapshot.child("Professor").getChildren()){
                    RegisterSaveData obj1 = professorsnapshot.getValue(RegisterSaveData.class);
                    String email = currentUser.getEmail();
                    if(!email.equals(obj1.getRegister_email())){
                        fetchprofessorList.add(obj1);
                    }

                }
                professor_list_Adapter adapter = new professor_list_Adapter(ProfessorListActivity.this,fetchprofessorList);
                listViewProfessor.setAdapter(adapter);
                loadingbar.dismiss();

                listViewProfessor.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        RegisterSaveData o;
                        o = fetchprofessorList.get(position);
                        String name=o.getRegister_name();
                        String collegeid = o.getRegister_collegeid();
                        String email = o.getRegister_email();
                        String pToken = o.getToken();

                        // Create object of SharedPreferences.
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ProfessorListActivity.this);
                        //now get Editor
                        SharedPreferences.Editor editor = sharedPref.edit();
                        //put your value
                        editor.putString("pname",name);
                        editor.putString("pcollegeid",collegeid);
                        editor.putString("Ptoken",pToken);
                        //commits your edits
                        editor.commit();

                        Intent i = new Intent(ProfessorListActivity.this, ChooseDateActivity.class);
                        Log.e("!_@@<DATA>",name);
                        i.putExtra("CollegeId", collegeid);
                        i.putExtra("Email", email);
                        startActivity(i);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                  Utils.displayToast(("something_went_wrong), LoginActivity.this"),Student_HomePageActivity.this);
                Toast.makeText(ProfessorListActivity.this,"Some error occured in database !",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
