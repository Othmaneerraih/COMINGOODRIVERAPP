package com.comingoo.user.comingoo.async;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.comingoo.user.comingoo.R;
import com.comingoo.user.comingoo.activity.AideActivity;
import com.comingoo.user.comingoo.others.HttpConnection;
import com.comingoo.user.comingoo.others.PathJSONParser;
import com.comingoo.user.comingoo.utility.LocalHelper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReadTask extends AsyncTask<String, Void, String> {
    private Context context;
    private GoogleMap mMap;
    private Resources resources;

    public  ReadTask(Context context, GoogleMap mMap){
        this.context = context;
        this.mMap = mMap;
        String language = context.getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");
        Context co = LocalHelper.setLocale(context, language);
        resources = co.getResources();
    }
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
        new ParserTask().execute(s);
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
            if (routes != null) {
                if (routes.size() > 0){
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
                }
            }
            if (polyLineOptions == null) {
                Toast.makeText(context, resources.getString(R.string.draw_path_warning_txt), Toast.LENGTH_LONG).show();
            } else {
                mMap.addPolyline(polyLineOptions);
            }
        }
    }

}