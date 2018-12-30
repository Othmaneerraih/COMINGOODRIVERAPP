package com.comingoo.user.comingoo.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comingoo.user.comingoo.R;
import com.comingoo.user.comingoo.adapters.FavouritePlaceAdapter;
import com.comingoo.user.comingoo.adapters.MyPlaceAdapter;
import com.comingoo.user.comingoo.async.ReverseGeocodingTask;
import com.comingoo.user.comingoo.interfaces.PickLocation;
import com.comingoo.user.comingoo.model.LocationInitializer;
import com.comingoo.user.comingoo.model.place;
import com.comingoo.user.comingoo.utility.AnimateConstraint;
import com.comingoo.user.comingoo.utility.LocalHelper;
import com.comingoo.user.comingoo.utility.SharedPreferenceTask;
import com.comingoo.user.comingoo.utility.Utility;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class Maps2Activity extends AppCompatActivity implements OnMapReadyCallback, PickLocation {

    private CircleImageView profile_image;
    private TextView drawer_user_name_tv;
    private RelativeLayout acceuil_layout;
    private TextView home_tv;
    private RelativeLayout historique_layout;
    private TextView history_tv;
    private RelativeLayout inbox_layout;
    private TextView inbox_tv;
    private RelativeLayout invite_layout;
    private TextView invite_tv;
    private RelativeLayout comingoonyou_layout;
    private TextView comingoou_tv;
    private RelativeLayout aide_layout;
    private TextView aide_tv;
    private RelativeLayout logout_layout;
    private TextView logout_tv;


    private RelativeLayout contentLayout;
    private RippleBackground rippleBackground;
    private FrameLayout framelayout;
    private ImageView location_start_pin;
    private TextView closestDriverPin;
    private FrameLayout framelayout2;
    private ImageView location_dest_pin;
    private FrameLayout framelayout3;
    private ImageView driver_pin;

    private RelativeLayout pin;
    private ImageView locationPin;
    private ImageView locationPinDest;
    private TextView closestDriver;


    private ImageView shadow_iv;
    private ImageView shadow2_iv;
    private ImageView shadow3_iv;
    private ImageButton my_position;
    private ImageButton x;
    private ImageButton menu_button;


    private RelativeLayout rl_calling;
    private CircularImageView iv_driver_image;
    private TextView tv_driver_name;
    private TextView iv_total_ride_number;
    private TextView iv_car_number;
    private LinearLayout ll_rating;
    private RatingBar rb_user;
    private TextView iv_total_rating_number;
    private ImageView iv_call_driver;
    private ImageView close_button;
    private RelativeLayout voip_view;
    private TextView tv_appelle_telephone;
    private TextView tv_appelle_voip;
    private RatingBar rbDriverRating;

    private ImageView shadow;
    private ImageView selectedOperation;
    private ImageView shadow_bg;
    private RelativeLayout bottomMenu;
    private ImageButton deliveryButton;
    private ImageButton carButton;
    private RelativeLayout start_edit_text;
    private EditText et_start_point;
    private Button coverButton;
    private RelativeLayout dest_edit_text;
    private EditText et_end_point;
    private RelativeLayout aR;
    private RecyclerView my_recycler_view;
    private RelativeLayout favorite;
    private RelativeLayout fR;
    private RecyclerView favorite_recycler;
    private TextView textView;
    private RelativeLayout rR;
    private RecyclerView recent_recycler;
    private TextView textView2;
    private RelativeLayout buttonsLayout;
    private RelativeLayout select_start;
    private Button confirm_start;
    private RelativeLayout select_dest;
    private Button confirm_dest;
    private Button passer;
    private RelativeLayout select_city;
    private TextView city;
    private ImageButton imageButton4;
    private ImageView destArrow;


    private RelativeLayout gooContent;
    private RelativeLayout gooBox;
    private ImageView iv_promo_code;
    private TextView promoCode;
    private RelativeLayout contentBlocker;
    private RelativeLayout loadingScreen;
    private CircleImageView iv_cancel_ride;
    private TextView tv_mad;
    private TextView tv_estimate_price;
    private ImageButton cancelRequest;
    private ImageButton gooButton;

    private CircleImageView selectedOpImage;
    private String language;
    private Context co;
    private RelativeLayout.LayoutParams params;
    private Handler mHandler = new Handler();
    private int mHour, mMinute;
    private Resources resources;
    private LatLng userLatLng;
    private LatLng startLatLng;
    private LatLng destLatLng;
    private String destCity;
    private GoogleMap mMap;
    private MyPlaceAdapter placeAdapter;
    private FavouritePlaceAdapter fPlaceAdapter;
    private MyPlaceAdapter rPlaceAdapter;
    private ArrayList<place> placeDataList;
    private ArrayList<place> fPlaceDataList;
    private ArrayList<place> rPlaceDataList;
    private GeoDataClient mGeoDataClient;
    private String searchLoc;
    private boolean isLoud = false;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private int HeightAbsolute;
    private boolean blockingTimeOver = true;
    private float dpHeight;
    private float dpWidth;
    private boolean courseScreenIsOn = false;
    private boolean courseScreenStageZero = false;
    private boolean courseScreenStageOne = false;
    private Marker driverPosMarker;
    private Marker startPositionMarker;
    private String outputeFile;
    private boolean audioRecorded = false;
    private boolean isFocusableNeeded = true;
    private ArrayList<String> driversKeys;
    private ArrayList<String> driversLocations;
    private ArrayList<String> driversKeysHold;
    private int orderDriverState;
    private GeoQuery geoQuery;
    private ImageButton search_dest_address_button;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");
        co = LocalHelper.setLocale(Maps2Activity.this, language);
        resources = co.getResources();

        if (!Utility.isNetworkConnectionAvailable(Maps2Activity.this)) {
            Utility.checkNetworkConnection(resources, Maps2Activity.this);
        }
        Utility.displayLocationSettingsRequest(Maps2Activity.this, REQUEST_CHECK_SETTINGS);
        SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
        String userId = prefs.getString("userID", null);
        if (userId == null || FirebaseAuth.getInstance().getCurrentUser() == null) {
            prefs.edit().remove("userID");
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Maps2Activity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        initialize();

    }

    private void initialize() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        profile_image = findViewById(R.id.profile_image);
        drawer_user_name_tv = findViewById(R.id.drawer_user_name_tv);
        home_tv = findViewById(R.id.home_tv);
        historique_layout = findViewById(R.id.historique_layout);
        history_tv = findViewById(R.id.history_tv);
        inbox_layout = findViewById(R.id.inbox_layout);
        inbox_tv = findViewById(R.id.inbox_tv);
        invite_tv = findViewById(R.id.invite_tv);
        comingoonyou_layout = findViewById(R.id.comingoonyou_layout);
        comingoou_tv = findViewById(R.id.comingoou_tv);
        aide_layout = findViewById(R.id.aide_layout);
        aide_tv = findViewById(R.id.aide_tv);
        logout_layout = findViewById(R.id.logout_layout);


        contentLayout = findViewById(R.id.contentLayout);
        rippleBackground = findViewById(R.id.gooVoidContent);
        framelayout = findViewById(R.id.framelayout);
        location_start_pin = findViewById(R.id.location_start_pin);
        closestDriverPin = findViewById(R.id.closestDriverPin);
        framelayout2 = findViewById(R.id.framelayout2);
        location_dest_pin = findViewById(R.id.location_dest_pin);
        driver_pin = findViewById(R.id.driver_pin);
        pin = findViewById(R.id.pin);
        locationPin = findViewById(R.id.locationPin);
        locationPinDest = findViewById(R.id.locationPinDest);
        closestDriver = findViewById(R.id.closestDriver);


        shadow_iv = findViewById(R.id.shadow_iv);
        shadow2_iv = findViewById(R.id.shadow2_iv);
        shadow3_iv = findViewById(R.id.shadow3_iv);
        my_position = findViewById(R.id.my_position);
        x = findViewById(R.id.x);
        menu_button = findViewById(R.id.menu_button);

        rl_calling = findViewById(R.id.rl_calling);
        iv_driver_image = findViewById(R.id.iv_driver_image);
        tv_driver_name = findViewById(R.id.tv_driver_name);
        iv_total_ride_number = findViewById(R.id.iv_total_ride_number);
        iv_car_number = findViewById(R.id.iv_car_number);
        ll_rating = findViewById(R.id.ll_rating);
        rb_user = findViewById(R.id.rb_user);
        iv_total_rating_number = findViewById(R.id.iv_total_rating_number);
        iv_call_driver = findViewById(R.id.iv_call_driver);
        close_button = findViewById(R.id.close_button);
        voip_view = findViewById(R.id.voip_view);
        tv_appelle_telephone = findViewById(R.id.tv_appelle_telephone);
        tv_appelle_voip = findViewById(R.id.tv_appelle_voip);
        rbDriverRating = findViewById(R.id.rb_user);

        shadow = findViewById(R.id.shadow);
        selectedOperation = findViewById(R.id.selectedOperation);
        shadow_bg = findViewById(R.id.shadow_bg);
        bottomMenu = findViewById(R.id.bottomMenu);
        deliveryButton = findViewById(R.id.deliveryButton);
        carButton = findViewById(R.id.carButton);
        start_edit_text = findViewById(R.id.start_edit_text);
        et_start_point = findViewById(R.id.et_start_point);
        coverButton = findViewById(R.id.coverButton);
        dest_edit_text = findViewById(R.id.dest_edit_text);
        et_end_point = findViewById(R.id.et_end_point);
        aR = findViewById(R.id.adress_result);
        my_recycler_view = findViewById(R.id.my_recycler_view);
        fR = findViewById(R.id.favorite);
        favorite = findViewById(R.id.favorite_recent);
        favorite_recycler = findViewById(R.id.favorite_recycler);
        textView = findViewById(R.id.textView);
        rR = findViewById(R.id.recent);
        recent_recycler = findViewById(R.id.recent_recycler);
        textView2 = findViewById(R.id.textView2);
        buttonsLayout = findViewById(R.id.buttonsLayout);
        select_start = findViewById(R.id.select_start);
        confirm_start = findViewById(R.id.confirm_start);
        search_dest_address_button = findViewById(R.id.search_dest_address_button);

        select_dest = findViewById(R.id.select_dest);
        confirm_dest = findViewById(R.id.confirm_dest);
        passer = findViewById(R.id.passer);
        select_city = findViewById(R.id.select_city);
        city = findViewById(R.id.city);
        imageButton4 = findViewById(R.id.imageButton4);
        destArrow = findViewById(R.id.destArrow);


        gooContent = findViewById(R.id.gooContent);
        gooBox = findViewById(R.id.gooBox);
        iv_promo_code = findViewById(R.id.iv_promo_code);
        promoCode = findViewById(R.id.promoCode);
        contentBlocker = findViewById(R.id.contentBlocker);
        loadingScreen = findViewById(R.id.loadingScreen);
        iv_cancel_ride = findViewById(R.id.iv_cancel_ride);
        gooButton = findViewById(R.id.gooButton);
        tv_mad = findViewById(R.id.tv_mad);
        tv_estimate_price = findViewById(R.id.tv_estimate_price);
        cancelRequest = findViewById(R.id.cancelRequest);

        searchLoc = "Casablanca";
        driversKeys = new ArrayList<>();
        driversLocations = new ArrayList<>();
        driversKeysHold = new ArrayList<>();
        selectedOpImage = findViewById(R.id.selectedOperation);
    }

    private void action() {
        promoCode.setText(resources.getString(R.string.promocode_txt));
        tv_appelle_voip.setClickable(true);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_button.setVisibility(View.GONE);
                iv_call_driver.setVisibility(View.VISIBLE);
                voip_view.setVisibility(View.GONE);
            }
        });

        SinchClient sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(userId)
                .applicationKey(getResources().getString(R.string.sinch_app_key))
                .applicationSecret(getResources().getString(R.string.sinch_app_secret))
                .environmentHost(getResources().getString(R.string.sinch_envirentmnet_host))
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();

        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());

        userLatLng = null;
        startLatLng = null;
        destLatLng = null;

        mGeoDataClient = Places.getGeoDataClient(this);

        placeDataList = new ArrayList<>();
        fPlaceDataList = new ArrayList<>();
        rPlaceDataList = new ArrayList<>();

        my_recycler_view.setHasFixedSize(true);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this));


        favorite_recycler.setHasFixedSize(true);
        favorite_recycler.setLayoutManager(new LinearLayoutManager(this));


        recent_recycler.setHasFixedSize(true);
        recent_recycler.setLayoutManager(new LinearLayoutManager(this));

        placeAdapter = new MyPlaceAdapter(getApplicationContext(), placeDataList, false, userId, this);
        my_recycler_view.setAdapter(placeAdapter);

        fPlaceAdapter = new FavouritePlaceAdapter(getApplicationContext(), fPlaceDataList, true, userId, this);
        favorite_recycler.setAdapter(fPlaceAdapter);

        rPlaceAdapter = new MyPlaceAdapter(getApplicationContext(), rPlaceDataList, false, userId, this);
        recent_recycler.setAdapter(rPlaceAdapter);

        iv_call_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_button.setVisibility(View.VISIBLE);
                iv_call_driver.setVisibility(View.GONE);
                voip_view.setVisibility(View.VISIBLE);
            }
        });

        historique_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Maps2Activity.this, HistoriqueActivity.class));
            }
        });

        invite_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Maps2Activity.this, InviteActivity.class));
            }
        });

        logout_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.comingoo.com/driver";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        inbox_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Maps2Activity.this, NotificationActivity.class));
            }
        });
        aide_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Maps2Activity.this, AideActivity.class));
            }
        });

        promoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPromoCodeDialog(Maps2Activity.this);
            }
        });

        int fHeight = 170;
        int rHeight = HeightAbsolute - fHeight - 5;

        fR.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, fHeight,
                getResources().getDisplayMetrics());
        rR.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rHeight,
                getResources().getDisplayMetrics());

        updateViews();

        gooButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (blockingTimeOver) {
                    gooButton.setClickable(false);
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(startLatLng)      // Sets the center of the map to Mountain View
                            .zoom(17)                   // Sets the zoom
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    AnimateConstraint.fadeOut(Maps2Activity.this, gooButton, 200, 10);
                    //AnimateConstraint.expandCircleAnimation(context, findViewById(R.id.gooLayout), dpHeight, dpWidth);
                    menu_button.setVisibility(View.VISIBLE);
                    startSearchUI();
                    hideAllUI();
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.inviteText),
                            Toast.LENGTH_LONG).show();
            }
        });

        confirm_dest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_end_point.getText().toString().equals("")) {
                    if (destPositionIsValid()) {
                        switchToCommandLayout();
                    }
                }
            }
        });

        passer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destLatLng = null;
                switchToCommandLayout();
            }
        });

        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout contentConstraint = findViewById(R.id.contentLayout);
                ConstraintLayout contentBlocker = findViewById(R.id.contentBlocker);
                AnimateConstraint.resideAnimation(Maps2Activity.this, contentConstraint, contentBlocker,
                        (int) dpWidth, (int) dpHeight, 200);
            }
        });


        select_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchLoc.equals("Casablanca")) {
                    searchLoc = "Rabat";
                    city.setText(searchLoc);
                } else if (searchLoc.equals("Rabat")) {
                    searchLoc = "";
                    city.setText("autres...");
                } else if (searchLoc.equals("")) {
                    searchLoc = "Casablanca";
                    city.setText(searchLoc);
                } else if (searchLoc.equals("sale")) {
                    searchLoc = "sale";
                    city.setText(searchLoc);
                } else if (searchLoc.equals("bouskoura")) {
                    searchLoc = "bouskoura";
                    city.setText(searchLoc);
                } else if (searchLoc.equals("aeroportCasa")) {
                    searchLoc = "aeroportCasa";
                    city.setText(searchLoc);
                } else if (searchLoc.equals("sidirahal")) {
                    searchLoc = "sidirahal";
                    city.setText(searchLoc);
                } else if (searchLoc.equals("darBouazza")) {
                    searchLoc = "darBouazza";
                    city.setText(searchLoc);
                } else if (searchLoc.equals("marrakech")) {
                    searchLoc = "marrakech";
                    city.setText(searchLoc);
                } else if (searchLoc.equals("jadida")) {
                    searchLoc = "jadida";
                    city.setText(searchLoc);
                }
            }
        });

        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_start_point.clearFocus();
                et_end_point.clearFocus();
                x.setVisibility(View.GONE);
                select_city.setVisibility(View.GONE);
                my_position.setVisibility(View.VISIBLE);
                hideSearchAddressStartUI();
            }
        });

        confirm_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Begin Selecting Destination Phase
                try {
                    if (!et_start_point.getText().toString().equals("")) {
                        if (startPositionIsValid()) {
                            orderDriverState = 1;
                            showSelectDestUI();
                            state = 1;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.my_position).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userLatLng != null) {
                    getLastLocation();
                }
            }
        });

        setSearchFunc();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("clientUSERS").child(clientID);
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
                    FirebaseDatabase.getInstance().getReference("clientUSERS").child(clientID).child("rating").
                            setValue(dataRating);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference rootFavPlace = FirebaseDatabase.getInstance().getReference("clientUSERS").child(clientID);
        rootFavPlace.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("favouritePlace")) {
                } else {
                    Map<String, String> dataFavPlace = new HashMap();
                    dataFavPlace.put(getString(R.string.txt_home), "");
                    dataFavPlace.put(getString(R.string.txt_work), "");
                    FirebaseDatabase.getInstance().getReference
                            ("clientUSERS").child(clientID).child("favouritePlace").setValue(dataFavPlace);

                    Map<String, String> homeSt = new HashMap();
                    homeSt.put("Lat", "");
                    homeSt.put("Long", "");
                    homeSt.put("Address", "");

                    Map<String, String> workSt = new HashMap();
                    workSt.put("Lat", "");
                    workSt.put("Long", "");
                    workSt.put("Address", "");

                    FirebaseDatabase.getInstance().
                            getReference("clientUSERS").child(clientID)
                            .child("favouritePlace").child(getString(R.string.txt_home)).setValue(homeSt);

                    FirebaseDatabase.getInstance().
                            getReference("clientUSERS").child(clientID)
                            .child("favouritePlace").child(getString(R.string.txt_work)).setValue(workSt);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showDialog(final Context context, final Call call) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_incomming_call, null, false);
            dialog.setContentView(view);

            final RelativeLayout relativeLayout = dialog.findViewById(R.id.incoming_call_view);
            CircleImageView iv_user_image_voip_one = dialog.findViewById(R.id.iv_user_image_voip_one);
            final CircleImageView iv_cancel_call_voip_one = dialog.findViewById(R.id.iv_cancel_call_voip_one);
            final CircleImageView iv_recv_call_voip_one = dialog.findViewById(R.id.iv_recv_call_voip_one);
            final TextView caller_name = dialog.findViewById(R.id.callerName);
            final TextView callState = dialog.findViewById(R.id.callState);

            final CircleImageView iv_mute = dialog.findViewById(R.id.iv_mute);
            final CircleImageView iv_loud = dialog.findViewById(R.id.iv_loud);
            TextView tv_name_voip_one = dialog.findViewById(R.id.tv_name_voip_one);

            final Runnable mUpdate = new Runnable() {

                @Override
                public void run() {
                    mMinute += 1;
                    // just some checks to keep everything in order
                    if (mMinute >= 60) {
                        mMinute = 0;
                        mHour += 1;
                    }
                    if (mHour >= 24) {
                        mHour = 0;
                    }
                    // or call your method
                    caller_name.setText(mHour + ":" + mMinute);
                    mHandler.postDelayed(this, 1000);
                }
            };

            iv_mute.setVisibility(View.GONE);
            iv_loud.setVisibility(View.GONE);
