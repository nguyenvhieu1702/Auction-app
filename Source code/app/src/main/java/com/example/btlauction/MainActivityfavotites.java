package com.example.btlauction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivityfavotites extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    ImageButton buttonback;

    RecyclerView rcv_photo;

    SwipeRefreshLayout swipeRefreshLayout;
    product_adapter productAdapter;

    List<Product> mProduct;
    BottomNavigationView bottomNavigationView;
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String uidNow = currentUser.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activityfavotites);
        setTitle("Đấu giá");


        rcv_photo = findViewById(R.id.rcv_activiyFavorites);

        mProduct = new ArrayList<>();


        productAdapter = new product_adapter( mProduct, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rcv_photo.setAdapter(productAdapter);
        rcv_photo.setLayoutManager(gridLayoutManager);


        getData();

        buttonback = findViewById(R.id.back_activityFavorites);
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityfavotites.this,MainActivityProfile.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = findViewById(R.id.refresh_activityfavorites);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UpdateFavorite();
                mProduct.clear();
                getData();
                productAdapter.notifyDataSetChanged();
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

                                    db.collection(favoritesCollectionPath)
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

                                                            Product product = new Product(id, name, describe, Price, bidPrice, Time, imageUrls,dayStart,monthStart, yearStart, hourStart,minuteStart,
                                                                    dayEnd, monthEnd,yearEnd,hourEnd,minuteEnd ,studio,ratio,material,condition,weight,dimension);

                                                            mProduct.add(product);
                                                            productAdapter.notifyDataSetChanged();
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


    private void UpdateFavorite() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("CurrentUser")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String uid = document.getString("Uid");
                            if (uid.equals(uidNow)) {
                                String documentId = document.getId();
                                String myAuctionCollectionPath = "CurrentUser/" + documentId + "/favorites";

                                // Kiểm tra tồn tại của collection "MyAuction"
                                db.collection(myAuctionCollectionPath)
                                        .get()
                                        .addOnCompleteListener(myAuctionTask -> {
                                            if (myAuctionTask.isSuccessful()) {
                                                if (myAuctionTask.getResult().isEmpty()) {

                                                } else {
                                                    // Sao chép dữ liệu từ collection "Allproduct" sang "MyAuction"
                                                    CollectionReference allProductRef = db.collection("Allproduct");
                                                    allProductRef.get().addOnCompleteListener(allProductTask -> {
                                                        if (allProductTask.isSuccessful()) {
                                                            for (QueryDocumentSnapshot allProductDoc : allProductTask.getResult()) {
                                                                String allProductDocumentId = allProductDoc.getId();

                                                                // Thực hiện kiểm tra documentID có tồn tại trong "favotites" hay không
                                                                db.collection(myAuctionCollectionPath)
                                                                        .document(allProductDocumentId)
                                                                        .get()
                                                                        .addOnCompleteListener(myAuctionDocTask -> {
                                                                            if (myAuctionDocTask.isSuccessful()) {
                                                                                DocumentSnapshot myAuctionDocSnapshot = myAuctionDocTask.getResult();
                                                                                if (myAuctionDocSnapshot.exists()) {
                                                                                    // Document tồn tại trong "favotites"
                                                                                    // Thực hiện sao chép dữ liệu từ document trong collection "Allproduct" sang collection "favotites"
                                                                                    Map<String, Object> data = allProductDoc.getData();
                                                                                    db.collection(myAuctionCollectionPath)
                                                                                            .document(allProductDocumentId)
                                                                                            .set(data)
                                                                                            .addOnSuccessListener(aVoid -> {

                                                                                            })
                                                                                            .addOnFailureListener(e -> {

                                                                                            });
                                                                                }
                                                                            } else {

                                                                            }
                                                                        });
                                                            }
                                                        } else {


                                                        }
                                                    });
                                                }
                                            } else {


                                            }
                                        });
                            }
                        }
                    } else {
                    }
                });
    }

}