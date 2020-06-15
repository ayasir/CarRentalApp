package com.kashsoftwares.dashboardsplashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationPhoneNo extends AppCompatActivity {

    private static final String TAG = "VerificationPhoneNo";
    private Button verify_btn;
    private TextView phoneNoEnteredByTheUser;
    private ProgressBar progressBar;
    private String verificationCodeBySystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //// Hide the status bar.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_verification_phone_no);

        verify_btn = findViewById(R.id.verify_btn);
        phoneNoEnteredByTheUser = findViewById(R.id.verification_code_entered_by_user);
        progressBar = findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.GONE);
        final String phoneNo = getIntent().getStringExtra("phoneno");

//        Log.d(TAG, "onCreate: "+phoneNo);
        sendVerificationCodeToUser(phoneNo);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = phoneNoEnteredByTheUser.getText().toString();

                if(code.isEmpty() || code.length() < 6){
                    phoneNoEnteredByTheUser.setError("Wrong OTP...");
                    phoneNoEnteredByTheUser.requestFocus();
                    return;
                }
              progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });
    }



    private void sendVerificationCodeToUser(String phoneNo) {


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNo,        // Phone number to verify
                100,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,   // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks


    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        progressBar.setVisibility(View.VISIBLE);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(VerificationPhoneNo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    //Get the code in global variable
                    verificationCodeBySystem = s;
                }
            };
             private void verifyCode(String codeByUser) {

                  PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
                  signInTheUserByCredentials(credential);

    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerificationPhoneNo.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(VerificationPhoneNo.this, "Your Account has been created successfully!", Toast.LENGTH_SHORT).show();

                            //Perform Your required action here to either let the user sign In or do something required
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            Toast.makeText(VerificationPhoneNo.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            Log.d(TAG, "onComplete: "+task.getException());
                        }
                    }
                });
    }


}
