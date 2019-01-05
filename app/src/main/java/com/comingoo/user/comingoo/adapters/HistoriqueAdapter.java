package com.comingoo.user.comingoo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.comingoo.user.comingoo.model.Course;
import com.comingoo.user.comingoo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ramotion.foldingcell.FoldingCell;

import java.text.DecimalFormat;
import java.util.List;

public class HistoriqueAdapter extends RecyclerView.Adapter<HistoriqueAdapter.ViewHolder> {
    private List<Course> mDataset;
    private Context mContext;
    private boolean isExpn = false;

    public HistoriqueAdapter(Context context, List<Course> myDataset) {
        this.mDataset = myDataset;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rows_history, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Course newCourse = mDataset.get(position);

        holder.fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.fc.toggle(false);
                if (isExpn) isExpn = false;
                else isExpn = true;
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

        holder.km.setText(new DecimalFormat("##.##").format(Double.parseDouble(newCourse.getDistance())) + "km");
        holder.waitTime.setText(newCourse.getWaitTime() + "min");

        holder.km2.setText(new DecimalFormat("##.##").format(Double.parseDouble(newCourse.getDistance())) + "km");
        holder.waitTime2.setText(newCourse.getWaitTime() + "min");


        if (Integer.parseInt(newCourse.getPreWaitTime()) > 180) {
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
                holder.basePriceVal.setText(dataSnapshot.child("base").getValue(String.class) + " MAD");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public View h;
        public TextView driverName, dateText, startText,
                startText2, endText2, endText, priceText, priceText3, km, kmVal, waitTime, waitTime2, km2, waitTimeVal, lateVal, basePriceVal, date2;
        public Button priceText2;
        public FoldingCell fc;

        TextView tvCourseDetails, tvPaymentDetails, textView31, textView35, textView38,
                textView50, textView29, departText, textView16, textView21, textView23, textView26;


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

            fc = v.findViewById(R.id.folding_cell);

            tvCourseDetails =  v.findViewById(R.id.textView28);
            tvCourseDetails.setText(mContext.getResources().getString(R.string.Détaildelacourse));

            tvPaymentDetails = (TextView) v.findViewById(R.id.textView30);
            tvPaymentDetails.setText(mContext.getResources().getString(R.string.Détailsdepaiement));

            textView31 = (TextView) v.findViewById(R.id.textView31);
            textView31.setText(mContext.getResources().getString(R.string.Kilométrage));

            textView35 = (TextView) v.findViewById(R.id.textView35);
            textView35.setText(mContext.getResources().getString(R.string.Tempsdattente));

            textView38 = (TextView) v.findViewById(R.id.textView38);
            textView38.setText(mContext.getResources().getString(R.string.Tarifderetard));

            textView50 = (TextView) v.findViewById(R.id.textView50);
            textView50.setText(mContext.getResources().getString(R.string.Prixdebase));

            textView29 = (TextView) v.findViewById(R.id.textView29);
            textView29.setText(mContext.getResources().getString(R.string.Facturetotale));

            departText = (TextView) v.findViewById(R.id.departText);
            departText.setText(mContext.getResources().getString(R.string.Départ));

            textView16 = (TextView) v.findViewById(R.id.textView16);
            textView16.setText(mContext.getResources().getString(R.string.Départ));

            textView21 = (TextView) v.findViewById(R.id.textView21);
            textView21.setText(mContext.getResources().getString(R.string.Kilométrage));


            textView23 = (TextView) v.findViewById(R.id.textView23);
            textView23.setText(mContext.getResources().getString(R.string.Tempsdattente));


            textView26 = (TextView) v.findViewById(R.id.textView26);
            textView26.setText(mContext.getResources().getString(R.string.Chauffeur));
        }
    }

}