//        iv_recv_call_voip_one.setEnabled(true);
            iv_recv_call_voip_one.setClickable(true);

            final AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.setMicrophoneMute(false);
            audioManager.setSpeakerphoneOn(false);

            final MediaPlayer mp = MediaPlayer.create(this, R.raw.ring);
            mp.start();

            call.addCallListener(new CallListener() {
                @Override
                public void onCallEnded(Call endedCall) {
                    //call ended by either party
                    dialog.findViewById(R.id.incoming_call_view).setVisibility(View.GONE);
                    setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
                    try {
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                        }
                        iv_mute.setVisibility(View.GONE);
                        iv_loud.setVisibility(View.GONE);
                        caller_name.setVisibility(View.GONE);
                        callState.setText("");
                        mHandler.removeCallbacks(mUpdate);// we need to remove our updates if the activity isn't focused(or even destroyed) or we could get in trouble
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCallEstablished(final Call establishedCall) {
                    //incoming call was picked up
                    dialog.findViewById(R.id.incoming_call_view).setVisibility(View.VISIBLE);
                    setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
                    callState.setText("connected");
                    iv_mute.setVisibility(View.VISIBLE);
                    iv_loud.setVisibility(View.VISIBLE);

                    iv_recv_call_voip_one.setVisibility(View.GONE);

                    params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    iv_cancel_call_voip_one.setLayoutParams(params);
                    try {
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                        }
                        mHour = 00;//c.get(Calendar.HOUR_OF_DAY);
                        mMinute = 00;//c.get(Calendar.MINUTE);
                        caller_name.setText(mHour + ":" + mMinute);
                        mHandler.postDelayed(mUpdate, 1000); // 60000 a minute
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCallProgressing(Call progressingCall) {
                    //call is ringing
                    try {
                        dialog.findViewById(R.id.incoming_call_view).setVisibility(View.VISIBLE);
                        caller_name.setText(progressingCall.getDetails().getDuration() + "");
                        caller_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        iv_mute.setVisibility(View.VISIBLE);
                        iv_loud.setVisibility(View.VISIBLE);
                        caller_name.setTypeface(null, Typeface.BOLD);
                        callState.setText("ringing");
                        iv_recv_call_voip_one.setVisibility(View.GONE);
                        params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        iv_cancel_call_voip_one.setLayoutParams(params);
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
                    //don't worry about this right now
                }
            });

            final AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            final int origionalVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

            switch (am.getRingerMode()) {
                case 0:

                    mp.start();
                    break;
                case 1:

                    mp.start();
                    break;
                case 2:

                    mp.start();
                    break;
            }

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    try {
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                        }
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, origionalVolume, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            if (ContextCompat.checkSelfPermission(Maps2Activity.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(Maps2Activity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Maps2Activity.this,
                        new String[]{android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.READ_PHONE_STATE},
                        10);
            }

            caller_name.setVisibility(View.VISIBLE);
            caller_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            caller_name.setTypeface(null, Typeface.NORMAL);      // for Normal Text

            caller_name.setText(driverName + resources.getString(R.string.vous_apple_txt));
            tv_name_voip_one.setText(driverName);
            if (!driverImage.isEmpty()) {
                Picasso.get().load(driverImage).into(iv_user_image_voip_one);
            }

            iv_cancel_call_voip_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    call.hangup();
                    try {
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                        }
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            params = (RelativeLayout.LayoutParams) iv_cancel_call_voip_one.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            iv_cancel_call_voip_one.setLayoutParams(params);

            iv_recv_call_voip_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                        }
                        call.answer();
                        audioManager.setMicrophoneMute(false);
                        audioManager.setSpeakerphoneOn(false);
                        iv_recv_call_voip_one.setClickable(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


            iv_loud.setBackgroundColor(Color.WHITE);
            iv_loud.setCircleBackgroundColor(Color.WHITE);
            iv_mute.setBackgroundColor(Color.WHITE);
            iv_mute.setCircleBackgroundColor(Color.WHITE);
            iv_loud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isLoud) {
                        iv_loud.setCircleBackgroundColor(Color.WHITE);
                        audioManager.setSpeakerphoneOn(true);
                        iv_loud.setImageResource(R.drawable.clicked_speaker_bt);
                        isLoud = true;
                    } else {
                        iv_loud.setCircleBackgroundColor(Color.WHITE);
                        audioManager.setSpeakerphoneOn(false);
                        iv_loud.setImageResource(R.drawable.speaker_bt);
                        isLoud = false;
                    }
                }
            });

            iv_mute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mute(audioManager, iv_mute);
                }
            });

            final Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            }
            if (window != null) {
                window.setGravity(Gravity.CENTER);
            }

            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean destPositionIsValid() {
        if (destLatLng == null)
            return false;
        if (PolyUtil.containsLocation(destLatLng.latitude, destLatLng.longitude, LocationInitializer.casaPoly(), true) ||
                PolyUtil.containsLocation(destLatLng.latitude, destLatLng.longitude, LocationInitializer.errahmaPoly(), true)) {
            destCity = "casa";
            return true;
        } else if (PolyUtil.containsLocation(destLatLng.latitude, destLatLng.longitude, LocationInitializer.rabatPoly(), true) ||
                PolyUtil.containsLocation(destLatLng.latitude, destLatLng.longitude, LocationInitializer.missingRabatPoly(), true)) {
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

    private void mute(AudioManager audioManager, CircleImageView iv_mute) {
        if (!audioManager.isMicrophoneMute()) {
            audioManager.setMicrophoneMute(true);
            iv_mute.setImageResource(R.drawable.clicked_mute);
        } else {
            audioManager.setMicrophoneMute(false);
            iv_mute.setImageResource(R.drawable.mute_bt);
        }
    }

    private void handleCourseCallBack() {
        try {
            if (statusT.equals("4")) {
                mMap.clear();
                my_position.setVisibility(View.VISIBLE);
                if (courseScreenIsOn) {
                    courseScreenIsOn = false;
                    courseScreenStageZero = false;
                    courseScreenStageOne = false;
                    findViewById(R.id.pin).setVisibility(View.VISIBLE);
                    state = 0;
                    cancelCommandLayout();
                    rl_calling.setVisibility(View.GONE);
                    hideSelectDestUI();
                    coverButton.setClickable(true);
                }

                findViewById(R.id.buttonsLayout).setVisibility(View.VISIBLE);
                return;
            }

            if (statusT.equals("1")) {
                rl_calling.setVisibility(View.VISIBLE);
            }
            stopSearchUI();
            shadow_bg.setVisibility(View.VISIBLE);
            menu_button.setImageResource(R.drawable.home_icon);
            menu_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // mDrawer.openMenu(true);
                    ConstraintLayout contentConstraint = findViewById(R.id.contentLayout);
                    ConstraintLayout contentBlocker = findViewById(R.id.contentBlocker);
                    AnimateConstraint.resideAnimation(Maps2Activity.this, contentConstraint, contentBlocker, (int) dpWidth, (int) dpHeight, 200);
                }
            });


            destArrow.setVisibility(View.VISIBLE);

            if (!courseScreenIsOn) {
                courseScreenIsOn = true;
                mMap.clear();

                et_start_point.setEnabled(false);
                et_end_point.setEnabled(false);
                coverButton.setClickable(false);

                et_start_point.setText(startText);
                et_end_point.setText(endText);

                findViewById(R.id.pin).setVisibility(View.GONE);

                AnimateConstraint.fadeIn(Maps2Activity.this, bottomMenu, 500, 0);
                AnimateConstraint.fadeIn(Maps2Activity.this, selectedOpImage, 500, 0);
                AnimateConstraint.fadeIn(Maps2Activity.this, rl_calling, 500, 0);
                findViewById(R.id.gooContent).setVisibility(View.GONE);
                cancelRequest.setVisibility(View.GONE);

                et_start_point.setCompoundDrawables(null, null, null, null);
                et_end_point.setCompoundDrawables(null, null, null, null);

                AnimateConstraint.animate(Maps2Activity.this, start_edit_text, (dpHeight - 135), 100, 0);
                AnimateConstraint.animate(Maps2Activity.this, dest_edit_text, dpHeight - 110, 180, 0);


                findViewById(R.id.buttonsLayout).setVisibility(View.GONE);

                tv_driver_name.setText(driverName);
                iv_car_number.setText(driverCarDescription);
                iv_total_ride_number.setText(driverCarName);
                if (iv_driver_image != null) {
                    if (driverImage.length() > 0) {
                        Picasso.get().load(driverImage).into(iv_driver_image);
                    }
                }

                LayerDrawable stars = (LayerDrawable) rbDriverRating.getProgressDrawable();
                stars.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

                if (ContextCompat.checkSelfPermission(Maps2Activity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(Maps2Activity.this,
                            new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            }, 1);
                }

            }


            if (statusT.equals("0") && !courseScreenStageZero) {
                if (!userLevel.equals("2")) {
                    iv_call_driver.setVisibility(View.VISIBLE);
                    iv_call_driver.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            close_button.setVisibility(View.VISIBLE);
                            iv_call_driver.setVisibility(View.GONE);
                            voip_view.setVisibility(View.VISIBLE);
                        }
                    });

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
                                tv_appelle_voip.setClickable(false);
