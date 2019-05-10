package com.example.appointment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.List;

public class Student_HomePageActivity extends AppCompatActivity {

    private DrawerLayout drawer;

    DatabaseReference database;
    SharedPreferences stusp,profsp;
    ListView listViewProfessor;
    List<RegisterSaveData> professorList;
    private ProgressDialog loadingbar;
    NavigationView navigationView;
    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__home_page);
        this.setTitle("Professor");



        profsp = getSharedPreferences("plogin",MODE_PRIVATE);
        stusp = getSharedPreferences("slogin",MODE_PRIVATE);
        SharedPreferences psharedPref = PreferenceManager.getDefaultSharedPreferences(Student_HomePageActivity.this);
        final String email = psharedPref.getString("SEmail", "Not Available");
        final String ci = psharedPref.getString("register_collegeId", "Not Available");
        final String name = psharedPref.getString("Sname", "Not Available");

        db = FirebaseDatabase.getInstance();
        ref =db.getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.StudentHomepageToolBar);
        setSupportActionBar(toolbar);

        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance().getReference();

        drawer = findViewById(R.id.StudenHomePageDrawer_layout);
        navigationView =(NavigationView)findViewById(R.id.nav_view);

        listViewProfessor = (ListView)findViewById(R.id.listviewMain);
        professorList =new ArrayList<RegisterSaveData>();
        loadingbar = new ProgressDialog(this);


        // this is navigation bar............................




        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.student_homepage_nav_home:
//                        Toast.makeText(ProfessorHomePageActivity.this,"Home",Toast.LENGTH_SHORT).show();
                        Intent home=new Intent(Student_HomePageActivity.this,Student_HomePageActivity.class);
                        startActivity(home);
                        break;
                    case R.id.student_homepage_nav_profile:
                        Intent profile=new Intent(Student_HomePageActivity.this,StudentProfileActivity.class);
                        startActivity(profile);
                        break;

                    case R.id.student_homepage_nav_message:
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("text/plain");
                        startActivity(emailIntent);
                        break;

                    case R.id.student_homepage_nav_appointment:
                        Intent profile2=new Intent(Student_HomePageActivity.this,StuAppointmentActivity.class);
                        startActivity(profile2);
                        break;
                    case R.id.student_homepage_nav_logout:
                        String t="f";
                        unique_login_class obj=new unique_login_class();
                        obj.setUsertype("Student");
                        obj.setFlag(t);
                        obj.setEmail(email);
                        String m_szAndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        String colid = email.replaceAll("[-+.^:,@]","");
                        ref.child("UniqueLogin").child(m_szAndroidID).child(colid).setValue(obj);



//                        sp = getSharedPreferences("login",MODE_PRIVATE);
//                        profsp.edit().putBoolean("logged",false).apply();
                        stusp.edit().putBoolean("slogged",false).apply();

                        Intent intent = new Intent(Student_HomePageActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                }


                return false;
            }
        });
    }


    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

//    This method for retrieve data from data base............................
    @Override
    protected void onStart() {

        SharedPreferences psharedPref = PreferenceManager.getDefaultSharedPreferences(Student_HomePageActivity.this);
        final String name = psharedPref.getString("Sname", "Not Available");
        View hView =navigationView.getHeaderView(0);
        TextView tv = (TextView)hView.findViewById(R.id.stu_nav_header_name);
        TextView emaiview = (TextView)hView.findViewById(R.id.stu_nav_header_email);
        tv.setText(name);
        emaiview.setText(currentUser.getEmail());
        super.onStart();
        loadingbar.show();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                professorList.clear();
                for(DataSnapshot professorsnapshot : dataSnapshot.child("Professor").getChildren()){
                    RegisterSaveData obj1 = professorsnapshot.getValue(RegisterSaveData.class);

                     professorList.add(obj1);
                 }
                professor_list_Adapter adapter = new professor_list_Adapter(Student_HomePageActivity.this,professorList);
                listViewProfessor.setAdapter(adapter);
                loadingbar.dismiss();

                listViewProfessor.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        RegisterSaveData o;
                        o = professorList.get(position);
                        String name=o.getRegister_name();
                        String collegeid = o.getRegister_collegeid();
                        String email = o.getRegister_email();
                        String pToken = o.getToken();

                        // Create object of SharedPreferences.
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Student_HomePageActivity.this);
                        //now get Editor
                        SharedPreferences.Editor editor = sharedPref.edit();
                        //put your value
                        editor.putString("pname",name);
                        editor.putString("pcollegeid",collegeid);
                        editor.putString("Ptoken",pToken);
                        //commits your edits
                        editor.commit();

                        Intent i = new Intent(Student_HomePageActivity.this, ChooseDateActivity.class);
//                        Log.e("!_@@<DATA>",name);
                        i.putExtra("CollegeId", collegeid);
                        i.putExtra("Email", email);
                        startActivity(i);

//                        Intent i2 = new Intent(Student_HomePageActivity.this, RequestingAppointmentActivity.class);
//                        i2.putExtra("Name", name);
//                        i2.putExtra("CollegeId", collegeid);
// Object o = listViewProfessor.getItemAtPosition(position);
//                        Toast.makeText(Student_HomePageActivity.this,"You selected : " +name,
//                                Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                  Utils.displayToast(("something_went_wrong), LoginActivity.this"),Student_HomePageActivity.this);
                  Toast.makeText(Student_HomePageActivity.this,"Some error occured in database !",Toast.LENGTH_SHORT).show();
            }
        });
    }
}


