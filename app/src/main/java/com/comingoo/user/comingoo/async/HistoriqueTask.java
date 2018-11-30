package com.comingoo.user.comingoo.async;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;

import com.comingoo.user.comingoo.adapters.HistoriqueAdapter;
import com.comingoo.user.comingoo.model.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HistoriqueTask extends AsyncTask<String, Integer, String> {
    private DatabaseReference mLocation;
    private HistoriqueAdapter cAdapter;
    private ArrayList<Course> CoursesData;

    public HistoriqueTask(DatabaseReference mLocation, HistoriqueAdapter cAdapter, ArrayList<Course> CoursesData){
        this.mLocation = mLocation;
        this.cAdapter = cAdapter;
        this.CoursesData = CoursesData;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
                    if(data.child("date").exists() && data.child("date").getValue(Long.class) != null){
                        try{
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
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
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

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000L);
        String date = DateFormat.format("dd-MM-yyyy\nhh:mm:ss", cal).toString();
        return date;
    }
}