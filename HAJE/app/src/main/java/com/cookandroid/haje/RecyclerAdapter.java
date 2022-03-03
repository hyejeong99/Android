package com.cookandroid.haje;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    List<Item> items;
    int item_layout;

    FirebaseFirestore db;
    String uuid;

    public RecyclerAdapter(Context context, List<Item> items, int item_layout, FirebaseFirestore db, String uuid) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
        this.db = db;
        this.uuid = uuid;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView rideDate, rideTime, ridePoint, arriveTime, arrivePoint;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            rideDate = (TextView) itemView.findViewById(R.id.tv_rideDate);
            rideTime = (TextView) itemView.findViewById(R.id.tv_rideTime);
            ridePoint = (TextView) itemView.findViewById(R.id.tv_ridePlace);
            arriveTime = (TextView) itemView.findViewById(R.id.tv_arriveTime);
            arrivePoint = (TextView) itemView.findViewById(R.id.tv_destination);
            cardview = (CardView) itemView.findViewById(R.id.cardview);

            db.collection("breakdown").document(uuid).get()
                    .addOnCompleteListener(task -> {
                       if(task.isSuccessful()){
                           rideDate.setText(task.getResult().get("date").toString());
                           rideTime.setText(task.getResult().get("startTime").toString());
                           ridePoint.setText(task.getResult().get("departure").toString());
                           arriveTime.setText(task.getResult().get("endTime").toString());
                           arrivePoint.setText(task.getResult().get("destination").toString());
                       }
                       else{
                           Log.d("db접근 실패", task.getException().getMessage());
                       }
                    });
        }
    }
}