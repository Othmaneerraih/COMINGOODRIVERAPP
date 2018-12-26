package com.comingoo.user.comingoo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comingoo.user.comingoo.R;
import com.comingoo.user.comingoo.ViewModel.MapsActivityVM;
import com.comingoo.user.comingoo.utility.AnimateConstraint;
import com.comingoo.user.comingoo.utility.LocalHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.skyfishjy.library.RippleBackground;

import static android.provider.Settings.*;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivityNew extends FragmentActivity implements OnMapReadyCallback{
    private String TAG = "MapsActivityNew";
    private ImageView ivDrawerToggol;
    private GoogleMap mMap;
    private EditText etSearchStartAddress;
    private EditText etSearchDestination;
    private ImageView ivCurrentLocation;

    private String language;
    private Resources resources;

  //  private float distance;
    private float density;
    private float dpHeight;
    private float dpWidth;
    int HeightAbsolute;
    private LinearLayout llDrawerHistory,
            llDrawerNotifications, llDrawerInvite, llDrawerComingooYou, llDrawerAide;
    // Pickup Edittext contents
    private Button btnPickUp;
    private LinearLayout llDeliveryCar;
    private RelativeLayout rlStartPoint, rlEndPoint;
    private ImageView ivWheel;
    private TextView tvPrice, tvPromoCode;
    private ImageView ivShadow, ivCancelRequest;
    private RippleBackground rippleBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_new);
        initialize();
        action();
    }

    private void initialize() {
        llDrawerInvite = findViewById(R.id.ll_drawer_refer_friend);
        llDrawerNotifications = findViewById(R.id.ll_drawer_notificatiions);
        llDrawerHistory = findViewById(R.id.ll_drawer_history);
        llDrawerAide = findViewById(R.id.ll_drawer_help);
        llDrawerComingooYou = findViewById(R.id.ll_drawer_comingoo_you);
        ivDrawerToggol = findViewById(R.id.iv_menu_maps);
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
    }

    private void action() {
        language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");
        resources = getApplicationContext().getResources();


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
                        dpHeight-250, 100, 500);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.setBuildingsEnabled(false);

        if (!isLocationEnabled(MapsActivityNew.this))
            checkLocationService();
        else {
            if (ContextCompat.checkSelfPermission(MapsActivityNew.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivityNew.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        }
    }

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:

                        break;
                    case Activity.RESULT_CANCELED:

                        break;
                }
                break;
        }
    }

    public boolean isLocationEnabled(Context context) {
        int locationMode;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Secure.getInt(context.getContentResolver(), Secure.LOCATION_MODE);
            } catch (SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Secure.getString(context.getContentResolver(),
                    Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    private void checkLocationService() {
        try {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MapsActivityNew.this);
            dialog.setMessage(resources.getString(R.string.txt_location_permission));
            dialog.setPositiveButton(resources.getString(R.string.txt_open_location), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });

            dialog.setNegativeButton(getString(R.string.txt_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
