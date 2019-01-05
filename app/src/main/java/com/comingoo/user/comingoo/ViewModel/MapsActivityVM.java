package com.comingoo.user.comingoo.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.comingoo.user.comingoo.Interfaces.Userinformation;
import com.comingoo.user.comingoo.R;
import com.comingoo.user.comingoo.activity.ComingooAndYouActivity;
import com.comingoo.user.comingoo.activity.LoginActivity;
import com.comingoo.user.comingoo.activity.MapsActivity;
import com.comingoo.user.comingoo.utility.AnimateConstraint;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class MapsActivityVM {

    private double wallet = 0.0;

    public void checkUserTask(final Context context, final Userinformation userinformation) {
        final SharedPreferences prefs = context.getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
        final String userId = prefs.getString("userID", null);
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


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild("rating")) {
                    Map<String, String> dataRating = new HashMap();
                    dataRating.put("1", "0");
                    dataRating.put("2", "0");
                    dataRating.put("3", "0");
                    dataRating.put("4", "0");
                    dataRating.put("5", "0");
                    FirebaseDatabase.getInstance().
                            getReference("clientUSERS").child(userId).child("rating").setValue(dataRating);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("favouritePlace")) {
                } else {
                    Map<String, String> dataFavPlace = new HashMap();
                    dataFavPlace.put(context.getString(R.string.txt_home), "");
                    dataFavPlace.put(context.getString(R.string.txt_work), "");
                    FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).child("favouritePlace").setValue(dataFavPlace);

                    Map<String, String> homeSt = new HashMap();
                    homeSt.put("Lat", "");
                    homeSt.put("Long", "");
                    homeSt.put("Address", "");

                    Map<String, String> workSt = new HashMap();
                    workSt.put("Lat", "");
                    workSt.put("Long", "");
                    workSt.put("Address", "");

                    FirebaseDatabase.getInstance().
                            getReference("clientUSERS").child(userId)
                            .child("favouritePlace").child(context.getString(R.string.txt_home)).setValue(homeSt);

                    FirebaseDatabase.getInstance().
                            getReference("clientUSERS").child(userId)
                            .child("favouritePlace").child(context.getString(R.string.txt_work)).setValue(workSt);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    public void checkCourseTask(final Context context, final Userinformation userinformation){
//       final SharedPreferences prefs = context.getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
//       final String userId = prefs.getString("userID", null);
//
//        FirebaseDatabase.getInstance().getReference("COURSES").orderByChild("client").
//                equalTo(userId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    try {
//                        for (final DataSnapshot data : dataSnapshot.getChildren()) {
//                            try {
//                                if (Objects.equals(data.child("driverPosLat").getValue(String.class), "") ||
//                                        Objects.equals(data.child("driverPosLong").getValue(String.class), "") ||
//                                        data.child("startLat").getValue(String.class).equals("") ||
//                                        data.child("startLong").getValue(String.class).equals("")) {
//                                    LatLng driverPosT = new LatLng(0.0,
//                                            0.0);
//
//                                    LatLng startPositionT = new LatLng(0.0,
//                                            0.0);
//                                } else {
//                                    driverPosT = new LatLng(Double.parseDouble(Objects.requireNonNull(data.child("driverPosLat").getValue(String.class))),
//                                            Double.parseDouble(Objects.requireNonNull(data.child("driverPosLong").getValue(String.class))));
//                                    startPositionT = new LatLng(Double.parseDouble(Objects.requireNonNull(data.child("startLat").getValue(String.class))),
//                                            Double.parseDouble(Objects.requireNonNull(data.child("startLong").getValue(String.class))));
//                                }
//                            } catch (NumberFormatException e) {
//                                e.printStackTrace();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            courseIDT = data.getKey();
//                            statusT = data.child("state").getValue(String.class);
//                            clientIdT = data.child("client").getValue(String.class);
//                            driverIDT = data.child("driver").getValue(String.class);
//
//
//                            driverLocT = new Location("");
//                            startLocT = new Location("");
//
//
//                            startText = data.child("startAddress").getValue(String.class);
//                            endText = data.child("endAddress").getValue(String.class);
//
//                            driverLocT.setLatitude(driverPosT.latitude);
//                            driverLocT.setLatitude(driverPosT.longitude);
//
//                            startLocT.setLatitude(startPositionT.latitude);
//                            startLocT.setLatitude(startPositionT.longitude);
//
//                            FirebaseDatabase.getInstance().getReference("DRIVERUSERS").child(driverIDT).addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    if (dataSnapshot.exists()) {
//                                        driverPhone = dataSnapshot.child("phoneNumber").getValue(String.class);
//                                        driverImage = dataSnapshot.child("image").getValue(String.class);
//                                        driverName = dataSnapshot.child("fullName").getValue(String.class);
//
//
//                                        FirebaseDatabase.getInstance().getReference("DRIVERUSERS").child(driverIDT).child("rating").addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                if (dataSnapshot.exists()) {
//                                                    int oneStarPerson = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("1").getValue(String.class)));
//                                                    int one = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("1").getValue(String.class)));
//                                                    int twoStarPerson = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("2").getValue(String.class)));
//                                                    int two = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("2").getValue(String.class))) * 2;
//                                                    int threeStarPerson = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("3").getValue(String.class)));
//                                                    int three = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("3").getValue(String.class))) * 3;
//                                                    int fourStarPerson = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("4").getValue(String.class)));
//                                                    int four = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("4").getValue(String.class))) * 4;
//                                                    int fiveStarPerson = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("5").getValue(String.class)));
//                                                    int five = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("5").getValue(String.class))) * 5;
//
//                                                    double totalRating = one + two + three + four + five;
//                                                    double totalRatingPerson = oneStarPerson + twoStarPerson + threeStarPerson + fourStarPerson + fiveStarPerson;
//
//                                                    double avgRating = totalRating / totalRatingPerson;
//                                                    String avg = String.format("%.2f", avgRating);
//                                                    String newString = avg.replace(",", ".");
//                                                    if (newString.equals("")) {
//                                                        iv_total_rating_number.setText(newString);
//                                                    } else
//                                                        iv_total_rating_number.setText(newString);
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                iv_total_rating_number.setText("4.0");
//                                            }
//                                        });
//
//                                        FirebaseDatabase.getInstance().getReference("DRIVERUSERS").child(driverIDT).child("CARS").orderByChild("selected").equalTo("1").addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                if (dataSnapshot.exists()) {
//                                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
//                                                        driverCarName = data.child("name").getValue(String.class);
//                                                        driverCarDescription = data.child("description").getValue(String.class);
//                                                    }
//                                                } else {
//                                                    driverCarName = "Renault Clio 4(Rouge)";
//                                                    driverCarDescription = "1359 A 4";
//                                                }
//
//                                                FirebaseDatabase.getInstance().getReference("clientUSERS").child(clientIdT).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                        userLevel = dataSnapshot.child("level").getValue(String.class);
//                                                        handleCourseCallBack();
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                    }
//                                                });
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                            }
//                                        });
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                }
//                            });
//                        }
//                    } catch (NumberFormatException e) {
//                        e.printStackTrace();
//                    } catch (NullPointerException e) {
//                        e.printStackTrace();
//                    }
//                } else {
////                        statusT = "4";
////                        handleCourseCallBack();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//
//    }

    public void punishment(final String userId) {

        FirebaseDatabase.getInstance().getReference("clientUSERS").
                child(userId).child("SOLDE").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    double oldWallet = Double.parseDouble(Objects.requireNonNull(dataSnapshot.getValue(String.class)));
                    double punishment = oldWallet - 10;
                    FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).child("SOLDE").setValue("" + punishment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
