package com.comingoo.user.comingoo;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comingoo.user.comingoo.adapters.FavouritePlaceAdapter;
import com.comingoo.user.comingoo.adapters.MyPlaceAdapter;
import com.comingoo.user.comingoo.others.HttpConnection;
import com.comingoo.user.comingoo.others.PathJSONParser;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivityNew extends FragmentActivity implements OnMapReadyCallback {
    private String TAG = "MapsActivityNew";
    private ImageView ivDrawerToggol;
    private GoogleMap mMap;
    private EditText etSearchStartAddress;
    private EditText etSearchDestination;
    private ImageView ivCurrentLocation;

    private float distance;
    private float density;
    private float dpHeight;
    private float dpWidth;
    int HeightAbsolute;
    private int orderDriverState;
    private boolean courseScreenIsOn = false;

    private TextView tvClosestDriver;
    private TextView tvFrameTime, tvClosestDriverText;

    // Drawer elements
    private ImageView ivProfilePicDrawer;
    private TextView tvNameDrawer;
    private LinearLayout llDrawerHome, llDrawerHistory,
            llDrawerNotifications, llDrawerInvite, llDrawerComingooYou, llDrawerAide;

    private ArrayList<String> driversKeys;
    private ArrayList<String> driversLocations;
    private ArrayList<String> driversKeysHold;

    private GeoQuery geoQuery;

    private LatLng userLatLng;
    private LatLng startLatLng;
    private LatLng destLatLng;

    // Pickup Edittext contents
    private Button btnPickUp;
    private RelativeLayout rlStartPoint, rlEndPoint;
    private ImageView ivWheel;
    private RecyclerView rvFavPlaces, rvRecentPlaces;
    private TextView tvFavPlace, tvRecentPlace;
    private FavouritePlaceAdapter fPlaceAdapter;
    private MyPlaceAdapter rPlaceAdapter;
    private ArrayList<place> placeDataList;
    private ArrayList<place> fPlaceDataList;
    private ArrayList<place> rPlaceDataList;
    private MyPlaceAdapter placeAdapter;

    private LinearLayout llConfirmDestination;
    private Button btnConfirmDestination, btnIgnoreDestination;
    private ImageView ivGoo, ivArraw;
    private View contentPricePromoCode;
    private TextView tvPrice, tvPromoCode, tvMAD;
    private RelativeLayout rlCallLayout;
    private LinearLayout llVoipView;
    private TextView tv_appelle_voip, tv_appelle_telephone;
    private ImageView ivShadow, ivCancelRequest;

    private String userId;


    private String courseIDT;
    private String statusT = "4";
    private String clientIdT;
    private String driverIDT;
    private LatLng driverPosT;
    private LatLng startPositionT;

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
    private boolean finished1 = false;
    private boolean finished2 = false;
    private boolean courseScreenStageZero = false;
    private boolean courseScreenStageOne = false;
    private Marker driverPosMarker;
    private Marker startPositionMarker;
    private boolean blockingTimeOver = true;
    private ImageView ivDriver;

    private TextView driverNameL, iv_total_ride_number, iv_car_number, iv_total_rating_number;
//    LinearLayout voip_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_new);
        initialize();
        action();
    }

    private void initialize() {
        driversKeys = new ArrayList<>();
        driversLocations = new ArrayList<>();
        driversKeysHold = new ArrayList<>();
        placeDataList = new ArrayList<>();
        fPlaceDataList = new ArrayList<>();
        rPlaceDataList = new ArrayList<>();

        llDrawerHome = findViewById(R.id.ll_drawer_home);
        llDrawerInvite = findViewById(R.id.ll_drawer_refer_friend);
        llDrawerNotifications  = findViewById(R.id.ll_drawer_notificatiions);
        llDrawerHistory = findViewById(R.id.ll_drawer_history);
        llDrawerAide = findViewById(R.id.ll_drawer_help);
        llDrawerComingooYou = findViewById(R.id.ll_drawer_comingoo_you);

        ivShadow = findViewById(R.id.iv_shadow);

        ivDrawerToggol = findViewById(R.id.iv_menu_maps);
        ivCurrentLocation = findViewById(R.id.iv_current_location);
        etSearchStartAddress = findViewById(R.id.et_start_point);
        etSearchDestination = findViewById(R.id.et_end_point);
        btnConfirmDestination = findViewById(R.id.btn_destination);
        ivArraw = findViewById(R.id.iv_arrow_start_end);
        ivCancelRequest = findViewById(R.id.iv_cancel_request);

        ivProfilePicDrawer = findViewById(R.id.iv_user_image_drawer);
        tvNameDrawer = findViewById(R.id.tv_user_name_drawer);

        tvClosestDriver = findViewById(R.id.tv_closest_driver);
        tvFrameTime = findViewById(R.id.tv_closest_driverPin);
        driverNameL = (TextView) findViewById(R.id.tv_driver_name);
        iv_total_rating_number = (TextView) findViewById(R.id.iv_total_rating_number);
        ivDriver = findViewById(R.id.iv_driver_image);
        iv_car_number = (TextView) findViewById(R.id.iv_car_number);
        iv_total_ride_number = (TextView) findViewById(R.id.iv_total_ride_number);

        tv_appelle_voip = (TextView) findViewById(R.id.tv_appelle_voip);
        tv_appelle_telephone = (TextView) findViewById(R.id.tv_appelle_telephone);

        btnPickUp = findViewById(R.id.btn_pickup);
        rlStartPoint = findViewById(R.id.rl_start_point);
        rlEndPoint = findViewById(R.id.rl_end_point);
        ivWheel = findViewById(R.id.iv_wheel);

        rlCallLayout = findViewById(R.id.rl_call_layout);
        llVoipView = findViewById(R.id.ll_voip_view);

        llConfirmDestination = findViewById(R.id.ll_destination);
        btnIgnoreDestination = findViewById(R.id.btn_destination_ignore);

        rvFavPlaces = findViewById(R.id.rv_fav_place);
        rvRecentPlaces = findViewById(R.id.rv_recent_places);
        tvFavPlace = findViewById(R.id.tv_fav_pace);
        tvRecentPlace = findViewById(R.id.tv_recent_place);

        ivGoo = findViewById(R.id.iv_goo);
        contentPricePromoCode = findViewById(R.id.content_price_promo_code);
        tvPromoCode = findViewById(R.id.tv_promo_code);
        tvPrice = findViewById(R.id.tv_mad);
        tvMAD = findViewById(R.id.tv_mad);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_maps_activity);
        mapFragment.getMapAsync(this);
    }

    private void action() {
        SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
        userId = prefs.getString("userID", null);

        // Getting display resoulation and toggle drawer
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        density = getResources().getDisplayMetrics().density;
        dpHeight = outMetrics.heightPixels / density;
        dpWidth = outMetrics.widthPixels / density;
        HeightAbsolute = (int) dpHeight - (200);
        ivDrawerToggol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View contentMap = findViewById(R.id.content_map_view);
                ConstraintLayout contentBlocker = findViewById(R.id.rl_block_content);
                AnimateConstraint.resideAnimation(getApplicationContext(), contentMap, contentBlocker, (int) dpWidth, (int) dpHeight, 200);

            }
        });
        ////////////// Drawer toggling end //////////////////

        // Checking Location Permission
        if (ContextCompat.checkSelfPermission(MapsActivityNew.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivityNew.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        ivCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userLatLng != null) {
                    getLastLocation();
                }
            }
        });

        btnPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (startPositionIsValid()) {
                    orderDriverState = 1;
                    showSelectDestUI();
                    ivWheel.setVisibility(View.GONE);
                    state = 1;
//                }
            }
        });

        btnConfirmDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (destPositionIsValid()) {
                switchToCommandLayout();
//                }
            }
        });

        etSearchStartAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = 1;
                AnimateConstraint.animate(MapsActivityNew.this,
                        rlStartPoint, (dpHeight - 80), 100, 0);
                ivWheel.setVisibility(View.GONE);
                btnPickUp.setVisibility(View.GONE);
                tvFavPlace.setVisibility(View.VISIBLE);
                tvRecentPlace.setVisibility(View.VISIBLE);
                rvFavPlaces.setVisibility(View.VISIBLE);
                rvRecentPlaces.setVisibility(View.VISIBLE);

                rlEndPoint.setVisibility(View.VISIBLE);
                AnimateConstraint.animate(getApplicationContext(),
                        rlEndPoint, 150, 1, 500);
            }
        });

        btnIgnoreDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destLatLng = null;
                switchToCommandLayout();
            }
        });

        llDrawerHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivityNew.this, historiqueActivity.class));
            }
        });

        llDrawerInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivityNew.this, inviteActivity.class));
            }
        });

        llDrawerNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivityNew.this, notificationActivity.class));
            }
        });
        llDrawerAide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivityNew.this, aideActivity.class));
            }
        });

        tvPromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog(MapsActivityNew.this);
            }
        });

        rvFavPlaces.setHasFixedSize(true);
        rvFavPlaces.setLayoutManager(new LinearLayoutManager(this));

        rvRecentPlaces.setHasFixedSize(true);
        rvRecentPlaces.setLayoutManager(new LinearLayoutManager(this));

        placeAdapter = new MyPlaceAdapter(getApplicationContext(), placeDataList, false, userId);
        rvRecentPlaces.setAdapter(placeAdapter);

        fPlaceAdapter = new FavouritePlaceAdapter(getApplicationContext(), fPlaceDataList, true, userId);
        rvFavPlaces.setAdapter(fPlaceAdapter);

        try {
            new CheckUserTask().execute();
            new checkFinishedCourse().execute();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ivShadow.setImageBitmap(scaleBitmap((int) dpWidth, 80, R.drawable.shadow_bottom));
    }

    private String userName;

    private class CheckUserTask extends AsyncTask<String, Integer, String> {
        SharedPreferences prefs;
        String userId;
        String image;

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
            userId = prefs.getString("userID", null);
            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {


            if (Looper.myLooper() == null) {
                Looper.prepare();
                Looper.myLooper();
            }
//            clientID = userId;
            if (userId == null) {
                userId = "123";
            }


            FirebaseDatabase.getInstance().getReference("clientUSERS").
                    child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        prefs.edit().remove("userID");
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MapsActivityNew.this, loginActivity.class);
                        startActivity(intent);
//                        finish();
                        return;
                    } else {
                        userName = dataSnapshot.child("fullName").getValue(String.class);
                        String userImage = dataSnapshot.child("image").getValue(String.class);
                        if (userImage != null) {
                            if (userImage.length() > 0) {
                                Picasso.get().load(userImage).centerCrop().fit().into(ivProfilePicDrawer);
                            }
                        }
                        tvNameDrawer.setText(userName);

                        llDrawerComingooYou.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MapsActivityNew.this, comingoonuActivity.class);
                                intent.putExtra("image", dataSnapshot.child("image").getValue(String.class));
                                intent.putExtra("name", dataSnapshot.child("fullName").getValue(String.class));
                                intent.putExtra("email", dataSnapshot.child("email").getValue(String.class));
                                String callNumber = dataSnapshot.child("phoneNumber").getValue(String.class);
                                if (callNumber.contains("+212")) {
                                    callNumber = callNumber.replace("+212", "");
                                }
                                intent.putExtra("phone", callNumber);
                                startActivity(intent);
                            }
                        });

                        AnimateConstraint.fadeOut(getApplicationContext(), findViewById(R.id.rl_loading_comingoo_logo),
                                500, 10);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                findViewById(R.id.rl_loading_comingoo_logo).setVisibility(View.GONE);
                            }
                        }, 500);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            return "";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    int RATE = 0;
    String tagStatus;
    private String COURSE;
    private String choseBox;
    private String dialogDriverId;

    private class checkFinishedCourse extends AsyncTask<String, Integer, String> {
        SharedPreferences prefs;
        String userId;

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
            userId = prefs.getString("userID", null);
            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {

            FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).child("COURSE").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        COURSE = dataSnapshot.getValue(String.class);
                        FirebaseDatabase.getInstance().getReference("CLIENTFINISHEDCOURSES").child(userId).child(dataSnapshot.getValue(String.class)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshott) {
                                rlCallLayout.setVisibility(View.GONE);
                                llVoipView.setVisibility(View.GONE);
                                final Dialog dialog = new Dialog(getApplicationContext());
                                dialogDriverId = dataSnapshott.child("driver").getValue(String.class);
                                dialog.setContentView(R.layout.finished_course);


                                TextView textView13 = (TextView) dialog.findViewById(R.id.textView13);
                                TextView textView14 = (TextView) dialog.findViewById(R.id.textView14);


                                //Set Texts
                                textView13.setText(getResources().getString(R.string.Montanttotalàpayer));
                                textView14.setText(getResources().getString(R.string.Evaluezvotreéxperience));


                                RelativeLayout body = (RelativeLayout) dialog.findViewById(R.id.body);
                                body.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) dpWidth, (int) dpWidth, R.drawable.finished_bg)));

                                Button dialogButton = (Button) dialog.findViewById(R.id.button);
                                Button price = (Button) dialog.findViewById(R.id.button3);
                                price.setText(dataSnapshott.child("price").getValue(String.class) + " MAD");

                                final Button star1 = (Button) dialog.findViewById(R.id.star1);
                                final Button star2 = (Button) dialog.findViewById(R.id.star2);
                                final Button star3 = (Button) dialog.findViewById(R.id.star3);
                                final Button star4 = (Button) dialog.findViewById(R.id.star4);
                                final Button star5 = (Button) dialog.findViewById(R.id.star5);

                                final ImageButton im1 = (ImageButton) dialog.findViewById(R.id.imageView4);
                                final ImageButton im2 = (ImageButton) dialog.findViewById(R.id.imageView5);
                                final ImageButton im3 = (ImageButton) dialog.findViewById(R.id.imageView6);
                                final ImageButton im4 = (ImageButton) dialog.findViewById(R.id.imageView7);
                                final ImageButton im5 = (ImageButton) dialog.findViewById(R.id.imageView8);

                                final ImageButton nextButton = (ImageButton) dialog.findViewById(R.id.next);

                                final ImageView imot = (ImageView) dialog.findViewById(R.id.stars_rating);

                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);

                                // defaul rate
                                RATE = 4;
                                star1.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.normal_star)));
                                star2.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.normal_star)));
                                star3.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.normal_star)));
                                star4.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.selected_star)));
                                star5.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.unselected_star)));
                                imot.setImageBitmap(scaleBitmap(150, 150, R.drawable.four_stars));


                                im1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        im1.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.belle_voiture)));
                                        im2.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.bonne_music_unselected)));
                                        im3.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.exellent_service_unselected)));
                                        im4.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.expert_en_navigation_unselected)));
                                        im5.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.voiture_propre_unselected)));
                                        tagStatus = "bonneVoirture";

                                    }
                                });
                                im2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        im1.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.belle_voiture)));
                                        im2.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.bonne_music)));
                                        im3.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.exellent_service_unselected)));
                                        im4.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.expert_en_navigation_unselected)));
                                        im5.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.voiture_propre_unselected)));
                                        tagStatus = "bonneMusic";

                                    }
                                });
                                im3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        im1.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.belle_voiture)));
                                        im2.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.bonne_music_unselected)));
                                        im3.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.exellent_service)));
                                        im4.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.expert_en_navigation_unselected)));
                                        im5.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.voiture_propre_unselected)));
                                        tagStatus = "exellentService";
                                    }
                                });
                                im4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        im1.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.belle_voiture)));
                                        im2.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.bonne_music_unselected)));
                                        im3.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.exellent_service_unselected)));
                                        im4.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.expert_en_navigation)));
                                        im5.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.voiture_propre_unselected)));
                                        tagStatus = "expertNavigation";
                                    }
                                });
                                im5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        im1.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.belle_voiture)));
                                        im2.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.bonne_music_unselected)));
                                        im3.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.exellent_service_unselected)));
                                        im4.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.expert_en_navigation_unselected)));
                                        im5.setBackground(new BitmapDrawable(getResources(), scaleBitmap(100, 100, R.drawable.voiture_propre)));
                                        tagStatus = "voiturePropre";
                                    }
                                });

                                star1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        RATE = 1;

                                        star1.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.selected_star)));
                                        star2.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.unselected_star)));
                                        star3.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.unselected_star)));
                                        star4.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.unselected_star)));
                                        star5.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.unselected_star)));

                                        imot.setImageBitmap(scaleBitmap(150, 150, R.drawable.one_star));
                                    }
                                });
                                star2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        RATE = 2;

                                        star1.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.normal_star)));
                                        star2.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.selected_star)));
                                        star3.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.unselected_star)));
                                        star4.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.unselected_star)));
                                        star5.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.unselected_star)));

                                        imot.setImageBitmap(scaleBitmap(150, 150, R.drawable.two_stars));
                                    }
                                });
                                star3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        RATE = 3;

                                        star1.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.normal_star)));
                                        star2.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.normal_star)));
                                        star3.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.selected_star)));
                                        star4.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.unselected_star)));
                                        star5.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.unselected_star)));

                                        imot.setImageBitmap(scaleBitmap(150, 150, R.drawable.three_stars));
                                    }
                                });
                                star4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        RATE = 4;

                                        star1.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.normal_star)));
                                        star2.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.normal_star)));
                                        star3.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.normal_star)));
                                        star4.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.selected_star)));
                                        star5.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.unselected_star)));

                                        imot.setImageBitmap(scaleBitmap(150, 150, R.drawable.four_stars));
                                    }
                                });

                                star5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        RATE = 5;
                                        star1.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.normal_star)));
                                        star2.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.normal_star)));
                                        star3.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.normal_star)));
                                        star4.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.normal_star)));
                                        star5.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.selected_star)));

                                        imot.setImageBitmap(scaleBitmap(150, 150, R.drawable.five_stars));
                                    }
                                });


                                nextButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (RATE > 0) {
                                            dialog.dismiss();
                                            FirebaseDatabase.getInstance().getReference("DRIVERUSERS").child(dialogDriverId).child("rating").child(Integer.toString(RATE)).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    int Rating = Integer.parseInt(dataSnapshot.getValue(String.class)) + 1;
                                                    FirebaseDatabase.getInstance().getReference("DRIVERUSERS").child(dialogDriverId).child("rating").child(Integer.toString(RATE)).setValue("" + Rating);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                            FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).child("COURSE").removeValue();
                                            if (RATE > 3) {
                                                if (ContextCompat.checkSelfPermission(MapsActivityNew.this,
                                                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                                                        ContextCompat.checkSelfPermission(MapsActivityNew.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(MapsActivityNew.this,
                                                            new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
                                                } else {
                                                    showVoiceDialog();
                                                }
                                            } else {

                                                try {
                                                    final Dialog newDialog = new Dialog(getApplicationContext());
                                                    newDialog.setContentView(R.layout.finished_course_2);
                                                    choseBox = null;

                                                    TextView textView15 = (TextView) dialog.findViewById(R.id.textView15);
                                                    TextView textView16 = (TextView) dialog.findViewById(R.id.textView16);
                                                    Button button5 = (Button) dialog.findViewById(R.id.button5);
                                                    Button button7 = (Button) dialog.findViewById(R.id.button7);
                                                    Button button8 = (Button) dialog.findViewById(R.id.button8);
                                                    Button button9 = (Button) dialog.findViewById(R.id.button9);
                                                    Button button10 = (Button) dialog.findViewById(R.id.button10);


                                                    //Set Texts
                                                    textView15.setText(getResources().getString(R.string.Noussommesdésolé));
                                                    textView16.setText(getResources().getString(R.string.whatswrong));
                                                    button5.setText(getResources().getString(R.string.Heuredarrivée));
                                                    button7.setText(getResources().getString(R.string.Etatdelavoiture));
                                                    button8.setText(getResources().getString(R.string.Conduite));
                                                    button9.setText(getResources().getString(R.string.Itinéraire));
                                                    button10.setText(getResources().getString(R.string.Autre));

                                                    RelativeLayout body = (RelativeLayout) newDialog.findViewById(R.id.body);
                                                    body.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) dpWidth, (int) dpWidth, R.drawable.finished_bg)));

                                                    final Button opt1 = (Button) newDialog.findViewById(R.id.button5);
                                                    final Button opt2 = (Button) newDialog.findViewById(R.id.button6);
                                                    final Button opt3 = (Button) newDialog.findViewById(R.id.button7);
                                                    final Button opt4 = (Button) newDialog.findViewById(R.id.button8);
                                                    final Button opt5 = (Button) newDialog.findViewById(R.id.button9);
                                                    final Button opt6 = (Button) newDialog.findViewById(R.id.button10);

                                                    final EditText messageText = (EditText) newDialog.findViewById(R.id.editText);

                                                    opt1.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            opt1.setBackgroundResource(R.drawable.box_shadow);
                                                            opt2.setBackgroundResource(R.drawable.select_box);
                                                            opt3.setBackgroundResource(R.drawable.select_box);
                                                            opt4.setBackgroundResource(R.drawable.select_box);
                                                            opt5.setBackgroundResource(R.drawable.select_box);
                                                            opt6.setBackgroundResource(R.drawable.select_box);

                                                            choseBox = "Heure d'arrivée";
                                                        }
                                                    });
                                                    opt2.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            opt1.setBackgroundResource(R.drawable.select_box);
                                                            opt2.setBackgroundResource(R.drawable.box_shadow);
                                                            opt3.setBackgroundResource(R.drawable.select_box);
                                                            opt4.setBackgroundResource(R.drawable.select_box);
                                                            opt5.setBackgroundResource(R.drawable.select_box);
                                                            opt6.setBackgroundResource(R.drawable.select_box);

                                                            choseBox = "Service";
                                                        }
                                                    });
                                                    opt3.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            opt1.setBackgroundResource(R.drawable.select_box);
                                                            opt2.setBackgroundResource(R.drawable.select_box);
                                                            opt3.setBackgroundResource(R.drawable.box_shadow);
                                                            opt4.setBackgroundResource(R.drawable.select_box);
                                                            opt5.setBackgroundResource(R.drawable.select_box);
                                                            opt6.setBackgroundResource(R.drawable.select_box);

                                                            choseBox = "Etat de la voiture";
                                                        }
                                                    });
                                                    opt4.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            opt1.setBackgroundResource(R.drawable.select_box);
                                                            opt2.setBackgroundResource(R.drawable.select_box);
                                                            opt3.setBackgroundResource(R.drawable.select_box);
                                                            opt4.setBackgroundResource(R.drawable.box_shadow);
                                                            opt5.setBackgroundResource(R.drawable.select_box);
                                                            opt6.setBackgroundResource(R.drawable.select_box);
                                                            choseBox = "Conduite";
                                                        }
                                                    });
                                                    opt5.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            opt1.setBackgroundResource(R.drawable.select_box);
                                                            opt2.setBackgroundResource(R.drawable.select_box);
                                                            opt3.setBackgroundResource(R.drawable.select_box);
                                                            opt4.setBackgroundResource(R.drawable.select_box);
                                                            opt5.setBackgroundResource(R.drawable.box_shadow);
                                                            opt6.setBackgroundResource(R.drawable.select_box);
                                                            choseBox = "Itinéraire";
                                                        }
                                                    });
                                                    opt6.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            opt1.setBackgroundResource(R.drawable.select_box);
                                                            opt2.setBackgroundResource(R.drawable.select_box);
                                                            opt3.setBackgroundResource(R.drawable.select_box);
                                                            opt4.setBackgroundResource(R.drawable.select_box);
                                                            opt5.setBackgroundResource(R.drawable.select_box);
                                                            opt6.setBackgroundResource(R.drawable.box_shadow);
                                                            choseBox = "Autre";
                                                        }
                                                    });


                                                    ImageButton nextBtn = newDialog.findViewById(R.id.imageButton3);


                                                    newDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                        @Override
                                                        public void onDismiss(DialogInterface dialog) {
                                                            if (ContextCompat.checkSelfPermission(MapsActivityNew.this,
                                                                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MapsActivityNew.this,
                                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                                ActivityCompat.requestPermissions(MapsActivityNew.this,
                                                                        new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
                                                            } else {
                                                                showVoiceDialog();
                                                            }
                                                        }
                                                    });
                                                    nextBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            if (choseBox != null) {
                                                                final String message = messageText.getText().toString();
                                                                Map<String, String> data = new HashMap<>();
                                                                data.put("complaint", choseBox);
                                                                data.put("message", message);
                                                                FirebaseDatabase.getInstance().getReference("COURSECOMPLAINT").child(COURSE).setValue(data);
                                                            }
                                                            newDialog.dismiss();
                                                        }
                                                    });

                                                    newDialog.findViewById(R.id.body).getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (dpWidth), getApplicationContext().getResources().getDisplayMetrics());

                                                    WindowManager.LayoutParams lp = newDialog.getWindow().getAttributes();
                                                    lp.dimAmount = 0.5f;
                                                    newDialog.getWindow().setAttributes(lp);
                                                    newDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                                    newDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                                    newDialog.show();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                });


                                dialog.show();

                                dialog.findViewById(R.id.body).getLayoutParams().width = (int)
                                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (dpWidth), getApplicationContext().getResources().getDisplayMetrics());


                                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                                lp.dimAmount = 0.5f;
                                dialog.getWindow().setAttributes(lp);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
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
            rlCallLayout.setVisibility(View.GONE);
        }
    }

    public void showCustomDialog(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.content_promo_code, null, false);
        final EditText etPromoCode = view.findViewById(R.id.et_promo_code);
        Button btnOk = view.findViewById(R.id.btn_ok_promo_code);
        Button btnCancel = view.findViewById(R.id.btn_cancel_promo_code);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etPromoCode.getText().toString().equals("")) {

                    FirebaseDatabase.getInstance().getReference("CLIENTNOTIFICATIONS").
                            child("qsjkldjqld").child("code").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue().equals(etPromoCode.getText().toString())) {

                                FirebaseDatabase.getInstance().getReference("CLIENTNOTIFICATIONS").
                                        child("qsjkldjqld").child("value").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        tvPrice.setText(dataSnapshot.getValue() + " MAD");
                                        tvPromoCode.setText(etPromoCode.getText().toString());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            } else
                                Toast.makeText(getApplicationContext(), getString(R.string.txt_promo_code_expired), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_enter_promo_code), Toast.LENGTH_LONG).show();
            }
        });

        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    boolean audioRecorded = false;
    ImageButton recordButton, playAudio, pauseAudio, deleteAudio;
    String outputeFile;

    private void showVoiceDialog() {
        final Dialog newDialog = new Dialog(getApplicationContext());
        newDialog.setContentView(R.layout.voice_record);

        TextView textView18 = (TextView) newDialog.findViewById(R.id.tv_destination);
        TextView textView19 = (TextView) newDialog.findViewById(R.id.textView19);
        TextView textView20 = (TextView) newDialog.findViewById(R.id.textView20);


        //Set Texts
        textView18.setText(getResources().getString(R.string.Appreciate));
        textView19.setText(getResources().getString(R.string.PS));
        textView20.setText(getResources().getString(R.string.Record));


        ImageButton nextBtn = (ImageButton) newDialog.findViewById(R.id.imageButton6);
        TextView name = (TextView) newDialog.findViewById(R.id.textView17);


        recordButton = (ImageButton) newDialog.findViewById(R.id.recordAudio);
        playAudio = (ImageButton) newDialog.findViewById(R.id.playAudio);
        pauseAudio = (ImageButton) newDialog.findViewById(R.id.pauseAudio);
        deleteAudio = (ImageButton) newDialog.findViewById(R.id.deleteAudio);
        final MediaPlayer mediaPlayer = new MediaPlayer();


        outputeFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        final MediaRecorder myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputeFile);
