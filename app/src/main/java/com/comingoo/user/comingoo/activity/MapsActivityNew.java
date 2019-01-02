package com.comingoo.user.comingoo.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.comingoo.user.comingoo.model.Place;
import com.comingoo.user.comingoo.utility.AnimateConstraint;
import com.comingoo.user.comingoo.utility.Utility;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivityNew extends FragmentActivity implements OnMapReadyCallback {
    private String TAG = "MapsActivityNew";

    private ImageView ivDrawerToggol;
    private CircleImageView userImageDrawer;
    private TextView userNameDrawer;
    private LinearLayout llDrawerHistory;
    private LinearLayout llDrawerNotifications;
    private LinearLayout llDrawerInvite;
    private LinearLayout llDrawerComingooYou;
    private LinearLayout llDrawerAide;
    private TextView homeTxt;
    private TextView historyTxt;
    private TextView notificationTxt;
    private TextView refferTxt;
    private TextView comingoouTxt;
    private TextView aideTxt;

    private FrameLayout locationStartFrameLayout;
    private ImageView locationStartPinImage;
    private TextView closestDriverPinTv;
    private FrameLayout locationDestinationTmage;
    private ImageView locationDestPinImage;
    private FrameLayout driverLocationPinLayout;
    private ImageView driverPinImage;

    private ArrayList<Place> rPlaceDataList;
    private MyPlaceAdapter placeAdapter;
    private FavouritePlaceAdapter fPlaceAdapter;
    private MyPlaceAdapter rPlaceAdapter;
    private ArrayList<Place> placeDataList;
    private ArrayList<Place> fPlaceDataList;

    private RelativeLayout departPinLayout;
    private ImageView locationPinDtartImage;
    private ImageView locationPinDestImg;
    private TextView closestDriverTv;

    private ImageView iv_shadow;
    private ImageView btn_delivary;
    private ImageView btn_car;
    private LinearLayout ll_destination;

    private GoogleMap mMap;
    private EditText etSearchStartAddress;
    private EditText etSearchDestination;
    private ImageView ivCurrentLocation;
    private Button destinationBtn;
    private Button destinationIgnoreBtn;
    private RelativeLayout favouritePlaceBtn;
    private TextView favPaceBtn;
    private RecyclerView favPlaceRecycle;
    private RelativeLayout recentPlaceLayout;
    private TextView recentPlaceTv;
    private RelativeLayout fevouriteLayout;
    private RecyclerView recentPlacesRecycle;
    private RecyclerView searchResultRecycle;
    private ImageView arrowStartEndImage;
    private ImageView currentLocationImage;
    private ImageView gooImage;
    private ImageView cancelRequestImage;
    private CircleImageView cancelRideImage;
    private LatLng userLatLng;
    private LatLng startLatLng;
    private LatLng destLatLng;
    private GeoQuery geoQuery;
    private boolean courseScreenIsOn = false;
    private int orderDriverState;

    private RelativeLayout endPointLayout;
    private RelativeLayout promoCodeLayout;
    private ImageView promoCodeImage;
    private TextView promoCodeTv;

    private RelativeLayout callLayout;
    private CircularImageView driverIV;
    private TextView driverNameTv;
    private TextView totalRideNumberTv;
    private TextView carNumberTv;
    private LinearLayout ratingLayout;
    private RatingBar ratingBar;
    private TextView totalRatingNumberTV;
    private ImageView callDriverIv;
    private ImageView closeBtn;
    private LinearLayout voipLayout;
    private TextView voipTv;
    private TextView telephoneTv;

    private String language;
    private Resources resources;

    //  private float distance;
    private float density;
    private float dpHeight;
    private float dpWidth;
    private Handler mHandler = new Handler();
    private int mHour, mMinute;
    int HeightAbsolute;
    private RelativeLayout.LayoutParams params;
    private boolean isLoud = false;

    // Pickup Edittext contents
    private Button btnPickUp;
    private LinearLayout llDeliveryCar;
    private RelativeLayout rlStartPoint, rlEndPoint;
    private ImageView ivWheel;
    private TextView tvPrice, tvPromoCode;
    private ImageView ivShadow, ivCancelRequest;
    private RippleBackground rippleBackground;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_new);
        initialize();
        action();
    }

    private void initialize() {

        ivCurrentLocation = findViewById(R.id.iv_current_location);
        rlStartPoint = findViewById(R.id.rl_start_point);
        etSearchStartAddress = findViewById(R.id.et_start_point);
        etSearchDestination = findViewById(R.id.et_end_point);
        rlEndPoint = findViewById(R.id.rl_end_point);
        rippleBackground = findViewById(R.id.rapple_animation);
        btnPickUp = findViewById(R.id.btn_pickup);
        ivCancelRequest = findViewById(R.id.iv_cancel_request);
        llDeliveryCar = findViewById(R.id.ll_delivery_car);
        ivWheel = findViewById(R.id.iv_wheel);
        tvPromoCode = findViewById(R.id.tv_promo_code);
        closeBtn = findViewById(R.id.close_button);

        // Note : Drawer init
        ivDrawerToggol = findViewById(R.id.iv_menu_maps);
        userImageDrawer = findViewById(R.id.iv_user_image_drawer);
        userNameDrawer = findViewById(R.id.tv_user_name_drawer);
        llDrawerInvite = findViewById(R.id.ll_drawer_refer_friend);
        llDrawerNotifications = findViewById(R.id.ll_drawer_notificatiions);
        llDrawerHistory = findViewById(R.id.ll_drawer_history);
        llDrawerAide = findViewById(R.id.ll_drawer_help);
        llDrawerComingooYou = findViewById(R.id.ll_drawer_comingoo_you);
        homeTxt = findViewById(R.id.home_txt);
        historyTxt = findViewById(R.id.history_txt);
        notificationTxt = findViewById(R.id.notification_txt);
        refferTxt = findViewById(R.id.invite_txt);
        comingoouTxt = findViewById(R.id.comingoou_txt);
        aideTxt = findViewById(R.id.aide_txt);

        // Note : pin setup
        locationStartFrameLayout = findViewById(R.id.location_start_frame_layout);
        locationStartPinImage = findViewById(R.id.location_start_pin);
        closestDriverPinTv = findViewById(R.id.tv_closest_driverPin);
        locationDestinationTmage = findViewById(R.id.fl_location_destination);
        locationDestPinImage = findViewById(R.id.iv_location_dest_pin);
        driverLocationPinLayout = findViewById(R.id.fl_driver_location_pin);
        driverPinImage = findViewById(R.id.driver_pin);

        departPinLayout = findViewById(R.id.rl_depart_pin);
        locationPinDtartImage = findViewById(R.id.iv_location_pin_start);
        locationPinDestImg = findViewById(R.id.iv_location_pin_dest);
        closestDriverTv = findViewById(R.id.tv_closest_driver);

        destinationBtn = findViewById(R.id.btn_destination);
        destinationIgnoreBtn = findViewById(R.id.btn_destination_ignore);
        favouritePlaceBtn = findViewById(R.id.rl_favourite_place);
        favPaceBtn = findViewById(R.id.tv_fav_pace);
        favPlaceRecycle = findViewById(R.id.rv_fav_place);
        recentPlaceLayout = findViewById(R.id.rl_recent_place);
        recentPlaceTv = findViewById(R.id.tv_recent_place);
        fevouriteLayout = findViewById(R.id.fevourite_layout);
        recentPlacesRecycle = findViewById(R.id.rv_recent_places);
        searchResultRecycle = findViewById(R.id.rv_search_result);
        arrowStartEndImage = findViewById(R.id.iv_arrow_start_end);
        currentLocationImage = findViewById(R.id.iv_current_location);
        gooImage = findViewById(R.id.iv_goo);
        cancelRequestImage = findViewById(R.id.iv_cancel_request);
        cancelRideImage = findViewById(R.id.iv_cancel_ride);

        // Note : driver init
        callLayout = findViewById(R.id.rl_call_layout);
        driverIV = findViewById(R.id.iv_driver_image);
        driverNameTv = findViewById(R.id.tv_driver_name);
        totalRideNumberTv = findViewById(R.id.iv_total_ride_number);
        carNumberTv = findViewById(R.id.iv_car_number);
        ratingLayout = findViewById(R.id.ll_rating);
        ratingBar = findViewById(R.id.rb_user);
        totalRatingNumberTV = findViewById(R.id.iv_total_rating_number);
        callDriverIv = findViewById(R.id.iv_call_driver);
        voipLayout = findViewById(R.id.ll_voip_view);
        voipTv = findViewById(R.id.tv_appelle_voip);
        telephoneTv = findViewById(R.id.tv_appelle_telephone);

        // Note : promocode init
        endPointLayout = findViewById(R.id.rl_end_point);
        promoCodeLayout = findViewById(R.id.promocode_layout);
        promoCodeImage = findViewById(R.id.iv_promo_code);
        promoCodeTv = findViewById(R.id.tv_promo_code);

        // Note : pin setup
        locationStartFrameLayout = findViewById(R.id.location_start_frame_layout);
        locationStartPinImage = findViewById(R.id.location_start_pin);
        closestDriverPinTv = findViewById(R.id.tv_closest_driverPin);
        locationDestinationTmage = findViewById(R.id.fl_location_destination);
        locationDestPinImage = findViewById(R.id.iv_location_dest_pin);
        driverLocationPinLayout = findViewById(R.id.fl_driver_location_pin);
        driverPinImage = findViewById(R.id.driver_pin);

        departPinLayout = findViewById(R.id.rl_depart_pin);
        locationPinDtartImage = findViewById(R.id.iv_location_pin_start);
        locationPinDestImg = findViewById(R.id.iv_location_pin_dest);
        closestDriverTv = findViewById(R.id.tv_closest_driver);

        // Note : recycle setup
        placeDataList = new ArrayList<>();
        fPlaceDataList = new ArrayList<>();
        rPlaceDataList = new ArrayList<>();


        searchResultRecycle.setHasFixedSize(true);
        searchResultRecycle.setLayoutManager(new LinearLayoutManager(this));
        favPlaceRecycle.setHasFixedSize(true);
        favPlaceRecycle.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView rLocationView = findViewById(R.id.recent_recycler);
        recentPlacesRecycle.setHasFixedSize(true);
        recentPlacesRecycle.setLayoutManager(new LinearLayoutManager(this));
//        placeAdapter = new MyPlaceAdapter(getApplicationContext(), placeDataList, false, userId, this);
//        searchResultRecycle.setAdapter(placeAdapter);
//        fPlaceAdapter = new FavouritePlaceAdapter(getApplicationContext(), fPlaceDataList, true, userId, this);
//        favPlaceRecycle.setAdapter(fPlaceAdapter);
//
//        rPlaceAdapter = new MyPlaceAdapter(getApplicationContext(), rPlaceDataList, false, userId, this);
//        recentPlacesRecycle.setAdapter(rPlaceAdapter);

    }

    private void action() {
        language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");
        resources = getApplicationContext().getResources();


        if (!Utility.isNetworkConnectionAvailable(MapsActivityNew.this)) {
            Utility.checkNetworkConnection(resources, MapsActivityNew.this);
        }
        Utility.displayLocationSettingsRequest(MapsActivityNew.this, REQUEST_CHECK_SETTINGS);

        SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
        String userId = prefs.getString("userID", null);

        if (userId == null || FirebaseAuth.getInstance().getCurrentUser() == null) {
            prefs.edit().remove("userID");
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MapsActivityNew.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        tvPromoCode.setText(resources.getString(R.string.promocode_txt));

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBtn.setVisibility(View.GONE);
                callDriverIv.setVisibility(View.VISIBLE);
                voipLayout.setVisibility(View.GONE);
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


        // Getting display resulation
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        density = getResources().getDisplayMetrics().density;
        dpHeight = outMetrics.heightPixels / density;
        dpWidth = outMetrics.widthPixels / density;
        HeightAbsolute = (int) dpHeight - (200);


        // Checking Location Permission
        if (ContextCompat.checkSelfPermission(MapsActivityNew.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivityNew.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        AnimateConstraint.fadeOut(MapsActivityNew.this, findViewById(R.id.gif_loading_maps_activity), 500, 10);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.gif_loading_maps_activity).setVisibility(View.GONE);
            }
        }, 500);

        // Drawer Toggoling
        ivDrawerToggol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View contentMap = findViewById(R.id.content_map_view);
                RelativeLayout contentBlocker = findViewById(R.id.rl_block_content);
                AnimateConstraint.resideAnimation(getApplicationContext(), contentMap,
                        contentBlocker, (int) dpWidth, (int) dpHeight, 200);

            }
        });

        llDrawerHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivityNew.this, HistoriqueActivity.class));
            }
        });

        llDrawerInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivityNew.this, InviteActivity.class));
            }
        });

        llDrawerNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivityNew.this, NotificationActivity.class));
            }
        });
        llDrawerAide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivityNew.this, AideActivity.class));
            }
        });

        tvPromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        btnPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimateConstraint.animate(getApplicationContext(), rlStartPoint,
                        dpHeight - 250, 100, 500);
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.setBuildingsEnabled(false);

        if (!Utility.isLocationEnabled(MapsActivityNew.this))
            Utility.checkLocationService(resources, MapsActivityNew.this);
        else {
            if (ContextCompat.checkSelfPermission(MapsActivityNew.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivityNew.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
                        new ReverseGeocodingTask(MapsActivityNew.this, orderDriverState, etSearchStartAddress, etSearchDestination, courseScreenIsOn).execute(startLatLng);
                }

                if (orderDriverState == 1) {
                    destLatLng = mMap.getCameraPosition().target;
                    if (!courseScreenIsOn)
                        new ReverseGeocodingTask(MapsActivityNew.this, orderDriverState, etSearchStartAddress, etSearchDestination, courseScreenIsOn).execute(destLatLng);
                }
            }
        });

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                etSearchStartAddress.setText(Utility.getCompleteAddressString(MapsActivityNew.this, startLatLng.latitude, startLatLng.longitude));
            }
        });

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

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            Log.e(TAG, "onIncomingCall: ");
            Toast.makeText(MapsActivityNew.this, resources.getString(R.string.incoming_call_txt), Toast.LENGTH_SHORT).show();
            try {
                if (VoipCallingActivity.activity != null)
                    if (!VoipCallingActivity.activity.isFinishing())
                        VoipCallingActivity.activity.finish();
                showDialog(MapsActivityNew.this, incomingCall);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void showDialog(final Context context, final Call call) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_incomming_call, null, false);
            dialog.setContentView(view);

            RelativeLayout relativeLayout = dialog.findViewById(R.id.incoming_call_view);
            CircleImageView iv_user_image_voip_one = dialog.findViewById(R.id.iv_user_image_voip_one);
            final CircleImageView iv_cancel_call_voip_one = dialog.findViewById(R.id.iv_cancel_call_voip_one);
            final CircleImageView iv_recv_call_voip_one = dialog.findViewById(R.id.iv_recv_call_voip_one);
            final TextView caller_name = dialog.findViewById(R.id.callerName);
            final TextView callState = dialog.findViewById(R.id.callState);
            final CircleImageView iv_mute = dialog.findViewById(R.id.iv_mute);
            final CircleImageView iv_loud = dialog.findViewById(R.id.iv_loud);
            TextView tv_name_voip_one = dialog.findViewById(R.id.tv_name_voip_one);
            iv_mute.setVisibility(View.GONE);
            iv_loud.setVisibility(View.GONE);
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
                        if (mp != null) {
                            if (mp.isPlaying()) {
                                mp.stop();
                                mp.release();
                            }
                        }
                        iv_mute.setVisibility(View.GONE);
                        iv_loud.setVisibility(View.GONE);
                        caller_name.setVisibility(View.GONE);
                        callState.setText("");
                        mHandler.removeCallbacks(mUpdate);// we need to remove our updates if the activity isn't focused(or even destroyed) or we could get in trouble
                        dialog.dismiss();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
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
                        if (mp != null) {
                            if (mp.isPlaying()) {
                                mp.stop();
                                mp.release();
                            }
                        }
                        mHour = 00;//c.get(Calendar.HOUR_OF_DAY);
                        mMinute = 00;//c.get(Calendar.MINUTE);
                        caller_name.setText(mHour + ":" + mMinute);
                        mHandler.postDelayed(mUpdate, 1000); // 60000 a minute
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
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
                        if (mp != null) {
                            if (mp.isPlaying()) {
                                mp.stop();
                                mp.release();
                            }
                        }
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
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
                        if (mp != null) {
                            if (mp.isPlaying()) {
                                mp.stop();
                                mp.release();
                            }
                        }
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, origionalVolume, 0);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            if (ContextCompat.checkSelfPermission(MapsActivityNew.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MapsActivityNew.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivityNew.this,
                        new String[]{android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.READ_PHONE_STATE},
                        10);
            }

            caller_name.setVisibility(View.VISIBLE);
            caller_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            caller_name.setTypeface(null, Typeface.NORMAL);      // for Normal Text

