package com.comingoo.user.comingoo.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comingoo.user.comingoo.activity.FevoriteLocationActivity;
import com.comingoo.user.comingoo.activity.MapsActivity;
import com.comingoo.user.comingoo.R;
import com.comingoo.user.comingoo.model.Place;

import java.util.List;

public class FavouritePlaceAdapter  extends RecyclerView.Adapter<FavouritePlaceAdapter.ViewHolder> {
    private List<Place> mDataset;
    private Context context;
    private boolean isAddButtonNeed;
    private String userId;

    public FavouritePlaceAdapter(Context context, List<Place> myDataset, boolean isAddNeed, String userId) {
        this.mDataset = myDataset;
        this.userId = userId;
        this.context = context;
        this.isAddButtonNeed = isAddNeed;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_rows, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Place newPlace = mDataset.get(position);

        if (isAddButtonNeed) holder.addBtn.setVisibility(View.VISIBLE);
        else holder.addBtn.setVisibility(View.GONE);

        holder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newPlace.getLat() != null && newPlace.getLng() != null) {
                    if (!newPlace.getLat().isEmpty() && !newPlace.getLng().isEmpty()) {
                        MapsActivity.showSearchAddressStartUI();
                        MapsActivity.goToLocation(context, Double.parseDouble(newPlace.
                                getLat()), Double.parseDouble(newPlace.getLng()), newPlace);
                    }
                }
            }
        });

        if (position ==0)
        holder.title.setText("Work");
        else if (position == 1) holder.title.setText("Home");

        holder.address.setText(newPlace.getAddress());

        holder.image.setImageResource(newPlace.getImage());
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FevoriteLocationActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("userId", userId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View h;
        public TextView title, address;
        public ImageView image, addBtn;
        public RelativeLayout clickView;

        ViewHolder(View v) {
            super(v);
            h = v;

            image = v.findViewById(R.id.imageView);
            title = v.findViewById(R.id.title);
            address = v.findViewById(R.id.address);
            addBtn = v.findViewById(R.id.add_btn);
            clickView = v.findViewById(R.id.click_view);
        }
    }

}