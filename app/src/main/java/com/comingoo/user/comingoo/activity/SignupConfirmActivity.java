package com.comingoo.user.comingoo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.comingoo.user.comingoo.R;
import com.comingoo.user.comingoo.utility.LocalHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class SignupConfirmActivity extends AppCompatActivity {
    private String phoneNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private Resources resources;
    private EditText code;
    private EditText code2;
    private EditText code3;
    private EditText code4;
    private EditText code5;
    private EditText code6;

    private String Email,name,password,imageURI;
    private String TAG = "SignupConfirmActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_confirm);

        code = findViewById(R.id.code);
        code2 = findViewById(R.id.code2);
        code3 = findViewById(R.id.code3);
        code4 = findViewById(R.id.code4);
        code5 = findViewById(R.id.code5);
        code6 = findViewById(R.id.code6);

        phoneNumber = getIntent().getStringExtra("phoneNumber");
        Email = getIntent().getStringExtra("Email");
        name = getIntent().getStringExtra("name");
        password = getIntent().getStringExtra("password");
        imageURI = getIntent().getStringExtra("imageURI");

        String language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");
        Context context = LocalHelper.setLocale(SignupConfirmActivity.this, language);
        resources = context.getResources();


        edittextFlow();


        findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code.getText().length() == 1 && code2.getText().length() == 1 && code3.getText().length() == 1 && code4.getText().length() == 1 && code5.getText().length() == 1 && code6.getText().length() == 1){
                    findViewById(R.id.imageButton).setVisibility(View.GONE);


                    final String smsId = getIntent().getStringExtra("id");
                    String OTPCode = code.getText().toString()+code2.getText().toString()+code3.getText().toString()+code4.getText().toString()+code5.getText().toString()+code6.getText().toString();

                    PhoneAuthCredential credentials = PhoneAuthProvider.getCredential(smsId, OTPCode);
                    FirebaseAuth.getInstance().signInWithCredential(credentials).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if((FirebaseAuth.getInstance().getCurrentUser()) != null){
                                FirebaseAuth.getInstance().getCurrentUser().delete();
                                signUpFirebase();

                            }else {
                                Toast.makeText(SignupConfirmActivity.this, resources.getString(R.string.wrong_code_txt), Toast.LENGTH_SHORT).show();
                                findViewById(R.id.imageButton).setVisibility(View.VISIBLE);
                            }
                        }
                    });



                }else{
                    Toast.makeText(SignupConfirmActivity.this, resources.getString(R.string.enter_otp_txt), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void signUpFirebase(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(Email, password);
                            final String id = FirebaseAuth.getInstance().getUid();
                            Map<String, Object> data = new HashMap<>();
                            data.put("fullName", name);
                            data.put("email", Email);
                            data.put("SOLDE", "0.0");
                            data.put("USECREDIT", "1");
                            data.put("LASTCOURSE", "**La Premiére Course**");
                            data.put("phoneNumber", phoneNumber);
                            data.put("image", imageURI);
                            data.put("level", "2");

                            Log.e(TAG, "onComplete: successfull");

                            FirebaseDatabase.getInstance().getReference("clientUSERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(!task.isSuccessful()){
                                        Log.e("signUpFacebookActivity", "onComplete: 1111 " );
                                        Toast.makeText(SignupConfirmActivity.this, resources.getString(R.string.error_txt), Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().getCurrentUser().delete();
                                        FirebaseAuth.getInstance().signOut();
                                    }else{
                                        Log.e("signUpFacebookActivity", "onComplete: 22222" );
                                        SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
                                        prefs.edit().putString("userID", FirebaseAuth.getInstance().getCurrentUser().getUid()).apply();
                                        startActivity(new Intent(SignupConfirmActivity.this, MapsActivity.class));
                                        finish();
                                    }

                                }
                            });
                        } else {
//                            FirebaseAuth.getInstance().getCurrentUser().delete();
                            removeUserFromFireBase();
                            String a = task.getResult().toString();
                            Log.e(TAG, "onComplete:Not " );
                            Log.e(TAG, "onComplete: successfull not"+task.getResult().toString());
                        }
                    }});
    }


    private void removeUserFromFireBase(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(Email, password);

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User re-authenticated.");
                    }
                });
    }

    private void edittextFlow(){
        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // add a condition to check length here - you can give here length according to your requirement to go to next EditTexts.
                if(code.getText().toString().trim().length() == 1){
                    code2.requestFocus();
                }
            }
        });

        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // add a condition to check length here - you can give here length according to your requirement to go to next EditTexts.
                if(code.getText().toString().trim().length() == 1){
                    code3.requestFocus();
                }
            }
        });

        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // add a condition to check length here - you can give here length according to your requirement to go to next EditTexts.
                if(code.getText().toString().trim().length() == 1){
                    code4.requestFocus();
                }
            }
        });

        code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // add a condition to check length here - you can give here length according to your requirement to go to next EditTexts.
                if(code.getText().toString().trim().length() == 1){
                    code5.requestFocus();
                }
            }
        });

        code5.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // add a condition to check length here - you can give here length according to your requirement to go to next EditTexts.
                if(code.getText().toString().trim().length() == 1){
                    code6.requestFocus();
                }
            }
        });
    }
}
