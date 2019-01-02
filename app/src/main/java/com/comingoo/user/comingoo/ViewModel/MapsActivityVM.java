package com.comingoo.user.comingoo.ViewModel;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.comingoo.user.comingoo.R;
import com.comingoo.user.comingoo.activity.MapsActivity;
import com.comingoo.user.comingoo.interfaces.CourseCallBack;
import com.comingoo.user.comingoo.interfaces.FinishedCourseTaskCallback;
import com.comingoo.user.comingoo.interfaces.PricaCallback;
import com.comingoo.user.comingoo.interfaces.PromoCodeCallback;
import com.comingoo.user.comingoo.interfaces.SendRequestsTaskCallback;
import com.comingoo.user.comingoo.interfaces.TaskCallback;
import com.comingoo.user.comingoo.model.Place;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private Place homePlace;
    private Place workPlace;
    private int driverSize;
    private Runnable runnable;
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
                                            callback.onFinishedCourseTask(driverId, finalPriceOfCourse, course);
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

    public void getRating(final String userId, final String dialogDriverId, final int RATE) {
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

    public Place getFevouritePlaceWorkData(String userId) {
        FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).child("favouritePlace").child("Work").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String address = dataSnapshot.child("Address").getValue(String.class);
                    String lat = dataSnapshot.child("Lat").getValue(String.class);
                    String Long = dataSnapshot.child("Long").getValue(String.class);
                    workPlace = new Place(address, address, lat, Long, R.drawable.mdaison_con);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return workPlace;
    }

    public Place getFevouritePlaceHomeData(String userId) {
        FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).child("favouritePlace").child("Home").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String homeAddress = dataSnapshot.child("Address").getValue(String.class);
                    String homeLat = dataSnapshot.child("Lat").getValue(String.class);
                    String homeLong = dataSnapshot.child("Long").getValue(String.class);

                    homePlace = new Place(homeAddress, homeAddress, homeLat, homeLong, R.drawable.work_icon);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return homePlace;
    }

    public void sendRequestsTask(final String userId, final ArrayList<String> driversKeys,
                                 final ArrayList<String> driversLocations, final ArrayList<String> driversKeysHold,
                                 final String clientID, final String searchEditText,
                                 final String searchDestEditText,
                                 final String userPromoCode,
                                 final LatLng startLatLng,
                                 final LatLng destLatLng, final SendRequestsTaskCallback callback) {
        final int Step = 3; //Number Of Drivers To Call Every Time
        driverSize = driversKeys.size();

        final Handler handler;


        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            int counter = 0;

            @Override
            public void run() {
                if (counter == 0) {
                    driversKeysHold.clear();
                    for (int j = counter; j < (counter + Step) && j < driversKeys.size(); j++) {
                        if (driversKeys.get(j) != null) {
                            final DatabaseReference pickupRequest =
                                    FirebaseDatabase.getInstance().getReference("PICKUPREQUEST").
                                            child(driversKeys.get(j)).child(Objects.requireNonNull(userId));

                            Map<String, String> data = new HashMap<>();
                            data.put("client", clientID);
                            data.put("start", searchEditText);
                            data.put("arrival", searchDestEditText);


                            data.put("destFix", "0");
                            data.put("fixedPrice", "");

                            data.put("startLat", "" + startLatLng.latitude);
                            data.put("startLong", "" + startLatLng.longitude);
                            if (destLatLng != null) {
                                data.put("endLat", "" + destLatLng.latitude);
                                data.put("endLong", "" + destLatLng.longitude);
                            } else {
                                data.put("endLat", "");
                                data.put("endLong", "");
                            }

                            data.put("distance", driversLocations.get(j));
                            data.put("Refused", "0");

//                                if (userPromoCode.equals(""))
                            data.put("PROMOCODE", userPromoCode);


                            pickupRequest.setValue(data);

                            driversKeysHold.add(driversKeys.get(j));

                            pickupRequest.onDisconnect().removeValue();


                            pickupRequest.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        driverSize -= 1;
                                        counter += Step;
                                        if (driverSize == 0) {

                                            counter = 0;
                                            pickupRequest.removeEventListener(this);

                                            FirebaseDatabase.getInstance().getReference("COURSES").orderByChild("client").equalTo(userId).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (!dataSnapshot.exists()) {
                                                        counter = 0;
                                                        handler.removeCallbacks(runnable);
                                                        callback.onPickUpRequest(true);

                                                    } else {

                                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                            FirebaseDatabase.getInstance().getReference("DRIVERUSERS").child(Objects.requireNonNull(data.child("driver").getValue(String.class))).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    if (dataSnapshot.exists()) {
                                                                        callback.onPickUpRequest(false);

//                                                                            rlCallLayout.setVisibility(View.VISIBLE);
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                }
                                                            });
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });
                                        }
                                        if (counter <= driversKeys.size())
                                            handler.postDelayed(runnable, 0);
                                        pickupRequest.removeEventListener(this);
                                        callback.normalRequest();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                } else {
                    if (counter < driversKeys.size()) {
                        final DatabaseReference pickupRequest = FirebaseDatabase.getInstance().getReference("PICKUPREQUEST").child(driversKeys.get(counter)).child(userId);
                        String level = FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).child("level").toString();
                        Map<String, String> data = new HashMap<>();
                        data.put("client", clientID);
                        data.put("start", searchEditText);
                        data.put("arrival", searchDestEditText);
                        data.put("startLat", "" + startLatLng.latitude);
                        data.put("startLong", "" + startLatLng.longitude);
                        data.put("destFix", "0");
                        data.put("fixedPrice", "");
                        if (destLatLng != null) {
                            data.put("endLat", "" + destLatLng.latitude);
                            data.put("endLong", "" + destLatLng.longitude);
                        } else {
                            data.put("endLat", "");
                            data.put("endLong", "");
                        }
                        data.put("distance", driversLocations.get(counter));
                        data.put("Refused", "0");
                        pickupRequest.setValue(data);
                        driversKeysHold.add(driversKeys.get(counter));
                        pickupRequest.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    counter++;
                                    if (counter <= driversKeys.size())
                                        handler.postDelayed(runnable, 0);

                                    pickupRequest.removeEventListener(this);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        return;
                    }
                }

                callback.onSendRequestsTaskCallback(counter, Step);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    public void getPrice(String startCity, String destCity, final Double distance, final PricaCallback callback) {
        if ((startCity.equals("casa") && destCity.equals("casa")) ||
                (startCity.equals("rabat") && destCity.equals("rabat")) || (startCity.equals("sale") && destCity.equals("sale"))
                || (startCity.equals("bouskoura") && destCity.equals("bouskoura")) || (startCity.equals("aeroportCasa") && destCity.equals("aeroportCasa"))
                || (startCity.equals("sidirahal") && destCity.equals("sidirahal"))
                || (startCity.equals("darBouazza") && destCity.equals("darBouazza"))
                || (startCity.equals("marrakech") && destCity.equals("marrakech"))
                || (startCity.equals("jadida") && destCity.equals("jadida"))) {

            FirebaseDatabase.getInstance().getReference("PRICES").
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            double price1 = Math.ceil((distance) * Double.parseDouble(Objects.requireNonNull(dataSnapshot.child("km").getValue(String.class))));
                            if (price1 < Double.parseDouble(Objects.requireNonNull(dataSnapshot.child("minimum").getValue(String.class))))
                                price1 = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("minimum").getValue(String.class)));
                            callback.ongetPrice(price1);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        } else {
            FirebaseDatabase.getInstance().getReference("FIXEDDESTS").
                    child(startCity).child("destinations").child(destCity).
                    child("price").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        callback.ongetPrice(Double.parseDouble(dataSnapshot.getValue(String.class)));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        }
    }

    public void getPromoCode(final String etPromoCode, final String userId, final Dialog dialog, final PromoCodeCallback callback) {
        FirebaseDatabase.getInstance().getReference("CLIENTNOTIFICATIONS").
                child("qsjkldjqld").child("code").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (Objects.requireNonNull(dataSnapshot.getValue()).equals(etPromoCode)) {

                    FirebaseDatabase.getInstance().getReference("CLIENTNOTIFICATIONS").
                            child("qsjkldjqld").child("value").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                FirebaseDatabase.getInstance().getReference("clientUSERS").
                                        child(userId).child("PROMOCODE").setValue(etPromoCode);

                                callback.onGetPromoCode(true, Objects.requireNonNull(dataSnapshot.getValue()).toString(), etPromoCode);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            dialog.dismiss();
                        }
                    });

                } else {
                    callback.onGetPromoCode(false, "", "");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });

    }
}

