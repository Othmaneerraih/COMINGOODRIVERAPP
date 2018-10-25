package comingoo.one.user.comingoouser;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import comingoo.one.user.comingoouser.R;

public class signupActivity extends AppCompatActivity {
    private String TAG = "signupActivity";
    private ImageButton registerButton;
    private EditText phoneNumberField;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mRef;

    private String Email,name,phoneNumber,password,imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("clientUSERS");
        mAuth = FirebaseAuth.getInstance();

        phoneNumberField =  findViewById(R.id.code);
        registerButton = findViewById(R.id.continuer);

        phoneNumber = getIntent().getStringExtra("phoneNumber");
        Email = getIntent().getStringExtra("Email");
        name = getIntent().getStringExtra("name");
        password = getIntent().getStringExtra("password");
        imageURI = getIntent().getStringExtra("imageURI");

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Intent intent = new Intent(signupActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(signupActivity.this, "ERROR : " + e, Toast.LENGTH_LONG).show();
                Log.e(TAG, "onVerificationFailed: "+e.getMessage());
                registerButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Intent intent = new Intent(signupActivity.this, signupConfirmActivity.class);
                intent.putExtra("id", s);
                intent.putExtra("phoneNumber", phoneNumberField.getText().toString());
                intent.putExtra("Email",  Email);
                intent.putExtra("name",  name);
                intent.putExtra("password",  password);
                intent.putExtra("imageURI",  imageURI);
                startActivity(intent);
                finish();
                registerButton.setVisibility(View.VISIBLE);
            }
        };



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButton.setVisibility(View.INVISIBLE);
                mRef.orderByChild("phoneNumber").equalTo(phoneNumberField.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Toast.makeText(signupActivity.this, "Number Already in Database", Toast.LENGTH_SHORT).show();
                            registerButton.setVisibility(View.VISIBLE);
                        }else {
                            String phoneNumber = phoneNumberField.getText().toString();//"+212" + phoneNumberField.getText();
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    phoneNumber,        // Phone number to verify
                                    120,                 // Timeout duration
                                    TimeUnit.SECONDS,   // Unit of timeout
                                    signupActivity.this,               // Activity (for callback binding)
                                    mCallBacks);        // OnVerificationStateChangedCallbacks
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(signupActivity.this, MapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