//                                tv_appelle_voip.setEnabled(false);

                                Intent intent = new Intent(Maps2Activity.this, VoipCallingActivity.class);
                                intent.putExtra("driverId", driverIDT);
                                intent.putExtra("clientId", clientID);
                                intent.putExtra("driverName", driverName);
                                intent.putExtra("driverImage", driverImage);
                                startActivityForResult(intent, 1);
                            }
                        }
                    });
                }

                courseScreenStageZero = true;
                try {
                    final Dialog dialog = new Dialog(Maps2Activity.this);
                    dialog.setContentView(R.layout.dialog_custom);
                    Button dialogButton = dialog.findViewById(R.id.button);
                    TextView textView8 = dialog.findViewById(R.id.textView8);

                    //Set Texts
                    textView8.setText(resources.getString(R.string.Votrechauffeurestenroute));
                    dialogButton.setText(resources.getString(R.string.Daccord));

                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                    WindowManager.LayoutParams lp = Objects.requireNonNull(dialog.getWindow()).getAttributes();
                    lp.dimAmount = 0.5f;
                    dialog.getWindow().setAttributes(lp);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                } catch (WindowManager.BadTokenException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            if (statusT.equals("0")) {
                iv_cancel_ride.setVisibility(View.VISIBLE);
                iv_cancel_ride.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rideCancelDialog();
                    }
                });

                if (driverPosMarker != null)
                    driverPosMarker.remove();

                if (startPositionMarker != null)
                    startPositionMarker.remove();


                framelayout3.setDrawingCacheEnabled(true);
                framelayout3.buildDrawingCache();
                Bitmap bm = framelayout3.getDrawingCache();
                driverPosMarker = mMap.addMarker(new MarkerOptions()
                        .position(driverPosT)
                        .icon(BitmapDescriptorFactory.fromBitmap(bm)));

                framelayout.setDrawingCacheEnabled(true);
                framelayout.buildDrawingCache();
                bm = framelayout.getDrawingCache();
                startPositionMarker = mMap.addMarker(new MarkerOptions()
                        .position(startPositionT)
                        .icon(BitmapDescriptorFactory.fromBitmap(bm)));
            }

            if (statusT.equals("1") && !courseScreenStageOne) {
                iv_cancel_ride.setVisibility(View.GONE);
                voip_view.setVisibility(View.GONE);
                rl_calling.setVisibility(View.VISIBLE);
                if (!userLevel.equals("2")) {
                    iv_call_driver.setVisibility(View.VISIBLE);
                    iv_call_driver.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            close_button.setVisibility(View.VISIBLE);
                            iv_call_driver.setVisibility(View.GONE);
                            voip_view.setVisibility(View.VISIBLE);
                        }
                    });

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
                                tv_appelle_voip.setClickable(false);
