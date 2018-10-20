package comingoo.one.user.comingoouser;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import comingoo.one.user.comingoouser.R;

public class signupConfirmActivity extends AppCompatActivity {
    private String phoneNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    static Activity context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_confirm);

        final EditText code = (EditText) findViewById(R.id.code);
        final EditText code2 = (EditText) findViewById(R.id.code2);
        final EditText code3 = (EditText) findViewById(R.id.code3);
        final EditText code4 = (EditText) findViewById(R.id.code4);
        final EditText code5 = (EditText) findViewById(R.id.code5);
        final EditText code6 = (EditText) findViewById(R.id.code6);


        context = this;

        phoneNumber = getIntent().getStringExtra("phoneNumber");

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
                                Intent intent = new Intent(signupConfirmActivity.this, signupFacebookActivity.class);
                                intent.putExtra("phoneNumber", phoneNumber);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(signupConfirmActivity.this, "WRONG CODE PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show();
                                findViewById(R.id.imageButton).setVisibility(View.VISIBLE);
                            }
                        }
                    });



                }else{
                    Toast.makeText(signupConfirmActivity.this, "Please Enter Your OTP Code", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    static void Dest(){
        context.finish();
    }
}
