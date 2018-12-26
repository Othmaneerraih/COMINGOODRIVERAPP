package com.comingoo.user.comingoo.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comingoo.user.comingoo.R;
import com.comingoo.user.comingoo.utility.AnimateConstraint;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.skyfishjy.library.RippleBackground;

public class MapsActivityNew extends FragmentActivity implements OnMapReadyCallback{
    private String TAG = "MapsActivityNew";
    private ImageView ivDrawerToggol;
    private GoogleMap mMap;
    private EditText etSearchStartAddress;
    private EditText etSearchDestination;
    private ImageView ivCurrentLocation;

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
//        ivArraw = findViewById(R.id.iv_arrow_start_end);
//        ivGoo = findViewById(R.id.iv_goo);
        btnPickUp = findViewById(R.id.btn_pickup);
//        btnConfirmDestination = findViewById(R.id.btn_destination);
        ivCancelRequest = findViewById(R.id.iv_cancel_request);
        llDeliveryCar = findViewById(R.id.ll_delivery_car);
        ivWheel = findViewById(R.id.iv_wheel);
//        contentPricePromoCode = findViewById(R.id.content_price_promo_code);
        tvPromoCode = findViewById(R.id.tv_promo_code);
    }

    private void action() {
        // Getting user id from shared pref
//        SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
//        userId = prefs.getString("userID", null);



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
                AnimateConstraint.animate(getApplicationContext(), rlStartPoint, dpHeight-80, 0, 500);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
