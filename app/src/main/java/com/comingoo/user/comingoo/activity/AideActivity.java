package com.comingoo.user.comingoo.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comingoo.user.comingoo.async.UpdateInfoAideTask;
import com.comingoo.user.comingoo.utility.AnimateConstraint;
import com.comingoo.user.comingoo.utility.LocalHelper;
import com.comingoo.user.comingoo.R;

import java.util.ArrayList;
import java.util.Locale;


public class AideActivity extends AppCompatActivity {
    private ConstraintLayout Q1, Q2, A1, A2;
    private boolean a1 = false, a2 = false;
    public ConstraintLayout fc, content;

    private ConstraintLayout image;
    private Uri imageUri = null;

    private EditText message;
    private TextView selectImage;
    private ImageView ivArrawOne, ivArrawTwo;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aide);

        final SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
        userId = prefs.getString("userID", null);

        message = findViewById(R.id.message);
        selectImage = findViewById(R.id.image_text);

        Q1 = findViewById(R.id.Q1);
        A1 = findViewById(R.id.A1);
        Q2 = findViewById(R.id.Q2);
        A2 = findViewById(R.id.A2);

        fc = findViewById(R.id.fc);
        content = findViewById(R.id.content);

        image = findViewById(R.id.add_image);
        ivArrawOne = findViewById(R.id.arrow1);
        ivArrawTwo = findViewById(R.id.arrow2);

        AnimateConstraint.animate(AideActivity.this, content, 250, 1, 0);

        findViewById(R.id.add_voice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 10);
                } else {
                    Toast.makeText(AideActivity.this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
                }
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AideActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AideActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 2);
                }
            }
        });

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateInfoAideTask(AideActivity.this, userId, imageUri, message, selectImage).execute();
                AnimateConstraint.animate(AideActivity.this, content, 1, 250, 500);
            }
        });


        Q1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!a1) {
                    AnimateConstraint.animate(AideActivity.this, A1, 100, 2, 500);
                    a1 = true;
                    ivArrawOne.setImageResource(R.drawable.ic_arraw_up);
                } else {
                    AnimateConstraint.animate(AideActivity.this, A1, 2, 100, 500);
                    a1 = false;
                    ivArrawOne.setImageResource(R.drawable.expand);
                }
            }
        });
        Q2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!a2) {
                    AnimateConstraint.animate(AideActivity.this, A2, 70, 2, 500);
                    a2 = true;
                    ivArrawTwo.setImageResource(R.drawable.ic_arraw_up);
                } else {
                    AnimateConstraint.animate(AideActivity.this, A2, 2, 70, 500);
                    a2 = false;
                    ivArrawTwo.setImageResource(R.drawable.expand);
                }

            }
        });


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        updateViews();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);

        if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 2);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK) {

            imageUri = data.getData();
            //image.setBackgroundResource();
            selectImage.setText("image choisi");


        }

        if (requestCode == 10 && resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                message.setText(result.get(0));
            }
        }

    }


    public void updateViews() {
        Context context;
        Resources resources;
        String language;
        language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");

        context = LocalHelper.setLocale(AideActivity.this, language);
        resources = context.getResources();

        TextView textView13 = (TextView) findViewById(R.id.textView13);
        TextView textView14 = (TextView) findViewById(R.id.textView14);
        TextView textView22 = (TextView) findViewById(R.id.textView22);
        TextView textView17 = (TextView) findViewById(R.id.textView17);
        TextView textView15 = (TextView) findViewById(R.id.textView15);
        TextView textView20 = (TextView) findViewById(R.id.textView20);
        TextView textView24 = (TextView) findViewById(R.id.textView24);
        TextView image_text = (TextView) findViewById(R.id.image_text);
        TextView textView34 = (TextView) findViewById(R.id.textView34);
        TextView send = (TextView) findViewById(R.id.send);


        //Set Texts
        textView13.setText(resources.getString(R.string.Aide));
        textView14.setText(resources.getString(R.string.Questionsfréquementposéés));
        textView22.setText(resources.getString(R.string.Q1));
        textView17.setText(resources.getString(R.string.A1));
        textView15.setText(resources.getString(R.string.Q2));
        textView20.setText(resources.getString(R.string.A2));
        textView24.setText(resources.getString(R.string.Contacteznous));
        image_text.setText(resources.getString(R.string.Ajouteruneimage));
        textView34.setText(resources.getString(R.string.Enregistreraudio));
        send.setText(resources.getString(R.string.Envoyer));
    }

}
