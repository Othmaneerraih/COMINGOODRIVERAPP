package com.comingoo.user.comingoo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.comingoo.user.comingoo.adapters.HistoriqueAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class historiqueActivity extends AppCompatActivity {
    private RecyclerView mLocationView;
    private DatabaseReference mLocation;
    private HistoriqueAdapter cAdapter;
    private List<Course> CoursesData;
    private String userId;

    Context context;
    Resources resources;
    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);
        language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");

        context = LocalHelper.setLocale(historiqueActivity.this, language);
        resources = context.getResources();


        SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
        userId = prefs.getString("userID", null);



        mLocation = FirebaseDatabase.getInstance().getReference("CLIENTFINISHEDCOURSES").child(userId);
        mLocation.keepSynced(true);

        CoursesData  = new ArrayList<>();
        mLocationView = (RecyclerView) findViewById(R.id.rv_fav_place);
        mLocationView.setHasFixedSize(true);
        mLocationView.setLayoutManager(new LinearLayoutManager(this));

        cAdapter = new HistoriqueAdapter(getApplicationContext(), CoursesData);
        mLocationView.setAdapter(cAdapter);
        new CheckUserTask().execute();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateViews();

    }

    private class CheckUserTask extends AsyncTask<String, Integer, String> {
        SharedPreferences prefs;
        String userId;
        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prefs = getSharedPreferences("COMINGOODRIVERDATA", MODE_PRIVATE);
            userId = prefs.getString("userId", null);
            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {

            mLocation.orderByChild("date").limitToFirst(20).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    CoursesData.clear();
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        if(data.child("date").exists()){
                            String dateString = getDate((data.child("date").getValue(Long.class) * -1));
                            Course newCourse = new Course(
                                    data.child("startAddress").getValue(String.class),
                                    data.child("endAddress").getValue(String.class),
                                    data.child("client").getValue(String.class),
                                    dateString,
                                    data.child("distance").getValue(String.class),
                                    data.child("driver").getValue(String.class),
                                    data.child("preWaitTime").getValue(String.class),
                                    data.child("price").getValue(String.class) + " MAD",
                                    data.child("waitTime").getValue(String.class)
                            );
                            CoursesData.add(newCourse);
                        }
                    }
                    cAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return "this string is passed to onPostExecute";
        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // Do things like update the progress bar
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Do things like hide the progress bar or change a TextView
        }
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000L);
        String date = DateFormat.format("dd-MM-yyyy\nhh:mm:ss", cal).toString();
        return date;
    }

    private String driverName;
    private int Rate;

    public void updateViews(){
        Context context;
        Resources resources;
        String language;
        language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");
        context = LocalHelper.setLocale(historiqueActivity.this, language);
        resources = context.getResources();
        TextView title = (TextView) findViewById(R.id.title);
        //Set Texts
        title.setText(resources.getString(R.string.Historique));
    }

}
