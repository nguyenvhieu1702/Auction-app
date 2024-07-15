package com.example.btlauction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivityMyAuction extends AppCompatActivity {

    ImageView back_activityMyAuction;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    RecyclerView rcv_photo;

    SwipeRefreshLayout swipeRefreshLayout;
    productEdit_adapter productEditAdapter;
    List<Product> mProduct;
    BottomNavigationView bottomNavigationView;
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String uidNow = currentUser.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_my_auction);
        back_activityMyAuction  = findViewById(R.id.back_activityMyAution);


        rcv_photo = findViewById(R.id.rcv_activiyMyAuction);

        mProduct = new ArrayList<>();


        productEditAdapter = new productEdit_adapter( mProduct, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rcv_photo.setAdapter(productEditAdapter);
        rcv_photo.setLayoutManager(gridLayoutManager);


        getData();




        back_activityMyAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityMyAuction.this,MainActivityProfile.class);
                startActivity(intent);
                finish();
            }
        });

        swipeRefreshLayout = findViewById(R.id.refresh_activityMyAuction);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mProduct.clear();
                getData();
                productEditAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void getData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("CurrentUser")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String uid = document.getString("Uid");
                                if (uid.equals(uidNow)) {
                                    String document_id = document.getId();
                                    String productCollectionPath = "CurrentUser/" +  document_id + "/products"; // đường dẫn đến collection products trong document có id là currentUserDocID
                                    String favoritesCollectionPath = "CurrentUser/" +  document_id + "/favorites";

                                    db.collection(productCollectionPath)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            String id = document.getId();
                                                            String name = document.getString("name");
                                                            String describe = document.getString("describe");
                                                            String Price = document.getString("priceStart");
                                                            String bidPrice =  document.getString("bidPrice");

                                                            String dayStart = document.getString("timeStart.day");
                                                            String monthStart = document.getString("timeStart.month");
                                                            String yearStart = document.getString("timeStart.year");
                                                            String hourStart = document.getString("timeStart.hour");
                                                            String minuteStart = document.getString("timeStart.minute");

                                                            String dayEnd = document.getString("timeEnd.day");
                                                            String monthEnd  = document.getString("timeEnd.month");
                                                            String yearEnd = document.getString("timeEnd.year");
                                                            String hourEnd = document.getString("timeEnd.hour");
                                                            String minuteEnd = document.getString("timeEnd.minute");

                                                            String studio = document.getString("detail.nameStudio");
                                                            String ratio = document.getString("detail.ratio");
                                                            String material = document.getString("detail.material");
                                                            String condition = document.getString("detail.condition");
                                                            String weight = document.getString("detail.weight");
                                                            String dimension = document.getString("detail.dimension");

                                                            String Time =  dayEnd+ "/"  + monthEnd + "/" +  yearEnd
                                                                    + "  "+ hourEnd + ":" + minuteEnd + ":" + "00";


                                                            List<String> imageUrls = (List<String>) document.get("image_urls");
                                                            Product product = new Product(id, name,describe, Price, bidPrice, Time, imageUrls,dayStart,monthStart, yearStart, hourStart,minuteStart,
                                                                    dayEnd, monthEnd,yearEnd,hourEnd,minuteEnd ,studio,ratio,material,condition,weight,dimension);

                                                            mProduct.add(product);
                                                            productEditAdapter.notifyDataSetChanged();
                                                            Log.d("KQ", document.getId() + " => " + document.getData());
                                                        }
                                                    } else {
                                                        Log.w("KQ", "Error getting documents.", task.getException());
                                                    }
                                                }

                                            });

                                }
                            }
                        } else {

                        }
                    }
                });
    }
}