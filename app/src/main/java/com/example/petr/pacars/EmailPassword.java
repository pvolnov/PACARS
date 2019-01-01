package com.example.petr.pacars;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.TimeUnit;

public class EmailPassword extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    FirebaseAnalytics mFirebaseAnalytics;

    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    int RC_SIGN_IN =1;


    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Bundle arguments = getIntent().getExtras();
        Boolean bgn;
        try{
        bgn = arguments.getBoolean ("bigen");}
        catch (Exception e){
            bgn=true;
        }

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user!=null&&hasConnection(this)&&bgn)
        {
            Intent intent = new Intent(EmailPassword.this, AppMenu2.class);
            startActivity(intent);
        }
        else if(!hasConnection(this)){
            shoeAlert("Ошибка","Нет соединения с интернетом");
        }
        if(!bgn)
            //go out
            FirebaseAuth.getInstance().signOut();
        findViewById(R.id.googlelogin).setOnClickListener(this);
    }
    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    private void signIn() {
        if(!hasConnection(this)){
            shoeAlert("Ошибка","Нет соединения с интернетом");
            return;
        }
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        try {

            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        catch (Exception e){
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Auth Error");
            bundle.putString(FirebaseAnalytics.Param.CONTENT, e.getMessage());
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Snackbar.make(findViewById(R.id.loginactivity), "Авторизация...", Snackbar.LENGTH_SHORT).show();
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult();
                firebaseAuthWithGoogle(account);
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Tag", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Snackbar.make(findViewById(R.id.loginactivity), "Authentication Sacsesful.", Snackbar.LENGTH_SHORT).show();
                            Intent intent = new Intent(EmailPassword.this, PersonalData.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("tag", "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.loginactivity), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
    @Override
    public void onClick(View view) {
        CheckBox iagree=findViewById(R.id.Iagree);
        if(iagree.isChecked())
            try{
                if(view.getId() == R.id.googlelogin) {
                    signIn();
                }
                }
                catch (Exception e){}
        else
            {
            Snackbar.make(findViewById(R.id.loginactivity), "Примите пользовательсеок соглашение", Snackbar.LENGTH_SHORT).show();
        }

    }

    void shoeAlert(String title,String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(EmailPassword.this);
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
}
