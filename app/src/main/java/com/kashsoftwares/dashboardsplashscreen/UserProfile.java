package com.kashsoftwares.dashboardsplashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    private static final String TAG = "UserProfile";

    private TextView profileNameLabel;
    private TextView profileUsernameLabel;
    private TextInputLayout profileName;
    private TextInputLayout profileEmail;
    private TextInputLayout profilePhone;
    private TextInputLayout profilePassword;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //// Hide the status bar.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);


        profileNameLabel = findViewById(R.id.full_profile_name);
        profileUsernameLabel = findViewById(R.id.profile_username_label);
        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        profilePhone = findViewById(R.id.profile_phone);
        profilePassword = findViewById(R.id.profile_password);

        Bundle extras = getIntent().getExtras();

        profileNameLabel.setText(extras.getString("fullname"));
        profileUsernameLabel.setText(extras.getString("username"));
        profilePhone.getEditText().setText(extras.getString("phone"));
        profilePassword.getEditText().setText(extras.getString("password"));
        profileName.getEditText().setText(extras.getString("fullname"));
        profileEmail.getEditText().setText(extras.getString("email"));
        updateButton = findViewById(R.id.profile_update_btn);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // field validation code
                if(!validateFullName() || !validateEmail() || !validatePhone() || !validatePassword())
                    return;


                    final UserHelper userHelper = new UserHelper();
                    userHelper.setUsername(profileUsernameLabel.getText().toString());
                   userHelper.setFullName(profileName.getEditText().getText().toString());
                   userHelper.setPassword( profilePassword.getEditText().getText().toString());
                   userHelper.setEmail(profileEmail.getEditText().getText().toString());
                    userHelper.setPhoneno(profilePhone.getEditText().getText().toString());



//               myRef.setValue("name", userHelper.getFullName());
//               myRef.setValue("username", userHelper.getUsername());
//               myRef.setValue("email", userHelper.getEmail());
//               myRef.setValue("phoneno", userHelper.getPhoneno());
//               myRef.setValue("password", userHelper.getPassword());
                    // myRef.setValue(userHelper);
                    final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users");
                  //  myRef.child(userHelper.getUsername()).setValue(userHelper);
                    // myRef.push().setValue(userHelper);
                   myRef.child(userHelper.getUsername()).setValue(userHelper);
//                   startActivity(getParentActivityIntent(),Login.REQUEST_CODE);
                Intent intent = getIntent();
                intent.putExtra("key", "User Updated Successfully!");
                setResult(RESULT_OK,intent);
                   finish();
//                final String userName = profileUsernameLabel.getText().toString();
//                Query checkUser = myRef.orderByChild("username").equalTo(userName);
//                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.exists()){
//                            UserHelper helper = dataSnapshot.child(userName).getValue(UserHelper.class);
//                            Log.d(TAG, "onDataChange: "+helper.getEmail());
//
//
//
//                        } else{
//                           return;
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Log.d(TAG, "onCancelled: "+databaseError);
//                    }
//                });
//
//
            }
        });
    }

    private boolean validateFullName(){

        if( profileName.getEditText().getText().toString().isEmpty()){
            profileName.setError("User name cannot be empty");
            return false;
        } else{
            profileName.setError(null);
            return  true;
        }
    }

    private boolean validatePassword(){

        String val = profilePassword.getEditText().getText().toString();
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
            profilePassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            profilePassword.setError("Password is too weak");
            return false;
        } else {
            profilePassword.setError(null);
            profilePassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail(){

        String val = profileEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            profileEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            profileEmail.setError("Invalid email address");
            return false;
        } else {
            profileEmail.setError(null);
            profileEmail.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePhone(){

        if( profilePhone.getEditText().getText().toString().isEmpty()){
            profilePhone.setError("User name cannot be empty");
            return false;
        } else{
            profilePhone.setError(null);
            return  true;
        }
    }

}
