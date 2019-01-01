package com.comingoo.user.comingoo.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;

import com.comingoo.user.comingoo.Interfaces.Userinformation;
import com.comingoo.user.comingoo.R;
import com.comingoo.user.comingoo.activity.ComingooAndYouActivity;
import com.comingoo.user.comingoo.activity.LoginActivity;
import com.comingoo.user.comingoo.activity.MapsActivity;
import com.comingoo.user.comingoo.utility.AnimateConstraint;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

public class MapsActivityVM {

    public void checkUserTask(final Context context, final Userinformation userinformation){
       final SharedPreferences prefs = context.getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
        String userId = prefs.getString("userID", null);
        FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    prefs.edit().remove("userID");
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                } else {
                    String userName = dataSnapshot.child("fullName").getValue(String.class);
                    String userImage = dataSnapshot.child("image").getValue(String.class);
                    String userEmail = dataSnapshot.child("email").getValue(String.class);
                    String callNumber = dataSnapshot.child("phoneNumber").getValue(String.class);
                    userinformation.gettingUserInfo(userName, userImage, userEmail, callNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