//            caller_name.setText(driverName + resources.getString(R.string.vous_apple_txt));
//            tv_name_voip_one.setText(driverName);
//            if (!driverImage.isEmpty()) {
//                Picasso.get().load(driverImage).into(iv_user_image_voip_one);
//            }

            iv_cancel_call_voip_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    call.hangup();
                    try {
                        if (mp != null) {
                            if (mp.isPlaying()) {
                                mp.stop();
                                mp.release();
                            }
                        }
                        dialog.dismiss();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
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
                        if (mp != null) {
                            if (mp.isPlaying()) {
                                mp.stop();
                                mp.release();
                            }
                        }
                        call.answer();
                        audioManager.setMicrophoneMute(false);
                        audioManager.setSpeakerphoneOn(false);
                        iv_recv_call_voip_one.setClickable(false);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
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

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mute(AudioManager audioManager, ImageView iv_mute) {
        if (!audioManager.isMicrophoneMute()) {
            audioManager.setMicrophoneMute(true);
            iv_mute.setImageResource(R.drawable.clicked_mute);
        } else {
            audioManager.setMicrophoneMute(false);
            iv_mute.setImageResource(R.drawable.mute_bt);
        }
    }

    private void getRecentPlaces(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("recent_places", "");

        Place[] rPlace = gson.fromJson(json, Place[].class);
        Place Place = new Place("Travail",
                "Casablanca, Morocco", "33.5725155", "-7.5962637", R.drawable.lieux_proches);

        if (rPlace == null || rPlace.length == 0) {
            rPlaceDataList.add(Place);
        } else {
            rPlaceDataList.addAll(Arrays.asList(rPlace));
        }
        rPlaceAdapter.notifyDataSetChanged();
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

                    } else
                        Toast.makeText(getApplicationContext(), resources.getString(R.string.promocode_validation_txt), Toast.LENGTH_LONG).show();
                }
            });

            dialog.setContentView(view);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showFavoritsAndRecents() {
        rPlaceDataList.clear();
        getRecentPlaces(MapsActivityNew.this);

        AnimateConstraint.animate(MapsActivityNew.this, fevouriteLayout, HeightAbsolute, 1, 100);

        findViewById(R.id.imageView7).setVisibility(View.VISIBLE);
        findViewById(R.id.x).setVisibility(View.VISIBLE);
        findViewById(R.id.my_position).setVisibility(View.GONE);
        findViewById(R.id.adress_result).setVisibility(View.INVISIBLE);
    }

    private void hideAllUI() {
//        startConstraint.setVisibility(View.INVISIBLE);
        etSearchStartAddress.setVisibility(View.INVISIBLE);
        gooImage.setVisibility(View.INVISIBLE);
        arrowStartEndImage.setVisibility(View.INVISIBLE);
        rlEndPoint.setVisibility(View.GONE);
    }

    private void goToLocation(Context context, Double lat, Double lng, Place rPlace) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        Log.e(TAG, "goToLocation: " + lat);
        Log.e(TAG, "goToLocation: " + lng);
        etSearchStartAddress.setText(Utility.getCompleteAddressString(context, lat, lng));
        etSearchDestination.setText(Utility.getCompleteAddressString(context, lat, lng));
    }

    private void showAllUI() {
//        startConstraint.setVisibility(View.VISIBLE);
        etSearchDestination.setVisibility(View.VISIBLE);
        gooImage.setVisibility(View.VISIBLE);
        arrowStartEndImage.setVisibility(View.VISIBLE);
        callLayout.setVisibility(View.GONE);
        findViewById(R.id.destPoint).setVisibility(View.VISIBLE);
    }


    private void setupPlayAudio(final String outputeFile, final View playAudio, final View pauseAudio, final MediaPlayer mediaPlayer) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && RESULT_OK == -1 && data.hasExtra("result")) {
            voipTv.setClickable(true);
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
}
