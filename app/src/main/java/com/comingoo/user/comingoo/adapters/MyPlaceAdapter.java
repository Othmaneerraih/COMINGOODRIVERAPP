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
import com.comingoo.user.comingoo.interfaces.PickLocation;
import com.comingoo.user.comingoo.R;
import com.comingoo.user.comingoo.model.place;


import java.util.ArrayList;

public class MyPlaceAdapter extends RecyclerView.Adapter<MyPlaceAdapter.ViewHolder> {
    private ArrayList<place> mDataset;
    private Context context;
    private boolean isAddButtonNeed;
    private String userId;
    private PickLocation pickLocation;

    public MyPlaceAdapter(Context context, ArrayList<place> myDataset,
                          boolean isAddNeed, String userId, PickLocation pickLocation) {
        this.mDataset = myDataset;
        this.context = context;
        this.userId = userId;
        this.isAddButtonNeed = isAddNeed;
        this.pickLocation = pickLocation;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rows_places, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final place newPlace = mDataset.get(position);

        if (isAddButtonNeed) holder.addBtn.setVisibility(View.VISIBLE);
        else holder.addBtn.setVisibility(View.GONE);

        holder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickLocation.pickedLocation(newPlace);
            }
        });

        holder.title.setText(newPlace.getName());
        holder.address.setText(newPlace.getAddress());
        holder.image.setImageResource(R.drawable.lieux_proches);
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FevoriteLocationActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("userId", userId);
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

