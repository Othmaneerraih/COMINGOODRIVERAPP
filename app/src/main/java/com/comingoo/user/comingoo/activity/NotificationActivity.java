package com.comingoo.user.comingoo.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.comingoo.user.comingoo.adapters.NotificationAdapter;
import com.comingoo.user.comingoo.async.CheckUserNotificationTask;
import com.comingoo.user.comingoo.model.Notification;
import com.comingoo.user.comingoo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class NotificationActivity extends AppCompatActivity {

    private RecyclerView mLocationView;
    private DatabaseReference mLocation;
    private NotificationAdapter cAdapter;
    private List<Notification> NotificationData;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
        userId = prefs.getString("userID", null);


        mLocation = FirebaseDatabase.getInstance().getReference("CLIENTNOTIFICATIONS");
        mLocation.keepSynced(true);

        NotificationData = new ArrayList<>();
        mLocationView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLocationView.setHasFixedSize(true);
        mLocationView.setLayoutManager(new LinearLayoutManager(this));

        cAdapter = new NotificationAdapter(NotificationData, userId, NotificationActivity.this);
        mLocationView.setAdapter(cAdapter);
        new CheckUserNotificationTask(mLocation, cAdapter, NotificationData).execute();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