//        if (ContextCompat.checkSelfPermission(MapsActivity.this,
//                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MapsActivity.this,
//                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
//        } else {

        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            recordButton.setScaleX((float) 1.3);
                            recordButton.setScaleY((float) 1.3);

                            myAudioRecorder.prepare();
                            myAudioRecorder.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        try {
                            audioRecorded = true;
                            recordButton.setScaleX((float) 1);
                            recordButton.setScaleY((float) 1);

                            deleteAudio.setVisibility(View.VISIBLE);
                            deleteAudio.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    newDialog.dismiss();
                                    showVoiceDialog();
                                }
                            });
                            if (myAudioRecorder != null) {
                                myAudioRecorder.stop();
                                myAudioRecorder.release();
                            }
                            recordButton.setVisibility(View.GONE);
                            playAudio.setVisibility(View.VISIBLE);
                            setupPlayAudio(outputeFile, playAudio, pauseAudio, mediaPlayer);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return false;
            }

        });


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                pauseAudio.setVisibility(View.GONE);
                playAudio.setVisibility(View.VISIBLE);
                mediaPlayer.reset();
                setupPlayAudio(outputeFile, playAudio, pauseAudio, mediaPlayer);
            }
        });

        name.setText(userName);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioRecorded) {
                    SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
                    String userId = prefs.getString("userID", null);

                    final StorageReference filepath = FirebaseStorage.getInstance().getReference("audios").child(userId).child(COURSE + ".3gp");
                    filepath.putFile(Uri.fromFile(new File(outputeFile)));
                }
                Toast.makeText(MapsActivityNew.this, "Thank you", Toast.LENGTH_SHORT).show();
                newDialog.dismiss();
            }
        });


        newDialog.findViewById(R.id.body).getLayoutParams().width = (int)
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (dpWidth), getResources().getDisplayMetrics());

        WindowManager.LayoutParams lp = newDialog.getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        newDialog.getWindow().setAttributes(lp);
        newDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        newDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        newDialog.show();
    }

    boolean isPlaying = false;

    private void setupPlayAudio(final String outputeFile,
                                final View playAudio, final View pauseAudio, final MediaPlayer mediaPlayer) {
        playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
                playAudio.setVisibility(View.GONE);
                try {
                    mediaPlayer.setDataSource(outputeFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                pauseAudio.setVisibility(View.VISIBLE);
                pauseAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pauseAudio.setVisibility(View.GONE);
                        playAudio.setVisibility(View.VISIBLE);
                        mediaPlayer.pause();
                        playAudio.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pauseAudio.setVisibility(View.VISIBLE);
                                playAudio.setVisibility(View.GONE);
                                mediaPlayer.start();
                            }
                        });
                    }
                });
            }
        });
    }



    BitmapFactory.Options bOptions;
    int imageHeight;
    int imageWidth;
    int lastImageHeight;
    int lastImageWidth;
    int inSampleSize;

    public Bitmap scaleBitmap(int reqWidth, int reqHeight, int resId) {
        // Raw height and width of image

        bOptions = new BitmapFactory.Options();
        bOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resId, bOptions);
        imageHeight = bOptions.outHeight;
        imageWidth = bOptions.outWidth;

        imageHeight = bOptions.outHeight;
        imageWidth = bOptions.outWidth;
        inSampleSize = 1;

        if (imageHeight > reqHeight || imageWidth > reqWidth) {

            lastImageHeight = imageHeight / 2;
            lastImageWidth = lastImageWidth / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((lastImageHeight / inSampleSize) >= reqHeight
                    && (lastImageWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }


        // First decode with inJustDecodeBounds=true to check dimensions
        bOptions = new BitmapFactory.Options();
        bOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resId, bOptions);

        // Calculate inSampleSize
        bOptions.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        bOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(getResources(), resId, bOptions);
    }

    private void showSelectDestUI() {
        orderDriverState = 1;
        hideSearchAddressStartUI();
        btnPickUp.setVisibility(View.GONE);
        rlEndPoint.setVisibility(View.VISIBLE);


        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth = outMetrics.widthPixels / density;

        AnimateConstraint.animate(getApplicationContext(), rlStartPoint, (dpHeight - 70), 70, 500);
        AnimateConstraint.fadeIn(getApplicationContext(), rlEndPoint, 500, 10);

        etSearchStartAddress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot, 0, 0, 0);

        findViewById(R.id.ic_location_pin_start).setVisibility(View.GONE);
        findViewById(R.id.tv_closest_driver).setVisibility(View.GONE);
        findViewById(R.id.iv_location_pin_dest).setVisibility(View.VISIBLE);

        tvFrameTime.setText(tvClosestDriverText.getText());

        ivDrawerToggol.setVisibility(View.VISIBLE);
        ivDrawerToggol.setImageResource(R.drawable.back_arrow);
        ivDrawerToggol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSelectDestUI();
            }
        });

    }

    public void hideSearchAddressStartUI() {
        placeDataList.clear();
        placeAdapter.notifyDataSetChanged();
        rvFavPlaces.setVisibility(View.INVISIBLE);

        if (rvFavPlaces.getHeight() >= HeightAbsolute)
            AnimateConstraint.animateCollapse(getApplicationContext(), rvFavPlaces, 1, HeightAbsolute, 300);
        rlStartPoint.setVisibility(View.VISIBLE);
    }

    private void switchToCommandLayout() {
        orderDriverState = 2;
        ivCurrentLocation.setVisibility(View.GONE);
        AnimateConstraint.animate(getApplicationContext(), rlEndPoint,
                dpHeight - 130, 100, 500,
                llConfirmDestination, ivArraw);


        AnimateConstraint.fadeIn(MapsActivityNew.this, ivGoo, 500, 10);
        AnimateConstraint.fadeIn(MapsActivityNew.this, contentPricePromoCode, 500, 10);

        rlStartPoint.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (int) (dpHeight - 62), getApplicationContext().getResources().getDisplayMetrics());

        llConfirmDestination.setVisibility(View.GONE);
        etSearchDestination.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot, 0, 0, 0);

        state = 2;

        findViewById(R.id.iv_location_pin_dest).setVisibility(View.GONE);
