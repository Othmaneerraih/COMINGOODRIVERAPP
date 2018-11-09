package com.comingoo.user.comingoo;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyPlaceAdapter extends RecyclerView.Adapter<MyPlaceAdapter.ViewHolder> {
    private List<place> mDataset;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public View h;
        public TextView title, address;
        public ImageView image;
        public ViewHolder(View v) {
            super(v);
            h = v;

            image = v.findViewById(R.id.imageView);
            title = v.findViewById(R.id.title);
            address = v.findViewById(R.id.address);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyPlaceAdapter(Context context, List<place> myDataset) {
        this.mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyPlaceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_rows, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final place newPlace = mDataset.get(position);

        holder.h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        MapsActivity.showSearchAddressStartUI();
                            MapsActivity.goToLocation(context, Double.parseDouble(newPlace.getLat()), Double.parseDouble(newPlace.getLng()));
            }
        });
        //holder.image.setImageResource(newPlace.getImage());
        holder.title.setText(newPlace.getName());
        holder.address.setText(newPlace.getAddress());
        holder.image.setImageResource(newPlace.getImage());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}

