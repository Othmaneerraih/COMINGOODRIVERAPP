package com.comingoo.user.comingoo;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

        View h;
        public TextView title, address;
        ImageView image,addBtn;
        RelativeLayout clickView;

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final place newPlace = mDataset.get(position);

        holder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        MapsActivity.showSearchAddressStartUI();
                            MapsActivity.goToLocation(context, Double.parseDouble(newPlace.getLat()), Double.parseDouble(newPlace.getLng()),newPlace);
            }
        });
        //holder.image.setImageResource(newPlace.getImage());
//        if(newPlace.getName() == null){
//            holder.title.setText("No Data Available");
//        }else{
//            holder.title.setText(newPlace.getName());
//        }
        holder.title.setText(newPlace.getName());

//        if(newPlace.getAddress()==null){
//            holder.title.setText("");
//        }else{
//            holder.title.setText(newPlace.getName());
//        }
        holder.title.setText(newPlace.getName());

        holder.address.setText(newPlace.getAddress());
        holder.image.setImageResource(newPlace.getImage());
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,FevoriteLocationActivity.class);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}

