package com.example.btlauction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivityEditAuction extends AppCompatActivity {
    EditText namePd_activityEditAuction,describe_activityEditAuction,price_activityEditAuction,bidPrice_activityEditAuction,dayInput_activityEditAuction,hourInput_activityEditAuction,minuteInput_activityEditAuction,
            nameBrand_activityEditAuction,ratio_activityEditAuction,material_activityEditAuction,condition_activityEditAuction,weight_activityEditAuction,dimension_activityEditAuction,nameOwner_activityEditAuction;

    Button upload_activityEditAuction;
    RecyclerView rcv_photo;

    DatePicker datePickerStart , datePickerEnd;
    TimePicker timePickerStart, timePickerEnd;
    ImageButton deleteimage_acitvityEditProfile,btnChoose,btndelAution_acivityEditAuction,back_activityEditAuction;
    photo_adapter photoAdapter;
    List<Uri> imageUris = new ArrayList<>();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    StorageReference storageReference;


    FirebaseUser currentUser = mAuth.getCurrentUser();
    String uidNow = currentUser.getUid();
    Uri filePath;
    final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_edit_auction);

        namePd_activityEditAuction = findViewById(R.id.namePd_activityEditAuction);
        describe_activityEditAuction = findViewById(R.id.describe_activityEditAuction);
        price_activityEditAuction = findViewById(R.id.priceStart_activityEditAuction);
        bidPrice_activityEditAuction = findViewById(R.id.bidPrice_activityEditAuction);
        nameBrand_activityEditAuction = findViewById(R.id.nameStudio_activityEditAuction);
        ratio_activityEditAuction = findViewById(R.id.ratio_activityEditAuction);
        material_activityEditAuction = findViewById(R.id.material_activityEditAuction);
        condition_activityEditAuction = findViewById(R.id.codition_activityEditAuction);
        weight_activityEditAuction = findViewById(R.id.weight_activityEditAuction);
        dimension_activityEditAuction = findViewById(R.id.dimension_activityEditAuction);
        deleteimage_acitvityEditProfile = findViewById(R.id.delete_image_activityEditAuction);
        btndelAution_acivityEditAuction = findViewById(R.id.btndelAution_acivityEditAuction);



        datePickerStart = findViewById(R.id.datePickerstart_activityEditAuction);
        datePickerEnd = findViewById(R.id.datePickerend_activityEditAuction);


        timePickerStart = findViewById(R.id.timePickerstart_activityEditAuction);
        timePickerStart.setIs24HourView(true);
        timePickerEnd = findViewById(R.id.timePickerend_activityEditAuction);
        timePickerEnd.setIs24HourView(true);




        btnChoose = findViewById(R.id.imageChoose_activityEditAuction);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        rcv_photo = findViewById(R.id.rcv_photo_activityEditAuction);
        photoAdapter = new photo_adapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
        rcv_photo.setLayoutManager(gridLayoutManager);
        rcv_photo.setFocusable(false);
        rcv_photo.setAdapter(photoAdapter);



        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
                deleteimage_acitvityEditProfile.setEnabled(true);
            }
        });





        deleteimage_acitvityEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUris.clear();
                deleteimage_acitvityEditProfile.setEnabled(false);
                photoAdapter.notifyDataSetChanged();
            }
        });



        upload_activityEditAuction = findViewById(R.id.btnUpload_activityEditAuction);
        upload_activityEditAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateData();
            }
        });

        btndelAution_acivityEditAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteAuction();
                Intent intent = new Intent(MainActivityEditAuction.this,MainActivityMyAuction.class);
                startActivity(intent);
                finish();
            }
        });

        back_activityEditAuction = findViewById(R.id.back_activityEditAuction);
        back_activityEditAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityEditAuction.this,MainActivityMyAuction.class);
                startActivity(intent);
                finish();
            }
        });

        GetData();
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
    private void GetData(){
        Intent intent = getIntent();
        String Id = intent.getStringExtra("id");
        String describe = intent.getStringExtra("describe");
        String Name = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");
        String bidPrice = intent.getStringExtra("bidPrice");

        int dayStart = Integer.parseInt(intent.getStringExtra("dayStart"));
        int monthStart = Integer.parseInt(intent.getStringExtra("monthStart"));
        int yearStart = Integer.parseInt(intent.getStringExtra("yearStart"));
        int hourStart = Integer.parseInt(intent.getStringExtra("hourStart"));
        int minuteStart = Integer.parseInt(intent.getStringExtra("minuteStart"));

        int dayEnd = Integer.parseInt(intent.getStringExtra("dayEnd"));
        int monthEnd = Integer.parseInt(intent.getStringExtra("monthEnd"));
        int yearEnd = Integer.parseInt(intent.getStringExtra("yearEnd"));
        int hourEnd = Integer.parseInt(intent.getStringExtra("hourEnd"));
        int minuteEnd = Integer.parseInt(intent.getStringExtra("minuteEnd"));

        String studio = intent.getStringExtra("studio");
        String ratio = intent.getStringExtra("ratio");
        String material = intent.getStringExtra("material");
        String condition = intent.getStringExtra("condition");
        String weight = intent.getStringExtra("weight");
        String dimension = intent.getStringExtra("dimension");
        namePd_activityEditAuction.setText(Name);
        describe_activityEditAuction.setText(describe);
        price_activityEditAuction.setText(price);
        bidPrice_activityEditAuction.setText(bidPrice);

        datePickerStart.post(() -> {
            datePickerStart.init(yearStart, monthStart-1, dayStart, null);
        });

        datePickerEnd.post(() -> {
            datePickerEnd.init(yearEnd, monthEnd-1, dayEnd, null);
        });

        timePickerStart.setHour(hourStart);
        timePickerStart.setMinute(minuteStart);

        timePickerEnd.setHour(hourEnd);
        timePickerEnd.setMinute(minuteEnd);


        nameBrand_activityEditAuction.setText(studio);
        ratio_activityEditAuction.setText(ratio);
        material_activityEditAuction.setText(material);
        condition_activityEditAuction.setText(condition);
        weight_activityEditAuction.setText(weight);
        dimension_activityEditAuction.setText(dimension);
        GetListImage();
    }

    private void GetListImage(){
        Intent intent = getIntent();
        String Id = intent.getStringExtra("id");

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


                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection(productCollectionPath).document(Id)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            List<String> imageUrls = (List<String>) document.get("image_urls");
                                                            for (String imageUrl : imageUrls) {
                                                                Glide.with(MainActivityEditAuction.this)
                                                                        .asFile()
                                                                        .load(imageUrl)
                                                                        .into(new CustomTarget<File>() {
                                                                            @Override
                                                                            public void onResourceReady(@NonNull File resource, @Nullable com.bumptech.glide.request.transition.Transition<? super File> transition) {
                                                                                Uri uri = Uri.fromFile(resource);
                                                                                imageUris.add(uri);
                                                                                // Cập nhật RecyclerView với danh sách URI hình ảnh
                                                                                photoAdapter.setdata((ArrayList<Uri>) imageUris);
                                                                            }

                                                                            @Override
                                                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                                                                // Not required
                                                                            }
                                                                        });
                                                            }

                                                        } else {
                                                            // Document does not exist
                                                        }
                                                    } else {
                                                        // Error getting document
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

    private void UpdateData(){

        Intent intent = getIntent();
        String Id = intent.getStringExtra("id");

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
                                        final ProgressDialog progressDialog = new ProgressDialog(MainActivityEditAuction.this);
                                        progressDialog.setTitle("Uploading...");
                                        if (!isFinishing()) {
                                            progressDialog.show();
                                        }

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
                                        Product.put("name", namePd_activityEditAuction.getText().toString());
                                        Product.put("describe",  describe_activityEditAuction.getText().toString());
                                        Product.put("priceStart",  price_activityEditAuction.getText().toString());
                                        Product.put("bidPrice",  bidPrice_activityEditAuction.getText().toString());
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
                                            put("nameStudio",  nameBrand_activityEditAuction.getText().toString());
                                            put("ratio",  ratio_activityEditAuction.getText().toString());
                                            put("material",  material_activityEditAuction.getText().toString());
                                            put("condition",  condition_activityEditAuction.getText().toString());
                                            put("weight",  weight_activityEditAuction.getText().toString());
                                            put("dimension",  dimension_activityEditAuction.getText().toString());
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

                                                                DocumentReference newCollectionRef = userRef.collection("products").document(Id);
                                                                // Lưu Map vào Firestore

                                                                newCollectionRef.update(Product).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {

                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {

                                                                    }
                                                                });


                                                                    Dialog myDialog = new Dialog(MainActivityEditAuction.this);
                                                                    myDialog.setContentView(R.layout.dialogsuccess_layout);
                                                                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                                    Button btnOk = myDialog.findViewById(R.id.btnOke_dialog);
                                                                    myDialog.setTitle("Thông báo");
                                                                    myDialog.show();

                                                                    btnOk.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View view) {
                                                                            Intent intent = new Intent(MainActivityEditAuction.this,MainActivityMyAuction.class);
                                                                            startActivity(intent);
                                                                            finish();
                                                                        }
                                                                    });
                                                                Toast.makeText(MainActivityEditAuction.this, "Sản phẩm  đăng tải thành công !!", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();

                                                                Dialog myDialog = new Dialog(MainActivityEditAuction.this);
                                                                myDialog.setContentView(R.layout.dialogfail_layout);
                                                                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                                Button btnOk = myDialog.findViewById(R.id.btnOke_dialogfail);
                                                                myDialog.setTitle("Thông báo");
                                                                myDialog.show();

                                                                btnOk.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        myDialog.dismiss();
                                                                    }
                                                                });




                                                            Toast.makeText(MainActivityEditAuction.this, "Đăng tải không thành công !! " + imageIndex, Toast.LENGTH_SHORT).show();
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

    private void DeleteAuction(){
        Intent intent = getIntent();
        String Id = intent.getStringExtra("id");

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
                                    DocumentReference CollectionRef = userRef.collection("products").document(Id);
                                    CollectionRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(MainActivityEditAuction.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });

                                    DocumentReference path = db.collection("Allproduct").document(Id);
                                    path.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(MainActivityEditAuction.this, "Xóa thành công khỏi Allproduct", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });

                                }
                            }
                        } else {
                            Log.d("TEST", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}