//        findViewById(R.id.iv_location_pin_start).setVisibility(View.GONE);


        if (destLatLng != null) {
            new DrawRouteTask().execute(startLatLng, destLatLng);
        } else {
            etSearchDestination.setText("Destination non choisi.");
        }

        ivGoo.setVisibility(View.VISIBLE);
        ivShadow.setVisibility(View.VISIBLE);

        ivDrawerToggol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelCommandLayout();
            }
        });
    }


    private void handleCourseCallBack() {
        if (statusT.equals("4")) {
            mMap.clear();
            ivCurrentLocation.setVisibility(View.VISIBLE);
            if (courseScreenIsOn) {
                courseScreenIsOn = false;
                courseScreenStageZero = false;
                courseScreenStageOne = false;
                findViewById(R.id.rl_depart_pin).setVisibility(View.VISIBLE);
                cancelCommandLayout();
                hideSelectDestUI();
            }

            findViewById(R.id.buttonsLayout).setVisibility(View.VISIBLE);
            return;
        }


        // changes

//        stopSearchUI();
        ivShadow.setVisibility(View.VISIBLE);
        ivDrawerToggol.setImageBitmap(scaleBitmap(45, 45, R.drawable.home_icon));
//        ivDrawerToggol.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // mDrawer.openMenu(true);
//                ConstraintLayout contentConstraint = (ConstraintLayout) findViewById(R.id.contentLayout);
//                ConstraintLayout contentBlocker = (ConstraintLayout) findViewById(R.id.rl_block_content);
//                AnimateConstraint.resideAnimation(getApplicationContext(), contentConstraint, contentBlocker, (int) dpWidth, (int) dpHeight, 200);
//            }
//        });


        ivArraw.setVisibility(View.VISIBLE);
        if (!courseScreenIsOn) {
            courseScreenIsOn = true;
            mMap.clear();


//            searchEditText.setEnabled(false);
//            searchDestEditText.setEnabled(false);
//            coverButton.setClickable(false);

            etSearchStartAddress.setText(startText);
            etSearchDestination.setText(endText);

            findViewById(R.id.rl_depart_pin).setVisibility(View.GONE);

//            AnimateConstraint.fadeIn(getApplicationContext(), bottomMenu, 500, 0);
            AnimateConstraint.fadeIn(getApplicationContext(), ivWheel, 500, 0);
            AnimateConstraint.fadeIn(getApplicationContext(), rlCallLayout, 500, 0);
            ivGoo.setVisibility(View.GONE);
            contentPricePromoCode.setVisibility(View.GONE);

            ivCancelRequest.setVisibility(View.GONE);

            etSearchStartAddress.setCompoundDrawables(null, null, null, null);
            etSearchDestination.setCompoundDrawables(null, null, null, null);

            AnimateConstraint.animate(MapsActivityNew.this, rlStartPoint, (dpHeight - 135), 100, 0);
            AnimateConstraint.animate(MapsActivityNew.this, rlEndPoint, dpHeight - 110, 180, 0);

            findViewById(R.id.buttonsLayout).setVisibility(View.GONE);

            driverNameL.setText(driverName);
            iv_car_number.setText(driverCarDescription);
            iv_total_ride_number.setText(driverCarName);
            if (driverImage != null) {
                if (driverImage.length() > 0) {
                    Picasso.get().load(driverImage).into(ivDriver);
                }
            }

            if (ContextCompat.checkSelfPermission(MapsActivityNew.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MapsActivityNew.this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        }


        if (statusT.equals("0") && !courseScreenStageZero) {

            if (!userLevel.equals("2")) {
//                ivCallDriver.setVisibility(View.VISIBLE);
//                ivCallDriver.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        close_button.setVisibility(View.VISIBLE);
//                        ivCallDriver.setVisibility(View.GONE);
//                        llVoipView.setVisibility(View.VISIBLE);
//                    }
//                });

                tv_appelle_telephone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (driverPhone != null) {
                            try {
                                String callNumber = driverPhone;
                                if (callNumber.contains("+212")) {
                                    callNumber = callNumber.replace("+212", "");
                                }

                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + callNumber));
                                startActivity(intent);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                tv_appelle_voip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!driverIDT.isEmpty()) {
                            Intent intent = new Intent(MapsActivityNew.this, VoipCallingActivity.class);
                            intent.putExtra("driverId", driverIDT);
//                            intent.putExtra("clientId", clientID);
                            intent.putExtra("driverName", driverName);
                            intent.putExtra("driverImage", driverImage);
                            startActivity(intent);
                        }
                    }
                });
            }

            courseScreenStageZero = true;
            final Dialog dialog = new Dialog(getApplicationContext());
            dialog.setContentView(R.layout.custom);

            Button dialogButton = (Button) dialog.findViewById(R.id.button);

            TextView textView8 = (TextView) dialog.findViewById(R.id.textView8);
            Button ddd = (Button) dialog.findViewById(R.id.button);


            //Set Texts
            textView8.setText(getResources().getString(R.string.Votrechauffeurestenroute));
            ddd.setText(getResources().getString(R.string.Daccord));


            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();

            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.dimAmount = 0.5f;
            dialog.getWindow().setAttributes(lp);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        }

