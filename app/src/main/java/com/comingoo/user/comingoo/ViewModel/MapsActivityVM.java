package com.comingoo.user.comingoo.ViewModel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.comingoo.user.comingoo.R;
import com.comingoo.user.comingoo.activity.ComingooAndYouActivity;
import com.comingoo.user.comingoo.activity.LoginActivity;
import com.comingoo.user.comingoo.activity.Maps2Activity;
import com.comingoo.user.comingoo.activity.MapsActivity;
import com.comingoo.user.comingoo.interfaces.CourseCallBack;
import com.comingoo.user.comingoo.interfaces.FinishedCourseTaskCallback;
import com.comingoo.user.comingoo.interfaces.TaskCallback;
import com.comingoo.user.comingoo.model.place;
import com.comingoo.user.comingoo.utility.AnimateConstraint;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivityVM {
    private Context mContext;
    private LatLng driverPosT;
    private LatLng startPositionT;
    private int lastImageWidth;
    private String userName;
    private String courseIDT;
    private String statusT = "4";
    private String clientIdT;
    private String driverIDT;
    private Location driverLocT;
    private Location startLocT;
    private String driverPhone;
    private String driverImage;
    private String userLevel;
    private String startText;
    private String endText;
    private String driverName;
    private String driverCarName;
    private String driverCarDescription;
    private String totalRatingNumber;
    private String course;
    private String TAG = "MapsActivityVM";

    public void checkCourseTask(Context context, final CourseCallBack callBack) {
        SharedPreferences prefs = context.getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
        String userId = prefs.getString("userID", null);
        FirebaseDatabase.getInstance().getReference("COURSES").orderByChild("client").
                equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        for (final DataSnapshot data : dataSnapshot.getChildren()) {
                            try {
                                if (Objects.equals(data.child("driverPosLat").getValue(String.class), "") ||
                                        Objects.equals(data.child("driverPosLong").getValue(String.class), "") ||
                                        data.child("startLat").getValue(String.class).equals("") ||
                                        data.child("startLong").getValue(String.class).equals("")) {
                                    driverPosT = new LatLng(0.0, 0.0);
                                    startPositionT = new LatLng(0.0, 0.0);
                                } else {
                                    driverPosT = new LatLng(Double.parseDouble(Objects.requireNonNull(data.child("driverPosLat").getValue(String.class))),
                                            Double.parseDouble(Objects.requireNonNull(data.child("driverPosLong").getValue(String.class))));
                                    startPositionT = new LatLng(Double.parseDouble(Objects.requireNonNull(data.child("startLat").getValue(String.class))),
                                            Double.parseDouble(Objects.requireNonNull(data.child("startLong").getValue(String.class))));
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            courseIDT = data.getKey();
                            statusT = data.child("state").getValue(String.class);
                            clientIdT = data.child("client").getValue(String.class);
                            driverIDT = data.child("driver").getValue(String.class);


                            driverLocT = new Location("");
                            startLocT = new Location("");


                            startText = data.child("startAddress").getValue(String.class);
                            endText = data.child("endAddress").getValue(String.class);

                            driverLocT.setLatitude(driverPosT.latitude);
                            driverLocT.setLatitude(driverPosT.longitude);

                            startLocT.setLatitude(startPositionT.latitude);
                            startLocT.setLatitude(startPositionT.longitude);

                            FirebaseDatabase.getInstance().getReference("DRIVERUSERS").child(driverIDT).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        driverPhone = dataSnapshot.child("phoneNumber").getValue(String.class);
                                        driverImage = dataSnapshot.child("image").getValue(String.class);
                                        driverName = dataSnapshot.child("fullName").getValue(String.class);


                                        FirebaseDatabase.getInstance().getReference("DRIVERUSERS").child(driverIDT).child("rating").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    int oneStarPerson = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("1").getValue(String.class)));
                                                    int one = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("1").getValue(String.class)));
                                                    int twoStarPerson = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("2").getValue(String.class)));
                                                    int two = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("2").getValue(String.class))) * 2;
                                                    int threeStarPerson = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("3").getValue(String.class)));
                                                    int three = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("3").getValue(String.class))) * 3;
                                                    int fourStarPerson = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("4").getValue(String.class)));
                                                    int four = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("4").getValue(String.class))) * 4;
                                                    int fiveStarPerson = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("5").getValue(String.class)));
                                                    int five = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("5").getValue(String.class))) * 5;

                                                    double totalRating = one + two + three + four + five;
                                                    double totalRatingPerson = oneStarPerson + twoStarPerson + threeStarPerson + fourStarPerson + fiveStarPerson;

                                                    double avgRating = totalRating / totalRatingPerson;
                                                    String avg = String.format("%.2f", avgRating);
                                                    totalRatingNumber = avg.replace(",", ".");

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                totalRatingNumber = "4.0";
                                            }
                                        });

                                        FirebaseDatabase.getInstance().getReference("DRIVERUSERS").child(driverIDT).child("CARS").orderByChild("selected").equalTo("1").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                        driverCarName = data.child("name").getValue(String.class);
                                                        driverCarDescription = data.child("description").getValue(String.class);
                                                    }
                                                } else {
                                                    driverCarName = "Renault Clio 4(Rouge)";
                                                    driverCarDescription = "1359 A 4";
                                                }

                                                FirebaseDatabase.getInstance().getReference("clientUSERS").child(clientIdT).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        userLevel = dataSnapshot.child("level").getValue(String.class);
