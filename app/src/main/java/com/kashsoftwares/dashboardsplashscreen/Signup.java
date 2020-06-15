package com.kashsoftwares.dashboardsplashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

public class Signup extends AppCompatActivity {


    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private UserHelper userHelper;

    private Button signupBtn;
    private TextInputLayout fullName;
    private TextInputLayout username;
    private TextInputLayout password;
    private TextInputLayout email;
    private TextInputLayout phoneno;
    private Button alreadySignin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //// Hide the status bar.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_signup);

        database = FirebaseDatabase.getInstance();
//        database.setLogLevel(Logger.Level.DEBUG);
        myRef = database.getReference("users");

       // myRef.setValue("Hello Firebase");
        signupBtn = findViewById(R.id.signup_btn);
        fullName = findViewById(R.id.fullname);
        username = findViewById(R.id.signup_username);
        password = findViewById(R.id.signup_password);
        email = findViewById(R.id.email);
        phoneno = findViewById(R.id.phoneno);
        alreadySignin = findViewById(R.id.already_acc_btn);


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // field validation code
                if(validateFullName() && validateUserName() && validateEmail() && validatePhone() && validatePassword()) {

                            userHelper = new UserHelper(fullName.getEditText().getText().toString(),
                            username.getEditText().getText().toString(),
                            password.getEditText().getText().toString(),
                            email.getEditText().getText().toString(),
                            phoneno.getEditText().getText().toString());
                    String phone_no = phoneno.getEditText().getText().toString();
                    Intent verificationIntent = new Intent(Signup.this, VerificationPhoneNo.class);
                    verificationIntent.putExtra("phoneno", phone_no);
                    startActivity(verificationIntent);

                    // add new record into the database
                    myRef.child(userHelper.getUsername()).setValue(userHelper);

//               myRef.setValue("name", userHelper.getFullName());
//               myRef.setValue("username", userHelper.getUsername());
//               myRef.setValue("email", userHelper.getEmail());
//               myRef.setValue("phoneno", userHelper.getPhoneno());
//               myRef.setValue("password", userHelper.getPassword());
                    // myRef.setValue(userHelper);
//                    myRef.child(userHelper.getUsername()).setValue(userHelper);
                    // myRef.push().setValue(userHelper);


                } else{
                    return;
                }

            }
        });


        alreadySignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(Signup.this, Login.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    private boolean validateFullName(){

        if( fullName.getEditText().getText().toString().isEmpty()){
            fullName.setError("User name cannot be empty");
            return false;
        } else{
            fullName.setError(null);
            return  true;
        }
    }
    private boolean validateUserName(){

        String val = username.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            username.setError("Username too long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            username.setError("White Spaces are not allowed");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePassword(){

        String val = password.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            password.setError("Password is too weak");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail(){

        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePhone(){

        if( phoneno.getEditText().getText().toString().isEmpty()){
            phoneno.setError("User name cannot be empty");
            return false;
        } else{
            phoneno.setError(null);
            return  true;
        }
    }

}
