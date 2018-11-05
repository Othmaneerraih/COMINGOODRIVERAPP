package com.comingoo.user.comingoo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

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

import comingoo.one.user.comingoouser.R;

public class loginActivity extends AppCompatActivity {
    private EditText phoneNumber;
    private EditText password;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
//            prefs.edit().putString("userID", FirebaseAuth.getInstance().getCurrentUser().getUid()).apply();
            startActivity(new Intent(loginActivity.this, MapsActivity.class));
            finish();
        }

//        phoneNumber = (EditText) findViewById(R.id.code);
//        password = (EditText) findViewById(R.id.password);
//        loginBtn = (ImageButton) findViewById(R.id.loginBtn);


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();

        final String EMAIL = "email";
        final LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

//        getDurationForRoute("Pabnartek Rd, Dhaka","Bridge, Z5478");

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

                                            Log.e("loginActivity", "onCompleted: email: " + Email);


                                            FirebaseDatabase.getInstance().getReference("clientUSERS").orderByChild("email").equalTo(Email).
                                                    limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {

                                                @Override
                                                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                                    if (!dataSnapshot.exists()) {
//                                                        loginBtn.setVisibility(View.VISIBLE);
//                                                        Toast.makeText(loginActivity.this, "Numéro erroné!!!", Toast.LENGTH_SHORT).show();

//                                                        loginBtn.setVisibility(View.VISIBLE);
//                                                        Toast.makeText(loginActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();

                                                        String[] stringArray = getResources().getStringArray(R.array.blocked_users);
                                                        Log.e("loginActivity", "onDataChange: "+stringArray[0] );
                                                        if (Arrays.asList(stringArray).contains(EMAIL)) {
                                                            // true
                                                            Toast.makeText(loginActivity.this, "This account is blocked", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            Intent intent = new Intent(loginActivity.this, signupActivity.class);
                                                            intent.putExtra("Email",  Email);
                                                            intent.putExtra("name",  name);
                                                            intent.putExtra("password",  password);
                                                            intent.putExtra("imageURI",  imageURI);
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
                                                                            startActivity(new Intent(loginActivity.this, MapsActivity.class));
                                                                            finish();
                                                                        } else {
//                                                                            loginBtn.setVisibility(View.VISIBLE);
                                                                            Toast.makeText(loginActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            } else {
//                                                                loginBtn.setVisibility(View.VISIBLE);
                                                                Toast.makeText(loginActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(loginActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(loginActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private String getDurationForRoute(String origin, String destination){
        // - We need a context to access the API
        GeoApiContext geoApiContext = new GeoApiContext.Builder()
                .apiKey(getResources().getString(R.string.google_maps_key))
                .build();

        // - Perform the actual request
        DirectionsResult directionsResult = null;
        try {
            directionsResult = DirectionsApi.newRequest(geoApiContext)
                    .mode(TravelMode.DRIVING)
                    .origin(origin)
                    .destination(destination)
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // - Parse the result
        DirectionsRoute route = directionsResult.routes[0];
        DirectionsLeg leg = route.legs[0];
        Duration duration = leg.duration;
        return duration.humanReadable;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
