package com.comingoo.user.comingoo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ramotion.foldingcell.FoldingCell;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import comingoo.one.user.comingoouser.R;

public class historiqueActivity extends AppCompatActivity {
    private RecyclerView mLocationView;
    private DatabaseReference mLocation;
    private MyAdapter cAdapter;
    private List<Course> CoursesData;
    private String userId;

    Context context;
    Resources resources;
    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);
        language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");

        context = LocalHelper.setLocale(historiqueActivity.this, language);
        resources = context.getResources();


        SharedPreferences prefs = getSharedPreferences("COMINGOOUSERDATA", MODE_PRIVATE);
        userId = prefs.getString("userID", null);



        mLocation = FirebaseDatabase.getInstance().getReference("CLIENTFINISHEDCOURSES").child(userId);
        mLocation.keepSynced(true);

        CoursesData  = new ArrayList<>();
        mLocationView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLocationView.setHasFixedSize(true);
        mLocationView.setLayoutManager(new LinearLayoutManager(this));

        cAdapter = new MyAdapter(CoursesData);
        mLocationView.setAdapter(cAdapter);
        new CheckUserTask().execute();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateViews();

    }

    private class CheckUserTask extends AsyncTask<String, Integer, String> {
        SharedPreferences prefs;
        String userId;
        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prefs = getSharedPreferences("COMINGOODRIVERDATA", MODE_PRIVATE);
            userId = prefs.getString("userId", null);
            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {

            mLocation.orderByChild("date").limitToFirst(20).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    CoursesData.clear();
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        if(data.child("date").exists()){
                            String dateString = getDate((data.child("date").getValue(Long.class) * -1));
                            Course newCourse = new Course(
                                    data.child("startAddress").getValue(String.class),
                                    data.child("endAddress").getValue(String.class),
                                    data.child("client").getValue(String.class),
                                    dateString,
                                    data.child("distance").getValue(String.class),
                                    data.child("driver").getValue(String.class),
                                    data.child("preWaitTime").getValue(String.class),
                                    data.child("price").getValue(String.class) + " MAD",
                                    data.child("waitTime").getValue(String.class)
                            );
                            CoursesData.add(newCourse);
                        }
                    }
                    cAdapter.notifyDataSetChanged();
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

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000L);
        String date = DateFormat.format("dd-MM-yyyy\nhh:mm:ss", cal).toString();
        return date;
    }

    private String driverName;
    private int Rate;
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<Course> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case

            public View h;
            public TextView driverName, dateText, startText, startText2, endText2, endText, priceText, priceText3, km, kmVal, waitTime, waitTime2, km2, waitTimeVal, lateVal, basePriceVal, date2;
            public Button priceText2;
            public FoldingCell fc;

            TextView textView28, textView30,textView31,textView35,textView38,textView50,textView29,departText, textView16, textView21, textView23, textView26;


            public ViewHolder(View v) {
                super(v);
                h = v;
                dateText = v.findViewById(R.id.date);
                startText = v.findViewById(R.id.depart);
                endText = v.findViewById(R.id.destination);
                priceText = v.findViewById(R.id.price);

                date2 = v.findViewById(R.id.dateText);
                startText2 = v.findViewById(R.id.depart_text);
                endText2 = v.findViewById(R.id.end_text);
                priceText2 = v.findViewById(R.id.price2);
                priceText3 = v.findViewById(R.id.price3);
                km = v.findViewById(R.id.km);
                kmVal = v.findViewById(R.id.kmValue);
                waitTime = v.findViewById(R.id.waitTime);
                waitTimeVal = v.findViewById(R.id.waitTimeValue);
                lateVal = v.findViewById(R.id.preWaitV);
                basePriceVal = v.findViewById(R.id.basePrice);
                driverName = v.findViewById(R.id.textView27);

                waitTime2 = v.findViewById(R.id.waitTime2);
                km2 = v.findViewById(R.id.km2);

                fc = (FoldingCell) v.findViewById(R.id.folding_cell);

                textView28 = (TextView) v.findViewById(R.id.textView28);
                textView28.setText(resources.getString(R.string.Détaildelacourse));

                textView30 = (TextView) v.findViewById(R.id.textView30);
                textView30.setText(resources.getString(R.string.Détailsdepaiement));

                textView31 = (TextView) v.findViewById(R.id.textView31);
                textView31.setText(resources.getString(R.string.Kilométrage));

                textView35 = (TextView) v.findViewById(R.id.textView35);
                textView35.setText(resources.getString(R.string.Tempsdattente));

                textView38 = (TextView) v.findViewById(R.id.textView38);
                textView38.setText(resources.getString(R.string.Tarifderetard));

                textView50 = (TextView) v.findViewById(R.id.textView50);
                textView50.setText(resources.getString(R.string.Prixdebase));

                textView29 = (TextView) v.findViewById(R.id.textView29);
                textView29.setText(resources.getString(R.string.Facturetotale));

                departText = (TextView) v.findViewById(R.id.departText);
                departText.setText(resources.getString(R.string.Départ));

                textView16 = (TextView) v.findViewById(R.id.textView16);
                textView16.setText(resources.getString(R.string.Départ));

                textView21 = (TextView) v.findViewById(R.id.textView21);
                textView21.setText(resources.getString(R.string.Kilométrage));


                textView23 = (TextView) v.findViewById(R.id.textView23);
                textView23.setText(resources.getString(R.string.Tempsdattente));


                textView26 = (TextView) v.findViewById(R.id.textView26);
                textView26.setText(resources.getString(R.string.Chauffeur));



            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(List<Course> myDataset) {
            this.mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.courses_rows, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            final Course newCourse = mDataset.get(position);

            holder.fc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.fc.toggle(false);
                }
            });

            holder.dateText.setText(newCourse.getDate());
            holder.date2.setText(newCourse.getDate());
            holder.startText.setText(newCourse.getStartAddress());
            holder.endText.setText(newCourse.getEndAddress());
            holder.startText2.setText(newCourse.getStartAddress());
            holder.endText2.setText(newCourse.getEndAddress());
            holder.priceText.setText(newCourse.getPrice());
            holder.priceText2.setText(newCourse.getPrice());
            holder.priceText3.setText(newCourse.getPrice());

            holder.km.setText(new DecimalFormat("##.##").format(Double.parseDouble(newCourse.getDistance()))+"km");
            holder.waitTime.setText(newCourse.getWaitTime() + "min");

            holder.km2.setText(new DecimalFormat("##.##").format(Double.parseDouble(newCourse.getDistance()))+"km");
            holder.waitTime2.setText(newCourse.getWaitTime() + "min");


            if ( Integer.parseInt(newCourse.getPreWaitTime()) > 180) {
                holder.lateVal.setText("3 MAD");
            } else {
                holder.lateVal.setText("0 MAD");
            }
            FirebaseDatabase.getInstance().getReference("DRIVERUSERS").child(newCourse.getDriver()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    holder.driverName.setText(dataSnapshot.child("fullName").getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            FirebaseDatabase.getInstance().getReference("PRICES").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    holder.kmVal.setText((new DecimalFormat("##.##").format(Double.parseDouble(newCourse.getDistance()) * Double.parseDouble(dataSnapshot.child("km").getValue(String.class)))) + " MAD");
                    holder.waitTimeVal.setText((new DecimalFormat("##.##").format(Double.parseDouble(newCourse.getWaitTime()) * Double.parseDouble(dataSnapshot.child("att").getValue(String.class)))) + " MAD");
                    holder.basePriceVal.setText(dataSnapshot.child("base").getValue(String.class)+ " MAD");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }


    public void updateViews(){
        Context context;
        Resources resources;
        String language;
        language = getApplicationContext().getSharedPreferences("COMINGOOLANGUAGE", Context.MODE_PRIVATE).getString("language", "fr");

        context = LocalHelper.setLocale(historiqueActivity.this, language);
        resources = context.getResources();

        TextView title = (TextView) findViewById(R.id.title);




        //Set Texts
        title.setText(resources.getString(R.string.Historique));

    }

}