//                                tv_appelle_voip.setEnabled(false);
                                Intent intent = new Intent(Maps2Activity.this, VoipCallingActivity.class);
                                intent.putExtra("driverId", driverIDT);
                                intent.putExtra("clientId", clientID);
                                intent.putExtra("driverName", driverName);
                                intent.putExtra("driverImage", driverImage);
                                startActivity(intent);
                            }
                        }
                    });
                }

                mMap.clear();
                courseScreenStageOne = true;
                try {
                    final Dialog dialog = new Dialog(Maps2Activity.this);
                    dialog.setContentView(R.layout.dialog_custom2);
                    TextView textView8 = dialog.findViewById(R.id.textView8);
                    Button ddd = dialog.findViewById(R.id.button);
                    //Set Texts
                    textView8.setText(resources.getString(R.string.Votrechauffeurestarrivé));
                    ddd.setText(resources.getString(R.string.Daccord));
                    Button dialogButton = dialog.findViewById(R.id.button);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    WindowManager.LayoutParams lp = Objects.requireNonNull(dialog.getWindow()).getAttributes();
                    lp.dimAmount = 0.5f;
                    dialog.getWindow().setAttributes(lp);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                framelayout.setDrawingCacheEnabled(true);
                framelayout.buildDrawingCache();


                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(startPositionT)      // Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                Bitmap bm = framelayout3.getDrawingCache();
                startPositionMarker = mMap.addMarker(new MarkerOptions()
                        .position(startPositionT)
                        .icon(BitmapDescriptorFactory.fromBitmap(bm)));

            }
            if (statusT.equals("2")) {
                mMap.clear();
                framelayout3.setDrawingCacheEnabled(true);
                framelayout3.buildDrawingCache();
                Bitmap bm = framelayout3.getDrawingCache();
                mMap.addMarker(new MarkerOptions()
                        .position(driverPosT)
                        .icon(BitmapDescriptorFactory.fromBitmap(bm)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && RESULT_OK == -1 && data.hasExtra("result")) {
            tv_appelle_voip.setClickable(true);
//            tv_appelle_voip.setEnabled(true);
        }

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        getLastLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }

    private void rideCancelDialog() {
        try {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MapsActivity.this);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_cancel_ride, null);
            alertDialog.getWindow().setContentView(dialogView);

            final Button btnYesCancelRide = dialogView.findViewById(R.id.btn_yes_cancel_ride);
            final Button btnNoDontCancelRide = dialogView.findViewById(R.id.btn_dont_cancel_ride);

            btnYesCancelRide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnYesCancelRide.setBackgroundColor(Color.WHITE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        btnYesCancelRide.setTextColor(getApplicationContext().getColor(R.color.primaryLight));
                    } else {
                        btnYesCancelRide.setTextColor(getApplicationContext().getResources().getColor(R.color.primaryLight));
                    }

                    btnNoDontCancelRide.setBackgroundColor(Color.TRANSPARENT);
                    btnNoDontCancelRide.setTextColor(Color.WHITE);


                    FirebaseDatabase.getInstance().getReference("COURSES").child(courseIDT).child("state").setValue("5");

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            FirebaseDatabase.getInstance().getReference("COURSES").child(courseIDT).removeValue();
                        }
                    }, 3000);

                    SharedPreferenceTask preferenceTask = new SharedPreferenceTask(getApplicationContext());
                    int prevCancel = preferenceTask.getCancelNumber();
                    preferenceTask.setCancelNumber(prevCancel + 1);
                    rl_calling.setVisibility(View.GONE);
                    voip_view.setVisibility(View.GONE);


                    if (preferenceTask.getCancelNumber() > 3) {
                        Toast.makeText(Maps2Activity.this,
                                resources.getString(R.string.vous_avez_txt),
                                Toast.LENGTH_LONG).show();

                        blockingTimeOver = false;

                        new CountDownTimer(3600000, 1000) {

                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                blockingTimeOver = true;
                            }
                        }.start();
                    }
                    alertDialog.dismiss();
                    iv_cancel_ride.setVisibility(View.GONE);

                }
            });

            btnNoDontCancelRide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    btnYesCancelRide.setBackgroundColor(Color.TRANSPARENT);
                    btnYesCancelRide.setTextColor(Color.WHITE);

                    btnNoDontCancelRide.setBackgroundColor(Color.WHITE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        btnNoDontCancelRide.setTextColor(getApplicationContext().getColor(R.color.primaryLight));
                    } else {
                        btnNoDontCancelRide.setTextColor(getApplicationContext().getResources().getColor(R.color.primaryLight));
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showVoiceDialog() {
        try {
            final Dialog newDialog = new Dialog(Maps2Activity.this);
            newDialog.setContentView(R.layout.dialog_voice_record);

            TextView textView18 = newDialog.findViewById(R.id.tv_destination);
            TextView textView19 = newDialog.findViewById(R.id.textView19);
            TextView textView20 = newDialog.findViewById(R.id.textView20);


            //Set Texts
            textView18.setText(resources.getString(R.string.Appreciate));
            textView19.setText(resources.getString(R.string.PS));
            textView20.setText(resources.getString(R.string.Record));


            ImageButton nextBtn = newDialog.findViewById(R.id.imageButton6);
            TextView name = newDialog.findViewById(R.id.textView17);

            final ImageButton recordButton = newDialog.findViewById(R.id.recordAudio);
            final ImageButton playAudio = newDialog.findViewById(R.id.playAudio);
            final ImageButton pauseAudio = newDialog.findViewById(R.id.pauseAudio);
            final ImageButton deleteAudio = newDialog.findViewById(R.id.deleteAudio);
            final MediaPlayer mediaPlayer = new MediaPlayer();


            outputeFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
            final MediaRecorder myAudioRecorder = new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            myAudioRecorder.setOutputFile(outputeFile);

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
                                        mediaPlayer.stop();
                                        mediaPlayer.release();
                                        newDialog.dismiss();
                                        showVoiceDialog();
                                    }
                                });

                                myAudioRecorder.stop();
                                myAudioRecorder.release();
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

                        final StorageReference filepath = FirebaseStorage.getInstance().getReference("audios").
                                child(Objects.requireNonNull(userId)).child(COURSE + ".3gp");
                        filepath.putFile(Uri.fromFile(new File(outputeFile)));
                    }
                    Toast.makeText(Maps2Activity.this, resources.getString(R.string.thank_you_txt), Toast.LENGTH_SHORT).show();
                    newDialog.dismiss();
                }
            });


            newDialog.findViewById(R.id.body).getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int)
                    (dpWidth), getResources().getDisplayMetrics());

            WindowManager.LayoutParams lp = Objects.requireNonNull(newDialog.getWindow()).getAttributes();
            lp.dimAmount = 0.5f;
            newDialog.getWindow().setAttributes(lp);
            newDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            newDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            newDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupPlayAudio(final String outputeFile,
                                final View playAudio, final View pauseAudio, final MediaPlayer mediaPlayer) {
        playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        if (mediaPlayer.isPlaying())
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

    private void getRecentPlaces(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("recent_places", "");

        place[] rPlace = gson.fromJson(json, place[].class);
        place Place = new place("Travail",
                "Casablanca, Morocco", "33.5725155", "-7.5962637", R.drawable.lieux_proches);

        if (rPlace == null || rPlace.length == 0) {
            rPlaceDataList.add(Place);
        } else {
            rPlaceDataList.addAll(Arrays.asList(rPlace));
        }
        rPlaceAdapter.notifyDataSetChanged();
    }

    private void hideAllUI() {
        start_edit_text.setVisibility(View.INVISIBLE);
        et_end_point.setVisibility(View.INVISIBLE);
        gooBox.setVisibility(View.INVISIBLE);
        destArrow.setVisibility(View.INVISIBLE);
        findViewById(R.id.destPoint).setVisibility(View.GONE);
    }

    private void showAllUI() {
        start_edit_text.setVisibility(View.VISIBLE);
        et_end_point.setVisibility(View.VISIBLE);
        gooBox.setVisibility(View.VISIBLE);
        destArrow.setVisibility(View.VISIBLE);
        rl_calling.setVisibility(View.GONE);
        findViewById(R.id.destPoint).setVisibility(View.VISIBLE);
    }

    public void showPromoCodeDialog(final Context context) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_promo_code, null, false);
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

