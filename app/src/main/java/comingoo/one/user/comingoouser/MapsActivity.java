package comingoo.one.user.comingoouser;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
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
import com.google.android.gms.maps.model.PolylineOptions;
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
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import comingoo.one.user.comingoouser.Database.SharedPreferenceTask;
import comingoo.one.user.comingoouser.R;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageButton;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    static GoogleMap mMap;
    private static EditText searchEditText;
    private static EditText searchDestEditText;
    private ImageButton searchButton;
    private ImageButton searchButtonDest;

    static ConstraintLayout bottomMenu;
    static CircleImageView selectedOp;
    static ImageView shadowBg;
    static ConstraintLayout selectStart;
    static ConstraintLayout selectDest;
    static Button confirmStart;
    static ProgressBar searchProgBar;
    static ProgressBar searchProgBarDest;

    static TextView promoCode;

    static RecyclerView mLocationView;
    static MyPlaceAdapter placeAdapter;
    static List<place> placeData;

    static RecyclerView fLocationView;
    static RecyclerView rLocationView;
    static MyPlaceAdapter fPlaceAdapter;
    static MyPlaceAdapter rPlaceAdapter;
    static List<place> fPlaceData;
    static List<place> rPlaceData;

    private static ConstraintLayout startConstraint;
    private ConstraintLayout endConstraint;

    private GeoDataClient mGeoDataClient;

    private LatLng userLatLng;
    private LatLng startLatLng;
    private LatLng destLatLng;

    private static int orderDriverState;

    private FrameLayout frameLayout;
    private FrameLayout frameLayout2;
    private FrameLayout frameLayout3;
    private TextView frameTime;
    private TextView closestDriverText;

    private static ConstraintLayout favorite;
    static Context context;

    static ConstraintLayout aR;
    static ConstraintLayout rR;
    static ConstraintLayout fR;

    private static final String APP_KEY = "04ae7d45-1084-4fb5-9d7c-08d82527d191";
    private static final String APP_SECRET = "TfJrquo6qEmkV8DG/EXQPg==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";


    private float density;
    private float dpHeight;
    private float dpWidth;

    static Window gWindow;
    static Button coverButton;

    static ImageView image1;
    static ImageView image2;
    static ImageButton X;
    static ImageButton positionButton;

    private ImageButton cancelRequest;

    private Button confirmDest;

    int Height;

    int HeightAbsolute;

    static ConstraintLayout citySelectLayout;

    private String searchLoc;
    private static TextView city;

    private ImageButton menuButton;
    private FlowingDrawer mDrawer;

    private ImageButton gooButton;

    private String clientID;

    private String startCity;
    private String destCity;

    private List<FixedLocation> fixedLocations;

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

    private ConstraintLayout gooBox;

    private ConstraintLayout gooVoid;

    private Button passer;

    private TextView driverCarDesc;
    private ImageButton callButton;

    ////////////////////////////////////////////


    private ConstraintLayout Acceuil;
    private ConstraintLayout Historique;
    private ConstraintLayout Inbox;
    private ConstraintLayout ComingoonYou;
    private ConstraintLayout Aide;
    private ConstraintLayout logout;


    Resources resources;
    String language;


    private ConstraintLayout driverInfoLayout;
    private TextView driverNameL;
    private CircleImageView driverImageL;


    ////////////////////////////////////////////


    private List<String> driversKeys;
    private List<String> driversLocations;
    private List<String> driversKeysHold;

    private GeoQuery geoQuery;


    ////////////////////////////////////////////

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
        destArrow.setImageBitmap(scaleBitmap(26, 57, R.drawable.arrow));
        menuButton.setImageBitmap(scaleBitmap(45, 45, R.drawable.home_icon));
        positionButton.setImageBitmap(scaleBitmap(40, 37, R.drawable.my_position_icon));
        X.setImageBitmap(scaleBitmap(30, 35, R.drawable.cancel));
//        searchEditText.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) (dpWidth - 30), (int )((dpWidth - 30) / 3.75), R.drawable.search_icon)));

        //citySelectLayout.setBackground(new BitmapDrawable(getResources(), scaleBitmap(115, 29, R.drawable.)));
        //gooButton.setImageBitmap(scaleBitmap(20, 20, R.drawable.goo));
        gooBox.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) (dpWidth - 30), (int) ((dpWidth - 30) / 3.75), R.drawable.footer_min)));
        shadowBg.setImageBitmap(scaleBitmap((int) dpWidth, 80, R.drawable.shadow_bottom));


        gooBox.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) ((dpWidth - 30) / 3.75), context.getResources().getDisplayMetrics());


    }

    private String userName;
    private Call call;

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
            clientID = userId;
            if (userId == null) {
                userId = "123";
            }
            sinchClient = Sinch.getSinchClientBuilder()
                    .context(MapsActivity.this)
                    .userId(userId)
                    .applicationKey("04ae7d45-1084-4fb5-9d7c-08d82527d191")
                    .applicationSecret("TfJrquo6qEmkV8DG/EXQPg==")
                    .environmentHost("clientapi.sinch.com")
//                    .applicationKey(resources.getString(R.string.sinch_app_key))
//                    .applicationSecret(resources.getString(R.string.sinch_app_secret))
//                    .environmentHost(resources.getString(R.string.sinch_envirentmnet_host))
                    .build();
            sinchClient.setSupportCalling(true);
            sinchClient.start();
            sinchClient.startListeningOnActiveConnection();


            sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());


            FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        prefs.edit().remove("userID");
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MapsActivity.this, loginActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    } else {
                        userName = dataSnapshot.child("fullName").getValue(String.class);
                        String userImage = dataSnapshot.child("image").getValue(String.class);
                        if (userImage != null) {
                            if (userImage.length() > 0) {
                                Picasso.get().load(userImage).centerCrop().fit().into(profileImage);
                            }
                        }
                        tvUserName.setText(userName);
                        ComingoonYou.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MapsActivity.this, comingoonuActivity.class);
                                intent.putExtra("image", dataSnapshot.child("image").getValue(String.class));
                                intent.putExtra("name", dataSnapshot.child("fullName").getValue(String.class));
                                intent.putExtra("phone", dataSnapshot.child("phoneNumber").getValue(String.class));
                                intent.putExtra("email", dataSnapshot.child("email").getValue(String.class));
