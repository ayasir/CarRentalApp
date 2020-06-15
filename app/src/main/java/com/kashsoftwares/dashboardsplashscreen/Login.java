package com.kashsoftwares.dashboardsplashscreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    public static final int REQUEST_CODE = 1;

    private Button newUserBtn;
    private ImageView imageView;
    private TextView logoTxt;
    private TextView sloganTxt;
    private TextInputLayout username;
    private TextInputLayout password;
    private Button forgotPasswdBtn;
    private Button signinBtn;


    FirebaseDatabase database;
    DatabaseReference reference;
    UserHelper userHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //// Hide the status bar.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");


        newUserBtn = findViewById(R.id.newUser_btn);
        imageView = findViewById(R.id.login_logo_img);
        logoTxt = findViewById(R.id.logo_txt);
        sloganTxt = findViewById(R.id.slogan_txt);
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        forgotPasswdBtn = findViewById(R.id.forgot_passwd_btn);
        signinBtn = findViewById(R.id.signin_btn);



        newUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newUserActivity = new Intent(Login.this, Signup.class);


                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View, String>(imageView,"logo_img_tran");
                pairs[1] = new Pair<View, String>(logoTxt,"logo_tran");
                pairs[2] = new Pair<View, String>(sloganTxt,"slogan_tran");
                pairs[3] = new Pair<View, String>(username,"username_tran");
                pairs[4] = new Pair<View, String>(password,"password_tran");
//                pairs[5] = new Pair<View, String>(forgotPasswdBtn,"forgotpasswd_tran");
                pairs[5] = new Pair<View, String>(signinBtn,"signin_tran");
                pairs[6] = new Pair<View, String>(newUserBtn,"newuser_tran");

//                for(Pair p : pairs){
//                    Log.d(TAG, "onClick: " + p.toString());
//                }
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                startActivity(newUserActivity, options.toBundle() );

            }
        });

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateUserName() || !validatePassword()) {
                    return;
                } else {

                    isUserExisting();
                }


//                reference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        userHelper = dataSnapshot.getValue(UserHelper.class);
//                        Log.d(TAG, "onDataChange: "+userHelper);
//
//                        for( DataSnapshot snapshot : dataSnapshot.getChildren()){
//                            UserHelper user =  snapshot.getValue(UserHelper.class);
//                            System.out.println("OnDataChange : "+user.getFullName());
//                            Log.d(TAG, "onDataChange: "+user.getFullName());
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        Log.d(TAG, "onCancelled: "+databaseError.getMessage());
//                    }
//                });
            }
        });


    }

    private void isUserExisting() {
        final String userName = username.getEditText().getText().toString().trim();
        final String passwd = password.getEditText().getText().toString().trim();
        Log.d(TAG, "isUserExisting: "+passwd);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        Query checkUser = ref.orderByChild("username").equalTo(userName);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    username.setError(null);
                    username.setErrorEnabled(false);

                    String passwdDB = dataSnapshot.child(userName).child("password").getValue(String.class);
                   // UserHelper userHelper = dataSnapshot.getValue(UserHelper.class);
                    Log.d(TAG, "onDataChange:::::: "+passwdDB);
                    if(passwd.equals(passwdDB)){

                        password.setError(null);
                        password.setErrorEnabled(false);

                        String emailDB = dataSnapshot.child(userName).child("email").getValue(String.class);
                        String fullNameDB = dataSnapshot.child(userName).child("fullName").getValue(String.class);
                        String phonedDB = dataSnapshot.child(userName).child("phoneno").getValue(String.class);
                        String usernameDB = dataSnapshot.child(userName).child("username").getValue(String.class);

                        Intent profile = new Intent(Login.this, UserProfile.class);

                        profile.putExtra("fullname",fullNameDB);
                        profile.putExtra("username",usernameDB);
                        profile.putExtra("email",emailDB);
                        profile.putExtra("phone",phonedDB);
                        profile.putExtra("password",passwdDB);

                        username.getEditText().setText(null);
                        password.getEditText().setText(null);

                        startActivityForResult(profile,REQUEST_CODE);


                    } else{
                        password.setError("Wrong Password!");
                        password.requestFocus();
                    }
                } else{
                    username.setError("No such user exists!");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: "+databaseError);
            }
        });
    }

    private boolean validateUserName(){

        String val = username.getEditText().getText().toString();


        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePassword(){

        String val = password.getEditText().getText().toString();

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode==RESULT_OK){
            String requiredValue = data.getStringExtra("key");
            username.requestFocus();
            Toast.makeText(Login.this, requiredValue,Toast.LENGTH_LONG).show();
//            Snackbar.make(Login.this, requiredValue.toString(), Snackbar.LENGTH_SHORT).show();
        }
    }
}
