package comingoo.one.user.comingoouser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import comingoo.one.user.comingoouser.R;


public class signupFacebookActivity extends AppCompatActivity {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_facebook);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();

        final String EMAIL = "email";
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
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
                                            final String Email  = object.getString("email");
                                            final String name = Profile.getCurrentProfile().getName();
                                            final String phoneNumber = getIntent().getStringExtra("phoneNumber");
                                            final String password = Profile.getCurrentProfile().getId();
                                            final String imageURI = Profile.getCurrentProfile().getProfilePictureUri(300, 300).toString();

                                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        FirebaseAuth.getInstance().signInWithEmailAndPassword(Email, password);
                                                        final String id = FirebaseAuth.getInstance().getUid();
                                                        Map<String, Object> data = new HashMap<>();
                                                        data.put("fullName", name);
                                                        data.put("email", Email);
                                                        data.put("SOLDE", "0");
                                                        data.put("USECREDIT", "1");
                                                        data.put("LASTCOURSE", "**La Premiére Course**");
                                                        data.put("phoneNumber", phoneNumber);
                                                        data.put("image", imageURI);
                                                        data.put("level", "2");

                                                        FirebaseDatabase.getInstance().getReference("clientUSERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(!task.isSuccessful()){
                                                                    Toast.makeText(signupFacebookActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
                                                                    FirebaseAuth.getInstance().getCurrentUser().delete();
                                                                    FirebaseAuth.getInstance().signOut();
                                                                }else{
                                                                    SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
                                                                    prefs.edit().putString("userID", FirebaseAuth.getInstance().getCurrentUser().getUid()).apply();
                                                                    startActivity(new Intent(signupFacebookActivity.this, MapsActivity.class));
                                                                    finish();
                                                                }

                                                            }
                                                        });
                                                    }
                                                }});

                                            LoginManager.getInstance().logOut();
                                        }catch(Exception e){
                                            Toast.makeText(signupFacebookActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday ");
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
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

}