//                                                        handleCourseCallBack();
                                                        callBack.onCourseCallBack(courseIDT, statusT, clientIdT,
                                                                driverIDT,
                                                                driverLocT,
                                                                startLocT,
                                                                driverPhone,
                                                                driverImage,
                                                                userLevel,
                                                                startText,
                                                                endText,
                                                                driverName,
                                                                driverCarName,
                                                                driverCarDescription,
                                                                totalRatingNumber);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    statusT = "4";
//                    handleCourseCallBack();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void CheckUserTask(final Context context, String clientId, final TaskCallback callback) {
        FirebaseDatabase.getInstance().getReference("clientUSERS").child(clientId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    FirebaseAuth.getInstance().signOut();
                    callback.oncheckTaskCallBack(false, "", "", "", "");
                } else {
                    String userName = dataSnapshot.child("fullName").getValue(String.class);
                    String userImage = "";
                    if (dataSnapshot.child("image").getValue(String.class) != null) {
                        if (Objects.requireNonNull(dataSnapshot.child("image").getValue(String.class)).length() > 0) {
                            userImage = dataSnapshot.child("image").getValue(String.class);
                        }
                    }
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String callNumber = dataSnapshot.child("phoneNumber").getValue(String.class);
                    course = dataSnapshot.child("COURSE").getValue(String.class);

                    callback.oncheckTaskCallBack(true, userName, userImage, email, callNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void checkFinishedCourseTask(final Context context, final String userId, final FinishedCourseTaskCallback callback) {
        FirebaseDatabase.getInstance().getReference("CLIENTFINISHEDCOURSES").child(userId).child(course).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshott) {


                        // finishing promo code
                        FirebaseDatabase.getInstance().getReference("clientUSERS").
                                child(userId).child("PROMOCODE").removeValue();

                        final String driverId = dataSnapshott.child("driver").getValue(String.class);


                        if (courseIDT != null) {
                            FirebaseDatabase.getInstance().getReference("COURSES").child(courseIDT).child("price").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    try {
                                        if (dataSnapshot.getValue(String.class) != null) {
                                            Log.e(TAG, "COURSES value onDataChange: " + dataSnapshot.getValue(String.class));
////
                                            double finalPriceOfCourse = Double.parseDouble(Objects.requireNonNull(dataSnapshot.getValue(String.class)));
                                            callback.onFinishedCourseTask(driverId, finalPriceOfCourse,course);
                                        }
//
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void getRating(final String userId, final String dialogDriverId,final int RATE){
        FirebaseDatabase.getInstance().getReference("DRIVERUSERS").
                child(dialogDriverId).child("rating").child(Integer.toString(RATE))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            int Rating = Integer.parseInt(Objects.requireNonNull(dataSnapshot.
                                    getValue(String.class))) + 1;
                            FirebaseDatabase.getInstance().getReference("DRIVERUSERS").
                                    child(dialogDriverId).child("rating").child(Integer.toString(RATE))
                                    .setValue("" + Rating);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        FirebaseDatabase.getInstance().getReference("clientUSERS")
                .child(userId).child("COURSE").removeValue();
    }


}
