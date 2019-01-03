package com.comingoo.user.comingoo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comingoo.user.comingoo.R;
import com.comingoo.user.comingoo.utility.LocalHelper;
import com.comingoo.user.comingoo.utility.SharedPreferenceTask;
import com.comingoo.user.comingoo.utility.Utility;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Duration;
import com.google.maps.model.TravelMode;

import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private Resources resources;
    private String name = "", password = "", imageURI = "", Email = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");
        Context context = LocalHelper.setLocale(LoginActivity.this, language);
        resources = context.getResources();

        if (!isNetworkConnectionAvailable()) {
            checkNetworkConnection();
        }

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MapsActivity.class));
            finish();
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();

        final String EMAIL = "email";
        final LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Collections.singletonList(EMAIL));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        
                                        try {
                                            name = Profile.getCurrentProfile().getName();
                                            password = Profile.getCurrentProfile().getId();
                                            imageURI = Profile.getCurrentProfile().getProfilePictureUri(300, 300).toString();
                                            if (object.has("email"))
                                                Email = object.getString("email");
                                            else getEmailAddress();
                                            Log.e("LoginActivity", "onCompleted: email: " + Email);


                                            FirebaseDatabase.getInstance().getReference("clientUSERS").orderByChild("email").equalTo(Email).
                                                    limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {

                                                @Override
                                                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                                    if (!dataSnapshot.exists()) {

                                                        goToSignupScreen();

                                                    } else {

                                                        for (DataSnapshot data : dataSnapshot.getChildren()) {

                                                            String email = data.child("email").getValue(String.class);

                                                            if (email != null) {
                                                                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                                        if (task.isSuccessful()) {
                                                                            SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
                                                                            prefs.edit().putString("userID", FirebaseAuth.getInstance().getCurrentUser().getUid()).apply();
                                                                            startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                                                                            finish();
                                                                        } else {
                                                                            Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            } else {
//                                                                loginBtn.setVisibility(View.VISIBLE);
                                                                Toast.makeText(LoginActivity.this, resources.getString(R.string.no_email_txt), Toast.LENGTH_SHORT).show();
                                                            }


                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                            LoginManager.getInstance().logOut();
                                        } catch (Exception e) {
                                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(LoginActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void checkNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(resources.getString(R.string.no_internet_txt));
        builder.setMessage(resources.getString(R.string.internet_warning_txt));
        builder.setNegativeButton(resources.getString(R.string.close_txt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void goToSignupScreen() {
        String[] stringArray = getResources().getStringArray(R.array.blocked_users);
        Log.e("LoginActivity", "onDataChange: " + stringArray[0]);
        if (Arrays.asList(stringArray).contains(Email)) {
            // true
            Toast.makeText(LoginActivity.this, resources.getString(R.string.account_blocked_txt), Toast.LENGTH_LONG).show();
        } else {
            if (validation()) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.putExtra("Email", Email);
                intent.putExtra("name", name);
                intent.putExtra("password", password);
                intent.putExtra("imageURI", imageURI);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, resources.getString(R.string.try_again_txt), Toast.LENGTH_LONG).show();
            }
        }

    }

    private boolean validation() {
        return !Email.equals("") || !name.equals("") || !password.equals("") || !imageURI.equals("");
    }


    private void getEmailAddress() {
        {
            try {
                android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(LoginActivity.this);
                final android.app.AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_email_address, null);
                alertDialog.getWindow().setContentView(dialogView);

                final Button cancelBtn = dialogView.findViewById(R.id.cancel_btn);
                final Button okBtn = dialogView.findViewById(R.id.ok_btn);
                final EditText emailAddress = dialogView.findViewById(R.id.email_address_et);


                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Utility.isValidEmail(emailAddress.getText().toString())) {
                            Email = emailAddress.getText().toString();
                            goToSignupScreen();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(LoginActivity.this, resources.getString(R.string.email_validation_txt), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

            } catch (WindowManager.BadTokenException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            Log.d("Network", "Connected");
            return true;
        } else {
            Log.d("Network", "Not Connected");
            return false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
