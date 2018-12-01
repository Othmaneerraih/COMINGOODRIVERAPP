package com.comingoo.user.comingoo.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.comingoo.user.comingoo.async.HistoriqueTask;
import com.comingoo.user.comingoo.model.Course;
import com.comingoo.user.comingoo.utility.LocalHelper;
import com.comingoo.user.comingoo.R;
import com.comingoo.user.comingoo.adapters.HistoriqueAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class HistoriqueActivity extends AppCompatActivity {
    private RecyclerView mLocationView;
    private DatabaseReference mLocation;
    private HistoriqueAdapter cAdapter;
    private ArrayList<Course> CoursesData;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);
        action();
        updateViews();

    }

    private void action() {
        SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
        userId = prefs.getString("userID", null);

        mLocation = FirebaseDatabase.getInstance().getReference("CLIENTFINISHEDCOURSES").child(userId);
        mLocation.keepSynced(true);

        CoursesData = new ArrayList<>();
        mLocationView = findViewById(R.id.rv_fav_place);
        mLocationView.setHasFixedSize(true);
        mLocationView.setLayoutManager(new LinearLayoutManager(this));

        cAdapter = new HistoriqueAdapter(getApplicationContext(), CoursesData);
        mLocationView.setAdapter(cAdapter);
        new HistoriqueTask(mLocation, cAdapter, CoursesData).execute();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void updateViews() {
        Context context;
        Resources resources;
        String language;
        language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");
        context = LocalHelper.setLocale(HistoriqueActivity.this, language);
        resources = context.getResources();
        TextView title = (TextView) findViewById(R.id.title);
        //Set Texts
        title.setText(resources.getString(R.string.Historique));
    }

}
