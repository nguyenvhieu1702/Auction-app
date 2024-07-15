package com.example.btlauction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TableLayout;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.StringValue;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity4 extends AppCompatActivity {

    FirebaseAuth  mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String uidNow = currentUser.getUid();
    ImageButton back_activity4, btnChoose, deleteimage_acitvity4;
    Button btnUpload;
    EditText namePD, describePD,
            priceStart, bidPrice, nameStudio, ratio, material, condition, weight, dimension;

    DatePicker datePickerStart , datePickerEnd;
    TimePicker timePickerStart, timePickerEnd;

    ImageView imageViewUpload;
    RecyclerView rcv_photo;
    photo_adapter photoAdapter;
    Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    List<Uri> imageUris = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final int PICK_IMAGE_REQUEST = 71;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {//
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        mapping();
        back_activity4 = findViewById(R.id.back_activity4);
        btnUpload = findViewById(R.id.btnUpload_activity4);
        btnChoose = findViewById(R.id.btnChoose_activity4);
        deleteimage_acitvity4 = findViewById(R.id.delete_image_activity4);
        rcv_photo = findViewById(R.id.rcv_photo);
        //Khoi tao adapter

        photoAdapter = new photo_adapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
        rcv_photo.setLayoutManager(gridLayoutManager);
        rcv_photo.setFocusable(false);
        rcv_photo.setAdapter(photoAdapter);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        deleteimage_acitvity4.setEnabled(false);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
                deleteimage_acitvity4.setEnabled(true);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateData();
            }
        });

        back_activity4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        deleteimage_acitvity4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUris.clear();
                deleteimage_acitvity4.setEnabled(false);
                photoAdapter.notifyDataSetChanged();
            }
        });
    }

    void mapping() {
        namePD = findViewById(R.id.namePd_activity4);
        describePD = findViewById(R.id.describe_activity4);
        priceStart = findViewById(R.id.priceStart_activity4);
        bidPrice = findViewById(R.id.bidPrice_activity4);
        nameStudio = findViewById(R.id.nameStudio_activity4);
        ratio = findViewById(R.id.ratio_activity4);
        material = findViewById(R.id.material_activity4);
        condition = findViewById(R.id.codition_activity4);
        weight = findViewById(R.id.weight_activity4);
        dimension = findViewById(R.id.dimension_activity4);

        datePickerStart = findViewById(R.id.datePickerstart_activity4);
        datePickerEnd = findViewById(R.id.datePickerend_activity4);


        timePickerStart = findViewById(R.id.timePickerstart_activity4);
        timePickerStart.setIs24HourView(true);
        timePickerEnd = findViewById(R.id.timePickerend_activity4);
        timePickerEnd.setIs24HourView(true);
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            Uri uri = data.getData();
            imageUris.add(uri);
            // Cập nhật ImageView với hình ảnh được chọn mới nhất
            photoAdapter.setdata((ArrayList<Uri>) imageUris);
        }
    }


    private void UpdateData(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uidNow = currentUser.getUid();

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
                                    // Use the document_id here

                                    DocumentReference userRef = db.collection("CurrentUser").document(document_id);

                                    if (imageUris.size() > 0) {
                                        final ProgressDialog progressDialog = new ProgressDialog(MainActivity4.this);
                                        progressDialog.setTitle("Đăng tải...");
                                        progressDialog.show();

                                        final List<String> uploadedImageUrls = new ArrayList<>();
                                        String dateStart = String.valueOf(datePickerStart.getDayOfMonth());
                                        String monthStart = String.valueOf(datePickerStart.getMonth()+1);
                                        String yearStart = String.valueOf(datePickerStart.getYear());
                                        String hourStart = String.valueOf(timePickerStart.getHour());
                                        String minuteStart = String.valueOf(timePickerStart.getMinute());


                                        String dateEnd = String.valueOf(datePickerEnd.getDayOfMonth());
                                        String monthEnd = String.valueOf(datePickerEnd.getMonth()+1);
                                        String yearEnd = String.valueOf(datePickerEnd.getYear());
                                        String hourEnd = String.valueOf(timePickerEnd.getHour());
                                        String minuteEnd = String.valueOf(timePickerEnd.getMinute());

                                        // Thêm dữ liệu vào Map
                                        Map<String, Object> Product = new HashMap<>();
                                        Product.put("name", namePD.getText().toString());
                                        Product.put("describe",  describePD.getText().toString());
                                        Product.put("priceStart",  priceStart.getText().toString());
                                        Product.put("bidPrice",  bidPrice.getText().toString());
                                        Product.put("timeStart", new HashMap<String, Object>() {{
                                            put("day",   dateStart);
                                            put("month", monthStart);
                                            put("year",  yearStart);
                                            put("hour",  hourStart);
                                            put("minute",minuteStart);
                                        }});
                                        Product.put("timeEnd", new HashMap<String, Object>() {{
                                            put("day",   dateEnd);
                                            put("month", monthEnd);
                                            put("year",  yearEnd);
                                            put("hour",  hourEnd);
                                            put("minute",minuteEnd);
                                        }});
                                        Product.put("detail", new HashMap<String, Object>() {{
                                            put("nameStudio",  nameStudio.getText().toString());
                                            put("ratio",  ratio.getText().toString());
                                            put("material",  material.getText().toString());
                                            put("condition",  condition.getText().toString());
                                            put("weight",  weight.getText().toString());
                                            put("dimension",  dimension.getText().toString());
                                        }});
                                        Product.put("Uid",uidNow);

                                        for (int i = 0; i < imageUris.size(); i++) {
                                            final int imageIndex = i;
                                            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());

                                            ref.putFile(imageUris.get(i))
                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                            // Lấy đường dẫn của ảnh trên Firebase Storage
                                                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                                            while (!urlTask.isSuccessful());
                                                            Uri downloadUrl = urlTask.getResult();

                                                            String imageUrl = downloadUrl.toString();

                                                            uploadedImageUrls.add(imageUrl);
                                                            if (uploadedImageUrls.size() == imageUris.size()) {
                                                                progressDialog.dismiss();

                                                                // Thêm mảng đường dẫn ảnh vào Map
                                                                Product.put("image_urls", uploadedImageUrls);

                                                                CollectionReference newCollectionRef = userRef.collection("products");
                                                                // Lưu Map vào Firestore
                                                                newCollectionRef.add(Product)
                                                                        .addOnSuccessListener(documentReference -> {
                                                                            Log.d("TAG", "Product added with ID: " + documentReference.getId());
                                                                        })
                                                                        .addOnFailureListener(e -> {
                                                                            Log.w("TAG", "Error adding product", e);
                                                                        });

                                                                SaveAllPD();

                                                                Dialog myDialog = new Dialog(MainActivity4.this);
                                                                myDialog.setContentView(R.layout.dialogsuccess_layout);
                                                                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                                Button btnOk = myDialog.findViewById(R.id.btnOke_dialog);
                                                                myDialog.setTitle("Thông báo");
                                                                myDialog.setCancelable(false);
                                                                myDialog.show();

                                                                btnOk.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        Intent intent = new Intent(MainActivity4.this,MainActivityMyAuction.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();

                                                            Dialog myDialog = new Dialog(MainActivity4.this);
                                                            myDialog.setContentView(R.layout.dialogfail_layout);
                                                            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                            Button btnOk = myDialog.findViewById(R.id.btnOke_dialogfail);
                                                            myDialog.setTitle("Thông báo");
                                                            myDialog.setCancelable(false);
                                                            myDialog.show();

                                                            btnOk.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    myDialog.dismiss();
                                                                }
                                                            });

                                                            Toast.makeText(MainActivity4.this, "Đăng tải không thành công !! " + imageIndex, Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                                            progressDialog.setMessage("Đang tải " + (int) progress + "%");
                                                        }
                                                    });
                                        }
                                    }

                                }
                            }
                        } else {
                            Log.d("TEST", "Error getting documents: ", task.getException());
                        }
                    }
                });

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
}