//                        FirebaseDatabase.getInstance().getReference("CLIENTNOTIFICATIONS").
//                                child("qsjkldjqld").child("code").addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                if (Objects.requireNonNull(dataSnapshot.getValue()).equals(etPromoCode.getText().toString())) {
//
//                                    FirebaseDatabase.getInstance().getReference("CLIENTNOTIFICATIONS").
//                                            child("qsjkldjqld").child("value").addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            if (dataSnapshot.exists()) {
//                                                price.setText(dataSnapshot.getValue() + " MAD");
//                                                promoCode.setText(etPromoCode.getText().toString());
//                                                userPromoCode = etPromoCode.getText().toString();
//
//                                                FirebaseDatabase.getInstance().getReference("clientUSERS").
//                                                        child(userId).child("PROMOCODE").setValue(userPromoCode);
//                                                dialog.dismiss();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                                            dialog.dismiss();
//                                        }
//                                    });
//
//                                } else
//                                    Toast.makeText(getApplicationContext(), resources.getString(R.string.promoce_expired_txt), Toast.LENGTH_LONG).show();
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                dialog.dismiss();
//                            }
//                        });

                    } else
                        Toast.makeText(getApplicationContext(), resources.getString(R.string.promocode_validation_txt), Toast.LENGTH_LONG).show();
                }
            });

            dialog.setContentView(view);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showFavoritsAndRecents() {
        rPlaceDataList.clear();
        getRecentPlaces(Maps2Activity.this);

        AnimateConstraint.animate(Maps2Activity.this, favorite, HeightAbsolute, 1, 100);

        findViewById(R.id.imageView7).setVisibility(View.VISIBLE);
        findViewById(R.id.x).setVisibility(View.VISIBLE);
        findViewById(R.id.my_position).setVisibility(View.GONE);
        findViewById(R.id.adress_result).setVisibility(View.INVISIBLE);
    }

    private void cancelCommandLayout() {
        orderDriverState = 1;

        et_end_point.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search_icon, 0);
        AnimateConstraint.animate(Maps2Activity.this, dest_edit_text, 180, dpHeight - 20, 500, select_dest, findViewById(R.id.destArrow));
        destArrow.setVisibility(View.GONE);
        findViewById(R.id.gooContent).setVisibility(View.GONE);
        my_position.setVisibility(View.VISIBLE);
        start_edit_text.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (int) (dpHeight - 42), getResources().getDisplayMetrics());
        shadow_bg.setVisibility(View.VISIBLE);
        search_dest_address_button.setVisibility(View.VISIBLE);
        menu_button.setVisibility(View.VISIBLE);
        coverButton.setVisibility(View.VISIBLE);
        et_end_point.setEnabled(true);

        findViewById(R.id.locationPinDest).setVisibility(View.VISIBLE);
        mMap.clear();
        showSelectDestUI();
    }

    private void switchToCommandLayout() {
        orderDriverState = 2;
        my_position.setVisibility(View.GONE);
        et_end_point.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        AnimateConstraint.animate(Maps2Activity.this, dest_edit_text, dpHeight - 40, 180, 500, selectDest,
                findViewById(R.id.destArrow));
        AnimateConstraint.fadeIn(Maps2Activity.this, findViewById(R.id.gooContent), 500, 10);

        start_edit_text.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (dpHeight - 62),
                getResources().getDisplayMetrics());
