package comingoo.one.user.comingoouser;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import comingoo.one.user.comingoouser.R;

public class inviteActivity extends AppCompatActivity {

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
        Resources resources;
        String language;
        language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");

        context = LocalHelper.setLocale(inviteActivity.this, language);
        resources = context.getResources();

        TextView textView33 = (TextView) findViewById(R.id.textView33);
        TextView textView37 = (TextView) findViewById(R.id.textView37);
        Button button2 = (Button) findViewById(R.id.button2);



        //Set Texts
        textView33.setText(resources.getString(R.string.Invitervosamis));
        textView37.setText(resources.getString(R.string.inviteText));
        button2.setText(resources.getString(R.string.Inviter));
    }

}
