package com.example.petr.pacars;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalData extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    HashMap<String, Object> Data=new HashMap<String, Object>();


    private EditText Email;
    private EditText Phone;
    private EditText Name;
    private EditText Surname;
    private EditText Fathersname;
    private EditText Passport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydata_end_menu);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myRef = FirebaseDatabase.getInstance().getReference();
        try {
            myRef.child("Users").child(user.getUid()).child("Data").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI

                    GenericTypeIndicator<HashMap<String, Object>> t = new GenericTypeIndicator<HashMap<String, Object>>() {
                    };
                    // GenericTypeIndicator<List<String>> t1 = new  GenericTypeIndicator<List<String>>(){};
                    try {
                        Data = (HashMap<String, Object>) dataSnapshot.getValue(t);
                        // dataS=dataSnapshot.getValue(t1);
                        readData();
                    } catch (Exception e) {

                    }
                }

                void readData() {
                    //Email.getText().insert(Email.getSelectionStart(), dataS.get(0));

                    Email.setText(Data.get("Email").toString());
                    Phone.setText(Data.get("Phone").toString());
                    Name.setText(Data.get("Name").toString());
                    Surname.setText(Data.get("Surname").toString());
                    Fathersname.setText(Data.get("Fathersname").toString());
                    Passport.setText(Data.get("Passport").toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("tag", "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            });
        }
        catch (Exception e){}

        Name=(EditText)findViewById(R.id.name);
        Surname=(EditText)findViewById(R.id.surName);
        Fathersname=(EditText)findViewById(R.id.Fathersname);
        Email=(EditText)findViewById(R.id.Email);
        Phone=(EditText)findViewById(R.id.Phone);
        Passport=(EditText)findViewById(R.id.Pasport);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public  void Save(View view) throws InterruptedException {//button save
        String email = Email.getText().toString();
        String phone = Phone.getText().toString();
        String name = Name.getText().toString();
        String surname = Surname.getText().toString();
        String fathersname = Fathersname.getText().toString();
        String passport = Passport.getText().toString();


        HashMap<String, Object> result = new HashMap<>();
        result.put("Name", name);
        result.put("Surname", surname);
        result.put("Fathersname", fathersname);
        result.put("Passport", passport);
        result.put("Phone", phone);
        result.put("Email", email);

        //itog.put("/" + user.getUid().toString() + "/",result);
        if(name==""||surname==""||fathersname==""||
                passport==""||phone==""||email==""||name==null||surname==null||fathersname==null||
                passport==null||phone==null||email==null)
            Toast.makeText(PersonalData.this, "Error: Заполните все поля", Toast.LENGTH_SHORT).show();
        else{
            myRef.child("Users").child(user.getUid()).child("Data").updateChildren(result);
            Toast.makeText(PersonalData.this, "Данные успешно сохранены", Toast.LENGTH_SHORT).show();
        }
    }
    void shoeAlert(String title,String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalData.this);
        builder.setTitle(title)
                .setMessage(text)
                .setIcon(R.drawable.error_al)
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            Intent intent = new Intent(PersonalData.this, AppMenu2.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_setting ){
            Intent intent = new Intent(PersonalData.this, setting.class);
            startActivity(intent);

        } else if (id == R.id.nav_data) {
            Intent intent = new Intent(PersonalData.this, PersonalData.class);
            startActivity(intent);

        } else if (id == R.id.nav_logIn) {

            Intent intent = new Intent(PersonalData.this, EmailPassword.class);
            intent.putExtra("bigen", false);
            startActivity(intent);

        } else if (id == R.id.nav_telegram) {
            Uri address = Uri.parse("https://t.me/necebo");
            Intent openlink = new Intent(Intent.ACTION_VIEW, address);
            startActivity(openlink);

        } else if (id == R.id.nav_vk) {
            Uri address = Uri.parse("https://vk.com/p.volnov");
            Intent openlink = new Intent(Intent.ACTION_VIEW, address);
            startActivity(openlink);
        }
        else if (id == R.id.nav_vtsup) {
            Uri address = Uri.parse("https://vk.com/p.volnov");
            Intent openlink = new Intent(Intent.ACTION_VIEW, address);
            startActivity(openlink);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(com.example.petr.pacars.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
