package com.cookandroid.haje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ShowRideActivity extends AppCompatActivity {

    final int ITEM_SIZE = 5;

    FirebaseFirestore db;
    Intent uuidIntent;
    String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ride);

        ImageButton btnPrev =findViewById(R.id.btnPrev);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GpsActivity.class);
                startActivity(intent);
            }
        });




/*
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        List<Item> items = new ArrayList<>();
        Item[] item = new Item[ITEM_SIZE];

        db = FirebaseFirestore.getInstance();
        uuidIntent = getIntent();
        uuid = uuidIntent.getStringExtra("uuid");

        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), items, R.layout.activity_show_ride, db, uuid));

*/
   }

    }


}