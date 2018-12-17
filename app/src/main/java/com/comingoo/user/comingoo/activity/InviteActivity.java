package com.comingoo.user.comingoo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.comingoo.user.comingoo.utility.LocalHelper;
import com.comingoo.user.comingoo.R;


public class InviteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        updateViews();
    }

    public void updateViews(){
        Context context;
        final Resources resources;
        String language;
        language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");

        context = LocalHelper.setLocale(InviteActivity.this, language);
        resources = context.getResources();

        TextView textView33 = findViewById(R.id.textView33);
        TextView textView37 = findViewById(R.id.textView37);
        Button button2 = findViewById(R.id.button2);



        //Set Texts
        textView33.setText(resources.getString(R.string.Invitervosamis));
        textView37.setText(resources.getString(R.string.inviteText));
        button2.setText(resources.getString(R.string.Inviter));

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareSocialMedia(resources);
            }
        });
    }

    private void shareSocialMedia(Resources resources){

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent .putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.invite_txt));
        try {
            this.startActivity(intent );
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(InviteActivity.this, resources.getString(R.string.facebookText), Toast.LENGTH_SHORT).show();
        }
    }

}
