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

import com.comingoo.user.comingoo.async.CheckUserHistoriqueTask;
import com.comingoo.user.comingoo.utility.LocalHelper;
import com.comingoo.user.comingoo.R;
import com.comingoo.user.comingoo.adapters.HistoriqueAdapter;
import com.comingoo.user.comingoo.model.Course;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class HistoriqueActivity extends AppCompatActivity {
    private RecyclerView mLocationView;
    private DatabaseReference mLocation;
    private HistoriqueAdapter cAdapter;
    private List<Course> CoursesData;
    private String userId;
    private TextView noDataTv;

    Context context;
    Resources resources;
    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");
        context = LocalHelper.setLocale(HistoriqueActivity.this, language);
        resources = context.getResources();
        SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
        userId = prefs.getString("userID", null);

        noDataTv = findViewById(R.id.no_data_txt);
        noDataTv.setVisibility(View.GONE);
        noDataTv.setText(resources.getString(R.string.no_data_availabl_txt));
        mLocation = FirebaseDatabase.getInstance().getReference("CLIENTFINISHEDCOURSES").child(userId);
        mLocation.keepSynced(true);

        CoursesData = new ArrayList<>();
        mLocationView = findViewById(R.id.my_recycler_view);
        mLocationView.setHasFixedSize(true);
        mLocationView.setLayoutManager(new LinearLayoutManager(this));

        cAdapter = new HistoriqueAdapter(getApplicationContext(), CoursesData);
        mLocationView.setAdapter(cAdapter);
        new CheckUserHistoriqueTask(mLocation, cAdapter, CoursesData).execute();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (CoursesData.size() > 0) {
            noDataTv.setVisibility(View.VISIBLE);
        }

        updateViews();

    }


    private String driverName;
    private int Rate;

    public void updateViews() {
        Context context;
        Resources resources;
        String language;
        language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");
        context = LocalHelper.setLocale(HistoriqueActivity.this, language);
        resources = context.getResources();
        TextView title = findViewById(R.id.title);
        //Set Texts
        title.setText(resources.getString(R.string.Historique));
    }

}
