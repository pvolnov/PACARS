package com.example.petr.pacars;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class setting extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final String APP_PREFERENCES = "mysettings";


    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    CheckBox supreme;
    CheckBox nike;
    CheckBox adidas;
    Switch inst;
    Switch vk;
    EditText vk_login;
    EditText vk_pas;
    EditText inst_login;
    EditText inst_pas;
    private SharedPreferences mSettings;

    HashMap<String, Object> result = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_and_menu);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        supreme=findViewById(R.id.supreme_cb);
        nike=findViewById(R.id.nike_cb);
        adidas=findViewById(R.id.adidas_cb);
        inst=findViewById(R.id.inst_repost);
        vk=findViewById(R.id.vk_repost);
        vk_login=findViewById(R.id.vk_login);
        vk_pas=findViewById(R.id.vk_pas);
        inst_login=findViewById(R.id.inst_login);
        inst_pas=findViewById(R.id.inst_password);

        try{
            loadData();
        }
        catch (Exception e){}

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View view) {
                                                               String vkL=vk_login.getText().toString();
                                                               String vkP=vk_pas.getText().toString();
                                                               String inL=inst_login.getText().toString();
                                                               String inP=inst_pas.getText().toString();
                                                               result.put("Vk_Login", vkL);
                                                               result.put("Vk_Passport", vkP);
                                                               result.put("Instagram_Login", inL);
                                                               result.put("Instagram_Password", inP);

                                                               try {
                                                                   myRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Setting").updateChildren(result);
                                                                   Toast.makeText(getApplicationContext(), "Настройки изменеы", Toast.LENGTH_SHORT).show();
                                                               }
                                                               catch (Exception e){
                                                                   Toast.makeText(getApplicationContext(), "Ошибка: проверьте соединение с интернетом", Toast.LENGTH_SHORT).show();
                                                               }
                                                           }
                                                       }

        );
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        myRef = FirebaseDatabase.getInstance().getReference();
        setCB();

        View.OnClickListener cl= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mSettings.edit();
                if(supreme.isChecked()) {
                    editor.putBoolean("supreme_set", true);
                    result.put("supreme_set", true);
                }
                else{
                    editor.putBoolean("supreme_set", false);
                    result.put("supreme_set", false);
                }

                if(nike.isChecked()) {
                    editor.putBoolean("nike_set", true);
                    result.put("nike_set", true);
                }
                else{
                    editor.putBoolean("nike_set", false);
                    result.put("nike_set", false);
                }

                if(adidas.isChecked()) {
                    editor.putBoolean("adidas_set", true);
                    result.put("adidas_set", true);
                }
                else{
                    editor.putBoolean("adidas_set", false);
                    result.put("adidas_set", false);
                }

                if(vk.isChecked()) {
                    editor.putBoolean("vk_rep", true);
                    result.put("vk_rep", true);

                }
                else{
                    editor.putBoolean("vk_rep", false);
                    result.put("vk_rep", false);
                }
                if(inst.isChecked()) {
                    editor.putBoolean("inst_rep", true);
                    result.put("inst_rep", true);
                    }
                else{
                    editor.putBoolean("inst_rep", false);
                    result.put("inst_rep", false);
                }

                editor.apply();
                myRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Setting").updateChildren(result);
            }
        };

        supreme.setOnClickListener(cl);
        nike.setOnClickListener(cl);
        adidas.setOnClickListener(cl);
        vk.setOnClickListener(cl);
        inst.setOnClickListener(cl);
    }
    void setCB(){
        nike.setChecked(mSettings.getBoolean("nike_set",true));
        adidas.setChecked(mSettings.getBoolean("adidas_set",true));
        supreme.setChecked(mSettings.getBoolean("supreme",true));
        vk.setChecked(mSettings.getBoolean("vk_rep",true));
        inst.setChecked(mSettings.getBoolean("inst_rep",true));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            Intent intent = new Intent(setting.this, AppMenu2.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_setting ){
            Intent intent = new Intent(setting.this, setting.class);
            startActivity(intent);

        } else if (id == R.id.nav_data) {
            Intent intent = new Intent(setting.this, PersonalData.class);
            startActivity(intent);

        } else if (id == R.id.nav_logIn) {

            Intent intent = new Intent(setting.this, EmailPassword.class);
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

    void loadData(){
        myRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Setting").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                GenericTypeIndicator<HashMap<String, Object>> t = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                // GenericTypeIndicator<List<String>> t1 = new  GenericTypeIndicator<List<String>>(){};
                try {
                    HashMap<String, Object> Data;
                    Data = (HashMap<String, Object>) dataSnapshot.getValue(t);
                    vk_login.getText().insert(vk_login.getSelectionStart(), Data.get("Vk_Login").toString());
                    vk_pas.getText().insert(vk_pas.getSelectionStart(), Data.get("Vk_Passport").toString());
                    inst_login.getText().insert(inst_login.getSelectionStart(), Data.get("Instagram_Login").toString());
                    inst_pas.getText().insert(inst_pas.getSelectionStart(), Data.get("Instagram_Password").toString());
                    // dataS=dataSnapshot.getValue(t1)
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }

}
