package com.comingoo.user.comingoo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.comingoo.user.comingoo.R;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    private EditText phoneNumber;
    private EditText password;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                                        // Application code
                                        try {
                                            final String Email = object.getString("email");
                                            final String name = Profile.getCurrentProfile().getName();
                                            final String password = Profile.getCurrentProfile().getId();
                                            final String imageURI = Profile.getCurrentProfile().getProfilePictureUri(300, 300).toString();

                                            Log.e("LoginActivity", "onCompleted: email: " + Email);


                                            FirebaseDatabase.getInstance().getReference("clientUSERS").orderByChild("email").equalTo(Email).
                                                    limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {

                                                @Override
                                                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                                    if (!dataSnapshot.exists()) {

                                                        String[] stringArray = getResources().getStringArray(R.array.blocked_users);
                                                        Log.e("LoginActivity", "onDataChange: " + stringArray[0]);
                                                        if (Arrays.asList(stringArray).contains(EMAIL)) {
                                                            // true
                                                            Toast.makeText(LoginActivity.this, "This account is blocked", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                                                            intent.putExtra("Email", Email);
                                                            intent.putExtra("name", name);
                                                            intent.putExtra("password", password);
                                                            intent.putExtra("imageURI", imageURI);
                                                            startActivity(intent);
                                                            finish();
                                                        }


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
                                                                Toast.makeText(LoginActivity.this, "not get your emaill address", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