//        ivCross = findViewById(R.id.iv_cancel_ride);
//
//        if (statusT.equals("0")) {
//            ivCross.setVisibility(View.VISIBLE);
//            ivCross.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    rideCancelDialog();
//                }
//            });
//
//            if (driverPosMarker != null)
//                driverPosMarker.remove();
//
//            if (startPositionMarker != null)
//                startPositionMarker.remove();
//
//
//            frameLayout3.setDrawingCacheEnabled(true);
//            frameLayout3.buildDrawingCache();
//            Bitmap bm = frameLayout3.getDrawingCache();
//            driverPosMarker = mMap.addMarker(new MarkerOptions()
//                    .position(driverPosT)
//                    .icon(BitmapDescriptorFactory.fromBitmap(bm)));
//
//            frameLayout.setDrawingCacheEnabled(true);
//            frameLayout.buildDrawingCache();
//            bm = frameLayout.getDrawingCache();
//            startPositionMarker = mMap.addMarker(new MarkerOptions()
//                    .position(startPositionT)
//                    .icon(BitmapDescriptorFactory.fromBitmap(bm)));
//
//        }
//
//        if (statusT.equals("1") && !courseScreenStageOne) {
//            ivCross.setVisibility(View.GONE);
//            if (!userLevel.equals("2")) {
//                ivCallDriver.setVisibility(View.VISIBLE);
//                ivCallDriver.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        close_button.setVisibility(View.VISIBLE);
//                        ivCallDriver.setVisibility(View.GONE);
//                        voip_view.setVisibility(View.VISIBLE);
//                    }
//                });
//
//                tv_appelle_telephone.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (driverPhone != null) {
//                            try {
//                                String callNumber = driverPhone;
//                                if (callNumber.contains("+212")) {
//                                    callNumber = callNumber.replace("+212", "");
//                                }
//                                Intent intent = new Intent(Intent.ACTION_DIAL);
//                                intent.setData(Uri.parse("tel:" + callNumber));
//                                startActivity(intent);
//                            } catch (NullPointerException e) {
//                                e.printStackTrace();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                });
//
//                tv_appelle_voip.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (!driverIDT.isEmpty()) {
//                            Intent intent = new Intent(MapsActivityNew.this, VoipCallingActivity.class);
//                            intent.putExtra("driverId", driverIDT);
//                            intent.putExtra("clientId", clientID);
//                            intent.putExtra("driverName", driverName);
//                            intent.putExtra("driverImage", driverImage);
//                            startActivity(intent);
//                        }
//                    }
//                });
//            }
//
//            mMap.clear();
//            courseScreenStageOne = true;
//
//            try {
//                final Dialog dialog = new Dialog(context);
//                dialog.setContentView(R.layout.custom2);
//                TextView textView8 = (TextView) dialog.findViewById(R.id.textView8);
//                Button ddd = (Button) dialog.findViewById(R.id.button);
//                //Set Texts
//                textView8.setText(resources.getString(R.string.Votrechauffeurestarrivé));
//                ddd.setText(resources.getString(R.string.Daccord));
//                Button dialogButton = (Button) dialog.findViewById(R.id.button);
//                dialogButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//                lp.dimAmount = 0.5f;
//                dialog.getWindow().setAttributes(lp);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            frameLayout.setDrawingCacheEnabled(true);
//            frameLayout.buildDrawingCache();
//
//
//            CameraPosition cameraPosition = new CameraPosition.Builder()
//                    .target(startPositionT)      // Sets the center of the map to Mountain View
//                    .zoom(17)                   // Sets the zoom
//                    .build();                   // Creates a CameraPosition from the builder
//            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//
//            Bitmap bm = frameLayout3.getDrawingCache();
//            startPositionMarker = mMap.addMarker(new MarkerOptions()
//                    .position(startPositionT)
//                    .icon(BitmapDescriptorFactory.fromBitmap(bm)));
//
//        }
//        if (statusT.equals("2")) {
//            mMap.clear();
//
//            frameLayout3.setDrawingCacheEnabled(true);
//            frameLayout3.buildDrawingCache();
//            Bitmap bm = frameLayout3.getDrawingCache();
//            mMap.addMarker(new MarkerOptions()
//                    .position(driverPosT)
//                    .icon(BitmapDescriptorFactory.fromBitmap(bm)));
//        }
//
//        if (statusT.equals("3")) {
//
//        }
//
//
//        if (statusT.equals("5")) {
//
//        }

    }

    private void cancelCommandLayout() {
        orderDriverState = 1;

        AnimateConstraint.animate(getApplicationContext(), rlEndPoint,
                180, dpHeight - 20, 500, llConfirmDestination,ivArraw);
        ivArraw.setVisibility(View.GONE);

        ivGoo.setVisibility(View.GONE);
        contentPricePromoCode.setVisibility(View.GONE);

        rlStartPoint.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (dpHeight - 42),
                getApplicationContext().getResources().getDisplayMetrics());

        rlStartPoint.setVisibility(View.VISIBLE);
        btnConfirmDestination.setVisibility(View.VISIBLE);
        ivDrawerToggol.setVisibility(View.VISIBLE);

        etSearchDestination.setEnabled(true);

        findViewById(R.id.iv_location_pin_dest).setVisibility(View.VISIBLE);
        mMap.clear();
        showSelectDestUI();
    }

    private class DrawRouteTask extends AsyncTask<LatLng, Integer, String> {
        LatLng start;
        LatLng arrival;
        List<LatLng> thePath;
        LatLng mid;
        LatLngBounds.Builder builder;

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Do something like display a progress bar
            mid = null;
            builder = new LatLngBounds.Builder();
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(LatLng... params) {


            start = params[0];
            arrival = params[1];

            if (start != null && arrival != null)
                mid = new LatLng((start.latitude + arrival.latitude) / 2, (start.longitude + arrival.longitude) / 2);

            //Define list to get all latlng for the route
            thePath = new ArrayList();
            thePath.add(start);

            builder.include(start);

            //Execute Directions API request
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey("AIzaSyDKndcnw3IXjPPsP1gmkFLbeuLDfHXxc4o")
                    .build();
            DirectionsApiRequest req = DirectionsApi.getDirections(context, start.latitude + "," + start.longitude, arrival.latitude + "," + arrival.longitude);
            try {
                DirectionsResult res = req.await();

                //Loop through legs and steps to get encoded polylines of each step
                if (res.routes != null && res.routes.length > 0) {
                    DirectionsRoute route = res.routes[0];

                    if (route.legs != null) {
                        for (int i = 0; i < route.legs.length; i++) {
                            DirectionsLeg leg = route.legs[i];
                            if (leg.steps != null) {
                                for (int j = 0; j < leg.steps.length; j++) {
                                    DirectionsStep step = leg.steps[j];
                                    if (step.steps != null && step.steps.length > 0) {
                                        for (int k = 0; k < step.steps.length; k++) {
                                            DirectionsStep step1 = step.steps[k];
                                            EncodedPolyline points1 = step1.polyline;
                                            if (points1 != null) {
                                                //Decode polyline and add points to list of route coordinates
                                                List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                                for (com.google.maps.model.LatLng coord1 : coords1) {
                                                    thePath.add(new LatLng(coord1.lat, coord1.lng));
                                                    builder.include(new LatLng(coord1.lat, coord1.lng));
                                                }
                                            }
                                        }
                                    } else {
                                        EncodedPolyline points = step.polyline;
                                        if (points != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords = points.decodePath();
                                            for (com.google.maps.model.LatLng coord : coords) {
                                                thePath.add(new LatLng(coord.lat, coord.lng));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return "this string is passed to onPostExecute";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (thePath != null) {
                if (thePath.size() > 0 && orderDriverState == 2) {
                    if (mid != null) {
                   /* mMap.animateCamera(CameraUpdateFactory.newCameraPosition(   new CameraPosition.Builder()
                            .target(mid)      // Sets the center of the map to Mountain View
                            .zoom(15)                   // Sets the zoom
                            .build()));*/

                        thePath.add(arrival);
                        distance = 0;
                        for (int i = 0; i < (thePath.size() - 1); i++) {
                            Location loc1 = new Location("");
                            loc1.setLatitude(thePath.get(i).latitude);
                            loc1.setLongitude(thePath.get(i).longitude);

                            Location loc2 = new Location("");
                            loc2.setLatitude(thePath.get(i + 1).latitude);
                            loc2.setLongitude(thePath.get(i + 1).longitude);

                            distance += loc1.distanceTo(loc2);

                        }
                        distance /= 1000;
//                        getPrice();
                        drawPolyLineOnMap(start, arrival);
                        builder.include(arrival);
                        int padding = 200;
                        LatLngBounds bounds = builder.build();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding)
                                , 1000, new GoogleMap.CancelableCallback() {
                                    @Override
                                    public void onFinish() {
                                        ivArraw.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                    }

                } else {
                    new DrawRouteTask().execute(start, arrival);
                }
            } else {
                new DrawRouteTask().execute(start, arrival);
            }
        }
    }

    private void getPrice() {
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
                            double price1 = Math.ceil((distance) * Double.parseDouble(dataSnapshot.child("km").getValue(String.class)));
                            int price2 = (int) price1;
                            if (price2 < Double.parseDouble(dataSnapshot.child("minimum").getValue(String.class)))
                                price2 = Integer.parseInt(dataSnapshot.child("minimum").getValue(String.class));
                            tvPrice.setText(price2 + " MAD");
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
                    tvPrice.setText(dataSnapshot.getValue(String.class) + " MAD");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    public void drawPolyLineOnMap(LatLng currentLatitude, LatLng currentLongitude) {
        String url = getMapsApiDirectionsUrl(currentLatitude, currentLongitude);
        Log.e(TAG, "drawPolyLineOnMap: " + url);
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);
    }

    private String getMapsApiDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key="
                + "AIzaSyA69yMLMZGzJzaa1pHoNIk9yGYqyhsa_lw" + "&sensor=true";

        return url;
    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new ReadTask.ParserTask().execute(s);
        }

        private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

            @Override
            protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

                JSONObject jObject;
                List<List<HashMap<String, String>>> routes = null;

                try {
                    jObject = new JSONObject(jsonData[0]);
                    PathJSONParser parser = new PathJSONParser();
                    routes = parser.parse(jObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return routes;
            }

            @Override
            protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
                ArrayList<LatLng> points = null;
                PolylineOptions polyLineOptions = null;

                // traversing through routes
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }

                    polyLineOptions.addAll(points);
                    polyLineOptions.color(Color.BLUE);

                }
                if (polyLineOptions == null) {
                    Toast.makeText(getApplicationContext(), "Something went wrong to draw path", Toast.LENGTH_LONG).show();
                } else {
                    mMap.addPolyline(polyLineOptions);
                }
            }
        }

    }

    private String startCity;
    private String destCity;

    //Check Start Position
    private boolean startPositionIsValid() {
        //PolyUtil.containsLocation(position.latitude, position.longitude, casaPoly, true);

        startCity = "casa";

//        if(startLatLng != null)
        if (PolyUtil.containsLocation(startLatLng.latitude, startLatLng.longitude, LocationInitializer.casaPoly(), true) || PolyUtil.containsLocation(startLatLng.latitude, startLatLng.longitude, LocationInitializer.errahmaPoly(), true)) {
            startCity = "casa";
        } else if (PolyUtil.containsLocation(startLatLng.latitude, startLatLng.longitude, LocationInitializer.salePoly(), true)) {
            startCity = "sale";
        } else if (PolyUtil.containsLocation(startLatLng.latitude, startLatLng.longitude, LocationInitializer.aeroportCasaPoly(), true)) {
            startCity = "aeroportCasa";
        } else if (PolyUtil.containsLocation(startLatLng.latitude, startLatLng.longitude, LocationInitializer.bouskouraPoly(), true)) {
            startCity = "bouskoura";
        } else if (PolyUtil.containsLocation(startLatLng.latitude, startLatLng.longitude, LocationInitializer.darBouazzaPoly(), true)) {
            startCity = "darBouazza";
        } else if (PolyUtil.containsLocation(startLatLng.latitude, startLatLng.longitude, LocationInitializer.jadidaPoly(), true)) {
            startCity = "jadida";
        } else if (PolyUtil.containsLocation(startLatLng.latitude, startLatLng.longitude, LocationInitializer.marrakechPoly(), true)) {
            startCity = "marrakech";
        } else if (PolyUtil.containsLocation(startLatLng.latitude, startLatLng.longitude, LocationInitializer.sidiRahalPoly(), true)) {
            startCity = "sidirahal";
        } else if (PolyUtil.containsLocation(startLatLng.latitude, startLatLng.longitude, LocationInitializer.rabatPoly(), true) || PolyUtil.containsLocation(startLatLng.latitude, startLatLng.longitude, LocationInitializer.missingRabatPoly(), true)) {
            startCity = "rabat";
        } else {
            Toast.makeText(getApplicationContext(), "On est seulement disponible sur Casablanca!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    private boolean destPositionIsValid() {
        if (destLatLng == null)
            return false;
        if (PolyUtil.containsLocation(destLatLng.latitude, destLatLng.longitude, LocationInitializer.casaPoly(), true) || PolyUtil.containsLocation(destLatLng.latitude, destLatLng.longitude, LocationInitializer.errahmaPoly(), true)) {
            destCity = "casa";
            return true;
        } else if (PolyUtil.containsLocation(destLatLng.latitude, destLatLng.longitude, LocationInitializer.rabatPoly(), true) || PolyUtil.containsLocation(destLatLng.latitude, destLatLng.longitude, LocationInitializer.missingRabatPoly(), true)) {
            destCity = "rabat";
            return true;
        } else if (PolyUtil.containsLocation(destLatLng.latitude, destLatLng.longitude, LocationInitializer.salePoly(), true)) {
            destCity = "sale";
            return true;
        } else if (PolyUtil.containsLocation(destLatLng.latitude, destLatLng.longitude, LocationInitializer.bouskouraPoly(), true)) {
            destCity = "bouskoura";
            return true;
        } else if (PolyUtil.containsLocation(destLatLng.latitude, destLatLng.longitude, LocationInitializer.aeroportCasaPoly(), true)) {
            destCity = "aeroportCasa";
            return true;
        }
        if (PolyUtil.containsLocation(destLatLng.latitude, destLatLng.longitude, LocationInitializer.sidiRahalPoly(), true)) {
            destCity = "sidiRahal";
            return true;
        } else if (PolyUtil.containsLocation(destLatLng.latitude, destLatLng.longitude, LocationInitializer.darBouazzaPoly(), true)) {
            destCity = "darBouazza";
            return true;
        } else if (PolyUtil.containsLocation(destLatLng.latitude, destLatLng.longitude, LocationInitializer.marrakechPoly(), true)) {
            destCity = "marrakech";
            return true;
        } else if (PolyUtil.containsLocation(destLatLng.latitude, destLatLng.longitude, LocationInitializer.jadidaPoly(), true)) {
            destCity = "jadida";
            return true;
        } else {
            return false;
        }
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.setBuildingsEnabled(false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            getLastLocation();
            if (userLatLng != null)
                startLatLng = userLatLng;
        }

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (orderDriverState == 0) {
                    if (geoQuery != null) {
                        geoQuery.setCenter(new GeoLocation(startLatLng.latitude, startLatLng.longitude));
                    }
                    if (startLatLng == null) {
                        startLatLng = mMap.getCameraPosition().target;
                        new LookForDriverTask().execute();
                    }
                    startLatLng = mMap.getCameraPosition().target;
                    if (!courseScreenIsOn)
                        new ReverseGeocodingTask(MapsActivityNew.this).execute(startLatLng);
                }

                if (orderDriverState == 1) {
                    destLatLng = mMap.getCameraPosition().target;
                    if (!courseScreenIsOn)
                        new ReverseGeocodingTask(MapsActivityNew.this).execute(destLatLng);
                }
            }
        });

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                etSearchStartAddress.setText(getCompleteAddressString(getApplicationContext(),
                        startLatLng.latitude, startLatLng.longitude));
            }
        });

        try {
            new checkCourseTask().execute();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean doubleBackToExitPressedOnce = false;
    private int state = 0;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        if (state == 1) {
            state = 0;
//            hideSelectDestUI();
            AnimateConstraint.animate(getApplicationContext(), etSearchStartAddress, 1, 1, 1);
            btnPickUp.setVisibility(View.VISIBLE);
            ivWheel.setVisibility(View.VISIBLE);
        }

        if (state != 1) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

    }


    private void hideSelectDestUI() {
        orderDriverState = 0;

        ivDrawerToggol.setClickable(true);

        hideSearchAddressStartUI();
        etSearchStartAddress.setVisibility(View.VISIBLE);
//        rlStartPoint.setVisibility(View.VISIBLE);

        etSearchDestination.setVisibility(View.GONE);
//        selectDest.setVisibility(View.GONE);


        etSearchStartAddress.setEnabled(true);

        AnimateConstraint.animate(MapsActivityNew.this, rlStartPoint, 120, (dpHeight - 80), 500);

        findViewById(R.id.ic_location_pin_start).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_closest_driver).setVisibility(View.VISIBLE);
        findViewById(R.id.iv_location_pin_dest).setVisibility(View.GONE);


        ivDrawerToggol.setImageResource(R.drawable.home_icon);
        ivDrawerToggol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mDrawer.openMenu(true);
                View contentMap = findViewById(R.id.content_map_view);
                RelativeLayout contentBlocker = findViewById(R.id.rl_block_content);
                AnimateConstraint.resideAnimation(getApplicationContext(), contentMap, contentBlocker,
                        (int) dpWidth, (int) dpHeight, 200);
            }
        });


        mMap.clear();

    }


    public void showSearchAddressStartUI() {
        ivDrawerToggol.setVisibility(View.VISIBLE);
        state = 0;
        placeDataList.clear();
        placeAdapter.notifyDataSetChanged();
        rlStartPoint.setVisibility(View.VISIBLE);
        etSearchStartAddress.clearFocus();
        etSearchStartAddress.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//        searchDestEditText.clearFocus();

        AnimateConstraint.animate(getApplicationContext(), etSearchStartAddress, 1, 1, 1);

//        if (orderDriverState == 0) {
//            selectStart.setVisibility(View.VISIBLE);
//            bottomMenu.setVisibility(View.VISIBLE);
//            selectedOp.setVisibility(View.VISIBLE);
//            shadowBg.setVisibility(View.VISIBLE);
//        }
//        if (orderDriverState == 1) {
//            selectDest.setVisibility(View.VISIBLE);
//        }
    }

    private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {
        Context mContext;

        public ReverseGeocodingTask(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected String doInBackground(LatLng... params) {
            if (orderDriverState != 0 && orderDriverState != 1)
                return "";
            return getCompleteAddressString(getApplicationContext(), params[0].latitude, params[0].longitude);
        }

        @Override
        protected void onPostExecute(String addressText) {
            if (courseScreenIsOn)
                return;
            if (orderDriverState == 0)
                etSearchStartAddress.setText(addressText);
            if (orderDriverState == 1)
                etSearchDestination.setText(addressText);

        }
    }

    private String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);

        if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        }

