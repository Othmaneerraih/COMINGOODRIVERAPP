package com.comingoo.user.comingoo;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class FevoriteLocationActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private String TAG = "FevoriteLocationActivity";

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private EditText searchEt;
    private Button confirmBtn;
    private int position;
    private String userId = "";
    private String febPlaceName = "";
    private String febPlaceLat = "";
    private String febPlacelong = "";
    private String febPlaceAddress = "";
    private PlaceAutocompleteFragment autocompleteFragment;
    private ImageButton positionButton;
    private LatLng searchLatLng;

    private GeoQuery geoQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fevorite_location);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        searchEt = (EditText) findViewById(R.id.search_edit_text);
        confirmBtn = (Button) findViewById(R.id.confirm_btn);
        positionButton = (ImageButton) findViewById(R.id.my_position);

        positionButton.setImageBitmap(scaleBitmap(40, 37, R.drawable.my_position_icon));

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


        position = getIntent().getIntExtra("position", 0);
        userId = getIntent().getStringExtra("userId");

        if (position == 0) {
            febPlaceName = "Work";
        } else {
            febPlaceName = "Home";
        }

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!febPlaceAddress.isEmpty() && febPlaceLat != "" && febPlacelong != "") {
                    FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).child("favouritePlace").child(febPlaceName).child("Lat").setValue(febPlaceLat);
                    FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).child("favouritePlace").child(febPlaceName).child("Long").setValue(febPlacelong);
                    FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).child("favouritePlace").child(febPlaceName).child("Address").setValue(febPlaceAddress);
                    onBackPressed();
                } else {
                    Toast.makeText(FevoriteLocationActivity.this, "Please set ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        positionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchLatLng != null) {
                    getLastLocation();
                }
            }
        });

        autocompleteFragment.getView().setBackgroundResource(R.drawable.main_edit_text);

        setAutocompleteFragmentAction();

        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("MA")
                .build();

        autocompleteFragment.setFilter(autocompleteFilter);
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

    @Override
    public void onPause() {
        super.onPause();

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
//        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
        mGoogleMap.setBuildingsEnabled(false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            getLastLocation();
        }

        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (geoQuery != null) {
                    geoQuery.setCenter(new GeoLocation(searchLatLng.latitude, searchLatLng.longitude));
                }

                searchLatLng = mGoogleMap.getCameraPosition().target;

                new ReverseGeocodingTask(FevoriteLocationActivity.this).execute(searchLatLng);

            }
        });

        mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                autocompleteFragment.setText(getCompleteAddressString(getApplicationContext(), searchLatLng.latitude, searchLatLng.longitude));
                searchEt.setText(getCompleteAddressString(getApplicationContext(), searchLatLng.latitude, searchLatLng.longitude));
            }
        });

//        animateMarker(searchLatLng, false);
    }

    MarkerOptions markerOptions;

    public void animateMarker(final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mGoogleMap.getProjection();
        int height = 150;
        int width = 80;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.depart_pin);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        markerOptions = new MarkerOptions().position(searchLatLng)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        Point startPoint = proj.toScreenLocation(markerOptions.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                mGoogleMap.addMarker(markerOptions).setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        mGoogleMap.addMarker(markerOptions).setVisible(false);
                    } else {
                        mGoogleMap.addMarker(markerOptions).setVisible(true);
                    }
                }
            }
        });
    }

    private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {
        Context mContext;
        double lat;
        double lon;

        public ReverseGeocodingTask(Context context) {
            super();
            mContext = context;
        }

        // Finding address using reverse geocoding
        @Override
        protected String doInBackground(LatLng... params) {
            lon = params[0].longitude;
            lat = params[0].latitude;
            return getCompleteAddressString(getApplicationContext(), params[0].latitude, params[0].longitude);
        }

        @Override
        protected void onPostExecute(String addressText) {
            searchEt.setText(addressText);
            autocompleteFragment.setText(addressText);
            febPlaceLat = String.valueOf(lat);
            febPlacelong = String.valueOf(lon);
            febPlaceAddress = addressText;
        }
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
                                searchLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                goToLocation(getApplicationContext(), searchLatLng.latitude, searchLatLng.longitude, null);
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

    public void goToLocation(Context context, Double lat, Double lng, place rPlace) {
        mGoogleMap.clear();
        int height = 150;
        int width = 80;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.depart_pin);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        autocompleteFragment.setText(getCompleteAddressString(getApplicationContext(), lat, lng));
//        mGoogleMap.addMarker(markerOptions);

    }

    private void setAutocompleteFragmentAction() {
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mGoogleMap.clear();
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 12.0f));

                int height = 150;
                int width = 80;
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.depart_pin);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                MarkerOptions markerOptions = new MarkerOptions().position(place.getLatLng())
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(place.getLatLng())      // Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .build();
                mGoogleMap.addMarker(markerOptions);


                searchEt.setText(place.getName().toString());
                febPlaceLat = String.valueOf(place.getLatLng().latitude);
                febPlacelong = String.valueOf(place.getLatLng().longitude);
                febPlaceAddress = place.getName().toString();
            }

            @Override
            public void onError(Status status) {

            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.depart_pin);
//        scaleBitmap(76, 56, R.drawable.depart_pin)

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("depart_pin", 56, 76)));
//        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        int height = 150;
        int width = 80;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.depart_pin);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .build();
        mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(FevoriteLocationActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

}