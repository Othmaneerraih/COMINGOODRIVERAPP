package com.comingoo.user.comingoo.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
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
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comingoo.user.comingoo.Interfaces.Userinformation;
import com.comingoo.user.comingoo.ViewModel.MapsActivityVM;
import com.comingoo.user.comingoo.async.ReadTask;
import com.comingoo.user.comingoo.async.ReverseGeocodingTask;
import com.comingoo.user.comingoo.model.MyPlace;
import com.comingoo.user.comingoo.utility.AnimateConstraint;
import com.comingoo.user.comingoo.Interfaces.PickLocation;
import com.comingoo.user.comingoo.utility.LocalHelper;
import com.comingoo.user.comingoo.model.LocationInitializer;
import com.comingoo.user.comingoo.R;
import com.comingoo.user.comingoo.utility.SharedPreferenceTask;
import com.comingoo.user.comingoo.adapters.FavouritePlaceAdapter;
import com.comingoo.user.comingoo.adapters.MyPlaceAdapter;
import com.comingoo.user.comingoo.utility.Utility;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
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
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, PickLocation {
    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private EditText searchEditText;
    private EditText searchDestEditText;
    private ImageButton searchButtonDest;
    private ConstraintLayout bottomMenu;
    private CircleImageView selectedOp;
    private ImageView shadowBg;
    private ConstraintLayout selectStart;
    private ConstraintLayout selectDest;
    private Button confirmStart;
    private ProgressBar searchProgBar;
    private ProgressBar searchProgBarDest;
    public static TextView promoCode;
    private ImageView ivPromocode;
    private MyPlaceAdapter placeAdapter;
    private FavouritePlaceAdapter fPlaceAdapter;
    private MyPlaceAdapter rPlaceAdapter;
    private ArrayList<MyPlace> placeDataList;
    private ArrayList<MyPlace> fPlaceDataList;
    private boolean audioRecorded = false;
    private ImageButton recordButton, playAudio, pauseAudio, deleteAudio;
    private String outputeFile;
    private boolean courseScreenIsOn = false;
    private RelativeLayout callLayout;
    private TextView tv_appelle_voip, tv_appelle_telephone;
    private LinearLayout voip_view;
    private RecyclerView mLocationView;
    private ArrayList<MyPlace> rPlaceDataList;
    private ConstraintLayout startConstraint;
    private ConstraintLayout endConstraint;
    private GeoDataClient mGeoDataClient;
    private int state = 0;

    private String language;
    private Context co;
    private LatLng userLatLng;
    private LatLng startLatLng;
    private LatLng destLatLng;
    private int orderDriverState;
    private boolean isFocusableNeeded = true;
    private FrameLayout frameLayout;
    private FrameLayout frameLayout2;
    private FrameLayout frameLayout3;
    private TextView frameTime;
    private TextView closestDriverText;
    private ConstraintLayout favorite;
    private ConstraintLayout aR;
    private ConstraintLayout fR;
    private float dpHeight;
    private float dpWidth;
    private Button coverButton, passer;
    private ImageButton X;
    private ImageButton positionButton;
    private ImageButton cancelRequest;
    private RippleBackground rippleBackground;
    private Button confirmDest;
    private int Height;
    private int HeightAbsolute;
    private ConstraintLayout citySelectLayout;
    private String searchLoc;
    private TextView city;
    private boolean courseScreenStageZero = false;
    private boolean courseScreenStageOne = false;
    private Marker driverPosMarker;
    private Marker startPositionMarker;
    private boolean blockingTimeOver = true;
    private ImageButton menuButton;
    private ImageButton gooButton;
    private String startCity;
    private String destCity;
    private float distance;
    private TextView price;


    private ImageView locationStartPin;
    private ImageView locationDestPin;
    private ImageView locationPinStart;
    private ImageView locationPinDriver;
    private ImageView locationPinDest;
    private ImageView profileImage;
    private TextView tvUserName;
    private ImageView destArrow;
    private CircleImageView selectedOpImage;
    private ImageButton deleviryButton;
    private ImageButton carButton;
    private ImageButton selectCity;
    private RelativeLayout gooBox;
    private ConstraintLayout acceuil, invite, historique, inbox, aide, logout;
    private ConstraintLayout ComingoonYou;
    private Utility utility;
    private boolean isLoud = false;
    private MediaPlayer mp;
    private TextView callState;
    private TextView caller_name;
    private CircleImageView iv_cancel_call_voip_one;
    private CircleImageView iv_mute;
    private CircleImageView iv_loud;
    private CircleImageView iv_recv_call_voip_one;
    //    private RelativeLayout rlCallLayout;
    private RelativeLayout.LayoutParams params;
    private Handler mHandler = new Handler();
    private int mHour, mMinute;
    private Resources resources;
    private RatingBar rbDriverRating;
    private boolean ispopupShowed = false;

    private MapsActivityVM mapsActivityVM;
    private TextView driverNameL, iv_total_ride_number, iv_car_number, iv_total_rating_number;
    private CircularImageView driverImageL;
    private ImageView ivCallDriver, close_button;
    private ImageView ivCross;
    private ArrayList<String> driversKeys;
    private ArrayList<String> driversLocations;
    private ArrayList<String> driversKeysHold;
    private GeoQuery geoQuery;
    private int lastImageWidth;
    private String userName;
    private String courseIDT;
    private String statusT = "";
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
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private String userId, userImage, userEmail, phoneNumber;
    private DatabaseReference rootRef;
    private Date startTime;

    private Bitmap scaleBitmap(int reqWidth, int reqHeight, int resId) {
//        try {
        // Raw height and width of image
        Bitmap bitmap;
        BitmapFactory.Options bOptions = new BitmapFactory.Options();
        bOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resId, bOptions);
        int imageHeight = bOptions.outHeight;
        int imageWidth = bOptions.outWidth;

        int inSampleSize = 1;

        if (imageHeight > reqHeight || imageWidth > reqWidth) {

            int lastImageHeight = imageHeight / 2;
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
        bitmap = BitmapFactory.decodeResource(getResources(), resId, bOptions);
        return bitmap;
//        }catch (OutOfMemoryError e){
//            e.printStackTrace();
//            return null;
//        } catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
    }

    private void loadImages() {
        locationStartPin.setImageBitmap(scaleBitmap(76, 56, R.drawable.depart_pin));
        locationDestPin.setImageBitmap(scaleBitmap(76, 56, R.drawable.destination_pin));
        locationPinStart.setImageBitmap(scaleBitmap(76, 56, R.drawable.depart_pin));
        locationPinDriver.setImageBitmap(scaleBitmap(76, 56, R.drawable.driver_pin));
        locationPinDest.setImageBitmap(scaleBitmap(76, 56, R.drawable.destination_pin));
        selectedOpImage.setImageBitmap(scaleBitmap(70, 70, R.drawable.wheel_icon));
        deleviryButton.setImageBitmap(scaleBitmap((int) (dpWidth / 2), 55, R.drawable.delivery_icon));
        carButton.setImageBitmap(scaleBitmap((int) (dpWidth / 2), 55, R.drawable.car_icon));
        selectCity.setImageBitmap(scaleBitmap(10, 15, R.drawable.city_arrow));
        destArrow.setImageBitmap(scaleBitmap(26, 190, R.drawable.arrow));
        menuButton.setImageBitmap(scaleBitmap(35, 35, R.drawable.home_icon));
        positionButton.setImageBitmap(scaleBitmap(40, 37, R.drawable.my_position_icon));
        shadowBg.setImageBitmap(scaleBitmap((int) dpWidth, 80, R.drawable.shadow_bottom));
    }

    @Override
    public void pickedLocation(MyPlace myPlace) {

        if (orderDriverState == 0)   selectStart.setVisibility(View.VISIBLE);
        else if (orderDriverState == 1) selectDest.setVisibility(View.VISIBLE);

        searchEditText.setFocusable(false);
        searchEditText.setFocusableInTouchMode(false);
        coverButton.setClickable(false);
        positionButton.setVisibility(View.VISIBLE);
        findViewById(R.id.shadow).setVisibility(View.VISIBLE);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Double.parseDouble(myPlace.getLat()), Double.parseDouble(myPlace.getLng())))
                .zoom(17)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                searchEditText.setFocusable(true);
                searchEditText.setFocusableInTouchMode(true);
                coverButton.setClickable(true);
            }

            @Override
            public void onCancel() {

            }
        });

        if (!contains(rPlaceDataList, myPlace)) {
            myPlace.setImage(R.drawable.lieux_proches);
            rPlaceDataList.add(myPlace);
            saveRecentPlaces(MapsActivity.this, rPlaceDataList);
            rPlaceAdapter.notifyDataSetChanged();
        }
        hideSearchAddressStartUI();
        isFocusableNeeded = false;
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            Toast.makeText(MapsActivity.this, resources.getString(R.string.incoming_call_txt), Toast.LENGTH_SHORT).show();
            try {
                if (VoipCallingActivity.activity != null)
                    if (!VoipCallingActivity.activity.isFinishing())
                        VoipCallingActivity.activity.finish();
                Log.e(TAG, "onIncomingCall: " + incomingCall.getCallId());
                showDialog(MapsActivity.this, incomingCall);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private Runnable mUpdate = new Runnable() {

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

    public void showDialog(final Context context, final Call call) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_incomming_call, null, false);
            dialog.setContentView(view);

            RelativeLayout relativeLayout = dialog.findViewById(R.id.incoming_call_view);
            CircleImageView iv_user_image_voip_one = dialog.findViewById(R.id.iv_user_image_voip_one);
            iv_cancel_call_voip_one = dialog.findViewById(R.id.iv_cancel_call_voip_one);
            iv_recv_call_voip_one = dialog.findViewById(R.id.iv_recv_call_voip_one);
            caller_name = dialog.findViewById(R.id.callerName);
            callState = dialog.findViewById(R.id.callState);

            iv_mute = dialog.findViewById(R.id.iv_mute);
            iv_loud = dialog.findViewById(R.id.iv_loud);
            TextView tv_name_voip_one = dialog.findViewById(R.id.tv_name_voip_one);
            iv_mute.setVisibility(View.GONE);
            iv_loud.setVisibility(View.GONE);
//        iv_recv_call_voip_one.setEnabled(true);
            iv_recv_call_voip_one.setClickable(true);

            final AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.setMicrophoneMute(false);
            audioManager.setSpeakerphoneOn(false);

            mp = MediaPlayer.create(this, R.raw.ring);
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

            if (ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivity.this,
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
                    mute(audioManager);
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

    private void mute(AudioManager audioManager) {
        if (!audioManager.isMicrophoneMute()) {
            audioManager.setMicrophoneMute(true);
            iv_mute.setImageResource(R.drawable.clicked_mute);
        } else {
            audioManager.setMicrophoneMute(false);
            iv_mute.setImageResource(R.drawable.mute_bt);
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
            // Do something like display a progress bar
        }


        @Override
        protected String doInBackground(String... params) {
            FirebaseDatabase.getInstance().getReference("COURSES").orderByChild("client").
                    equalTo(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        try {
                            for (final DataSnapshot data : dataSnapshot.getChildren()) {
                                try {
                                    if (Objects.equals(data.child("driverPosLat").getValue(String.class), "") || Objects.equals(data.child("driverPosLong").getValue(String.class), "") || data.child("startLat").getValue(String.class).equals("") || data.child("startLong").getValue(String.class).equals("")) {
                                        driverPosT = new LatLng(0.0,
                                                0.0);

                                        startPositionT = new LatLng(0.0,
                                                0.0);
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
                                                        String newString = avg.replace(",", ".");
                                                        if (newString.equals("")) {
                                                            iv_total_rating_number.setText("4.0");
                                                        } else
                                                            iv_total_rating_number.setText(newString);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    iv_total_rating_number.setText("4.0");
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
                                                            handleCourseCallBack();
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
//                        statusT = "4";
//                        handleCourseCallBack();
                        if (courseScreenIsOn) {
                            courseScreenIsOn = false;
                            courseScreenStageZero = false;
                            courseScreenStageOne = false;
                            findViewById(R.id.pin).setVisibility(View.VISIBLE);
                            state = 0;
                            cancelCommandLayout();
                            callLayout.setVisibility(View.GONE);
                            hideSelectDestUI();
                            coverButton.setClickable(true);
                        }
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
        }
    }

    private void handleCourseCallBack() {
        try {
            if (statusT.equals("3")) {
                if (!ispopupShowed)
                    new checkFinishedCourse().execute();
                mMap.clear();
                positionButton.setVisibility(View.VISIBLE);
                if (courseScreenIsOn) {
                    courseScreenIsOn = false;
                    courseScreenStageZero = false;
                    courseScreenStageOne = false;
                    findViewById(R.id.pin).setVisibility(View.VISIBLE);
                    state = 0;
                    cancelCommandLayout();
                    callLayout.setVisibility(View.GONE);
                    hideSelectDestUI();
                    coverButton.setClickable(true);
                }

                findViewById(R.id.buttonsLayout).setVisibility(View.VISIBLE);
                return;
            }

            if (statusT.equals("1")) {
                callLayout.setVisibility(View.VISIBLE);
            }

            stopSearchUI();
            shadowBg.setVisibility(View.VISIBLE);
            menuButton.setImageBitmap(scaleBitmap(35, 35, R.drawable.home_icon));
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // mDrawer.openMenu(true);
                    ConstraintLayout contentConstraint = findViewById(R.id.contentLayout);
                    ConstraintLayout contentBlocker = findViewById(R.id.contentBlocker);
                    AnimateConstraint.resideAnimation(MapsActivity.this, contentConstraint, contentBlocker, (int) dpWidth, (int) dpHeight, 200);
                }
            });


            destArrow.setVisibility(View.VISIBLE);

            if (!courseScreenIsOn) {
                courseScreenIsOn = true;
                mMap.clear();

                searchEditText.setEnabled(false);
                searchDestEditText.setEnabled(false);
                coverButton.setClickable(false);

                searchEditText.setText(startText);
                searchDestEditText.setText(endText);

                findViewById(R.id.pin).setVisibility(View.GONE);

                AnimateConstraint.fadeIn(MapsActivity.this, bottomMenu, 500, 0);
                AnimateConstraint.fadeIn(MapsActivity.this, selectedOpImage, 500, 0);
                AnimateConstraint.fadeIn(MapsActivity.this, callLayout, 500, 0);
                findViewById(R.id.gooContent).setVisibility(View.GONE);
                cancelRequest.setVisibility(View.GONE);

                searchEditText.setCompoundDrawables(null, null, null, null);
                searchDestEditText.setCompoundDrawables(null, null, null, null);

                AnimateConstraint.animate(MapsActivity.this, startConstraint, (dpHeight - 135), 100, 0);
                AnimateConstraint.animate(MapsActivity.this, endConstraint, dpHeight - 110, 180, 0);


                findViewById(R.id.buttonsLayout).setVisibility(View.VISIBLE);

                driverNameL.setText(driverName);
                iv_car_number.setText(driverCarDescription);
                iv_total_ride_number.setText(driverCarName);
                if (driverImage != null) {
                    if (driverImage.length() > 0) {
                        Picasso.get().load(driverImage).into(driverImageL);
                    }
                }

                LayerDrawable stars = (LayerDrawable) rbDriverRating.getProgressDrawable();
                stars.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

                if (ContextCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MapsActivity.this,
                            new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            }, 1);
                }

            }

            if (statusT.equals("0") && !courseScreenStageZero) {
                confirmStart.setVisibility(View.GONE);
                startTime = Calendar.getInstance().getTime();

                if (!userLevel.equals("2")) {
                    ivCallDriver.setVisibility(View.VISIBLE);
                    ivCallDriver.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            close_button.setVisibility(View.VISIBLE);
                            ivCallDriver.setVisibility(View.GONE);
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
                                Intent intent = new Intent(MapsActivity.this, VoipCallingActivity.class);
                                intent.putExtra("driverId", driverIDT);
                                intent.putExtra("userId", userId);
                                intent.putExtra("driverName", driverName);
                                intent.putExtra("driverImage", driverImage);
                                startActivityForResult(intent, 1);
                            }
                        }
                    });
                }

                courseScreenStageZero = true;
                try {
                    final Dialog dialog = new Dialog(MapsActivity.this);
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
                ivCross.setVisibility(View.VISIBLE);
                ivCross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rideCancelDialog();
                    }
                });

                if (driverPosMarker != null)
                    driverPosMarker.remove();

                if (startPositionMarker != null)
                    startPositionMarker.remove();


                frameLayout3.setDrawingCacheEnabled(true);
                frameLayout3.buildDrawingCache();
                Bitmap bm = frameLayout3.getDrawingCache();
                driverPosMarker = mMap.addMarker(new MarkerOptions()
                        .position(driverPosT)
                        .icon(BitmapDescriptorFactory.fromBitmap(bm)));

                frameLayout.setDrawingCacheEnabled(true);
                frameLayout.buildDrawingCache();
                bm = frameLayout.getDrawingCache();
                startPositionMarker = mMap.addMarker(new MarkerOptions()
                        .position(startPositionT)
                        .icon(BitmapDescriptorFactory.fromBitmap(bm)));
            }

            if (statusT.equals("1") && !courseScreenStageOne) {
                ivCross.setVisibility(View.GONE);
                voip_view.setVisibility(View.GONE);
                callLayout.setVisibility(View.VISIBLE);
                if (!userLevel.equals("2")) {
                    ivCallDriver.setVisibility(View.VISIBLE);
                    ivCallDriver.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            close_button.setVisibility(View.VISIBLE);
                            ivCallDriver.setVisibility(View.GONE);
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
                                Intent intent = new Intent(MapsActivity.this, VoipCallingActivity.class);
                                intent.putExtra("driverId", driverIDT);
                                intent.putExtra("userId", userId);
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
                    final Dialog dialog = new Dialog(MapsActivity.this);
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
                } catch (WindowManager.BadTokenException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                frameLayout.setDrawingCacheEnabled(true);
                frameLayout.buildDrawingCache();


                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(startPositionT)      // Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                Bitmap bm = frameLayout3.getDrawingCache();
                startPositionMarker = mMap.addMarker(new MarkerOptions()
                        .position(startPositionT)
                        .icon(BitmapDescriptorFactory.fromBitmap(bm)));

            }
            if (statusT.equals("2")) {
                mMap.clear();
                frameLayout3.setDrawingCacheEnabled(true);
                frameLayout3.buildDrawingCache();
                Bitmap bm = frameLayout3.getDrawingCache();
                mMap.addMarker(new MarkerOptions()
                        .position(driverPosT)
                        .icon(BitmapDescriptorFactory.fromBitmap(bm)));
                ispopupShowed = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    private void checkLocationService() {
        try {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MapsActivity.this);
            dialog.setMessage(resources.getString(R.string.txt_location_permission));
            dialog.setPositiveButton(resources.getString(R.string.txt_open_location), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });

            dialog.setNegativeButton(getString(R.string.txt_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                }
            });

            dialog.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
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

                    mapsActivityVM.punishment(getApplicationContext(), userId, startTime);

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
                    callLayout.setVisibility(View.GONE);
                    voip_view.setVisibility(View.GONE);


                    if (preferenceTask.getCancelNumber() > 3) {
                        Toast.makeText(MapsActivity.this,
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
                    ivCross.setVisibility(View.GONE);

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

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int RATE = 0;
    String tagStatus;
    private String COURSE;
    private String choseBox;
    //    private String dialogDriverId = "";
    private DecimalFormat df2 = new DecimalFormat(".##");

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
            try {
                ispopupShowed = true;
                FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).child("COURSE").
                        addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    COURSE = dataSnapshot.getValue(String.class);
                                    FirebaseDatabase.getInstance().getReference("CLIENTFINISHEDCOURSES").child(userId).child(dataSnapshot.getValue(String.class)).
                                            addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshott) {
                                                    callLayout.setVisibility(View.GONE);

                                                    if (ivCross != null)
                                                        ivCross.setVisibility(View.GONE);

                                                    if (voip_view != null)
                                                        voip_view.setVisibility(View.GONE);

                                                    // finishing promo code
                                                    FirebaseDatabase.getInstance().getReference("clientUSERS").
                                                            child(userId).child("PROMOCODE").removeValue();

                                                    try {
                                                        final Dialog dialog = new Dialog(MapsActivity.this);
                                                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                        View view = inflater.inflate(R.layout.dialog_finished_course, null, false);
                                                        dialog.setContentView(view);
                                                        TextView textView13 = view.findViewById(R.id.textView13);
                                                        TextView textView14 = view.findViewById(R.id.textView14);

                                                        //Set Texts
                                                        textView13.setText(resources.getString(R.string.Montanttotalàpayer));
                                                        textView14.setText(resources.getString(R.string.Evaluezvotreéxperience));


                                                        RelativeLayout body = view.findViewById(R.id.body);
                                                        body.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) dpWidth, (int) dpWidth, R.drawable.finished_bg)));

                                                        final Button price = view.findViewById(R.id.button3);

                                                        if (courseIDT != null) {
                                                            FirebaseDatabase.getInstance().getReference("COURSES").child(courseIDT).child("price").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    try {
                                                                        if (dataSnapshot.getValue(String.class) != null) {
                                                                            double finalPriceOfCourse = Double.parseDouble(Objects.requireNonNull(dataSnapshot.getValue(String.class)));
                                                                            price.setText(df2.format(finalPriceOfCourse) + " MAD");
                                                                        }
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });

                                                        }

                                                        final Button star1 = view.findViewById(R.id.star1);
                                                        final Button star2 = view.findViewById(R.id.star2);
                                                        final Button star3 = view.findViewById(R.id.star3);
                                                        final Button star4 = view.findViewById(R.id.star4);
                                                        final Button star5 = view.findViewById(R.id.star5);

                                                        final ImageButton nextButton = view.findViewById(R.id.next);

                                                        final ImageView imot = view.findViewById(R.id.stars_rating);

                                                        dialog.setCancelable(false);
                                                        dialog.setCanceledOnTouchOutside(false);

                                                        // defaul rate
                                                        RATE = 4;
                                                        star1.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.normal_star)));
                                                        star2.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.normal_star)));
                                                        star3.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.normal_star)));
                                                        star4.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.selected_star)));
                                                        star5.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.unselected_star)));
                                                        imot.setImageBitmap(scaleBitmap(150, 150, R.drawable.four_stars));

                                                        star1.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                RATE = 1;

                                                                star1.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.selected_star)));
                                                                star2.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.unselected_star)));
                                                                star3.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.unselected_star)));
                                                                star4.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.unselected_star)));
                                                                star5.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.unselected_star)));

                                                                imot.setImageBitmap(scaleBitmap(150, 150, R.drawable.one_star));
                                                            }
                                                        });
                                                        star2.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                RATE = 2;

                                                                star1.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.normal_star)));
                                                                star2.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.selected_star)));
                                                                star3.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.unselected_star)));
                                                                star4.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.unselected_star)));
                                                                star5.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.unselected_star)));

                                                                imot.setImageBitmap(scaleBitmap(150, 150, R.drawable.two_stars));
                                                            }
                                                        });
                                                        star3.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                RATE = 3;

                                                                star1.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.normal_star)));
                                                                star2.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.normal_star)));
                                                                star3.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.selected_star)));
                                                                star4.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.unselected_star)));
                                                                star5.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.unselected_star)));

                                                                imot.setImageBitmap(scaleBitmap(150, 150, R.drawable.three_stars));
                                                            }
                                                        });
                                                        star4.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                RATE = 4;

                                                                star1.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.normal_star)));
                                                                star2.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.normal_star)));
                                                                star3.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.normal_star)));
                                                                star4.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.selected_star)));
                                                                star5.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.unselected_star)));

                                                                imot.setImageBitmap(scaleBitmap(150, 150, R.drawable.four_stars));
                                                            }
                                                        });

                                                        star5.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                RATE = 5;
                                                                star1.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.normal_star)));
                                                                star2.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.normal_star)));
                                                                star3.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.normal_star)));
                                                                star4.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.normal_star)));
                                                                star5.setBackground(new BitmapDrawable(getResources(), scaleBitmap(45, 45, R.drawable.selected_star)));

                                                                imot.setImageBitmap(scaleBitmap(150, 150, R.drawable.five_stars));
                                                            }
                                                        });

                                                        nextButton.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                try {
                                                                    if (RATE > 0) {
                                                                        if (!driverIDT.equals("")) {
                                                                            FirebaseDatabase.getInstance().getReference("DRIVERUSERS").child(driverIDT).child("rating").child(Integer.toString(RATE)).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                    if (dataSnapshot.exists()) {
                                                                                        int Rating = Integer.parseInt(Objects.requireNonNull(dataSnapshot.getValue(String.class))) + 1;
                                                                                        FirebaseDatabase.getInstance().getReference("DRIVERUSERS").child(driverIDT).child("rating").child(Integer.toString(RATE)).setValue("" + Rating);
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                }
                                                                            });

                                                                            FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).child("COURSE").removeValue();

                                                                            if (RATE > 3) {
                                                                                if (ContextCompat.checkSelfPermission(MapsActivity.this,
                                                                                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                                                    ActivityCompat.requestPermissions(MapsActivity.this,
                                                                                            new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
                                                                                } else {
                                                                                    showVoiceDialog();
                                                                                }
                                                                            } else {
                                                                                try {
                                                                                    final Dialog newDialog = new Dialog(MapsActivity.this);

                                                                                    newDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                                                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                                                    View view = inflater.inflate(R.layout.dialog_finished_course_2, null, false);
                                                                                    newDialog.setContentView(view);
                                                                                    choseBox = null;

                                                                                    TextView textView15 = view.findViewById(R.id.textView15);
                                                                                    TextView textView16 = view.findViewById(R.id.textView16);
                                                                                    Button button5 = view.findViewById(R.id.button5);
                                                                                    Button button7 = view.findViewById(R.id.button7);
                                                                                    Button button8 = view.findViewById(R.id.button8);
                                                                                    Button button9 = view.findViewById(R.id.button9);
                                                                                    Button button10 = view.findViewById(R.id.button10);

                                                                                    //Set Texts
                                                                                    textView15.setText(resources.getString(R.string.Noussommesdésolé));
                                                                                    textView16.setText(resources.getString(R.string.whatswrong));
                                                                                    button5.setText(resources.getString(R.string.Heuredarrivée));
                                                                                    button7.setText(resources.getString(R.string.Etatdelavoiture));
                                                                                    button8.setText(resources.getString(R.string.Conduite));
                                                                                    button9.setText(resources.getString(R.string.Itinéraire));
                                                                                    button10.setText(resources.getString(R.string.Autre));

                                                                                    RelativeLayout body = view.findViewById(R.id.body);
                                                                                    body.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) dpWidth, (int) dpWidth, R.drawable.finished_bg)));

                                                                                    final Button opt1 = view.findViewById(R.id.button5);
                                                                                    final Button opt2 = view.findViewById(R.id.button6);
                                                                                    final Button opt3 = view.findViewById(R.id.button7);
                                                                                    final Button opt4 = view.findViewById(R.id.button8);
                                                                                    final Button opt5 = view.findViewById(R.id.button9);
                                                                                    final Button opt6 = view.findViewById(R.id.button10);

                                                                                    final EditText messageText = view.findViewById(R.id.editText);

                                                                                    opt1.setOnClickListener(new View.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(View v) {
                                                                                            opt1.setBackgroundResource(R.drawable.box_shadow);
                                                                                            opt2.setBackgroundResource(R.drawable.select_box);
                                                                                            opt3.setBackgroundResource(R.drawable.select_box);
                                                                                            opt4.setBackgroundResource(R.drawable.select_box);
                                                                                            opt5.setBackgroundResource(R.drawable.select_box);
                                                                                            opt6.setBackgroundResource(R.drawable.select_box);

                                                                                            choseBox = resources.getString(R.string.arrive_txt);
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

                                                                                            choseBox = resources.getString(R.string.service_txt);
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

                                                                                            choseBox = resources.getString(R.string.voiture_txt);
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
                                                                                            choseBox = resources.getString(R.string.conduite_txt);
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
                                                                                            choseBox = resources.getString(R.string.itineraire_txt);
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
                                                                                            choseBox = resources.getString(R.string.autre_txt);
                                                                                        }
                                                                                    });

                                                                                    newDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                                                        @Override
                                                                                        public void onDismiss(DialogInterface dialog) {
                                                                                            if (ContextCompat.checkSelfPermission(MapsActivity.this,
                                                                                                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                                                                ActivityCompat.requestPermissions(MapsActivity.this,
                                                                                                        new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
                                                                                            } else {
                                                                                                showVoiceDialog();
                                                                                            }
                                                                                        }
                                                                                    });

                                                                                    ImageButton nextBtn = newDialog.findViewById(R.id.imageButton3);

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

                                                                                    newDialog.findViewById(R.id.body).getLayoutParams().width = (int)
                                                                                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (dpWidth), getResources().getDisplayMetrics());

                                                                                    WindowManager.LayoutParams lp = Objects.requireNonNull(newDialog.getWindow()).getAttributes();
                                                                                    lp.dimAmount = 0.5f;
                                                                                    newDialog.getWindow().setAttributes(lp);
                                                                                    newDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                                                                    newDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                                                                    newDialog.show();

                                                                                } catch (WindowManager.BadTokenException e) {
                                                                                    Log.e(TAG, "BadTokenException:excp " + e.getMessage());
                                                                                    e.printStackTrace();
                                                                                } catch (IllegalStateException e) {
                                                                                    Log.e(TAG, "IllegalStateException:excp " + e.getMessage());
                                                                                    e.printStackTrace();
                                                                                } catch (Exception e) {
                                                                                    Log.e(TAG, "Exception:excp " + e.getMessage());
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        }
                                                                    }

                                                                } catch (WindowManager.BadTokenException e) {
                                                                    e.printStackTrace();
                                                                } catch (IllegalStateException e) {
                                                                    e.printStackTrace();
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                                dialog.dismiss();
                                                            }
                                                        });


                                                        dialog.show();

                                                        dialog.findViewById(R.id.body).getLayoutParams().width = (int)
                                                                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                                                        (int) (dpWidth), getResources().getDisplayMetrics());


                                                        WindowManager.LayoutParams lp = Objects.requireNonNull(dialog.getWindow()).getAttributes();
                                                        lp.dimAmount = 0.5f;
                                                        dialog.getWindow().setAttributes(lp);
                                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                                        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                                    } catch (WindowManager.BadTokenException e) {
                                                        e.printStackTrace();
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
            } catch (Exception e) {
                e.printStackTrace();
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

    private void showVoiceDialog() {
        try {
            final Dialog newDialog = new Dialog(MapsActivity.this);

            newDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_voice_record, null, false);
            newDialog.setContentView(view);

            TextView textView18 = view.findViewById(R.id.tv_destination);
            TextView textView19 = view.findViewById(R.id.textView19);
            TextView textView20 = view.findViewById(R.id.textView20);


            //Set Texts
            textView18.setText(resources.getString(R.string.Appreciate));
            textView19.setText(resources.getString(R.string.PS));
            textView20.setText(resources.getString(R.string.Record));


            ImageButton nextBtn = view.findViewById(R.id.imageButton6);
            TextView name = view.findViewById(R.id.textView17);


            recordButton = view.findViewById(R.id.recordAudio);
            playAudio = view.findViewById(R.id.playAudio);
            pauseAudio = view.findViewById(R.id.pauseAudio);
            deleteAudio = view.findViewById(R.id.deleteAudio);
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
                    Toast.makeText(MapsActivity.this, resources.getString(R.string.thank_you_txt), Toast.LENGTH_SHORT).show();
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

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isPlaying = false;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initialize();
        mapsActivityVM = new MapsActivityVM();

        language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");
        co = LocalHelper.setLocale(MapsActivity.this, language);
        resources = co.getResources();

        if (!isNetworkConnectionAvailable()) {
            checkNetworkConnection();
        }
        displayLocationSettingsRequest(MapsActivity.this);
        SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
        userId = prefs.getString("userID", null);

        if (userId == null || FirebaseAuth.getInstance().getCurrentUser() == null) {
            prefs.edit().remove("userID");
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MapsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mapsActivityVM.checkUserTask(MapsActivity.this, new Userinformation() {
            @Override
            public void gettingUserInfo(String name, String image, String email, String number) {
                userName = name;
                userImage = image;
                userEmail = email;
                phoneNumber = number;
                tvUserName.setText(userName);
                if (userImage != null) {
                    if (userImage.length() > 0) {
                        Picasso.get().load(userImage).centerCrop().fit().into(profileImage);
                    }
                }
            }
        });

        AnimateConstraint.fadeOut(MapsActivity.this, findViewById(R.id.loadingScreen), 500, 10);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.loadingScreen).setVisibility(View.GONE);
            }
        }, 500);

        ComingoonYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, ComingooAndYouActivity.class);
                intent.putExtra("image", userImage);
                intent.putExtra("name", userName);
                intent.putExtra("email", userEmail);

                if (phoneNumber != null && phoneNumber.contains("+212")) {
                    phoneNumber = phoneNumber.replace("+212", "");
                }
                intent.putExtra("phone", phoneNumber);
                startActivity(intent);
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        promoCode = findViewById(R.id.promoCode);
        ivPromocode = findViewById(R.id.iv_promo_code);
        promoCode.setText(resources.getString(R.string.promocode_txt));
        callLayout = findViewById(R.id.rl_calling);

        ivCallDriver = findViewById(R.id.iv_call_driver);

        utility = new Utility();

        driverNameL = findViewById(R.id.tv_driver_name);
        iv_total_rating_number = findViewById(R.id.iv_total_rating_number);
        driverImageL = findViewById(R.id.iv_driver_image);
        iv_car_number = findViewById(R.id.iv_car_number);
        iv_total_ride_number = findViewById(R.id.iv_total_ride_number);
        rbDriverRating = findViewById(R.id.rb_user);
        voip_view = findViewById(R.id.voip_view);
        tv_appelle_voip = findViewById(R.id.tv_appelle_voip);
        tv_appelle_telephone = findViewById(R.id.tv_appelle_telephone);

        tv_appelle_voip.setClickable(true);

        close_button = findViewById(R.id.close_button);

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_button.setVisibility(View.GONE);
                ivCallDriver.setVisibility(View.VISIBLE);
                voip_view.setVisibility(View.GONE);
            }
        });


        driversKeys = new ArrayList<>();
        driversLocations = new ArrayList<>();
        driversKeysHold = new ArrayList<>();


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


        orderDriverState = 0;


        searchLoc = "Casablanca";

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        dpHeight = outMetrics.heightPixels / density;
        dpWidth = outMetrics.widthPixels / density;
        HeightAbsolute = (int) dpHeight - (200);


        ConstraintLayout rR = findViewById(R.id.recent);

        userLatLng = null;
        startLatLng = null;
        destLatLng = null;

        mGeoDataClient = Places.getGeoDataClient(this);


        placeDataList = new ArrayList<>();
        fPlaceDataList = new ArrayList<>();
        rPlaceDataList = new ArrayList<>();

        mLocationView = findViewById(R.id.my_recycler_view);
        mLocationView.setHasFixedSize(true);
        mLocationView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView fLocationView = findViewById(R.id.favorite_recycler);
        fLocationView.setHasFixedSize(true);
        fLocationView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView rLocationView = findViewById(R.id.recent_recycler);
        rLocationView.setHasFixedSize(true);
        rLocationView.setLayoutManager(new LinearLayoutManager(this));

        placeAdapter = new MyPlaceAdapter(getApplicationContext(), placeDataList, false, userId, this);
        mLocationView.setAdapter(placeAdapter);

        fPlaceAdapter = new FavouritePlaceAdapter(getApplicationContext(), fPlaceDataList, true, userId, this);
        fLocationView.setAdapter(fPlaceAdapter);

        rPlaceAdapter = new MyPlaceAdapter(getApplicationContext(), rPlaceDataList, false, userId, this);
        rLocationView.setAdapter(rPlaceAdapter);

        ivCallDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_button.setVisibility(View.VISIBLE);
                ivCallDriver.setVisibility(View.GONE);
                voip_view.setVisibility(View.VISIBLE);
            }
        });

        historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, HistoriqueActivity.class));
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, InviteActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.comingoo.com/driver";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, NotificationActivity.class));
            }
        });

        aide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, AideActivity.class));
            }
        });

        promoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPromoCodeDialog(MapsActivity.this);
            }
        });

        int fHeight = 170;
        int rHeight = HeightAbsolute - fHeight - 5;

        fR.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, fHeight, getResources().getDisplayMetrics());
        rR.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rHeight, getResources().getDisplayMetrics());

        loadImages();
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
                    AnimateConstraint.fadeOut(MapsActivity.this, gooButton, 200, 10);
                    //AnimateConstraint.expandCircleAnimation(context, findViewById(R.id.gooLayout), dpHeight, dpWidth);
                    menuButton.setVisibility(View.VISIBLE);
                    startSearchUI();
                    hideAllUI();

                    try {
                        new LookForDriverTask().execute();
                        new sendRequestsTask().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.inviteText), Toast.LENGTH_LONG).show();
            }
        });

        confirmDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!searchDestEditText.getText().toString().equals("")) {
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

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstraintLayout contentConstraint = findViewById(R.id.contentLayout);
                ConstraintLayout contentBlocker = findViewById(R.id.contentBlocker);
                AnimateConstraint.resideAnimation(MapsActivity.this, contentConstraint, contentBlocker, (int) dpWidth, (int) dpHeight, 200);
            }
        });


        findViewById(R.id.select_city).setOnClickListener(new View.OnClickListener() {
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

        X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEditText.clearFocus();
                searchDestEditText.clearFocus();
                X.setVisibility(View.GONE);
                citySelectLayout.setVisibility(View.GONE);
                positionButton.setVisibility(View.VISIBLE);
                hideSearchAddressStartUI();
            }
        });

        confirmStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Begin Selecting Destination Phase
                try {
                    if (!searchEditText.getText().toString().equals("")) {
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

    }

    public void checkNetworkConnection() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle(resources.getString(R.string.no_internet_txt));
        builder.setMessage(resources.getString(R.string.internet_warning_txt));
        builder.setNegativeButton(resources.getString(R.string.close_txt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            Log.d("Network", "Connected");
            return true;
        } else {
            Log.d("Network", "Not Connected");
            return false;
        }
    }

    private void initialize() {
        citySelectLayout = findViewById(R.id.select_city);
        city = findViewById(R.id.city);
        X = findViewById(R.id.x);
        positionButton = findViewById(R.id.my_position);
        gooBox = findViewById(R.id.gooBox);
        price = findViewById(R.id.tv_mad);
        coverButton = findViewById(R.id.coverButton);

        cancelRequest = findViewById(R.id.cancelRequest);

        passer = findViewById(R.id.passer);
        ivCross = findViewById(R.id.iv_cancel_ride);

        locationStartPin = findViewById(R.id.location_start_pin);
        locationDestPin = findViewById(R.id.location_dest_pin);
        locationPinStart = findViewById(R.id.locationPin);

        menuButton = findViewById(R.id.menu_button);
        gooButton = findViewById(R.id.gooButton);
        aR = findViewById(R.id.adress_result);
        fR = findViewById(R.id.favorite);
        acceuil = findViewById(R.id.acceuil);
        historique = findViewById(R.id.historique);
        invite = findViewById(R.id.invite);
        inbox = findViewById(R.id.inbox);
        ComingoonYou = findViewById(R.id.comingoonyou);
        aide = findViewById(R.id.aide);
        logout = findViewById(R.id.logout);
        locationPinDest = findViewById(R.id.locationPinDest);
        locationPinDriver = findViewById(R.id.driver_pin);
        profileImage = findViewById(R.id.profile_image);
        tvUserName = findViewById(R.id.textView3);

        selectedOpImage = findViewById(R.id.selectedOperation);
        deleviryButton = findViewById(R.id.deliveryButton);
        carButton = findViewById(R.id.carButton);
        selectCity = findViewById(R.id.imageButton4);
        destArrow = findViewById(R.id.destArrow);
        rippleBackground = findViewById(R.id.gooVoidContent);
        frameLayout = findViewById(R.id.framelayout);
        frameLayout2 = findViewById(R.id.framelayout2);
        frameLayout3 = findViewById(R.id.framelayout3);
        frameTime = findViewById(R.id.closestDriverPin);
        closestDriverText = findViewById(R.id.closestDriver);

        searchEditText = findViewById(R.id.search_edit_text);
        searchButtonDest = findViewById(R.id.search_dest_address_button);
        searchDestEditText = findViewById(R.id.search_dest_edit_text);
        searchProgBar = findViewById(R.id.search_prog_bar);
        searchProgBarDest = findViewById(R.id.search_dest_prog_bar);

        bottomMenu = findViewById(R.id.bottomMenu);
        selectedOp = findViewById(R.id.selectedOperation);
        shadowBg = findViewById(R.id.shadow_bg);
        selectStart = findViewById(R.id.select_start);
        selectDest = findViewById(R.id.select_dest);
        confirmStart = findViewById(R.id.confirm_start);
        confirmDest = findViewById(R.id.confirm_dest);

        startConstraint = findViewById(R.id.start_edit_text);
        endConstraint = findViewById(R.id.dest_edit_text);

        favorite = findViewById(R.id.favorite_recent);
    }

    private void getRecentPlaces(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("recent_places", "");

        MyPlace[] rPlace = gson.fromJson(json, MyPlace[].class);
        MyPlace myPlace = new MyPlace("Travail",
                "Casablanca, Morocco", "33.5725155", "-7.5962637", R.drawable.lieux_proches);

        if (rPlace == null || rPlace.length == 0) {
            rPlaceDataList.add(myPlace);
        } else {
            rPlaceDataList.addAll(Arrays.asList(rPlace));
        }
        rPlaceAdapter.notifyDataSetChanged();
    }

    private void hideAllUI() {
        startConstraint.setVisibility(View.INVISIBLE);
        searchDestEditText.setVisibility(View.INVISIBLE);
        gooBox.setVisibility(View.INVISIBLE);
        destArrow.setVisibility(View.INVISIBLE);
        findViewById(R.id.destPoint).setVisibility(View.GONE);
    }

    private void showAllUI() {
        startConstraint.setVisibility(View.VISIBLE);
        searchDestEditText.setVisibility(View.VISIBLE);
        gooBox.setVisibility(View.VISIBLE);
        destArrow.setVisibility(View.VISIBLE);
        callLayout.setVisibility(View.GONE);
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

                        FirebaseDatabase.getInstance().getReference("CLIENTNOTIFICATIONS").
                                child("qsjkldjqld").child("code").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (Objects.requireNonNull(dataSnapshot.getValue()).equals(etPromoCode.getText().toString())) {

                                    FirebaseDatabase.getInstance().getReference("CLIENTNOTIFICATIONS").
                                            child("qsjkldjqld").child("value").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                price.setText(dataSnapshot.getValue() + " MAD");
                                                promoCode.setText(etPromoCode.getText().toString());
                                                userPromoCode = etPromoCode.getText().toString();

                                                FirebaseDatabase.getInstance().getReference("clientUSERS").
                                                        child(userId).child("PROMOCODE").setValue(userPromoCode);
                                                ivPromocode.setImageResource(R.drawable.ic_promo_code_ok);
                                                dialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            dialog.dismiss();
                                        }
                                    });

                                } else
                                    Toast.makeText(getApplicationContext(), resources.getString(R.string.promoce_expired_txt), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                dialog.dismiss();
                            }
                        });

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
        getRecentPlaces(MapsActivity.this);

        AnimateConstraint.animate(MapsActivity.this, favorite, HeightAbsolute, 1, 100);


        findViewById(R.id.imageView7).setVisibility(View.VISIBLE);
        findViewById(R.id.x).setVisibility(View.VISIBLE);
        findViewById(R.id.my_position).setVisibility(View.GONE);
        findViewById(R.id.adress_result).setVisibility(View.INVISIBLE);
    }


    //Check Start Position
    private boolean startPositionIsValid() {
        startCity = "casa";

        if (PolyUtil.containsLocation(startLatLng.latitude, startLatLng.longitude, LocationInitializer.casaPoly(), true) ||
                PolyUtil.containsLocation(startLatLng.latitude, startLatLng.longitude, LocationInitializer.errahmaPoly(), true)) {
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
        } else if (PolyUtil.containsLocation(startLatLng.latitude, startLatLng.longitude, LocationInitializer.rabatPoly(), true) ||
                PolyUtil.containsLocation(startLatLng.latitude, startLatLng.longitude, LocationInitializer.missingRabatPoly(), true)) {
            startCity = "rabat";
        } else {
            Toast.makeText(MapsActivity.this, resources.getString(R.string.sur_casablanca_txt), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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
                            double price1 = Math.ceil((distance) * Double.parseDouble(Objects.requireNonNull(dataSnapshot.child("km").getValue(String.class))));
                            int price2 = (int) price1;
                            if (price2 < Double.parseDouble(Objects.requireNonNull(dataSnapshot.child("minimum").getValue(String.class))))
                                price2 = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("minimum").getValue(String.class)));
                            price.setText(price2 + " MAD");
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
                    price.setText(dataSnapshot.getValue(String.class) + " MAD");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        }
    }

    private void cancelCommandLayout() {
        orderDriverState = 1;

        searchDestEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search_icon, 0);
        AnimateConstraint.animate(MapsActivity.this, endConstraint, 180, dpHeight - 20, 500, selectDest, findViewById(R.id.destArrow));
        destArrow.setVisibility(View.GONE);
        findViewById(R.id.gooContent).setVisibility(View.GONE);
        positionButton.setVisibility(View.VISIBLE);
        startConstraint.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (dpHeight - 42), getResources().getDisplayMetrics());
        shadowBg.setVisibility(View.VISIBLE);
        searchButtonDest.setVisibility(View.VISIBLE);
        menuButton.setVisibility(View.VISIBLE);
        coverButton.setVisibility(View.VISIBLE);
        searchDestEditText.setEnabled(true);
        searchDestEditText.setVisibility(View.VISIBLE);

        findViewById(R.id.locationPinDest).setVisibility(View.VISIBLE);
        mMap.clear();
        showSelectDestUI();
    }

    private void switchToCommandLayout() {
        orderDriverState = 2;
        positionButton.setVisibility(View.GONE);
        searchDestEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        AnimateConstraint.animate(MapsActivity.this, endConstraint, dpHeight - 25,
                180, 500, selectDest, findViewById(R.id.destArrow));
        AnimateConstraint.fadeIn(MapsActivity.this, findViewById(R.id.gooContent), 500, 10);

        startConstraint.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (dpHeight - 52),
                getResources().getDisplayMetrics());
        searchButtonDest.setVisibility(View.GONE);
        state = 2;

        coverButton.setVisibility(View.GONE);
        searchDestEditText.setEnabled(false);

        findViewById(R.id.locationPinDest).setVisibility(View.GONE);

        if (destLatLng != null) {
            new DrawRouteTask().execute(startLatLng, destLatLng);
            gooButton.setVisibility(View.GONE);
            frameLayout2.setDrawingCacheEnabled(true);
            frameLayout2.buildDrawingCache();
            Bitmap bm = frameLayout2.getDrawingCache();
            Marker myMarker = mMap.addMarker(new MarkerOptions()
                    .position(destLatLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(bm)));

        } else {
            gooButton.setVisibility(View.VISIBLE);
            searchDestEditText.setText("Destination non choisi.");
        }

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = 0;
                if (rippleBackground.isRippleAnimationRunning()) {
                    rippleBackground.stopRippleAnimation();
                    DatabaseReference pickupRequest = FirebaseDatabase.getInstance().
                            getReference("PICKUPREQUEST");
                    pickupRequest.removeValue();
                }
                cancelCommandLayout();
            }
        });

        gooBox.setVisibility(View.VISIBLE);
        gooButton.setVisibility(View.VISIBLE);
        cancelRequest.setVisibility(View.GONE);
    }

    private void hideSelectDestUI() {
        orderDriverState = 0;
//        callLayout.setVisibility(View.GONE);
        ivCross.setVisibility(View.GONE);
        coverButton.setClickable(true);
        voip_view.setVisibility(View.GONE);
        positionButton.setVisibility(View.VISIBLE);
        hideSearchAddressStartUI();
        confirmStart.setVisibility(View.VISIBLE);
        findViewById(R.id.shadow).setVisibility(View.VISIBLE);
        bottomMenu.setVisibility(View.VISIBLE);
        selectedOp.setVisibility(View.VISIBLE);
        shadowBg.setVisibility(View.VISIBLE);
        endConstraint.setVisibility(View.GONE);
        selectDest.setVisibility(View.GONE);
        searchEditText.setEnabled(true);

        startConstraint.setVisibility(View.VISIBLE);
        searchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.search_icon, 0);

        AnimateConstraint.animate(MapsActivity.this, startConstraint, 80, (dpHeight - 130), 500);

        findViewById(R.id.locationPin).setVisibility(View.VISIBLE);
        findViewById(R.id.closestDriver).setVisibility(View.VISIBLE);
        findViewById(R.id.locationPinDest).setVisibility(View.GONE);


        menuButton.setImageBitmap(scaleBitmap(35, 35, R.drawable.home_icon));
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout contentConstraint = findViewById(R.id.contentLayout);
                ConstraintLayout contentBlocker = findViewById(R.id.contentBlocker);
                AnimateConstraint.resideAnimation(MapsActivity.this, contentConstraint, contentBlocker, (int) dpWidth, (int) dpHeight, 200);
            }
        });

        mMap.clear();
    }

    private void showSelectDestUI() {
        orderDriverState = 1;
        hideSearchAddressStartUI();
        confirmStart.setVisibility(View.GONE);
        findViewById(R.id.shadow).setVisibility(View.GONE);
        bottomMenu.setVisibility(View.GONE);
        selectedOp.setVisibility(View.GONE);
        shadowBg.setVisibility(View.GONE);
        endConstraint.setVisibility(View.VISIBLE);
        searchEditText.setEnabled(false);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth = outMetrics.widthPixels / density;

        AnimateConstraint.animate(MapsActivity.this, startConstraint, (dpHeight - 130), 100, 500);
        AnimateConstraint.fadeIn(MapsActivity.this, endConstraint, 500, 10);
        AnimateConstraint.fadeIn(MapsActivity.this, selectDest, 500, 10);

        searchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        findViewById(R.id.locationPin).setVisibility(View.GONE);
        findViewById(R.id.closestDriver).setVisibility(View.GONE);
        findViewById(R.id.locationPinDest).setVisibility(View.VISIBLE);

        frameLayout.setDrawingCacheEnabled(true);
        frameLayout.buildDrawingCache();
        frameTime.setText(closestDriverText.getText());
        Bitmap bm = frameLayout.getDrawingCache();
        Marker myMarker = mMap.addMarker(new MarkerOptions()
                .position(startLatLng)
                .icon(BitmapDescriptorFactory.fromBitmap(bm)));

        menuButton.setVisibility(View.VISIBLE);
        menuButton.setImageBitmap(scaleBitmap(35, 35, R.drawable.back_arrow));
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rippleBackground.isRippleAnimationRunning()) {
                    rippleBackground.stopRippleAnimation();
                    DatabaseReference pickupRequest = FirebaseDatabase.getInstance().
                            getReference("PICKUPREQUEST");
                    pickupRequest.removeValue();
                }
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
                findViewById(R.id.shadow).setVisibility(View.GONE);
                bottomMenu.setVisibility(View.GONE);
                selectedOp.setVisibility(View.GONE);
                findViewById(R.id.coverButton).setVisibility(View.GONE);
                favorite.setBackgroundColor(Color.WHITE);
                isFocusableNeeded = true;
                state = -1;
                showFavoritsAndRecents();
                selectStart.setVisibility(View.GONE);
                selectDest.setVisibility(View.GONE);
            }
        });

        searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    searchEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            txt = this;
                            if (isFocusableNeeded) {
                                lookForAddress();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });
                } else {
                    searchEditText.removeTextChangedListener(txt);
                }
            }
        });

        searchDestEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    searchDestEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            txtDest = this;
                            if (isFocusableNeeded) {
                                lookForAddress();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                        }
                    });
                } else {
                    searchDestEditText.removeTextChangedListener(txtDest);
                }
            }
        });

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(MapsActivity.this);
                    searchEditText.clearFocus();
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    return true;
                }
                return false;
            }
        });
        searchDestEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(MapsActivity.this);
                    searchDestEditText.clearFocus();
                    return true;
                }
                return false;
            }
        });

        searchButtonDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(MapsActivity.this);
                searchDestEditText.clearFocus();
                searchButtonDest.setVisibility(View.GONE);
                searchProgBarDest.setVisibility(View.VISIBLE);
                lookForAddress();
            }
        });

    }

    public void lookForAddress() {
        if ((searchEditText.getText().toString().length() == 0 && orderDriverState == 0) ||
                (searchDestEditText.getText().toString().length() == 0 && orderDriverState == 1)) {
            findViewById(R.id.imageView111).setVisibility(View.VISIBLE);
            showSearchAddressStartUI();
            return;
        }
        if (orderDriverState == 1) {
            startConstraint.setVisibility(View.INVISIBLE);
        }
        placeDataList.clear();
        new LookForAddressTask().execute();
    }


    private class LookForAddressTask extends AsyncTask<String, Integer, String> {
        // Runs in UI before background thread is called
        boolean finished;
        String searchText;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            placeDataList.clear();
            finished = false;
            if (orderDriverState == 0)
                searchText = searchEditText.getText().toString();
            if (orderDriverState == 1)
                searchText = searchDestEditText.getText().toString();

            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {


            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                    .setCountry("MA")
                    .build();

            mGeoDataClient.getAutocompletePredictions(searchText + " " + searchLoc, null,
                    typeFilter).addOnCompleteListener(new OnCompleteListener<AutocompletePredictionBufferResponse>() {
                @Override
                public void onComplete(@NonNull Task<AutocompletePredictionBufferResponse> task) {
                    if (task.isSuccessful() && Objects.requireNonNull(task.getResult()).getCount() > 0) {
                        finished = true;
                        AutocompletePredictionBufferResponse aa = task.getResult();
                        for (final AutocompletePrediction ap : aa) {
                            mGeoDataClient.getPlaceById(ap.getPlaceId()).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
                                @Override
                                public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                                    if (task.isSuccessful() && Objects.requireNonNull(task.getResult()).getCount() > 0) {
                                        for (com.google.android.gms.location.places.Place gotPlace : task.getResult()) {
                                            MyPlace myPlace = new MyPlace(gotPlace.getName().toString(),
                                                    Objects.requireNonNull(gotPlace.getAddress()).toString(), "" + gotPlace.getLatLng().latitude,
                                                    "" + gotPlace.getLatLng().longitude, R.drawable.lieux_proches);
                                            placeDataList.add(myPlace);
                                        }
                                        favorite.setBackgroundColor(Color.WHITE);
                                        mLocationView.setBackgroundColor(Color.WHITE);
                                        fR.setBackgroundColor(Color.WHITE);
                                        aR.setBackgroundColor(Color.WHITE);
                                        placeAdapter.notifyDataSetChanged();
                                        finished = true;
                                    } else {
                                        finished = true;
                                    }
                                }
                            });
                        }
                    } else {
                        finished = true;
                    }
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

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            int Height = 67 * placeDataList.size();
            AnimateConstraint.animate(MapsActivity.this, aR, HeightAbsolute, HeightAbsolute, 1);
            AnimateConstraint.animate(MapsActivity.this, favorite, 1, 1, 1);
            if (orderDriverState == 0) {
                findViewById(R.id.imageView111).setVisibility(View.VISIBLE);
                aR.setVisibility(View.VISIBLE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//                selectStart.setVisibility(View.GONE);
                selectedOp.setVisibility(View.GONE);
                bottomMenu.setVisibility(View.GONE);
                findViewById(R.id.shadow).setVisibility(View.GONE);
            }

            if (orderDriverState == 1) {
                searchButtonDest.setVisibility(View.VISIBLE);
                aR.setVisibility(View.VISIBLE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                selectDest.setVisibility(View.GONE);
            }
            searchProgBar.setVisibility(View.GONE);
            searchProgBarDest.setVisibility(View.GONE);

        }
    }

    private void saveRecentPlaces(Context context, ArrayList<MyPlace> rPlaceDataList) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(rPlaceDataList);
        prefsEditor.putString("recent_places", json);
        prefsEditor.commit();
    }

    private void goToLocation(Context context, Double lat, Double lng, MyPlace rPlace) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        Log.e(TAG, "goToLocation: " + lat);
        Log.e(TAG, "goToLocation: " + lng);
        String completeAddress = utility.getCompleteAddressString(context, lat, lng);
        Log.e(TAG, "goToLocation: completeAddress " + completeAddress);
        searchEditText.setText(completeAddress);
        searchDestEditText.setText(completeAddress);
    }

    private boolean contains(ArrayList<MyPlace> list, MyPlace myPlace) {
        for (MyPlace item : list) {
            if (item.getName().equals(myPlace.name) || item.getLat().equals(myPlace.lat) || item.getLng().equals(myPlace.lng)
                    || item.getAddress().equals(myPlace.address)) {
                return true;
            }
        }
        return false;
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
            AnimateConstraint.animateCollapse(MapsActivity.this, favorite, 1, HeightAbsolute, 300);
        if (aR.getHeight() >= HeightAbsolute)
            AnimateConstraint.animateCollapse(MapsActivity.this, aR, 1, HeightAbsolute, 300);
        findViewById(R.id.imageView7).setVisibility(View.INVISIBLE);
        findViewById(R.id.imageView8).setVisibility(View.INVISIBLE);
        coverButton.setVisibility(View.VISIBLE);
        hideKeyboard(MapsActivity.this);
        startConstraint.setVisibility(View.VISIBLE);
        if (orderDriverState == 0) {
            selectStart.setVisibility(View.VISIBLE);
            bottomMenu.setVisibility(View.VISIBLE);
            selectedOp.setVisibility(View.VISIBLE);
        }
        if (orderDriverState == 1) {
            selectDest.setVisibility(View.VISIBLE);
        }

    }

    private void showSearchAddressStartUI() {
        X.setVisibility(View.VISIBLE);
        menuButton.setVisibility(View.VISIBLE);
        state = 0;
        placeDataList.clear();
        placeAdapter.notifyDataSetChanged();
        startConstraint.setVisibility(View.VISIBLE);
        searchEditText.clearFocus();
        searchEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        searchDestEditText.clearFocus();

        AnimateConstraint.animate(MapsActivity.this, favorite, 1, 1, 1);
        AnimateConstraint.animate(MapsActivity.this, aR, 1, 1, 1);
        coverButton.setVisibility(View.VISIBLE);
        hideKeyboard(MapsActivity.this);

        if (orderDriverState == 0) {
            selectStart.setVisibility(View.VISIBLE);
            bottomMenu.setVisibility(View.VISIBLE);
            selectedOp.setVisibility(View.VISIBLE);
            shadowBg.setVisibility(View.VISIBLE);
        }
        if (orderDriverState == 1) {
            selectDest.setVisibility(View.VISIBLE);
        }
    }

    private class LookForDriverTask extends AsyncTask<String, Integer, String> {
        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Do something like display a progress bar
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


                            searchEditText.setText(utility.getCompleteAddressString(MapsActivity.this, startLatLng.latitude, startLatLng.longitude));


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

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    private void afterLook() {
        if (driversKeys.size() > 0) {
            double distanceKmTime = Math.floor(Double.parseDouble(driversLocations.get(0)));

            if (distanceKmTime >= 10) distanceKmTime -= (distanceKmTime * (distanceKmTime / 100));

            if (!courseScreenIsOn) {
//                callLayout.setVisibility(View.GONE);
                closestDriverText.setText((int) distanceKmTime + "\nmin");
                if (orderDriverState == 1) {
                    if (closestDriverText.getText().toString().startsWith("-")) {
                        frameTime.setText(closestDriverText.getText().toString().substring(1));
                    } else {
                        frameTime.setText(closestDriverText.getText());
                    }
                }
                if (orderDriverState == 2) {
                    if (closestDriverText.getText().toString().startsWith("-")) {
                        frameTime.setText(closestDriverText.getText().toString().substring(1));
                    } else {
                        frameTime.setText(closestDriverText.getText());
                    }
                }
            }
        } else {
            if (!courseScreenIsOn) {
                callLayout.setVisibility(View.GONE);
                closestDriverText.setText("4\nmin");
                frameTime.setText("4\nMin");
                if (orderDriverState == 1) {
                    closestDriverText.setText("4\nmin");
                    frameTime.setText("4\nMin");
                }
                if (orderDriverState == 2) {
                    closestDriverText.setText("4\nmin");
                    frameTime.setText("4\nMin");
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

        if (!isLocationEnabled(MapsActivity.this))
            checkLocationService();
        else {
            if (ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
                        new LookForDriverTask().execute();
                    }
                    startLatLng = mMap.getCameraPosition().target;
                    if (!courseScreenIsOn)
                        new ReverseGeocodingTask(MapsActivity.this, orderDriverState, searchEditText, searchDestEditText, courseScreenIsOn).execute(startLatLng);
                }

                if (orderDriverState == 1) {
                    destLatLng = mMap.getCameraPosition().target;
                    if (!courseScreenIsOn)
                        new ReverseGeocodingTask(MapsActivity.this, orderDriverState, searchEditText, searchDestEditText, courseScreenIsOn).execute(destLatLng);
                }
            }
        });

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                searchEditText.setText(utility.getCompleteAddressString(MapsActivity.this, startLatLng.latitude, startLatLng.longitude));
            }
        });


        try {
            new checkCourseTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            if (ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MapsActivity.this,
                    android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivity.this,
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
                    .apiKey(getResources().getString(R.string.google_maps_key))
                    .build();
            DirectionsApiRequest req = DirectionsApi.getDirections(context, start.latitude + ","
                    + start.longitude, arrival.latitude + "," + arrival.longitude);
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
                        getPrice();
                        drawPolyLineOnMap(start, arrival);
                        builder.include(arrival);
                        int padding = 200;
                        LatLngBounds bounds = builder.build();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding)
                                , 1000, new GoogleMap.CancelableCallback() {
                                    @Override
                                    public void onFinish() {
                                        gooButton.setVisibility(View.VISIBLE);
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

    public void drawPolyLineOnMap(LatLng currentLatitude, LatLng currentLongitude) {
        String url = getMapsApiDirectionsUrl(currentLatitude, currentLongitude);
        Log.e(TAG, "drawPolyLineOnMap: " + url);
        ReadTask downloadTask = new ReadTask(getApplicationContext(), mMap);
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

        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key="
                + "AIzaSyA69yMLMZGzJzaa1pHoNIk9yGYqyhsa_lw" + "&sensor=true";
    }

    private int driverSize;
    private Runnable runnable;
    private Handler handler;
    SharedPreferences prefs;

    private String userPromoCode = "";

    private class sendRequestsTask extends AsyncTask<String, Integer, String> {
        SharedPreferences prefs;

        String image;
        boolean finishedSendReq = false;

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            finishedSendReq = false;
            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            final int Step = 3; //Number Of Drivers To Call Every Time
            driverSize = driversKeys.size();

            if (driverSize == 0) {
                finishedSendReq = true;
            }
            handler = new Handler(Looper.getMainLooper());
            runnable = new Runnable() {
                int counter = 0;

                @Override
                public void run() {
                    if (counter == 0) {
                        Log.e(TAG, "run: if size: " + counter + Step);
                        Log.e(TAG, "run: if driverSize: " + driversKeys.size());
                        driversKeysHold.clear();
                        for (int j = counter; j < (counter + Step) && j < driversKeys.size(); j++) {
                            if (driversKeys.get(j) != null) {
                                final DatabaseReference pickupRequest =
                                        FirebaseDatabase.getInstance().getReference("PICKUPREQUEST").
                                                child(driversKeys.get(j)).child(Objects.requireNonNull(userId));

                                Map<String, String> data = new HashMap<>();
                                data.put("client", userId);
                                data.put("start", searchEditText.getText().toString());
                                data.put("arrival", searchDestEditText.getText().toString());


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

                                                stopSearchUI();
                                                showAllUI();
                                                finishedSendReq = true;
                                                counter = 0;
                                                pickupRequest.removeEventListener(this);

                                                FirebaseDatabase.getInstance().getReference("COURSES").orderByChild("client").equalTo(userId).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (!dataSnapshot.exists()) {
                                                            Toast.makeText(MapsActivity.this, resources.getString(R.string.no_driver_txt), Toast.LENGTH_SHORT).show();
                                                            courseScreenIsOn = false;
                                                            finishedSendReq = true;
                                                            handler.removeCallbacks(runnable);
                                                            geoQuery.setCenter(new GeoLocation(startLatLng.latitude, startLatLng.longitude));
                                                            counter = 0;
                                                            stopSearchUI();
                                                            showAllUI();
                                                        } else {

                                                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                                FirebaseDatabase.getInstance().getReference("DRIVERUSERS")
                                                                        .child(Objects.requireNonNull(data.child("driver").getValue(String.class))).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.exists()) {
                                                                            callLayout.setVisibility(View.VISIBLE);
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
                                            stopSearchUI();
                                            showAllUI();
                                            pickupRequest.removeEventListener(this);
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
                            data.put("client", userId);
                            data.put("start", searchEditText.getText().toString());
                            data.put("arrival", searchDestEditText.getText().toString());
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

                    setCancelSearchButton(userId, counter, Step);
                }
            };
            handler.postDelayed(runnable, 1000);

            return "";

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
        }
    }

    private void startSearchUI() {
        rippleBackground.startRippleAnimation();
        cancelRequest.setVisibility(View.VISIBLE);
    }

    private void stopSearchUI() {
        driversKeysHold.clear();
        AnimateConstraint.fadeIn(MapsActivity.this, gooButton, 200, 10);
        rippleBackground.stopRippleAnimation();
        menuButton.setVisibility(View.VISIBLE);
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

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        if (courseScreenIsOn) {
            rideCancelDialog();
        } else if (rippleBackground.isRippleAnimationRunning()) {
            rippleBackground.stopRippleAnimation();
            stopSearchUI();
            showAllUI();
            DatabaseReference pickupRequest = FirebaseDatabase.getInstance().
                    getReference("PICKUPREQUEST");
            pickupRequest.removeValue();
        } else {
            if (state == 1 || state == -1) {
                state = 0;
                hideSelectDestUI();
                return;
            }

            if (state == 2) {
                state = 1;
                cancelCommandLayout();
                return;
            }
        }


        if (state != 1 && state != 2 && !courseScreenIsOn) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, resources.getString(R.string.back_exit_txt), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    public void updateViews() {

        TextView textView7 = findViewById(R.id.textView7);
        TextView textView8 = findViewById(R.id.textView8);
        TextView textView9 = findViewById(R.id.textView9);
        TextView textVie = findViewById(R.id.textVie);
        TextView textView12 = findViewById(R.id.textView12);
        TextView textView11 = findViewById(R.id.textView11);
        TextView textView = findViewById(R.id.textView);
        TextView textView2 = findViewById(R.id.textView2);
        Button confirm_start = findViewById(R.id.confirm_start);
        Button confirm_dest = findViewById(R.id.confirm_dest);
        Button passer = findViewById(R.id.passer);

        //Set Texts
        textView7.setText(resources.getString(R.string.Acceuil));
        textView8.setText(resources.getString(R.string.Historique));
        textView9.setText(resources.getString(R.string.Inbox));
        textVie.setText(resources.getString(R.string.Invitervosamis));
        textView12.setText(resources.getString(R.string.Aide));
        textView11.setText(resources.getString(R.string.Sedeconnecter));
        textView.setText(resources.getString(R.string.Lieuxfavoris));
        textView2.setText(resources.getString(R.string.Lieuxrécents));

        confirm_start.setText(resources.getString(R.string.confirm_start));
        confirm_dest.setText(resources.getString(R.string.Ajouterladestination));
        passer.setText(resources.getString(R.string.passer));
    }

    String address = "", lat = "", Long = "";
    String homeAddress = "", homeLat = "", homeLong = "";

    @Override
    protected void onResume() {
        super.onResume();


        language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");
        co = LocalHelper.setLocale(MapsActivity.this, language);
        resources = co.getResources();

        updateViews();
        fPlaceDataList.clear();

//        if (isLocationEnabled(MapsActivity.this)) {
//            if (ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//            } else {
//                getLastLocation();
//            }
//        } else {
//            getLastLocation();
//        }

        try {
            prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
            userId = prefs.getString("userID", "");
            Log.e(TAG, "onResume: " + userId);
            FirebaseDatabase.getInstance().getReference("clientUSERS").
                    child(userId).child("favouritePlace").child("Work").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        address = dataSnapshot.child("Address").getValue(String.class);
                        lat = dataSnapshot.child("Lat").getValue(String.class);
                        Long = dataSnapshot.child("Long").getValue(String.class);
                        MyPlace workPlace = new MyPlace(address, address, lat, Long, R.drawable.mdaison_con);

                        fPlaceDataList.add(workPlace);
                        fPlaceAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId)
                    .child("favouritePlace").child("Home").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        homeAddress = dataSnapshot.child("Address").getValue(String.class);
                        homeLat = dataSnapshot.child("Lat").getValue(String.class);
                        homeLong = dataSnapshot.child("Long").getValue(String.class);

                        MyPlace homePlace = new MyPlace(homeAddress, homeAddress, homeLat, homeLong, R.drawable.work_icon);
                        fPlaceDataList.add(homePlace);
                        fPlaceAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}