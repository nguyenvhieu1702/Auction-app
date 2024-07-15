package com.example.btlauction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseUser currentUser = mAuth.getCurrentUser();
    String uidNow = currentUser.getUid();

    Button button;
    RecyclerView rcv_photo;

    SwipeRefreshLayout swipeRefreshLayout;
    product_adapter productAdapter;

    List<Product> mProduct;

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Nhà");
        rcv_photo = findViewById(R.id.rcv_sp);

        mProduct = new ArrayList<>();


        productAdapter = new product_adapter( mProduct, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rcv_photo.setAdapter(productAdapter);
        rcv_photo.setLayoutManager(gridLayoutManager);


        CheckUpdateProfile();
        SaveAllPD();
        GetAllData();


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home_btn);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_btn:
                        return true;
                    case R.id.bid_btn:
                        startActivity(new Intent(getApplicationContext(),MainActivity3.class));
                        return true;
                    case R.id.add_btn:
                        startActivity(new Intent(getApplicationContext(),MainActivity4.class));
                        return true;
                    case R.id.chat_btn:
                        startActivity(new Intent(getApplicationContext(),MainActivity5.class));
                        return true;
                    case R.id.profile_menu:
                        startActivity(new Intent(getApplicationContext(),MainActivityProfile.class));
                        return true;
                }
                return false;
            }
        });

        swipeRefreshLayout = findViewById(R.id.refresh_activityMain);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mProduct.clear();
                GetAllData();
                productAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.option1:
                startActivity(new Intent(getApplicationContext(),MainActivitySearch.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void SaveAllPD(){
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

                                    FirebaseFirestore ref = FirebaseFirestore.getInstance();

                                    // Đường dẫn đến collection trong products
                                    CollectionReference sourceCollectionRef = ref.collection(productCollectionPath);

                                    // Đường dẫn đến collection mới Allproduct
                                    CollectionReference destCollectionRef = ref.collection("Allproduct");

                                    // Lấy tất cả các document trong collection sourceCollectionRef và lưu vào collection mới destCollectionRef
                                    sourceCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot querySnapshot) {
                                            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                                                String documentId = documentSnapshot.getId();
                                                Map<String, Object> data = documentSnapshot.getData();
                                                destCollectionRef.document(documentId).set(data);
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Xử lý khi có lỗi xảy ra
                                        }
                                    });

                                }
                            }
                        } else {

                        }
                    }
                });

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

                                String Time =  dayEnd+ "/"  + monthEnd + "/" +  yearEnd
                                        + "  "+ hourEnd + ":" + minuteEnd + ":" + "00";

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

    private void CheckUpdateProfile(){
        db.collection("CurrentUser")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String uid = document.getString("Uid");
                                if (uid.equals(uidNow)) {
                                    String nameOwner = document.getString("Name");
                                    if (nameOwner == null || nameOwner.isEmpty()) {

                                        Dialog myDialog = new Dialog(MainActivity.this);
                                        myDialog.setContentView(R.layout.alertupdateprofile_dialog);
                                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        Button btnOk = myDialog.findViewById(R.id.btnOke_dialogUpdateprofile);
                                        myDialog.setTitle("Thông báo");
                                        myDialog.setCancelable(false);
                                        myDialog.show();

                                        btnOk.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                myDialog.dismiss();
                                                Intent intent = new Intent(MainActivity.this, DetailProfile.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                    } else {
                                    }
                                }
                            }
                        } else {

                        }
                    }
                });
    }

}