//        searchButtonDest.setVisibility(View.GONE);
        state = 2;

        coverButton.setVisibility(View.GONE);
        et_end_point.setEnabled(false);

        findViewById(R.id.locationPinDest).setVisibility(View.GONE);

        if (destLatLng != null) {
//            new MapsActivity.DrawRouteTask().execute(startLatLng, destLatLng);
            gooButton.setVisibility(View.GONE);
            framelayout2.setDrawingCacheEnabled(true);
            framelayout2.buildDrawingCache();
            Bitmap bm = framelayout2.getDrawingCache();
            Marker myMarker = mMap.addMarker(new MarkerOptions()
                    .position(destLatLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(bm)));

        } else {
            gooButton.setVisibility(View.VISIBLE);
            et_end_point.setText("Destination non choisi.");
        }

        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = 0;
                cancelCommandLayout();
            }
        });
    }

    private void hideSelectDestUI() {
        orderDriverState = 0;
//        callLayout.setVisibility(View.GONE);
        iv_cancel_ride.setVisibility(View.GONE);
        coverButton.setClickable(true);
        voip_view.setVisibility(View.GONE);
        my_position.setVisibility(View.VISIBLE);
        hideSearchAddressStartUI();
        confirm_start.setVisibility(View.VISIBLE);
        findViewById(R.id.shadow).setVisibility(View.VISIBLE);
        bottomMenu.setVisibility(View.VISIBLE);
        selectedOperation.setVisibility(View.VISIBLE);
        shadow_bg.setVisibility(View.VISIBLE);
        dest_edit_text.setVisibility(View.GONE);
        select_dest.setVisibility(View.GONE);
        et_start_point.setEnabled(true);

        start_edit_text.setVisibility(View.VISIBLE);
        et_start_point.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search_icon, 0);

        AnimateConstraint.animate(Maps2Activity.this, start_edit_text, 80, (dpHeight - 130), 500);

        findViewById(R.id.locationPin).setVisibility(View.VISIBLE);
        findViewById(R.id.closestDriver).setVisibility(View.VISIBLE);
        findViewById(R.id.locationPinDest).setVisibility(View.GONE);


        menu_button.setImageResource(R.drawable.home_icon);
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout contentConstraint = findViewById(R.id.contentLayout);
                ConstraintLayout contentBlocker = findViewById(R.id.contentBlocker);
                AnimateConstraint.resideAnimation(Maps2Activity.this, contentConstraint, contentBlocker, (int) dpWidth, (int) dpHeight, 200);
            }
        });

        mMap.clear();
    }

    private void showSelectDestUI() {
        orderDriverState = 1;
        hideSearchAddressStartUI();
        confirm_start.setVisibility(View.GONE);
        findViewById(R.id.shadow).setVisibility(View.GONE);
        bottomMenu.setVisibility(View.GONE);
        selectedOperation.setVisibility(View.GONE);
        shadow_bg.setVisibility(View.GONE);
        dest_edit_text.setVisibility(View.VISIBLE);
        et_start_point.setEnabled(false);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth = outMetrics.widthPixels / density;

        AnimateConstraint.animate(Maps2Activity.this, start_edit_text, (dpHeight - 130), 100, 500);
        AnimateConstraint.fadeIn(Maps2Activity.this, dest_edit_text, 500, 10);
        AnimateConstraint.fadeIn(Maps2Activity.this, select_dest, 500, 10);

        et_start_point.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        findViewById(R.id.locationPin).setVisibility(View.GONE);
        findViewById(R.id.closestDriver).setVisibility(View.GONE);
        findViewById(R.id.locationPinDest).setVisibility(View.VISIBLE);

        framelayout.setDrawingCacheEnabled(true);
        framelayout.buildDrawingCache();
        closestDriverPin.setText(closestDriver.getText());
        Bitmap bm = framelayout.getDrawingCache();
        Marker myMarker = mMap.addMarker(new MarkerOptions()
                .position(startLatLng)
                .icon(BitmapDescriptorFactory.fromBitmap(bm)));

        menu_button.setVisibility(View.VISIBLE);
        menu_button.setImageResource(R.drawable.back_arrow);
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSelectDestUI();
            }
        });

    }

    private TextWatcher txt;
    private TextWatcher txtDest;

    private void setSearchFunc() {
        findViewById(R.id.coverButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_start.setVisibility(View.GONE);
                findViewById(R.id.shadow).setVisibility(View.GONE);
                bottomMenu.setVisibility(View.GONE);
                selectedOperation.setVisibility(View.GONE);
                select_dest.setVisibility(View.GONE);
                findViewById(R.id.coverButton).setVisibility(View.GONE);
                favorite.setBackgroundColor(Color.WHITE);
                isFocusableNeeded = true;
                state = -1;
                showFavoritsAndRecents();
            }
        });

        et_start_point.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    et_start_point.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            txt = this;
                            if (et_start_point.getText().toString().length() >= 3) {
                                if (isFocusableNeeded) {
                                    lookForAddress();
                                }
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });
                } else {
                    et_start_point.removeTextChangedListener(txt);
                }
            }
        });

        et_end_point.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    et_end_point.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            txtDest = this;
                            if (et_end_point.getText().toString().length() >= 3) {
                                if (isFocusableNeeded) {
                                    lookForAddress();
                                }
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });
                } else {
                    et_end_point.removeTextChangedListener(txtDest);
                }
            }
        });

        et_start_point.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(Maps2Activity.this);
                    et_start_point.clearFocus();
                    et_start_point.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    return true;
                }
                return false;
            }
        });
        et_end_point.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(Maps2Activity.this);
                    et_end_point.clearFocus();
                    return true;
                }
                return false;
            }
        });

        search_dest_address_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(Maps2Activity.this);
                et_start_point.clearFocus();
