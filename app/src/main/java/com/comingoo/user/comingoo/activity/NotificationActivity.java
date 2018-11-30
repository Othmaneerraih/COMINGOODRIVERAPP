package com.comingoo.user.comingoo.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.comingoo.user.comingoo.async.CheckUserTask;
import com.comingoo.user.comingoo.adapters.MyAdapter;
import com.comingoo.user.comingoo.model.Notification;
import com.comingoo.user.comingoo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class NotificationActivity extends AppCompatActivity {

    private RecyclerView mLocationView;
    private DatabaseReference mLocation;
    private MyAdapter cAdapter;
    private ArrayList<Notification> NotificationData;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initialize();
        action();

    }

    private void initialize() {
        SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
        userId = prefs.getString("userID", null);
        mLocationView = findViewById(R.id.my_recycler_view);
        mLocation = FirebaseDatabase.getInstance().getReference("CLIENTNOTIFICATIONS");
        mLocation.keepSynced(true);
        NotificationData = new ArrayList<>();
    }

    private void action() {
        mLocationView.setHasFixedSize(true);
        mLocationView.setLayoutManager(new LinearLayoutManager(this));

        cAdapter = new MyAdapter(this, NotificationData, userId);
        mLocationView.setAdapter(cAdapter);
        new CheckUserTask(mLocation, cAdapter, NotificationData).execute();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