//                                intent.putExtra("phone", "+212 " + dataSnapshot.child("phoneNumber").getValue(String.class));
                                startActivity(intent);
                            }
                        });
                        AnimateConstraint.fadeOut(context, findViewById(R.id.loadingScreen), 500, 10);
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                findViewById(R.id.loadingScreen).setVisibility(View.GONE);
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


    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            call = incomingCall;
            Toast.makeText(MapsActivity.this, "incoming call", Toast.LENGTH_SHORT).show();
            call.answer();
            call.addCallListener(new MapsActivity.SinchCallListener());
        }
    }

    ///////////////////////////////////////////////////
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
    private String driverCar;
    private boolean finished1 = false;
    private boolean finished2 = false;


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

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            FirebaseDatabase.getInstance().getReference("COURSES").orderByChild("client").
                    equalTo(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        try {
                            for (final DataSnapshot data : dataSnapshot.getChildren()) {
                                courseIDT = data.getKey();
                                statusT = data.child("state").getValue(String.class);
                                clientIdT = data.child("client").getValue(String.class);
                                driverIDT = data.child("driver").getValue(String.class);
                                driverPosT = new LatLng(Double.parseDouble(data.child("driverPosLat").getValue(String.class)),
                                        Double.parseDouble(data.child("driverPosLong").getValue(String.class)));
                                startPositionT = new LatLng(Double.parseDouble(data.child("startLat").getValue(String.class)),
                                        Double.parseDouble(data.child("startLong").getValue(String.class)));


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

                                            FirebaseDatabase.getInstance().getReference("DRIVERUSERS").child(driverIDT).child("CARS").orderByChild("selected").equalTo("1").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {

                                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                            driverCar = data.child("name").getValue(String.class) + " " + data.child("description").getValue(String.class);
                                                        }
                                                    } else {
                                                        driverCar = "data not available!";
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
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    } else {
                        statusT = "4";
                        handleCourseCallBack();
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


    private boolean courseScreenStageZero = false;
    private boolean courseScreenStageOne = false;
    private Marker driverPosMarker;
    private Marker startPositionMarker;
    private boolean blockingTimeOver = true;

    private void handleCourseCallBack() {
        if (statusT.equals("4")) {
            mMap.clear();
            positionButton.setVisibility(View.VISIBLE);
            if (courseScreenIsOn) {
                courseScreenIsOn = false;
                courseScreenStageZero = false;
                courseScreenStageOne = false;
                findViewById(R.id.pin).setVisibility(View.VISIBLE);
                cancelCommandLayout();
                hideSelectDestUI();
                coverButton.setClickable(true);
            }

//            findViewById(R.id.driverInfoLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.buttonsLayout).setVisibility(View.VISIBLE);
            return;
        }
        stopSearchUI();
        shadowBg.setVisibility(View.VISIBLE);
        menuButton.setImageBitmap(scaleBitmap(45, 45, R.drawable.home_icon));
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mDrawer.openMenu(true);
                ConstraintLayout contentConstraint = (ConstraintLayout) findViewById(R.id.contentLayout);
                ConstraintLayout contentBlocker = (ConstraintLayout) findViewById(R.id.contentBlocker);
                AnimateConstraint.resideAnimation(context, contentConstraint, contentBlocker, (int) dpWidth, (int) dpHeight, 200);
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

            findViewById(R.id.cancelCourse).setVisibility(View.GONE);
            findViewById(R.id.cancelCourse).setOnClickListener(null);


            AnimateConstraint.fadeIn(context, bottomMenu, 500, 0);
            AnimateConstraint.fadeIn(context, selectedOpImage, 500, 0);
            AnimateConstraint.fadeIn(context, driverInfoLayout, 500, 0);
            findViewById(R.id.gooContent).setVisibility(View.GONE);
            cancelRequest.setVisibility(View.GONE);


            AnimateConstraint.animate(MapsActivity.this, startConstraint, (dpHeight - 80), 100, 0);
            AnimateConstraint.animate(MapsActivity.this, endConstraint, dpHeight - 70, 180, 0);

            findViewById(R.id.buttonsLayout).setVisibility(View.GONE);

            driverNameL.setText(driverName);
            driverCarDesc.setText(driverCar);
            if (driverImage != null) {
                if (driverImage.length() > 0) {
                    Picasso.get().load(driverImage).centerCrop().fit().into(driverImageL);
                }
            }

            if (ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.READ_PHONE_STATE},
                        1);
            }

            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Call call = sinchClient.getCallClient().callUser(driverIDT);
                    call.addCallListener(new SinchCallListener());
                }
            });

        }


        if (statusT.equals("0") && !courseScreenStageZero) {

            if (!userLevel.equals("2")) {
                callButton.setVisibility(View.VISIBLE);
                callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!driverPhone.isEmpty() || driverPhone != null) {
                            try {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + driverPhone));
                                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                    startActivity(callIntent);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

            courseScreenStageZero = true;
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom);

            Button dialogButton = (Button) dialog.findViewById(R.id.button);

            TextView textView8 = (TextView) dialog.findViewById(R.id.textView8);
            Button ddd = (Button) dialog.findViewById(R.id.button);


            //Set Texts
            textView8.setText(resources.getString(R.string.Votrechauffeurestenroute));
            ddd.setText(resources.getString(R.string.Daccord));


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


        if (statusT.equals("0")) {
            findViewById(R.id.cancelCourse).setVisibility(View.VISIBLE);
            findViewById(R.id.cancelCourse).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:

                                        FirebaseDatabase.getInstance().getReference("COURSES").child(courseIDT).child("state").setValue("5");
                                        FirebaseDatabase.getInstance().getReference("COURSES").child(courseIDT).removeValue();

                                        SharedPreferenceTask preferenceTask = new SharedPreferenceTask(getApplicationContext());
                                        int prevCancel = preferenceTask.getCancelNumber();
                                        preferenceTask.setCancelNumber(prevCancel + 1);
                                        driverInfoLayout.setVisibility(View.GONE);

                                        if (preferenceTask.getCancelNumber() > 3) {
                                            Toast.makeText(MapsActivity.this,
                                                    "Vous avez annulé beaucoup de fois, l’application va se bloquer pendant 1h",
                                                    Toast.LENGTH_LONG).show();

                                            blockingTimeOver = false;

                                            new CountDownTimer(3600000, 1000) {

                                                public void onTick(long millisUntilFinished) {
//                                              mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                                                }

                                                public void onFinish() {
//                                              mTextField.setTextext("done!");
                                                    blockingTimeOver = true;
                                                }
                                            }.start();
                                        }

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                        builder.setTitle("Vous étes sure?").setMessage("Voulez-vous annuler la course?\n Additional charge may apply").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
            findViewById(R.id.cancelCourse).setVisibility(View.GONE);
            if (!userLevel.equals("2")) {
                callButton.setVisibility(View.VISIBLE);
                findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!driverPhone.isEmpty() || driverPhone != null) {
                            try {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + driverPhone));
                                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                    startActivity(callIntent);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

            mMap.clear();
            courseScreenStageOne = true;
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom2);


            TextView textView8 = (TextView) dialog.findViewById(R.id.textView8);
            Button ddd = (Button) dialog.findViewById(R.id.button);


            //Set Texts
            textView8.setText(resources.getString(R.string.Votrechauffeurestarrivé));
            ddd.setText(resources.getString(R.string.Daccord));


            Button dialogButton = (Button) dialog.findViewById(R.id.button);
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
            findViewById(R.id.cancelCourse).setVisibility(View.GONE);
            mMap.clear();

            frameLayout3.setDrawingCacheEnabled(true);
            frameLayout3.buildDrawingCache();
            Bitmap bm = frameLayout3.getDrawingCache();
            mMap.addMarker(new MarkerOptions()
                    .position(driverPosT)
                    .icon(BitmapDescriptorFactory.fromBitmap(bm)));
        }

        if (statusT.equals("3")) {

        }


        if (statusT.equals("5")) {

        }

    }


    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            //call ended by either party
            findViewById(R.id.callLayout).setVisibility(View.GONE);
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }

        @Override
        public void onCallEstablished(final Call establishedCall) {
            //incoming call was picked up
            findViewById(R.id.callLayout).setVisibility(View.VISIBLE);
//            Button hangup = findViewById(R.id.hangup);
//            hangup.setText("Hangup");
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
//            hangup.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    establishedCall.hangup();
//                }
//            });
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
            //call is ringing
            findViewById(R.id.callLayout).setVisibility(View.VISIBLE);
//            Button hangup = (Button) findViewById(R.id.hangup);
//            hangup.setText("RINGING...");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            //don't worry about this right now
        }
    }


    ////////////////////////////////////////////////////


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

