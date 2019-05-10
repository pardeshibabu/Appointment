package com.example.appointment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowStuApptDetailActivity extends AppCompatActivity {

    static String StuEmail;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference database,ref;
    ListView DetaillistViewdataAppt;
    List<DataApptClass> choosedates_array2;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_stu_appt_detail);


        choosedates_array2 =new ArrayList<DataApptClass>();
        DetaillistViewdataAppt = (ListView)findViewById(R.id.Showappt_Stu_Listview2);
        loadingbar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        StuEmail=currentUser.getEmail();
        SharedPreferences psharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final String pci = psharedPref.getString("ShowPCi", "Not Available");
        String colid = currentUser.getEmail().replaceAll("[-+.^:,@]","");

        final String[] sName = new String[1];
        sName[0]="pkm";



    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        StuEmail=currentUser.getEmail();
        SharedPreferences psharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final String pci = psharedPref.getString("ShowPCi", "Not Available");
        String colid = StuEmail.replaceAll("[-+.^:,@]","");

        final String[] sName = new String[1];
//        sName[0]="pkm";
        loadingbar.show();
//        Toast.makeText(ShowStuApptDetailActivity.this,pci+"kam hai",Toast.LENGTH_SHORT);
        database = FirebaseDatabase.getInstance().getReference("Student").child(colid);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RegisterSaveData obj=dataSnapshot.getValue(RegisterSaveData.class);
                sName[0] =obj.getRegister_name();
//                Toast.makeText(ShowStuApptDetailActivity.this,sName[0]+"kam kare rh",Toast.LENGTH_SHORT).show();
                ref = FirebaseDatabase.getInstance().getReference("DATABASE").child("AppointmentDATA").child(pci);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        choosedates_array2.clear();
                        for(DataSnapshot ds:dataSnapshot.getChildren()){
                            DataApptClass onj =ds.getValue(DataApptClass.class);
                            if(sName[0].equals(onj.getRequesterName())){
                                choosedates_array2.add(onj);
                            }
                        }
                        dataappt_adapter_class dataapptAdapterClass=new dataappt_adapter_class(ShowStuApptDetailActivity.this,choosedates_array2);
                        DetaillistViewdataAppt.setAdapter(dataapptAdapterClass);
                        loadingbar.dismiss();

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("<error>",databaseError.toString());

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("<error>",databaseError.toString());
            }
        });
//        Log.e("checkkro",sName[0]);

    }
}
