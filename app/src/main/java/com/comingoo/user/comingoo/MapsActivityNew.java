package com.comingoo.user.comingoo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comingoo.user.comingoo.adapters.FavouritePlaceAdapter;
import com.comingoo.user.comingoo.adapters.MyPlaceAdapter;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivityNew extends FragmentActivity implements OnMapReadyCallback {
    private String TAG = "MapsActivityNew";
    private ImageView ivDrawerToggol;
    private GoogleMap mMap;
    private EditText etSearchStartAddress;
    private EditText etSearchDestination;
    private ImageView ivCurrentLocation;

    private float density;
    private float dpHeight;
    private float dpWidth;
    int HeightAbsolute;
    private int orderDriverState;
    private boolean courseScreenIsOn = false;

    private TextView tvClosestDriver;
    private TextView tvFrameTime, tvClosestDriverText;

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

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_new);
        initialize();
        action();
    }

    private void initialize() {
        driversKeys = new ArrayList<String>();
        driversLocations = new ArrayList<String>();
        driversKeysHold = new ArrayList<String>();
        placeDataList = new ArrayList<>();
        fPlaceDataList = new ArrayList<>();
        rPlaceDataList = new ArrayList<>();


        ivDrawerToggol = findViewById(R.id.iv_menu_maps);
        ivCurrentLocation = findViewById(R.id.iv_current_location);
        etSearchStartAddress = findViewById(R.id.et_start_point);
        etSearchDestination = findViewById(R.id.et_end_point);
        btnConfirmDestination = findViewById(R.id.btn_destination);

        tvClosestDriver = findViewById(R.id.tv_closest_driver);
        tvFrameTime = findViewById(R.id.tv_closest_driverPin);
        tvClosestDriverText = findViewById(R.id.tv_closest_driver);

        btnPickUp = findViewById(R.id.btn_pickup);
        rlStartPoint = findViewById(R.id.rl_start_point);
        rlEndPoint = findViewById(R.id.rl_end_point);
        ivWheel = findViewById(R.id.iv_wheel);

        llConfirmDestination = findViewById(R.id.ll_destination);
        btnIgnoreDestination = findViewById(R.id.btn_destination_ignore);

        rvFavPlaces = findViewById(R.id.rv_fav_place);
        rvRecentPlaces = findViewById(R.id.rv_recent_places);
        tvFavPlace = findViewById(R.id.tv_fav_pace);
        tvRecentPlace = findViewById(R.id.tv_recent_place);

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
                ConstraintLayout contentBlocker = findViewById(R.id.contentBlocker);
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

        etSearchStartAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = 1;
                AnimateConstraint.animate(MapsActivityNew.this, rlStartPoint, (dpHeight - 80), 100, 0);
                ivWheel.setVisibility(View.GONE);
                btnPickUp.setVisibility(View.GONE);
                tvFavPlace.setVisibility(View.VISIBLE);
                tvRecentPlace.setVisibility(View.VISIBLE);
                rvFavPlaces.setVisibility(View.VISIBLE);
                rvRecentPlaces.setVisibility(View.VISIBLE);

                rlEndPoint.setVisibility(View.VISIBLE);
                AnimateConstraint.animate(getApplicationContext(), rlEndPoint, 150, 1, 500);
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

        btnIgnoreDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destLatLng = null;
                switchToCommandLayout();
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

        AnimateConstraint.animate(getApplicationContext(), rlStartPoint, (dpHeight - 20), 50, 500);
        AnimateConstraint.fadeIn(getApplicationContext(), rlEndPoint, 500, 10);


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
//        hideKeyboard(geta);
        rlStartPoint.setVisibility(View.VISIBLE);
    }

    private void switchToCommandLayout() {
        orderDriverState = 2;
        ivCurrentLocation.setVisibility(View.GONE);
        AnimateConstraint.animate(getApplicationContext(), rlEndPoint,
                dpHeight - 40, 180, 500, llConfirmDestination, findViewById(R.id.iv_arrow_start_end));


        AnimateConstraint.fadeIn(MapsActivityNew.this, findViewById(R.id.gooContent), 500, 10);

        rlStartPoint.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (int) (dpHeight - 62), getApplicationContext().getResources().getDisplayMetrics());

        llConfirmDestination.setVisibility(View.GONE);

        state = 2;

        findViewById(R.id.iv_location_pin_dest).setVisibility(View.GONE);


//        if (destLatLng != null) {
//            new DrawRouteTask().execute(startLatLng, destLatLng);
//            gooButton.setVisibility(View.GONE);
//            frameLayout2.setDrawingCacheEnabled(true);
//            frameLayout2.buildDrawingCache();
//            Bitmap bm = frameLayout2.getDrawingCache();
//            Marker myMarker = mMap.addMarker(new MarkerOptions()
//                    .position(destLatLng)
//                    .icon(BitmapDescriptorFactory.fromBitmap(bm)));
//
//
//        } else {
//            gooButton.setVisibility(View.VISIBLE);
//            searchDestEditText.setText("Destination non choisi.");
//        }
//
//        ivDrawerToggol.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cancelCommandLayout();
//            }
//        });
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

//        hideSearchAddressStartUI();
        etSearchStartAddress.setVisibility(View.VISIBLE);

//        searchButton.setVisibility(View.VISIBLE);

        etSearchDestination.setVisibility(View.GONE);
//        selectDest.setVisibility(View.GONE);


        etSearchStartAddress.setEnabled(true);

        AnimateConstraint.animate(MapsActivityNew.this, etSearchStartAddress, 120, (dpHeight - 80), 500);

        findViewById(R.id.ic_location_pin_start).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_closest_driver).setVisibility(View.VISIBLE);
        findViewById(R.id.iv_location_pin_dest).setVisibility(View.GONE);


        ivDrawerToggol.setImageResource(R.drawable.home_icon);
        ivDrawerToggol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mDrawer.openMenu(true);
                View contentMap = findViewById(R.id.content_map_view);
                ConstraintLayout contentBlocker = findViewById(R.id.contentBlocker);
                AnimateConstraint.resideAnimation(getApplicationContext(), contentMap, contentBlocker, (int) dpWidth, (int) dpHeight, 200);
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
