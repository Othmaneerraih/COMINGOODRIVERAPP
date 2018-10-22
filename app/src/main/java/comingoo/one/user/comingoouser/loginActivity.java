package comingoo.one.user.comingoouser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

import org.json.JSONObject;

import java.util.Arrays;

import comingoo.one.user.comingoouser.R;

public class loginActivity extends AppCompatActivity {

    private EditText phoneNumber;
    private EditText password;
    private ImageButton loginBtn;
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
                                            final String phoneNumber = getIntent().getStringExtra("phoneNumber");
                                            final String password = Profile.getCurrentProfile().getId();
                                            final String imageURI = Profile.getCurrentProfile().getProfilePictureUri(300, 300).toString();

                                            Log.e("loginActivity", "onCompleted: email: " + Email);

                                            FirebaseAuth.getInstance().signInWithEmailAndPassword(Email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        SharedPreferences prefs = getSharedPreferences("COMINGOOUSERD`ATA", MODE_PRIVATE);
                                                        prefs.edit().putString("userID", FirebaseAuth.getInstance().getCurrentUser().getUid()).apply();
                                                        startActivity(new Intent(loginActivity.this, MapsActivity.class));
                                                        finish();
                                                    } else {
                                                        loginBtn.setVisibility(View.VISIBLE);
                                                        Toast.makeText(loginActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(loginActivity.this, signupActivity.class));
                                                        finish();
                                                    }

                                                }
                                            });

                                            LoginManager.getInstance().logOut();
                                        } catch (Exception e) {
                                            Toast.makeText(loginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(loginActivity.this, signupActivity.class));
                                            finish();
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
                        startActivity(new Intent(loginActivity.this, signupActivity.class));
                        finish();
                    }
                });
    }


    private void login(String phoneNumber, final String password) {
        loginBtn.setVisibility(View.GONE);
        if (password == null || phoneNumber == null) {
            loginBtn.setVisibility(View.VISIBLE);
            return;
        }
        if (phoneNumber.length() > 9) {
            phoneNumber = phoneNumber.substring(1, 10);
        }

        FirebaseDatabase.getInstance().getReference("clientUSERS").orderByChild("phoneNumber").equalTo(phoneNumber).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    loginBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(loginActivity.this, "Numéro erroné!!!", Toast.LENGTH_SHORT).show();
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
                                        loginBtn.setVisibility(View.VISIBLE);
                                        Toast.makeText(loginActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            loginBtn.setVisibility(View.VISIBLE);
                            Toast.makeText(loginActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
                        }


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