//                searchButtonDest.setVisibility(View.GONE);
//                searchProgBarDest.setVisibility(View.VISIBLE);
                lookForAddress();
            }
        });

    }

    public void lookForAddress() {
        if ((et_start_point.getText().toString().length() == 0 && orderDriverState == 0) ||
                (et_start_point.getText().toString().length() == 0 && orderDriverState == 1)) {
            findViewById(R.id.imageView111).setVisibility(View.VISIBLE);
            showSearchAddressStartUI();
            return;
        }
        if (orderDriverState == 1) {
            start_edit_text.setVisibility(View.INVISIBLE);
        }
        placeDataList.clear();
//        new MapsActivity.LookForAddressTask().execute();
    }

    private void goToLocation(Context context, Double lat, Double lng, place rPlace) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        et_start_point.setText(Utility.getCompleteAddressString(context, lat, lng));
        et_end_point.setText(Utility.getCompleteAddressString(context, lat, lng));
    }

    private void hideSearchAddressStartUI() {
        placeDataList.clear();
        placeAdapter.notifyDataSetChanged();
        findViewById(R.id.imageView7).setVisibility(View.INVISIBLE);
        findViewById(R.id.imageView8).setVisibility(View.INVISIBLE);
        favorite.setVisibility(View.INVISIBLE);
        aR.setVisibility(View.INVISIBLE);
        findViewById(R.id.imageView111).setVisibility(View.INVISIBLE);

        if (favorite.getHeight() >= HeightAbsolute)
            AnimateConstraint.animateCollapse(Maps2Activity.this,
                    favorite, 1, HeightAbsolute, 300);
        if (aR.getHeight() >= HeightAbsolute)
            AnimateConstraint.animateCollapse(Maps2Activity.this, aR, 1, HeightAbsolute, 300);
        findViewById(R.id.imageView7).setVisibility(View.INVISIBLE);
        findViewById(R.id.imageView8).setVisibility(View.INVISIBLE);
        coverButton.setVisibility(View.VISIBLE);
        hideKeyboard(Maps2Activity.this);
        start_edit_text.setVisibility(View.VISIBLE);
        if (orderDriverState == 0) {
            select_start.setVisibility(View.VISIBLE);
            bottomMenu.setVisibility(View.VISIBLE);
            selectedOperation.setVisibility(View.VISIBLE);
        }
        if (orderDriverState == 1) {
            select_dest.setVisibility(View.VISIBLE);
        }

    }

    private void showSearchAddressStartUI() {
        x.setVisibility(View.VISIBLE);
        menu_button.setVisibility(View.VISIBLE);
        state = 0;
        placeDataList.clear();
        placeAdapter.notifyDataSetChanged();
        start_edit_text.setVisibility(View.VISIBLE);
        et_start_point.clearFocus();
        et_start_point.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        et_end_point.clearFocus();

        AnimateConstraint.animate(Maps2Activity.this, favorite, 1, 1, 1);
        AnimateConstraint.animate(Maps2Activity.this, aR, 1, 1, 1);
        coverButton.setVisibility(View.VISIBLE);
        hideKeyboard(Maps2Activity.this);

        if (orderDriverState == 0) {
            select_start.setVisibility(View.VISIBLE);
            bottomMenu.setVisibility(View.VISIBLE);
            selectedOperation.setVisibility(View.VISIBLE);
            shadow_bg.setVisibility(View.VISIBLE);
        }
        if (orderDriverState == 1) {
            select_dest.setVisibility(View.VISIBLE);
        }
    }

    private void afterLook() {
        if (driversKeys.size() > 0) {
            double distanceKmTime = Math.floor(Double.parseDouble(driversLocations.get(0)));
            if (distanceKmTime >= 10) distanceKmTime -= (distanceKmTime * (distanceKmTime / 100));

            if (!courseScreenIsOn) {
                closestDriver.setText((int) distanceKmTime + "\nmin");
                if (orderDriverState == 1) {
                    if (closestDriver.getText().toString().startsWith("-")) {
                        closestDriverPin.setText(closestDriver.getText().toString().substring(1));
                    } else {
                        closestDriverPin.setText(closestDriver.getText());
                    }
                }
                if (orderDriverState == 2) {
                    if (closestDriver.getText().toString().startsWith("-")) {
                        closestDriverPin.setText(closestDriver.getText().toString().substring(1));
                    } else {
                        closestDriverPin.setText(closestDriver.getText());
                    }
                }
            }
        } else {
            if (!courseScreenIsOn) {
                rl_calling.setVisibility(View.GONE);
                closestDriver.setText("4\nmin");
                closestDriverPin.setText("4\nMin");
                if (orderDriverState == 1) {
                    closestDriver.setText("4\nmin");
                    closestDriverPin.setText("4\nMin");
                }
                if (orderDriverState == 2) {
                    closestDriver.setText("4\nmin");
                    closestDriverPin.setText("4\nMin");
                }
            }
        }
    }

    private int getPosition(List<String> sList, String element) {
        for (int i = 0; i < sList.size(); i++) {
            if (sList.get(i).equals(element)) return i;
        }
        return -1;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.setBuildingsEnabled(false);

        if (!Utility.isLocationEnabled(Maps2Activity.this))
            Utility.checkLocationService(resources,Maps2Activity.this);
        else {
            if (ContextCompat.checkSelfPermission(Maps2Activity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Maps2Activity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                getLastLocation();
                if (userLatLng != null)
                    startLatLng = userLatLng;
            }
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
//                        new MapsActivity.LookForDriverTask().execute();
                    }
                    startLatLng = mMap.getCameraPosition().target;
                    if (!courseScreenIsOn)
                        new ReverseGeocodingTask(Maps2Activity.this, orderDriverState, et_start_point, et_end_point, courseScreenIsOn).execute(startLatLng);
                }

                if (orderDriverState == 1) {
                    destLatLng = mMap.getCameraPosition().target;
                    if (!courseScreenIsOn)
                        new ReverseGeocodingTask(Maps2Activity.this, orderDriverState, et_start_point, et_end_point, courseScreenIsOn).execute(destLatLng);
                }
            }
        });

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                et_start_point.setText(Utility.getCompleteAddressString(Maps2Activity.this, startLatLng.latitude, startLatLng.longitude));
            }
        });

    }


    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);

        if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
            if (ContextCompat.checkSelfPermission(Maps2Activity.this, android.Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(Maps2Activity.this,
                    android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Maps2Activity.this,
                        new String[]{android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.READ_PHONE_STATE},
                        1);
            }
        }

        if (requestCode == 10) {
            if (grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                showVoiceDialog();
            }
        }
    }

    private void startSearchUI() {
        rippleBackground.startRippleAnimation();
        cancelRequest.setVisibility(View.VISIBLE);
    }

    private void stopSearchUI() {
        driversKeysHold.clear();
        AnimateConstraint.fadeIn(Maps2Activity.this, gooButton, 200, 10);
        rippleBackground.stopRippleAnimation();
        menu_button.setVisibility(View.VISIBLE);
        cancelRequest.setVisibility(View.GONE);
//        callLayout.setVisibility(View.GONE);
        gooButton.setClickable(true);
        driversKeys.clear();
        driversKeysHold.clear();
        if (startLatLng != null)
            geoQuery.setCenter(new GeoLocation(startLatLng.latitude, startLatLng.longitude));
    }


    public void setCancelSearchButton(final String userId, final int counter, final int Step) {
        cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSearchUI();
                showAllUI();
                for (int h = (counter - Step); h < (counter + Step) && h < driversKeys.size(); h++) {
                    DatabaseReference pickupRequest = FirebaseDatabase.getInstance().
                            getReference("PICKUPREQUEST");
                    pickupRequest.removeValue();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//        if (courseScreenIsOn) {
//            rideCancelDialog();
//        } else if (rippleBackground.isRippleAnimationRunning()) {
//            rippleBackground.stopRippleAnimation();
//            stopSearchUI();
//            showAllUI();
//            DatabaseReference pickupRequest = FirebaseDatabase.getInstance().
//                    getReference("PICKUPREQUEST");
//            pickupRequest.removeValue();
//        } else {
//            if (state == 1 || state == -1) {
//                state = 0;
//                hideSelectDestUI();
//                return;
//            }
//
//            if (state == 2) {
//                state = 1;
//                cancelCommandLayout();
//                return;
//            }
//        }
//
//
//        if (state != 1 && state != 2 && !courseScreenIsOn) {
//            this.doubleBackToExitPressedOnce = true;
//            Toast.makeText(this, resources.getString(R.string.back_exit_txt), Toast.LENGTH_SHORT).show();
//
//            new Handler().postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    doubleBackToExitPressedOnce = false;
//                }
//            }, 2000);
//        }
//    }

    public void updateViews() {
        //Set Texts
        home_tv.setText(resources.getString(R.string.Acceuil));
        history_tv.setText(resources.getString(R.string.Historique));
        inbox_tv.setText(resources.getString(R.string.Inbox));
        history_tv.setText(resources.getString(R.string.Invitervosamis));
        aide_tv.setText(resources.getString(R.string.Aide));
        logout_tv.setText(resources.getString(R.string.Sedeconnecter));
        textView.setText(resources.getString(R.string.Lieuxfavoris));
        textView2.setText(resources.getString(R.string.Lieuxrécents));

        confirm_start.setText(resources.getString(R.string.confirm_start));
        confirm_dest.setText(resources.getString(R.string.Ajouterladestination));
        passer.setText(resources.getString(R.string.passer));
    }

    @Override
    protected void onResume() {
        super.onResume();

        language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");
        co = LocalHelper.setLocale(Maps2Activity.this, language);
        resources = co.getResources();

        updateViews();
        fPlaceDataList.clear();
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            Toast.makeText(Maps2Activity.this, resources.getString(R.string.incoming_call_txt), Toast.LENGTH_SHORT).show();
            try {
                if (VoipCallingActivity.activity != null)
                    if (!VoipCallingActivity.activity.isFinishing())
                        VoipCallingActivity.activity.finish();
                showDialog(Maps2Activity.this, incomingCall);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void pickedLocation(place place) {
        et_start_point.setFocusable(false);
        et_start_point.setFocusableInTouchMode(false);
        coverButton.setClickable(false);
        my_position.setVisibility(View.VISIBLE);
        findViewById(R.id.shadow).setVisibility(View.VISIBLE);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Double.parseDouble(place.getLat()), Double.parseDouble(place.getLng())))
                .zoom(17)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                et_start_point.setFocusable(true);
                et_start_point.setFocusableInTouchMode(true);
                coverButton.setClickable(true);
            }

            @Override
            public void onCancel() {

            }
        });

        if (!Utility.contains(rPlaceDataList, place)) {
            place.setImage(R.drawable.lieux_proches);
            rPlaceDataList.add(place);
            Utility.saveRecentPlaces(Maps2Activity.this, rPlaceDataList);
            rPlaceAdapter.notifyDataSetChanged();
        }
        hideSearchAddressStartUI();
        isFocusableNeeded = false;
    }
}
