package com.comingoo.user.comingoo.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import com.comingoo.user.comingoo.utility.Utility;
import com.google.android.gms.maps.model.LatLng;

public class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {
    private Context mContext;
    private Utility utility;
    private String TAG = "ReverseGeocodingTask";
    private int orderDriverState;
    private EditText searchEditText;
    private EditText searchDestEditText;
    private boolean courseScreenIsOn;

    public ReverseGeocodingTask(Context mContext, int orderDriverState, EditText searchEditText, EditText searchDestEditText, boolean courseScreenIsOn){
        this.mContext = mContext;
        this.orderDriverState = orderDriverState;
        this.searchEditText = searchEditText;
        this.searchDestEditText = searchDestEditText;
        this.courseScreenIsOn = courseScreenIsOn;
    }

    // Finding address using reverse geocoding
    @Override
    protected String doInBackground(LatLng... params) {
        if (orderDriverState != 0 && orderDriverState != 1)
            return "";
        utility = new Utility();
        return utility.getCompleteAddressString(mContext, params[0].latitude, params[0].longitude);
    }

    @Override
    protected void onPostExecute(String addressText) {
        Log.e(TAG, "onPostExecute: " + addressText);
        if (courseScreenIsOn)
            return;
        if (orderDriverState == 0)
            searchEditText.setText(addressText);
        if (orderDriverState == 1)
            searchDestEditText.setText(addressText);

    }


}