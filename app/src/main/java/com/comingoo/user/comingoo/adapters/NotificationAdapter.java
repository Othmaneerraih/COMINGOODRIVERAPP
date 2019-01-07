package com.comingoo.user.comingoo.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.comingoo.user.comingoo.R;
import com.comingoo.user.comingoo.activity.MapsActivity;
import com.comingoo.user.comingoo.model.Notification;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<Notification> mDataset;
    private String userId;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public View h;
        public TextView title, content;
        public ImageView promoImg;
        public Button promoCode, apply;

        public ViewHolder(View v) {
            super(v);
            h = v;

            title = v.findViewById(R.id.title);
            content = v.findViewById(R.id.content);
            promoImg = v.findViewById(R.id.imageView8);
            promoCode = v.findViewById(R.id.tv_promo_code);
            apply = v.findViewById(R.id.apply);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NotificationAdapter(List<Notification> myDataset, String userId, Context context) {
        this.mDataset = myDataset;
        this.userId = userId;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rows_notification, parent, false);
        NotificationAdapter.ViewHolder vh = new NotificationAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final NotificationAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Notification newCourse = mDataset.get(position);

        holder.title.setText(newCourse.getTitle());
        holder.content.setText(newCourse.getContent());
        if (newCourse.getCode() != null) {
            if (newCourse.getCode().length() > 0) {
                holder.promoImg.setVisibility(View.VISIBLE);
                holder.promoCode.setVisibility(View.VISIBLE);
                holder.apply.setVisibility(View.VISIBLE);

                holder.promoCode.setText(newCourse.getCode());
                holder.apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (newCourse != null) {
                                if (newCourse.getCode() != null) {
                                    MapsActivity.promoCode.setText(newCourse.getCode());
                                    MapsActivity.ivPromocode.setImageResource(R.drawable.ic_promo_code_ok);
                                    FirebaseDatabase.getInstance().getReference("clientUSERS").child(userId).child("PROMOCODE").setValue(newCourse.getCode());

                                    ((Activity) context).finish();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
            } else {
                holder.apply.setVisibility(View.GONE);
                holder.promoCode.setVisibility(View.GONE);
            }

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}