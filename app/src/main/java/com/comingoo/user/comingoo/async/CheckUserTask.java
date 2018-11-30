package com.comingoo.user.comingoo.async;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.comingoo.user.comingoo.adapters.MyAdapter;
import com.comingoo.user.comingoo.model.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckUserTask extends AsyncTask<String, Integer, String> {

    private DatabaseReference mLocation;
    private MyAdapter cAdapter;
    private ArrayList<Notification> notificationData;

    public CheckUserTask(DatabaseReference mLocation, MyAdapter cAdapter, ArrayList<Notification> notificationData){
        this.mLocation = mLocation;
        this.cAdapter = cAdapter;
        this.notificationData = notificationData;
    }

    // Runs in UI before background thread is called
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Do something like display a progress bar
    }

    // This is run in a background thread
    @Override
    protected String doInBackground(String... params) {


        mLocation.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationData.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Notification newNot = new Notification(
                            data.child("title").getValue(String.class),
                            data.child("content").getValue(String.class),
                            data.child("code").getValue(String.class)
                    );
                    notificationData.add(newNot);
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