//        if (requestCode == 10) {
//            if (grantResult[0] == PackageManager.PERMISSION_GRANTED) {
//                showVoiceDialog();
//            } else {
//            }
//        }
    }

    public void getLastLocation() {
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // GPS location can be null if GPS is switched off
                            if (location != null) {
                                userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                goToLocation(getApplicationContext(), userLatLng.latitude, userLatLng.longitude, null);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    public void goToLocation(Context context, Double lat, Double lng, place rPlace) {

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    private class LookForDriverTask extends AsyncTask<String, Integer, String> {
        String image;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            if (startLatLng != null) {

                if (driversKeys != null) {
                    driversKeys.clear();
                    driversLocations.clear();
                }

                DatabaseReference onlineDrivers = FirebaseDatabase.getInstance().getReference("ONLINEDRIVERS");
                GeoFire geoFire = new GeoFire(onlineDrivers);
                geoQuery = geoFire.queryAtLocation(new GeoLocation(startLatLng.latitude, startLatLng.longitude), 50);

                geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
                    @Override
                    public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {
                        if (dataSnapshot.exists()) {
                            Location loc = new Location("A");
                            loc.setLatitude(location.latitude);
                            loc.setLongitude(location.longitude);
                            Location loca = new Location("B");
                            loca.setLatitude(startLatLng.latitude);
                            loca.setLongitude(startLatLng.longitude);

                            String distance = "" + (loc.distanceTo(loca) / 1000);

                            for (int j = 0; j < driversKeys.size(); j++) {
                                if (Double.parseDouble(driversLocations.get(j)) > Double.parseDouble(distance)) {
                                    driversKeys.add(j, dataSnapshot.getKey());
                                    driversLocations.add(j, distance);
                                    if (j == 0)
                                        afterLook();
                                    return;
                                }
                            }

                            driversLocations.add(distance);
                            driversKeys.add(dataSnapshot.getKey());
                        }
                        afterLook();
                    }

                    @Override
                    public void onDataExited(DataSnapshot dataSnapshot) {
                        int i = getPosition(driversKeys, dataSnapshot.getKey());
                        if (i >= 0) {
                            driversKeys.remove(i);
                            driversLocations.remove(i);
                        }
                        afterLook();
                    }

                    @Override
                    public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {
                        int i = getPosition(driversKeys, dataSnapshot.getKey());
                        if (i >= 0) {
                            driversKeys.remove(i);
                            driversLocations.remove(i);

                            Location loc = new Location("A");
                            loc.setLatitude(location.latitude);
                            loc.setLongitude(location.longitude);
                            Location loca = new Location("B");
                            loca.setLatitude(startLatLng.latitude);
                            loca.setLongitude(startLatLng.longitude);

                            String distance = "" + (loc.distanceTo(loca) / 1000);


                            etSearchStartAddress.setText(getCompleteAddressString(getApplicationContext(), startLatLng.latitude, startLatLng.longitude));


                            for (int j = 0; j < driversKeys.size(); j++) {
                                if (Double.parseDouble(driversLocations.get(j)) > Double.parseDouble(distance)) {
                                    driversKeys.add(j, dataSnapshot.getKey());
                                    driversLocations.add(j, distance);
                                    return;
                                }
                            }
                            driversKeys.add(dataSnapshot.getKey());
                            driversLocations.add(distance);
                            afterLook();
                        }
                    }

                    @Override
                    public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {
                    }

                    @Override
                    public void onGeoQueryReady() {
                        afterLook();
                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {
                    }
                });
            }

            return "this string is passed to onPostExecute";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    private int getPosition(List<String> sList, String element) {
        for (int i = 0; i < sList.size(); i++) {
            if (sList.get(i).equals(element)) return i;
        }
        return -1;
    }

    private void afterLook() {
        if (driversKeys.size() > 0) {
            double distanceKmTime = Math.floor(Double.parseDouble(driversLocations.get(0)));

            if (distanceKmTime >= 10) distanceKmTime -= (distanceKmTime * (distanceKmTime / 100));

            if (!courseScreenIsOn) {
                tvClosestDriver.setText((int) distanceKmTime + "\nmin");
                if (orderDriverState == 1) {
                    if (tvClosestDriver.getText().toString().startsWith("-")) {
                        tvFrameTime.setText(tvClosestDriver.getText().toString().substring(1));
                    } else {
                        tvFrameTime.setText(tvClosestDriver.getText());
                    }
                }
                if (orderDriverState == 2) {
                    if (tvClosestDriver.getText().toString().startsWith("-")) {
                        tvFrameTime.setText(tvClosestDriver.getText().toString().substring(1));
                    } else {
                        tvFrameTime.setText(tvClosestDriver.getText());
                    }
                }
            }
        } else {
            if (!courseScreenIsOn) {

                tvClosestDriver.setText("4\nmin");
                tvFrameTime.setText("4\nMin");
                if (orderDriverState == 1) {
                    tvClosestDriver.setText("4\nmin");
                    tvFrameTime.setText("4\nMin");
                }
                if (orderDriverState == 2) {
                    tvClosestDriver.setText("4\nmin");
                    tvFrameTime.setText("4\nMin");
                }
            }
        }
    }


    private class checkCourseTask extends AsyncTask<String, Integer, String> {
        SharedPreferences prefs;
        String userId;

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
            userId = prefs.getString("userID", null);
        }


        @Override
        protected String doInBackground(String... params) {
            FirebaseDatabase.getInstance().getReference("COURSES").orderByChild("client").
                    equalTo(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        try {
//                            for (final DataSnapshot data : dataSnapshot.getChildren()) {
//                                courseIDT = data.getKey();
//                                statusT = data.child("state").getValue(String.class);
//                                clientIdT = data.child("client").getValue(String.class);
//                                driverIDT = data.child("driver").getValue(String.class);
//                                driverPosT = new LatLng(Double.parseDouble(data.child("driverPosLat").getValue(String.class)),
//                                        Double.parseDouble(data.child("driverPosLong").getValue(String.class)));
//                                startPositionT = new LatLng(Double.parseDouble(data.child("startLat").getValue(String.class)),
//                                        Double.parseDouble(data.child("startLong").getValue(String.class)));
//
//
//                                driverLocT = new Location("");
//                                startLocT = new Location("");
//
//
//                                startText = data.child("startAddress").getValue(String.class);
//                                endText = data.child("endAddress").getValue(String.class);
//
//                                driverLocT.setLatitude(driverPosT.latitude);
//                                driverLocT.setLatitude(driverPosT.longitude);
//
//                                startLocT.setLatitude(startPositionT.latitude);
//                                startLocT.setLatitude(startPositionT.longitude);
//
//
//                                FirebaseDatabase.getInstance().getReference("DRIVERUSERS").child(driverIDT).addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        if (dataSnapshot.exists()) {
//                                            driverPhone = dataSnapshot.child("phoneNumber").getValue(String.class);
//                                            driverImage = dataSnapshot.child("image").getValue(String.class);
//                                            driverName = dataSnapshot.child("fullName").getValue(String.class);
//
//
//                                            FirebaseDatabase.getInstance().getReference("DRIVERUSERS").child(driverIDT).child("rating").addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
//                                                    iv_total_rating_number.setText(newString);
////                                                    int rating = Integer.parseInt(dataSnapshot.getValue(String.class)) + 1;
////                                                    FirebaseDatabase.getInstance().getReference("clientUSERS").child(clientId).child("rating").child(Integer.toString(RATE)).setValue("" + rating);
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                    iv_total_rating_number.setText(4.5 + "");
//                                                }
//                                            });
//
//                                            FirebaseDatabase.getInstance().getReference("DRIVERUSERS").child(driverIDT).child("CARS").orderByChild("selected").equalTo("1").addListenerForSingleValueEvent(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                    if (dataSnapshot.exists()) {
//
//                                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
//                                                            driverCarName = data.child("name").getValue(String.class);
//                                                            driverCarDescription = data.child("description").getValue(String.class);
//                                                        }
//                                                    } else {
//                                                        driverCarName = "Renault Clio 4(Rouge)";
//                                                        driverCarDescription = "1359 A 4";
//                                                    }
//
//                                                    FirebaseDatabase.getInstance().getReference("clientUSERS").child(clientIdT).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                        @Override
//                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                            userLevel = dataSnapshot.child("level").getValue(String.class);
//                                                            handleCourseCallBack();
//
//                                                        }
//
//                                                        @Override
//                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                        }
//                                                    });
//
//                                                }
//
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                }
//                                            });
//
//                                        }
//
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });
//
//
//                            }
//                        } catch (NullPointerException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        statusT = "4";
//                        handleCourseCallBack();
//                    }
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
}
