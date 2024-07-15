package com.example.btlauction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivitySearch extends AppCompatActivity {

    TextView cancel_ActivitySearch;
    RecyclerView rcv_acitvitySearch;

    android.widget.SearchView searchView;
    product_adapter productAdapter;

    List<Product> mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);
        cancel_ActivitySearch = findViewById(R.id.cancel_activitySearch);
        cancel_ActivitySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rcv_acitvitySearch = findViewById(R.id.rcv_activitySearch);
        searchView = findViewById(R.id.SearchView_activitySearch);
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                productAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                productAdapter.getFilter().filter(s);
                return false;
            }
        });


        mProduct = new ArrayList<>();

        productAdapter = new product_adapter( mProduct, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rcv_acitvitySearch.setAdapter(productAdapter);
        rcv_acitvitySearch.setLayoutManager(gridLayoutManager);

        GetAllData();
    }

    private void GetAllData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Allproduct")
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

                                String Time = "Thời gian kết thúc: \n" + dayEnd+ "/"  + monthEnd + "/" +  yearEnd
                                        + "\n"+ hourEnd + ":" + minuteEnd + ":" + "00";

                                List<String> imageUrls = (List<String>) document.get("image_urls");
                                Product product = new Product(id, name,describe, Price, bidPrice, Time, imageUrls,dayStart,monthStart, yearStart, hourStart,minuteStart,
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