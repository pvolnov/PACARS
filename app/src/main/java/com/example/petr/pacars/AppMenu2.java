package com.example.petr.pacars;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AppMenu2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference myRef;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_menu2);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        myRef = FirebaseDatabase.getInstance().getReference();


        setSupportActionBar(toolbar);
        updateAdapter(this);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Эта функция пока не доступна", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.app_menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            Intent intent = new Intent(AppMenu2.this, AppMenu2.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_setting ){
            Intent intent = new Intent(AppMenu2.this, setting.class);
            startActivity(intent);

        } else if (id == R.id.nav_data) {
            Intent intent = new Intent(AppMenu2.this, PersonalData.class);
            startActivity(intent);

        } else if (id == R.id.nav_logIn) {

            Intent intent = new Intent(AppMenu2.this, EmailPassword.class);
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
    private void updateAdapter(final Context context){
        //считываем данные из БД и записываем их в MesFiller
        myRef.child("messages").addListenerForSingleValueEvent(new ValueEventListener() {
            HashMap<String, Object> MesFiller;
            List<Message> messages = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(ProgressBar.VISIBLE);
                // Get Post object and use the values to update the UI
                GenericTypeIndicator<HashMap<String, Object>> t = new GenericTypeIndicator<HashMap<String, Object>>() {};
                //messages.add(new Message("Hellow", "Привет","images/6.png"));

                DataSnapshot dts;

                    for (Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator(); it.hasNext(); ) {
                        dts = it.next();
                        //В сообщении может быть персонализация, что бы не вывожить такие сообщени ясделаем проверку
                        Map<String,Object> m = dts.getValue(t);
                        try {
                            messages.add(new Message(m.get("name").toString(), m.get("text").toString(), m.get("url").toString(), m.get("Fulltext").toString()));
                        }
                        catch (Exception e){
                            Log.w("tag", "loadPost:onCancelled", e);}
                    }
                setInitialData();
            }
            void setInitialData() {
                // messages.add(new Message(MesFiller.get("name").toString(), MesFiller.get("text").toString(), MesFiller.get("url").toString()));

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
                LinearLayoutManager llm = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(llm);

                MesAdapter adapter = new MesAdapter(context, messages);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("tag", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}