//            userId = "123";
            FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).child("COURSE").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        COURSE = dataSnapshot.getValue(String.class);
                        FirebaseDatabase.getInstance().getReference("CLIENTFINISHEDCOURSES").child(userId).child(dataSnapshot.getValue(String.class)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshott) {
                                final Dialog dialog = new Dialog(context);
                                dialogDriverId = dataSnapshott.child("driver").getValue(String.class);
                                dialog.setContentView(R.layout.finished_course);


                                TextView textView13 = (TextView) dialog.findViewById(R.id.textView13);
                                TextView textView14 = (TextView) dialog.findViewById(R.id.textView14);


                                //Set Texts
                                textView13.setText(resources.getString(R.string.Montanttotalàpayer));
                                textView14.setText(resources.getString(R.string.Evaluezvotreéxperience));


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


                                star1.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.unselected_star)));
                                star2.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.unselected_star)));
                                star3.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.unselected_star)));
                                star4.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.unselected_star)));
                                star5.setBackground(new BitmapDrawable(getResources(), scaleBitmap((int) 45, (int) 45, R.drawable.unselected_star)));


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
                                                showVoiceDialog();
                                            } else {

                                                final Dialog newDialog = new Dialog(context);
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
                                                textView15.setText(resources.getString(R.string.Noussommesdésolé));
                                                textView16.setText(resources.getString(R.string.whatswrong));
                                                button5.setText(resources.getString(R.string.Heuredarrivée));
                                                button7.setText(resources.getString(R.string.Etatdelavoiture));
                                                button8.setText(resources.getString(R.string.Conduite));
                                                button9.setText(resources.getString(R.string.Itinéraire));
                                                button10.setText(resources.getString(R.string.Autre));

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


                                                ImageButton nextBtn = (ImageButton) newDialog.findViewById(R.id.imageButton3);


                                                newDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                    @Override
                                                    public void onDismiss(DialogInterface dialog) {
                                                        showVoiceDialog();
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

                                                newDialog.findViewById(R.id.body).getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (dpWidth), context.getResources().getDisplayMetrics());

                                                WindowManager.LayoutParams lp = newDialog.getWindow().getAttributes();
                                                lp.dimAmount = 0.5f;
                                                newDialog.getWindow().setAttributes(lp);
                                                newDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                                newDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                                                newDialog.show();

                                            }
                                        }
                                    }
                                });


                                dialog.show();

                                dialog.findViewById(R.id.body).getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (dpWidth), context.getResources().getDisplayMetrics());


                                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                                lp.dimAmount = 0.5f;
                                dialog.getWindow().setAttributes(lp);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                            /*    String price = dataSnapshott.child("price").getValue(String.class);
                                Intent finishedCour²se = new Intent(MainActivity.this, finishedCourse.class);
                                finishedCourse.putExtra("price", price+" MAD");
                                finishedCourse.putExtra("courseID", dataSnapshot.getValue(String.class));
                                finishedCourse.putExtra("driverID", dataSnapshott.child("driver").getValue(String.class));

                                startActivity(finishedCourse);
*/
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
        }
    }


    private MediaRecorder myAudioRecorder;
    private String outputeFile;
    private boolean audioRecorded;

    private void showVoiceDialog() {

        audioRecorded = false;

        final Dialog newDialog = new Dialog(context);
        newDialog.setContentView(R.layout.voice_record);


        TextView textView18 = (TextView) newDialog.findViewById(R.id.textView18);
        TextView textView19 = (TextView) newDialog.findViewById(R.id.textView19);
        TextView textView20 = (TextView) newDialog.findViewById(R.id.textView20);


        //Set Texts
        textView18.setText(resources.getString(R.string.Appreciate));
        textView19.setText(resources.getString(R.string.PS));
        textView20.setText(resources.getString(R.string.Record));


        ImageButton nextBtn = (ImageButton) newDialog.findViewById(R.id.imageButton6);
        TextView name = (TextView) newDialog.findViewById(R.id.textView17);


        final ImageButton recordButton = (ImageButton) newDialog.findViewById(R.id.recordAudio);
        final ImageButton playAudio = (ImageButton) newDialog.findViewById(R.id.playAudio);
        final ImageButton pauseAudio = (ImageButton) newDialog.findViewById(R.id.pauseAudio);
        final ImageButton deleteAudio = (ImageButton) newDialog.findViewById(R.id.deleteAudio);
        final MediaPlayer mediaPlayer = new MediaPlayer();


        outputeFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputeFile);
        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 55);
        } else {
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

                            } catch (Exception e) {

                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            audioRecorded = true;
                            recordButton.setScaleX((float) 1);
                            recordButton.setScaleY((float) 1);

                            deleteAudio.setVisibility(View.VISIBLE);
                            deleteAudio.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showVoiceDialog();
                                    newDialog.dismiss();
                                }
                            });

                            myAudioRecorder.stop();
                            myAudioRecorder.release();
                            myAudioRecorder = null;

                            recordButton.setVisibility(View.GONE);
                            playAudio.setVisibility(View.VISIBLE);
                            setupPlayAudio(outputeFile, playAudio, pauseAudio, mediaPlayer);
                            break;
                    }
                    return false;
                }

            });
        }

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
                Toast.makeText(MapsActivity.this, "Thank you", Toast.LENGTH_SHORT).show();
                newDialog.dismiss();
            }
        });


        newDialog.findViewById(R.id.body).getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (dpWidth), context.getResources().getDisplayMetrics());

        WindowManager.LayoutParams lp = newDialog.getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        newDialog.getWindow().setAttributes(lp);
        newDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        newDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        newDialog.show();
    }


    private void setupPlayAudio(final String outputeFile, final View playAudio, final View pauseAudio, final MediaPlayer mediaPlayer) {
        playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    playAudio.setVisibility(View.GONE);
                    mediaPlayer.setDataSource(outputeFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

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


                } catch (Exception e) {

                }
            }
        });
    }
    //////////////////////////////////////////////////////


    private SinchClient sinchClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LeakCanary.install(getApplication());
        SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
        String userId = prefs.getString("userID", null);

        // if user not login any more
        if (userId == null || FirebaseAuth.getInstance().getCurrentUser() == null) {
            prefs.edit().remove("userID");
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MapsActivity.this, loginActivity.class);
            startActivity(intent);
            finish();
        }

        try {
            new CheckUserTask().execute();
            new checkFinishedCourse().execute();
        } catch (NullPointerException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }


        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");

        Context co = LocalHelper.setLocale(MapsActivity.this, language);
        resources = co.getResources();


        promoCode = (TextView) findViewById(R.id.promoCode);

        driverInfoLayout = (ConstraintLayout) findViewById(R.id.driverInfoLayout);
        driverNameL = (TextView) findViewById(R.id.driverName);
        driverImageL = (CircleImageView) findViewById(R.id.driverImage);

        driversKeys = new ArrayList<String>();
        driversLocations = new ArrayList<String>();
        driversKeysHold = new ArrayList<String>();

        callButton = (ImageButton) findViewById(R.id.call);

        locationPinDest = (ImageView) findViewById(R.id.locationPinDest);
        locationPinDriver = (ImageView) findViewById(R.id.driver_pin);
        profileImage = (ImageView) findViewById(R.id.profile_image);
        tvUserName = (TextView) findViewById(R.id.textView3);
        selectedOpImage = (CircleImageView) findViewById(R.id.selectedOperation);
        deleviryButton = (ImageButton) findViewById(R.id.deliveryButton);
        carButton = (ImageButton) findViewById(R.id.carButton);
        selectCity = (ImageButton) findViewById(R.id.imageButton4);
        destArrow = (ImageView) findViewById(R.id.destArrow);

        if (ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.READ_PHONE_STATE},
                    1);
        }

        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(userId)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT)
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();

        sinchClient.getCallClient().addCallClientListener(new MapsActivity.SinchCallClientListener());

        price = (TextView) findViewById(R.id.price);
        fixedLocations = new ArrayList<>();
        context = MapsActivity.this;
        orderDriverState = 0;
        citySelectLayout = (ConstraintLayout) findViewById(R.id.select_city);
        city = (TextView) findViewById(R.id.city);
        image1 = (ImageView) findViewById(R.id.imageView7);
        image2 = (ImageView) findViewById(R.id.imageView8);
        X = (ImageButton) findViewById(R.id.x);
        positionButton = (ImageButton) findViewById(R.id.my_position);
        gooBox = (ConstraintLayout) findViewById(R.id.gooBox);

        coverButton = (Button) findViewById(R.id.coverButton);

        cancelRequest = (ImageButton) findViewById(R.id.cancelRequest);

        passer = (Button) findViewById(R.id.passer);

        locationStartPin = (ImageView) findViewById(R.id.location_start_pin);
        locationDestPin = (ImageView) findViewById(R.id.location_dest_pin);
        locationPinStart = (ImageView) findViewById(R.id.locationPin);

        menuButton = (ImageButton) findViewById(R.id.menu_button);

        gooButton = (ImageButton) findViewById(R.id.gooButton);

        searchLoc = "Casablanca";

        gWindow = getWindow();
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        density = getResources().getDisplayMetrics().density;
        dpHeight = outMetrics.heightPixels / density;
        dpWidth = outMetrics.widthPixels / density;
        HeightAbsolute = (int) dpHeight - (200);


        aR = (ConstraintLayout) findViewById(R.id.adress_result);
        fR = (ConstraintLayout) findViewById(R.id.favorite);
        rR = (ConstraintLayout) findViewById(R.id.recent);

        userLatLng = null;
        startLatLng = null;
        destLatLng = null;

        mGeoDataClient = Places.getGeoDataClient(this);

        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        frameLayout2 = (FrameLayout) findViewById(R.id.framelayout2);
        frameLayout3 = (FrameLayout) findViewById(R.id.framelayout3);
        frameTime = (TextView) findViewById(R.id.closestDriverPin);
        closestDriverText = (TextView) findViewById(R.id.closestDriver);

        searchEditText = (EditText) findViewById(R.id.search_edit_text);
        searchButton = (ImageButton) findViewById(R.id.search_address_button);
        searchButtonDest = (ImageButton) findViewById(R.id.search_dest_address_button);

        searchDestEditText = (EditText) findViewById(R.id.search_dest_edit_text);

        searchProgBar = (ProgressBar) findViewById(R.id.search_prog_bar);
        searchProgBarDest = (ProgressBar) findViewById(R.id.search_dest_prog_bar);

        bottomMenu = (ConstraintLayout) findViewById(R.id.bottomMenu);
        selectedOp = (CircleImageView) findViewById(R.id.selectedOperation);
        shadowBg = (ImageView) findViewById(R.id.shadow_bg);
        selectStart = (ConstraintLayout) findViewById(R.id.select_start);
        selectDest = (ConstraintLayout) findViewById(R.id.select_dest);
        confirmStart = (Button) findViewById(R.id.confirm_start);
        confirmDest = (Button) findViewById(R.id.confirm_dest);

        startConstraint = (ConstraintLayout) findViewById(R.id.start_edit_text);
        endConstraint = (ConstraintLayout) findViewById(R.id.dest_edit_text);

        favorite = (ConstraintLayout) findViewById(R.id.favorite_recent);

        driverCarDesc = (TextView) findViewById(R.id.driverCarDesc);

        placeData = new ArrayList<>();
        fPlaceData = new ArrayList<>();
        rPlaceData = new ArrayList<>();

        mLocationView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLocationView.setHasFixedSize(true);
        mLocationView.setLayoutManager(new LinearLayoutManager(this));

        fLocationView = (RecyclerView) findViewById(R.id.favorite_recycler);
        fLocationView.setHasFixedSize(true);
        fLocationView.setLayoutManager(new LinearLayoutManager(this));

        rLocationView = (RecyclerView) findViewById(R.id.recent_recycler);
        rLocationView.setHasFixedSize(true);
        rLocationView.setLayoutManager(new LinearLayoutManager(this));

        placeAdapter = new MyPlaceAdapter(getApplicationContext(), placeData);
        mLocationView.setAdapter(placeAdapter);

        fPlaceAdapter = new MyPlaceAdapter(getApplicationContext(), fPlaceData);
        fLocationView.setAdapter(fPlaceAdapter);

        rPlaceAdapter = new MyPlaceAdapter(getApplicationContext(), rPlaceData);
        rLocationView.setAdapter(rPlaceAdapter);


        Acceuil = (ConstraintLayout) findViewById(R.id.acceuil);
        Historique = (ConstraintLayout) findViewById(R.id.historique);
        Inbox = (ConstraintLayout) findViewById(R.id.inbox);
        ComingoonYou = (ConstraintLayout) findViewById(R.id.comingoonyou);
        Aide = (ConstraintLayout) findViewById(R.id.aide);
        logout = (ConstraintLayout) findViewById(R.id.logout);


        Historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, historiqueActivity.class));
            }
        });
        findViewById(R.id.invite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, inviteActivity.class));
            }
        });
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
                prefs.edit().remove("userID");
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MapsActivity.this, loginActivity.class));
                finish();
            }
        });
        Inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, notificationActivity.class));
            }
        });
        Aide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, aideActivity.class));
            }
        });

        gooBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog(MapsActivity.this);
            }
        });

        driverInfoLayout.setVisibility(View.GONE);

        int fHeight = 170;
        int rHeight = HeightAbsolute - fHeight - 5;

        fR.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) fHeight, context.getResources().getDisplayMetrics());
        rR.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) rHeight, context.getResources().getDisplayMetrics());

        loadImages();
        updateViews();
        gooButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (blockingTimeOver) {
                    gooButton.setClickable(false);
               /* Handler h = new Handler();
                Runnable r= new Runnable(){
                    @Override
                    public void run() {
                        gooButton.setVisibility(View.GONE);
                        startTheWaitGame();
                    }
                };
                h.postDelayed(r, 300);*/

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(startLatLng)      // Sets the center of the map to Mountain View
                            .zoom(17)                   // Sets the zoom
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    AnimateConstraint.fadeOut(context, gooButton, 200, 10);
                    //AnimateConstraint.expandCircleAnimation(context, findViewById(R.id.gooLayout), dpHeight, dpWidth);
                    startSearchUI();

                    try {
                        new sendRequestsTask().execute();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        confirmDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (destPositionIsValid()) {
                    switchToCommandLayout();
                } else {

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
                // mDrawer.openMenu(true);
                ConstraintLayout contentConstraint = findViewById(R.id.contentLayout);
                ConstraintLayout contentBlocker = findViewById(R.id.contentBlocker);
                AnimateConstraint.resideAnimation(context, contentConstraint, contentBlocker, (int) dpWidth, (int) dpHeight, 200);
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
                if (startPositionIsValid()) {
                    orderDriverState = 1;
                    showSelectDestUI();
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

    public void showCustomDialog(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.content_promo_code, null, false);
        final EditText etPromoCode = view.findViewById(R.id.et_user_promo_code);
        Button btnOk = view.findViewById(R.id.btn_promo_code_ok);
        Button btnCancel = view.findViewById(R.id.btn_promo_code_cancel);

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
                                        price.setText(dataSnapshot.getValue() + " MAD");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            } else
                                Toast.makeText(getApplicationContext(), "Promo Code is expired", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else
                    Toast.makeText(getApplicationContext(), "Please Enter Promo Code", Toast.LENGTH_LONG).show();
            }
        });

        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }


    public void showFavoritsAndRecents() {
        fPlaceData.clear();
        rPlaceData.clear();
        place Place = new place("Travail", "This feature is not yet available", "33.5725155", "-7.5962637", R.drawable.lieux_proches);
        place Place2 = new place("Maison", "This feature is not yet available", "33.5725155", "-7.5962637", R.drawable.lieux_proches);
        fPlaceData.add(Place);
        fPlaceData.add(Place2);

        Place = new place("Location", "This feature is not yet available", "33.5725155", "-7.5962637 ", R.drawable.lieux_recent);
        rPlaceData.add(Place);
        rPlaceData.add(Place);
        rPlaceData.add(Place);
        rPlaceData.add(Place);

        fPlaceAdapter.notifyDataSetChanged();
        rPlaceAdapter.notifyDataSetChanged();


        AnimateConstraint.animate(MapsActivity.this, favorite, HeightAbsolute, 1, 100);


        findViewById(R.id.imageView7).setVisibility(View.VISIBLE);
        //findViewById(R.id.imageView8).setVisibility(View.VISIBLE);
        findViewById(R.id.x).setVisibility(View.VISIBLE);
        findViewById(R.id.my_position).setVisibility(View.GONE);
        findViewById(R.id.adress_result).setVisibility(View.INVISIBLE);


        //  AnimateConstraint.animate(MapsActivity.this,fR,  fHeight, 1, 1);
        // AnimateConstraint.animate(MapsActivity.this,rR,  rHeight, 1, 1);
    }


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
            Toast.makeText(context, "On est seulement disponible sur Casablanca!", Toast.LENGTH_SHORT).show();
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

        AnimateConstraint.animate(context, endConstraint, 180, dpHeight - 20, 500, selectDest, findViewById(R.id.destArrow));
        destArrow.setVisibility(View.GONE);
        findViewById(R.id.gooContent).setVisibility(View.GONE);

        startConstraint.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (dpHeight - 42), context.getResources().getDisplayMetrics());

        shadowBg.setVisibility(View.VISIBLE);
        searchButtonDest.setVisibility(View.VISIBLE);


        coverButton.setVisibility(View.VISIBLE);
        searchDestEditText.setEnabled(true);

        findViewById(R.id.locationPinDest).setVisibility(View.VISIBLE);
        mMap.clear();
        showSelectDestUI();
    }

    private void switchToCommandLayout() {
        orderDriverState = 2;

        positionButton.setVisibility(View.GONE);
        AnimateConstraint.animate(context, endConstraint, dpHeight - 20, 180, 500, selectDest, findViewById(R.id.destArrow));
        AnimateConstraint.fadeIn(MapsActivity.this, findViewById(R.id.gooContent), 500, 10);

        startConstraint.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) (dpHeight - 42), context.getResources().getDisplayMetrics());

        //shadowBg.setVisibility(View.GONE);
        searchButtonDest.setVisibility(View.GONE);


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
//            price.setText("13 MAD");
        }

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelCommandLayout();
            }
        });
    }


    int Score = 0;

    private void startTheWaitGame() {
    /*    final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final ConstraintLayout gameLayout = (ConstraintLayout)  findViewById(R.id.gameLayout);
                final GifImageButton gifFromResource = createGif();
                gifFromResource.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Score += 30;
                        //Kill The Monster
                        createKillGif((int) gifFromResource.getX(), (int) gifFromResource.getY());
                        gameLayout.removeView(gifFromResource);
                        TextView txt = findViewById(R.id.score);
                        txt.setText(""+Score);
                    }
                });

                handler.postDelayed(this, new Random().nextInt(2000) + 500);
            }
        };
        handler.postDelayed(runnable, 1000);
*/
    }

    public void createKillGif(int x, int y) {

/*
        final GifImageButton gifFromResource = new GifImageButton( MapsActivity.this);
        final ConstraintLayout gameLayout = (ConstraintLayout)  findViewById(R.id.gameLayout);


        int gifHeight = 200;
        int gifWidth = 270;

        gifFromResource.setImageResource(R.mipmap.disappear);
        gifFromResource.setLayoutParams(new LinearLayout.LayoutParams(
                (int)  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  gifHeight, context.getResources().getDisplayMetrics()),
                (int)   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, gifWidth, context.getResources().getDisplayMetrics())
        ));

        gifFromResource.setBackgroundColor(0);
        gifFromResource.setTranslationX(x);
        gifFromResource.setTranslationY(y);

        gameLayout.addView(gifFromResource);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                gameLayout.removeView(gifFromResource);
            }
        };
        handler.postDelayed(runnable, 500);

*/

    }

    public GifImageButton createGif() {
/*
        final GifImageButton gifFromResource = new GifImageButton( MapsActivity.this);
        final ConstraintLayout gameLayout = (ConstraintLayout)  findViewById(R.id.gameLayout);


        int gifHeight = 200;
        int gifWidth = 270;

        gifFromResource.setImageResource(R.mipmap.monster);
        gifFromResource.setLayoutParams(new LinearLayout.LayoutParams(
                (int)  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,  gifHeight, context.getResources().getDisplayMetrics()),
                (int)   TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, gifWidth, context.getResources().getDisplayMetrics())
        ));

        int yPos = new Random().nextInt(gameLayout.getHeight() - gifHeight);
        int xPos = new Random().nextInt(gameLayout.getWidth() - gifWidth );

        gifFromResource.setBackgroundColor(0);
        gifFromResource.setTranslationX(xPos);
        gifFromResource.setTranslationY(yPos);

        gameLayout.addView(gifFromResource);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                gameLayout.removeView(gifFromResource);
            }
        };
        handler.postDelayed(runnable, new Random().nextInt(400) + 400);


        return gifFromResource;
           */
        return null;
    }

    private void hideSelectDestUI() {
        orderDriverState = 0;

        coverButton.setClickable(true);

        hideSearchAddressStartUI();
        confirmStart.setVisibility(View.VISIBLE);
        findViewById(R.id.shadow).setVisibility(View.VISIBLE);
        bottomMenu.setVisibility(View.VISIBLE);
        selectedOp.setVisibility(View.VISIBLE);
        shadowBg.setVisibility(View.VISIBLE);
        searchButton.setVisibility(View.VISIBLE);

        endConstraint.setVisibility(View.GONE);
        selectDest.setVisibility(View.GONE);


        searchEditText.setEnabled(true);

        AnimateConstraint.animate(MapsActivity.this, startConstraint, 80, (dpHeight - 130), 500);

        findViewById(R.id.locationPin).setVisibility(View.VISIBLE);
        findViewById(R.id.closestDriver).setVisibility(View.VISIBLE);
        findViewById(R.id.locationPinDest).setVisibility(View.GONE);


        menuButton.setImageBitmap(scaleBitmap(45, 45, R.drawable.home_icon));
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mDrawer.openMenu(true);
                ConstraintLayout contentConstraint = (ConstraintLayout) findViewById(R.id.contentLayout);
                ConstraintLayout contentBlocker = (ConstraintLayout) findViewById(R.id.contentBlocker);
                AnimateConstraint.resideAnimation(context, contentConstraint, contentBlocker, (int) dpWidth, (int) dpHeight, 200);
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
        searchButton.setVisibility(View.GONE);

        searchEditText.setEnabled(false);


        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth = outMetrics.widthPixels / density;

        AnimateConstraint.animate(MapsActivity.this, startConstraint, (dpHeight - 80), 100, 500);
        AnimateConstraint.fadeIn(MapsActivity.this, endConstraint, 500, 10);
        AnimateConstraint.fadeIn(MapsActivity.this, selectDest, 500, 10);


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


        menuButton.setImageBitmap(scaleBitmap(45, 45, R.drawable.cancel));
        menuButton.setOnClickListener(new View.OnClickListener() {
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
                selectStart.setVisibility(View.GONE);
                findViewById(R.id.shadow).setVisibility(View.GONE);
                bottomMenu.setVisibility(View.GONE);
                selectedOp.setVisibility(View.GONE);
                selectDest.setVisibility(View.GONE);
                findViewById(R.id.coverButton).setVisibility(View.GONE);

                showFavoritsAndRecents();
            }
        });

        searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    // hideSearchAddressStartUI();
                    searchEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            txt = this;
                            if (searchEditText.getText().toString().length() >= 3) {
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
                    // hideSearchAddressStartUI();
                    searchDestEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            txtDest = this;
                            if (searchDestEditText.getText().toString().length() >= 3) {
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
                    //lookForAddress();
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
                    //lookForAddress();
                    return true;
                }
                return false;
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(MapsActivity.this);
                searchEditText.clearFocus();
                searchButton.setVisibility(View.GONE);
                searchProgBar.setVisibility(View.VISIBLE);
                lookForAddress();
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
        //searchEditText.clearFocus();
        //searchDestEditText.clearFocus();
        //hideKeyboard(MapsActivity.this);
        if ((searchEditText.getText().toString().length() == 0 && orderDriverState == 0) || (searchDestEditText.getText().toString().length() == 0 && orderDriverState == 1)) {
            findViewById(R.id.imageView111).setVisibility(View.VISIBLE);
            showSearchAddressStartUI();
            return;
        }
        if (orderDriverState == 1) {
            startConstraint.setVisibility(View.INVISIBLE);
        }
        new LookForAddressTask().execute();
    }


    private class LookForAddressTask extends AsyncTask<String, Integer, String> {
        // Runs in UI before background thread is called
        boolean finished;
        String searchText;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            placeData.clear();
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
                    .setCountry("MA")
                    .build();
            mGeoDataClient.getAutocompletePredictions(searchText + " " + searchLoc, null,
                    typeFilter).addOnCompleteListener(new OnCompleteListener<AutocompletePredictionBufferResponse>() {
                @Override
                public void onComplete(@NonNull Task<AutocompletePredictionBufferResponse> task) {
                    if (task.isSuccessful() && task.getResult().getCount() > 0) {
                        finished = true;
                        AutocompletePredictionBufferResponse aa = task.getResult();
                        for (final AutocompletePrediction ap : aa) {
                            mGeoDataClient.getPlaceById(ap.getPlaceId()).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
                                @Override
                                public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                                    if (task.isSuccessful() && task.getResult().getCount() > 0) {
                                        for (Place gotPlace : task.getResult()) {
                                            place Place = new place(gotPlace.getName().toString(), gotPlace.getAddress().toString(), "" + gotPlace.getLatLng().latitude, "" + gotPlace.getLatLng().longitude, R.drawable.lieux_proches);
                                            placeData.add(Place);
                                        }
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

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            int Height = 67 * placeData.size();
            AnimateConstraint.animate(context, aR, HeightAbsolute, HeightAbsolute, 1);
            AnimateConstraint.animate(context, favorite, 1, 1, 1);
            if (orderDriverState == 0) {
                searchButton.setVisibility(View.VISIBLE);
                findViewById(R.id.imageView111).setVisibility(View.VISIBLE);
                aR.setVisibility(View.VISIBLE);
                //if(Height > (dpHeight - (270)))
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                selectStart.setVisibility(View.GONE);
                selectedOp.setVisibility(View.GONE);
                bottomMenu.setVisibility(View.GONE);
                findViewById(R.id.shadow).setVisibility(View.GONE);


            }
            if (orderDriverState == 1) {
                searchButtonDest.setVisibility(View.VISIBLE);
                aR.setVisibility(View.VISIBLE);
                //if(Height > (dpHeight - (270)))
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                selectDest.setVisibility(View.GONE);
            }
            searchProgBar.setVisibility(View.GONE);
            searchProgBarDest.setVisibility(View.GONE);
            // Do things like hide the progress bar or change a TextView
        }
    }

    private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {
        Context mContext;

        public ReverseGeocodingTask(Context context) {
            super();
            mContext = context;
        }

        // Finding address using reverse geocoding
        @Override
        protected String doInBackground(LatLng... params) {
            if (orderDriverState != 0 && orderDriverState != 1)
                return "";

//            Geocoder geocoder = new Geocoder(mContext);
//            double latitude = params[0].latitude;
//            double longitude = params[0].longitude;
//
//            List<Address> addresses = null;
//            String addressText = "";
//
//            try {
//                addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if (addresses != null && addresses.size() > 0) {
//                Address address = addresses.get(0);
//
//                if (address != null) {
//                    if (address.getThoroughfare() != null) {
//                        addressText = String.format("%s, %s.",
//                                address.getThoroughfare().length() > 0 ? address.getThoroughfare() : "",
//                                address.getLocality());
//                    } else {
//                        addressText = "Address non trouvé";
//                    }
//                } else {
//                    addressText = "Address non trouvé";
//                }
//
//            }

//            return addressText;
            return getCompleteAddressString(context, params[0].latitude, params[0].longitude);
        }

        @Override
        protected void onPostExecute(String addressText) {
            // Setting the title for the marker.
            // This will be displayed on taping the marker
            // Placing a marker on the touched position
            if (courseScreenIsOn)
                return;
            if (orderDriverState == 0)
                searchEditText.setText(addressText);
            if (orderDriverState == 1)
                searchDestEditText.setText(addressText);

        }
    }


    static void goToLocation(Context context, Double lat, Double lng) {
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 17));
        image1.setVisibility(View.INVISIBLE);
        image2.setVisibility(View.INVISIBLE);
        positionButton.setVisibility(View.VISIBLE);
        X.setVisibility(View.GONE);
        citySelectLayout.setVisibility(View.GONE);
        searchEditText.clearFocus();
        searchDestEditText.clearFocus();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }


    public void hideSearchAddressStartUI() {
        //bottomMenu.setVisibility(View.GONE);
        //selectedOp.setVisibility(View.GONE);
        placeData.clear();
        placeAdapter.notifyDataSetChanged();
        findViewById(R.id.imageView7).setVisibility(View.INVISIBLE);
        findViewById(R.id.imageView8).setVisibility(View.INVISIBLE);
        favorite.setVisibility(View.INVISIBLE);
        aR.setVisibility(View.INVISIBLE);
        findViewById(R.id.imageView111).setVisibility(View.INVISIBLE);

        if (favorite.getHeight() >= HeightAbsolute)
            AnimateConstraint.animateCollapse(context, favorite, 1, HeightAbsolute, 300);
        if (aR.getHeight() >= HeightAbsolute)
            AnimateConstraint.animateCollapse(context, aR, 1, HeightAbsolute, 300);


        findViewById(R.id.imageView7).setVisibility(View.INVISIBLE);
        findViewById(R.id.imageView8).setVisibility(View.INVISIBLE);
        coverButton.setVisibility(View.VISIBLE);
        hideKeyboard((Activity) context);
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

    static void showSearchAddressStartUI() {
        placeData.clear();
        placeAdapter.notifyDataSetChanged();
        startConstraint.setVisibility(View.VISIBLE);
        searchEditText.clearFocus();
        searchDestEditText.clearFocus();

        AnimateConstraint.animate(context, favorite, 1, 1, 1);
        AnimateConstraint.animate(context, aR, 1, 1, 1);
        //gWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        coverButton.setVisibility(View.VISIBLE);
        hideKeyboard((Activity) context);

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
        SharedPreferences prefs;
        String userId;
        String image;

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {

           /* if(params[0].equals("true"))
                getData = true;*/

            if (startLatLng != null) {

//                if (driversKeys != null) {
//                    driversKeys.clear();
//                    driversLocations.clear();
//                }

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
                            afterLook();
                        }
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


                            searchEditText.setText(getCompleteAddressString(context, startLatLng.latitude, startLatLng.longitude));


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
                        driversKeys.clear();
                        driversLocations.clear();
                        geoQuery.removeAllListeners();
                        geoQuery.addGeoQueryDataEventListener(this);
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

            // Do things like update the progress bar
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //afterLook(getData);


            // Do things like hide the progress bar or change a TextView
        }
    }


    private boolean courseScreenIsOn = false;

    private void afterLook() {
        if (driversKeys.size() > 0) {
            double distanceKmTime = Math.floor(Double.parseDouble(driversLocations.get(0)));

            if (distanceKmTime >= 10) distanceKmTime -= (distanceKmTime * (distanceKmTime / 100));

            if (!courseScreenIsOn) {
                closestDriverText.setText((int) distanceKmTime + "\nmin");
                if (orderDriverState == 1) {
                    frameTime.setText(closestDriverText.getText());
                }
                if (orderDriverState == 2) {
                    frameTime.setText(closestDriverText.getText());
                }
            }
        } else {
            if (!courseScreenIsOn) {
                closestDriverText.setText("\n...");

                if (orderDriverState == 1) {
                    frameTime.setText("...");
                }
                if (orderDriverState == 2) {
                    frameTime.setText("...");
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

       // mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        try {
            new checkCourseTask().execute();
        } catch (NullPointerException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        if (ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            getLastLocation();
            if (userLatLng != null)
                startLatLng = userLatLng;
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
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
                        new ReverseGeocodingTask(MapsActivity.this).execute(startLatLng);
                }
                if (orderDriverState == 1) {
                    destLatLng = mMap.getCameraPosition().target;
                    if (!courseScreenIsOn)
                        new ReverseGeocodingTask(MapsActivity.this).execute(destLatLng);
                }
            }
        });

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                searchEditText.setText(getCompleteAddressString(context, startLatLng.latitude, startLatLng.longitude));
//                Log.e("MapsActivity", "goToLocation: "+getCompleteAddressString(context, lat, lng) );
            }
        });
    }

    private static String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    public static void hideKeyboard(Activity activity) {
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
                                goToLocation(getApplicationContext(), userLatLng.latitude, userLatLng.longitude);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("MapDemoActivity", "Error trying to get last GPS location");
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
            }

            return "this string is passed to onPostExecute";
        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //Draw the polyline
            // Do things like update the progress bar
        }

        // This runs in UI when background thread finishes
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
                        getPrice();
                        //drawPolyGradiant(thePath, "#f9ad81" ,"#aba100",9, 6);
                        drawPolyGradiant(thePath, "#76b5f9", "#1c549d", 9, 4);
                        builder.include(arrival);
                        int padding = 200;
                        LatLngBounds bounds = builder.build();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding), 1000, new GoogleMap.CancelableCallback() {
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

    private void drawPolyGradiant(List<LatLng> thePath, String startColor, String endColor, int width, int quality) {

        int Size = thePath.size();

        int Red = Integer.valueOf(startColor.substring(1, 3), 16);
        int Green = Integer.valueOf(startColor.substring(3, 5), 16);
        int Blue = Integer.valueOf(startColor.substring(5, 7), 16);
        int finalRed = Integer.valueOf(endColor.substring(1, 3), 16);
        int finalGreen = Integer.valueOf(endColor.substring(3, 5), 16);
        int finalBlue = Integer.valueOf(endColor.substring(5, 7), 16);

        for (int i = 0; i < quality - 1; i++) {

            float percent = (float) (1 / (float) (2 * quality)) + (float) i / (float) quality;
            int color = Color.argb(255,
                    (Red > finalRed) ? (int) (Red - ((Red - finalRed) * percent)) : (int) (Red + ((finalRed - Red) * percent)),
                    (Green > finalGreen) ? (int) (Green - ((Green - finalGreen) * percent)) : (int) (Green + ((finalGreen - Green) * percent)),
                    (Blue > finalBlue) ? (int) (Blue - ((Blue - finalBlue) * percent)) : (int) (Blue + ((finalBlue - Blue) * percent)));

            PolylineOptions opts = new PolylineOptions().geodesic(false).addAll(thePath.subList((int) ((Size / quality) * i), (int) (Size / quality) * (i + 2))).color(color).width(width + 1);
            mMap.addPolyline(opts);
        }

        float percentage = (float) (1 / (float) (2 * quality)) + (float) (quality - 1) / (float) quality;
        int color = Color.argb(255,
                (Red > finalRed) ? (int) (Red - ((Red - finalRed) * percentage)) : (int) (Red + ((finalRed - Red) * percentage)),
                (Green > finalGreen) ? (int) (Green - ((Green - finalGreen) * percentage)) : (int) (Green + ((finalGreen - Green) * percentage)),
                (Blue > finalBlue) ? (int) (Blue - ((Blue - finalBlue) * percentage)) : (int) (Blue + ((finalBlue - Blue) * percentage)));

        PolylineOptions opts = new PolylineOptions().geodesic(false).addAll(thePath.subList((int) ((Size / quality) * (quality - 1)), (int) (Size / quality) * (quality))).color(color).width(width + 1);
        mMap.addPolyline(opts);

        for (int i = 0; i < (Size - 1); i++) {

            float percent = ((float) i / (float) Size);
            int usedColor = Color.argb(255,
                    (Red > finalRed) ? (int) (Red - ((Red - finalRed) * percent)) : (int) (Red + ((finalRed - Red) * percent)),
                    (Green > finalGreen) ? (int) (Green - ((Green - finalGreen) * percent)) : (int) (Green + ((finalGreen - Green) * percent)),
                    (Blue > finalBlue) ? (int) (Blue - ((Blue - finalBlue) * percent)) : (int) (Blue + ((finalBlue - Blue) * percent)));

            opts = new PolylineOptions().add(thePath.get(i)).geodesic(false).add(thePath.get(i + 1)).color(usedColor).width(width);
            mMap.addPolyline(opts);
        }
    }


    private int driverSize;
    private Runnable runnable;
    private int stop = 0;

    private class sendRequestsTask extends AsyncTask<String, Integer, String> {
        SharedPreferences prefs;
        String userId;
        String image;
        boolean finishedSendReq;

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
            userId = prefs.getString("userID", null);
            finishedSendReq = false;
            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {

            SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
            final String userId = prefs.getString("userID", null);

            final int Step = 3; //Number Of Drivers To Call Every Time
            final int secondsDelay = 15000; // Time To Wait Before Sending Request To The Next Set O Drivers

            driverSize = driversKeys.size();
            driverSize = driversKeys.size();

            if (driverSize == 0) {
                finishedSendReq = true;
            }
            final Handler handler = new Handler(Looper.getMainLooper());
            runnable = new Runnable() {
                int counter = 0;

                @Override
                public void run() {
                    // Do the task...
                    if (stop == 1 || counter >= driversKeys.size() || idInList(driversKeys.get(counter), driversKeysHold)) {
                        finishedSendReq = true;
                        handler.removeCallbacks(this);
                        driversKeys.clear();
                        driversKeysHold.clear();
                        geoQuery.setCenter(new GeoLocation(startLatLng.latitude, startLatLng.longitude));
                        counter = 0;
                        stop = 0;

                        return;
                    }


                    //Initialize The First Requests
                    if (counter == 0) {
                        driversKeysHold.clear();
                        for (int j = counter; j < (counter + Step) && j < driversKeys.size(); j++) {
                            if (driversKeys.get(j) != null) {
                                final DatabaseReference pickupRequest = FirebaseDatabase.getInstance().getReference("PICKUPREQUEST").child(driversKeys.get(j)).child(userId);

                                Map<String, String> data = new HashMap<>();
                                data.put("client", clientID);
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

                                                finishedSendReq = true;
                                                FirebaseDatabase.getInstance().getReference("COURSES").orderByChild("client").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                            //findViewById(R.id.commander).setVisibility(View.GONE);
                                                            //selectDest.setVisibility(View.GONE);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                                counter = 0;
                                                stop = 0;
                                                pickupRequest.removeEventListener(this);

                                                FirebaseDatabase.getInstance().getReference("COURSES").orderByChild("client").equalTo(userId).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (!dataSnapshot.exists()) {
                                                            Toast.makeText(MapsActivity.this, "No Driver Found Please Try Again", Toast.LENGTH_SHORT).show();
                                                            courseScreenIsOn = false;
                                                            finishedSendReq = true;
                                                            handler.removeCallbacks(runnable);
                                                            driversKeys.clear();
                                                            driversKeysHold.clear();
                                                            geoQuery.setCenter(new GeoLocation(startLatLng.latitude, startLatLng.longitude));
                                                            counter = 0;
                                                            stop = 0;

                                                            stopSearchUI();
                                                        } else {

                                                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                                FirebaseDatabase.getInstance().getReference("DRIVERUSERS").child(data.child("driver").getValue(String.class)).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.exists()) {
                                                                            // driverName.setText(dataSnapshot.child("fullName").getValue(String.class));
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });


                                                                // startSearchUI();
                                                            }
                                                            //findViewById(R.id.driverInfo).setVisibility(View.VISIBLE);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                                return;
                                            }
                                            if (counter <= driversKeys.size())
                                                handler.postDelayed(runnable, 0);

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
                            data.put("client", clientID);
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
            handler.postDelayed(runnable, 100);

            while (!finishedSendReq) {
            }
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
            stopSearchUI();
            // Do things like hide the progress bar or change a TextView
        }
    }

    private void startSearchUI() {
        final RippleBackground rippleBackground = (RippleBackground) findViewById(R.id.gooVoidContent);
        rippleBackground.startRippleAnimation();
        cancelRequest.setVisibility(View.VISIBLE);
        menuButton.setVisibility(View.GONE);
    }

    private void stopSearchUI() {
        driversKeysHold.clear();
        AnimateConstraint.fadeIn(context, gooButton, 200, 10);
        //gooButton.setVisibility(View.VISIBLE);
        //gooButton.setAlpha(1);
        final RippleBackground rippleBackground = (RippleBackground) findViewById(R.id.gooVoidContent);
        rippleBackground.stopRippleAnimation();
        menuButton.setVisibility(View.VISIBLE);
        cancelRequest.setVisibility(View.GONE);
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
                stop = 1;
                for (int h = (counter - Step); h < (counter + Step) && h < driversKeys.size(); h++) {
                    //The Driver Has Not Answered The Pickup Call(Refused)
                    if (h >= 0) {
                        DatabaseReference pickupRequest = FirebaseDatabase.getInstance().getReference("PICKUPREQUEST").child(driversKeys.get(h)).child(userId);
                        pickupRequest.removeValue();
                    }
                }
            }
        });
    }


    public boolean idInList(final String ID, final List<String> idList) {
        for (String userId : idList) {
            if (ID.equals(userId)) {
                return true;
            }
        }
        return false;
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    public void updateViews() {
        Context context;
        Resources resources;
        String language;
        language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");

        context = LocalHelper.setLocale(MapsActivity.this, language);
        resources = context.getResources();

        TextView textView7 = (TextView) findViewById(R.id.textView7);
        TextView textView8 = (TextView) findViewById(R.id.textView8);
        TextView textView9 = (TextView) findViewById(R.id.textView9);
        TextView textVie = (TextView) findViewById(R.id.textVie);
        TextView textView12 = (TextView) findViewById(R.id.textView12);
        TextView textView11 = (TextView) findViewById(R.id.textView11);
        TextView textView = (TextView) findViewById(R.id.textView);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        Button confirm_start = (Button) findViewById(R.id.confirm_start);
        Button confirm_dest = (Button) findViewById(R.id.confirm_dest);
        Button passer = (Button) findViewById(R.id.passer);


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

}


