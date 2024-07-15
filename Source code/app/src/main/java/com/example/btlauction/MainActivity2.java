package com.example.btlauction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MainActivity2 extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ImageButton backButton, btnFavorite_activity2,btnFavoriteOwner_activity2;
    TextView namePd_activity2,describe_activity2,price_activity2,bidPrice_activity2,hourInput_activity2,minuteInput_activity2,secondInput_activity2,
            nameBrand_activity2,scale_activity2,material_activity2,condition_activity2,weight_activity2,dimension_activity2,nameOwner_acitivty2;
    EditText bidPut_activity2;

    Button buttonBid_activity2;
    ImageView imageView, avavtar_activity2;

    RecyclerView rcv_photo , rcv_bidderBoard;
    photo_adapter photoAdapter;

    List<Uri> imageUris = new ArrayList<>();
    private long timeLeftInMillis;

    private SharedPreferences prefs;

    private boolean isFavorite = false;
    private ImageAdapter mImageAdapter;

    private BidAdapter bidAdapter;

    private long endTimeInMillis;
    private Handler handler;
    private Runnable runnable;
    List<Bid> mBid;

    // Khởi tạo danh sách bitmap
    private List<Bitmap> mBitmapList = new ArrayList<>();
    private CountDownTimer countDownTimer;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser currentUser = mAuth.getCurrentUser();
    String uidNow = currentUser.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        backButton = findViewById(R.id.back_activity2);
        imageView = findViewById(R.id.imageView);
        namePd_activity2 = findViewById(R.id.namePd_activity2);
        price_activity2 = findViewById(R.id.price_activity2);
        bidPrice_activity2 = findViewById(R.id.bidPricePD_activity2);
        hourInput_activity2 = findViewById(R.id.hourInput_activity2);
        minuteInput_activity2 = findViewById(R.id.minuteInput_activity2);
        secondInput_activity2 = findViewById(R.id.secondInput_activity2);
        nameBrand_activity2 = findViewById(R.id.nameBrand_activity2);
        scale_activity2 = findViewById(R.id.scale_activity2);
        material_activity2 = findViewById(R.id.material_activity2);
        condition_activity2 = findViewById(R.id.condition_activity2);
        weight_activity2 = findViewById(R.id.weight_activity2);
        dimension_activity2 = findViewById(R.id.dimension_activity2);
        bidPut_activity2 = findViewById(R.id.bidPut_activity2);
        buttonBid_activity2 = findViewById(R.id.buttonBid_activity2);
        btnFavorite_activity2 = findViewById(R.id.btn_favorite_activity2);
        btnFavoriteOwner_activity2 = findViewById(R.id.btn_favoriteOwner_activity2);
        nameOwner_acitivty2 = findViewById(R.id.nameOwner_activity2);
        avavtar_activity2 = findViewById(R.id.avavtar_activity2);
        describe_activity2 = findViewById(R.id.describe_activity2);

        rcv_photo = findViewById(R.id.rcv_photo);
        rcv_bidderBoard = findViewById(R.id.rcv_bidderBoard);


        mBid = new ArrayList<>();

        bidAdapter = new BidAdapter( mBid, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rcv_bidderBoard.setAdapter(bidAdapter);
        rcv_bidderBoard.setLayoutManager(gridLayoutManager);


        getData();
        CheckBidder();
        Timer();
        checkFavorite();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });



        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        String timeBid = String.valueOf(hour +":"+ minute +":"+second);
        String dayBid =  String.valueOf( day+"/"+month+"/"+year);
        buttonBid_activity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("CurrentUser")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String uid = document.getString("Uid");
                                        if (uid.equals(uidNow)) {
                                            String NameCus = document.getString("Name");
                                            String EmailCus = document.getString("Email");
                                            //Đăng tải dữ liệu bidder vào product
                                            Bid bid = new Bid(NameCus, bidPut_activity2.getText().toString(),timeBid,dayBid);
                                            mBid.add(bid);
                                            bidAdapter.notifyDataSetChanged();
                                            InAuction();
                                            Intent intent = getIntent();
                                            String Id = intent.getStringExtra("id");

                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            CollectionReference allProductsRef = db.collection("Allproduct");
                                            DocumentReference productDocRef = allProductsRef.document(Id);
                                            HashMap<String, Object> Bidder = new HashMap<>();
                                            Bidder.put("UID", uidNow);
                                            Bidder.put("BidPrice", bidPut_activity2.getText().toString());
                                            Bidder.put("Name",NameCus);
                                            Bidder.put("Email",EmailCus);
                                            Bidder.put("Time",timeBid);
                                            Bidder.put("Day",dayBid);
                                            productDocRef.collection("Bidder").add(Bidder)
                                                    .addOnSuccessListener(documentReference -> {
                                                        // Xử lý khi thêm thành công
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        // Xử lý khi thêm thất bại
                                                    });
                                        }
                                    }
                                } else {

                                }
                            }
                        });


            }
        });
    }

    public void checkFavorite(){
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
                                    String favoritesCollectionPath = "CurrentUser/" +  document_id + "/favorites";

                                    CollectionReference favoriteRef = db.collection(favoritesCollectionPath);
                                    favoriteRef.document(Id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete( Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    // id is in favorite collection
                                                    isFavorite = !isFavorite;
                                                    Log.d("SHOW", "id is in favorite collection");
                                                    btnFavorite_activity2.setBackgroundResource(R.drawable.heart_fill);
                                                } else {
                                                    // id is not in favorite collection
                                                    Log.d("SHOW", "id is not in favorite collection");
                                                    btnFavorite_activity2.setBackgroundResource(R.drawable.heart_empty);
                                                }
                                            } else {
                                                Log.d("SHOW", "Error getting document: ", task.getException());
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

    public void getData(){
        Intent intent = getIntent();
        String Id = intent.getStringExtra("id");
        String Name = intent.getStringExtra("name");
        String Describe = intent.getStringExtra("describe");
        String price = intent.getStringExtra("price");
        String bidPrice = intent.getStringExtra("bidPrice");
        String studio = intent.getStringExtra("studio");
        String ratio = intent.getStringExtra("ratio");
        String material = intent.getStringExtra("material");
        String condition = intent.getStringExtra("condition");
        String weight = intent.getStringExtra("weight");
        String dimension = intent.getStringExtra("dimension");
        String image = intent.getStringExtra("image");



        Glide.with(this)
                .load(image)
                .into(imageView);

        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        int Price = Integer.parseInt(price);
        int BidPrice = Integer.parseInt(bidPrice);

        String priceNumber = decimalFormat.format(Price);
        String bidPriceNumber =  decimalFormat.format(BidPrice);

        namePd_activity2.setText(Name);
        describe_activity2.setText(Describe);
        price_activity2.setText( priceNumber +" đ");
        bidPrice_activity2.setText("Bước nhảy kế tiếp: " + bidPriceNumber + " vnd");
        nameBrand_activity2.setText(studio);
        scale_activity2.setText(ratio);
        material_activity2.setText(material);
        condition_activity2.setText(condition);
        weight_activity2.setText(weight);
        dimension_activity2.setText(dimension);




        DocumentReference docRef = db.collection("Allproduct").document(Id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String uid = documentSnapshot.getString("Uid");
                    if (uid.equals(uidNow)) {
                        bidPut_activity2.setHint("Bạn không được tham gia !");
                        bidPut_activity2.setEnabled(false);
                        buttonBid_activity2.setEnabled(false);
                    }else{
                        bidPut_activity2.setHint("Nhập số tiền ");

                        bidPut_activity2.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                String input = editable.toString().trim();
                                if (input.isEmpty()) {
                                    bidPut_activity2.setError(null);
                                    return;
                                }
                                int bidNow = Integer.parseInt(price)+BidPrice;
                                int bidNew = Integer.parseInt(input);
                                if (bidNew < bidNow) {
                                    bidPut_activity2.setError("Bạn bắt buộc phải nhập số tiền lớn hơn số tiền đấu giá hiện tại");
                                } else {
                                    bidPut_activity2.setError(null);
                                }
                            }
                        });
                    }

                } else {

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý lỗi
            }
        });





        //Set adapter với các hình ảnh khía cạnh khác của collection
        mImageAdapter = new ImageAdapter(new ArrayList<Bitmap>(), new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        });
        rcv_photo.setAdapter(mImageAdapter);
        rcv_photo.setLayoutManager(new LinearLayoutManager(MainActivity2.this,LinearLayoutManager.HORIZONTAL, false));

        GetListImage();
        FavoriteList();
        GetOwner();

    }

    private void GetOwner(){
        Intent intent = getIntent();
        String Id = intent.getStringExtra("id");
        db.collection("CurrentUser")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String uid = document.getString("Uid");
                                String nameOwner = document.getString("Name");
                                String Avatar = document.getString("avatar_url");

                                DocumentReference docRef = db.collection("Allproduct").document(Id);

                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            String uidFrompd = documentSnapshot.getString("Uid");
                                            if(uid.equals(uidFrompd)){
                                                nameOwner_acitivty2.setText(nameOwner);
                                                Glide.with(MainActivity2.this)
                                                        .load(Avatar)
                                                        .into(avavtar_activity2);

                                            }
                                        } else {

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Xử lý lỗi
                                    }
                                });


                            }
                        } else {

                        }
                    }
                });
    }

    private  void FavoriteList(){

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
                                    String Allproduct = "Allproduct" ;
                                    String favoritesCollectionPath = "CurrentUser/" +  document_id + "/favorites";
                                    //Set mục yêu thích cho người dùng
                                    Animation pulseAnimation = AnimationUtils.loadAnimation(MainActivity2.this, R.anim.heart_pulse);

                                    btnFavorite_activity2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            isFavorite = !isFavorite; // Đảo ngược giá trị của biến boolean

                                            if (isFavorite) {
                                                // Nếu đang yêu thích, đổi hình ảnh nền sang trái tim màu đỏ
                                                btnFavorite_activity2.setBackgroundResource(R.drawable.heart_fill);
                                                btnFavorite_activity2.startAnimation(pulseAnimation);

                                                FirebaseFirestore ref = FirebaseFirestore.getInstance();

                                                // Đường dẫn đến document trong products
                                                DocumentReference sourceDocRef = ref.collection(Allproduct).document(Id);

                                                // Đường dẫn đến document trong favorites
                                                DocumentReference destDocRef = ref.collection(favoritesCollectionPath).document(Id);

                                                // Lấy dữ liệu của document trong source_collection và lưu vào document mới trong destination_collection
                                                sourceDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        if (documentSnapshot.exists()) {
                                                            Map<String, Object> data = documentSnapshot.getData();
                                                            destDocRef.set(data); // hoặc destDocRef.update(data) nếu bạn muốn cập nhật document đã tồn tại
                                                        } else {
                                                            // Document không tồn tại
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure( Exception e) {
                                                        // Xử lý khi có lỗi xảy ra
                                                    }
                                                });
                                                Toast.makeText(MainActivity2.this, "Thêm thành công vào mục Yêu Thích", Toast.LENGTH_SHORT).show();

                                            } else {
                                                // Nếu không yêu thích, đổi hình ảnh nền sang trái tim màu đen
                                                btnFavorite_activity2.setBackgroundResource(R.drawable.heart_empty);

                                                db.collection(favoritesCollectionPath).document(Id)
                                                        .delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d("KQ", "DocumentSnapshot successfully deleted!");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(Exception e) {
                                                                Log.w("KQ", "Error deleting document", e);
                                                            }
                                                        });

                                                Toast.makeText(MainActivity2.this, "Xóa thành công khỏi mục Yêu Thích", Toast.LENGTH_SHORT).show();
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

    private void InAuction(){
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
                                        String Allproduct = "Allproduct" ;
                                        String favoritesCollectionPath = "CurrentUser/" +  document_id + "/MyAuction";

                                        FirebaseFirestore ref = FirebaseFirestore.getInstance();

                                        // Đường dẫn đến document trong products
                                        DocumentReference sourceDocRef = ref.collection(Allproduct).document(Id);

                                        // Đường dẫn đến document trong favorites
                                        DocumentReference destDocRef = ref.collection(favoritesCollectionPath).document(Id);

                                        // Lấy dữ liệu của document trong source_collection và lưu vào document mới trong destination_collection
                                        sourceDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    Map<String, Object> data = documentSnapshot.getData();
                                                    destDocRef.set(data); // hoặc destDocRef.update(data) nếu bạn muốn cập nhật document đã tồn tại
                                                } else {
                                                    // Document không tồn tại
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure( Exception e) {
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
                                                                Glide.with(MainActivity2.this)
                                                                        .asBitmap()
                                                                        .load(imageUrl)
                                                                        .into(new CustomTarget<Bitmap>() {
                                                                            @Override
                                                                            public void onResourceReady( Bitmap resource, com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                                                                mBitmapList.add(resource);
                                                                                mImageAdapter.notifyDataSetChanged();
                                                                                if (mImageAdapter != null) {
                                                                                    mImageAdapter.notifyDataSetChanged();
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onLoadCleared(Drawable placeholder) {
                                                                                // Not required
                                                                            }
                                                                        });
                                                            }

                                                            rcv_photo.setLayoutManager(new LinearLayoutManager(MainActivity2.this,LinearLayoutManager.HORIZONTAL, false));
                                                            mImageAdapter = new ImageAdapter((ArrayList<Bitmap>) mBitmapList,new ImageAdapter.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(Bitmap bitmap) {
                                                                    imageView.setImageBitmap(bitmap);
                                                                }
                                                            });
                                                            rcv_photo.setAdapter(mImageAdapter);
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

    private void Timer(){
        Intent intent = getIntent();

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



        // Set thời gian bắt đầu và kết thúc
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(yearStart, monthStart-1, dayStart, hourStart, minuteStart, 0); // Ngày bắt đầu phiên đấu giá (tháng bắt đầu từ 0)

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(yearEnd,  monthEnd-1,  dayEnd, hourEnd,  minuteEnd, 0); // Ngày kết thúc phiên đấu giá (tháng bắt đầu từ 0)

        // Tính toán thời gian còn lại
        Calendar currentCalendar = Calendar.getInstance();// Thời gian hiện tại
        if (currentCalendar.before(startCalendar)) {
            // Phiên đấu giá chưa bắt đầu
            // Cập nhật UI với thông báo tương ứng
            //Toast.makeText(this, "Phiên đấu giá chưa bắt đầu", Toast.LENGTH_SHORT).show();
            bidPut_activity2.setHint("Phiên đấu giá chưa bắt đầu !");
            bidPut_activity2.setEnabled(false);
            buttonBid_activity2.setEnabled(false);
        } else if (currentCalendar.after(endCalendar)) {
            // Phiên đấu giá đã kết thúc
            // Cập nhật UI với thông báo tương ứng
            //Toast.makeText(this, "Phiên đấu giá đã kết thúc", Toast.LENGTH_SHORT).show();
            WinnerBid();
        } else {
            // Phiên đấu giá đang diễn ra
            // Tính toán thời gian còn lại
            endTimeInMillis = endCalendar.getTimeInMillis();

            // Khởi tạo Handler và Runnable
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    // Tính toán thời gian còn lại dựa trên thời gian hiện tại
                    long currentTimeInMillis = System.currentTimeMillis();
                    long remainingTimeInMillis = endTimeInMillis - currentTimeInMillis;

                    if (remainingTimeInMillis > 0) {
                        // Chuyển đổi thời gian còn lại thành giờ, phút, giây
                        long remainingHours = (remainingTimeInMillis / (60 * 60 * 1000));
                        long remainingMinutes = (remainingTimeInMillis % (60 * 60 * 1000)) / (60 * 1000);
                        long remainingSeconds = (remainingTimeInMillis % (60 * 1000)) / 1000;



                        String Hours = String.valueOf(remainingHours );
                        String Minute = String.valueOf(remainingMinutes );
                        String Second = String.valueOf(remainingSeconds);

                        hourInput_activity2.setText(Hours);
                        minuteInput_activity2.setText(Minute);
                        secondInput_activity2.setText(Second);

                        // Lặp lại việc cập nhật sau một khoảng thời gian nhất định (ví dụ: 1 giây)
                        handler.postDelayed(this, 1000);
                    } else {
                        // Thời gian còn lại đã hết
                        // Cập nhật UI với thông báo tương ứng
                        WinnerBid();
                    }
                }

            };

            // Bắt đầu cập nhật UI
            handler.post(runnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hủy bỏ việc cập nhật UI khi Activity bị hủy
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }



    private void CheckBidder(){

        Intent intent = getIntent();
        String Id = intent.getStringExtra("id");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference allProductRef = db.collection("Allproduct");
        DocumentReference productDocRef = allProductRef.document(Id);
        productDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot productDocSnapshot = task.getResult();
                if (productDocSnapshot.exists()) {
                    // Lấy dữ liệu từ collection Bidder trong mỗi document
                    Query query = productDocRef.collection("Bidder");
                    query.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (QueryDocumentSnapshot bidderDocSnapshot : task1.getResult()) {
                                // Lấy thông tin trong mỗi document của collection Bidder
                                String bidderName =  bidderDocSnapshot.getString("Name");
                                String BidPrice =  bidderDocSnapshot.getString("BidPrice");
                                String timeBid  = bidderDocSnapshot.getString("Time");
                                String dayBid = bidderDocSnapshot.getString("Day");
                                Bid bid = new Bid(bidderName, BidPrice,timeBid,dayBid);
                                mBid.add(bid);
                                bidAdapter.notifyDataSetChanged();
                            }
                        } else {

                        }
                    });
                } else {

                }
            } else {

            }
        });

    }

    private void WinnerBid(){
        Intent intent = getIntent();
        String Id = intent.getStringExtra("id");

        CollectionReference allProductRef = db.collection("Allproduct");
        DocumentReference productDocRef = allProductRef.document(Id);
        productDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot productDocSnapshot = task.getResult();
                if (productDocSnapshot.exists()) {
                    // Lấy dữ liệu từ collection Bidder trong mỗi document
                    Query query = productDocRef.collection("Bidder").orderBy("BidPrice", Query.Direction.DESCENDING).limit(1);
                    query.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            if (!task1.getResult().isEmpty()) {
                                DocumentSnapshot maxBidDocSnapshot = task1.getResult().getDocuments().get(0);
                                String bidPrice = maxBidDocSnapshot.getString("BidPrice");
                                String bidderName =  maxBidDocSnapshot.getString("Name");
                                String Uid = maxBidDocSnapshot.getString("UID");

                                //Doan code nay kiem tra de set nut bid
                                DocumentReference docRef = db.collection("Allproduct").document(Id);
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            String uid = documentSnapshot.getString("Uid");
                                            if (uid.equals(uidNow)) {
                                                bidPut_activity2.setHint("Bạn không được tham gia !");
                                                bidPut_activity2.setEnabled(false);
                                                buttonBid_activity2.setEnabled(false);
                                            }else{
                                                bidPut_activity2.setHint("Phiên đấu giá đã kết thúc !");
                                                bidPut_activity2.setEnabled(false);
                                                buttonBid_activity2.setEnabled(false);
                                            }

                                        } else {

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Xử lý lỗi
                                    }
                                });


                                if(Uid.equals(uidNow)){

                                    Dialog myDialog = new Dialog(MainActivity2.this);
                                    myDialog.setContentView(R.layout.userwinner_dialog);
                                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    Button btnOk = myDialog.findViewById(R.id.btnOke_dialogUserwinner);
                                    TextView textView = myDialog.findViewById(R.id.userWinner_dialogUserwinner);

                                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
                                    int bidprice = Integer.parseInt(bidPrice);
                                    String bidPiceWin = decimalFormat.format(bidprice);

                                    textView.setText(bidderName + " bạn là người chiến thắng ");
                                    myDialog.setTitle("Thông báo");
                                    myDialog.setCancelable(false);
                                    myDialog.show();

                                    btnOk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            myDialog.dismiss();
                                        }
                                    });
                                }else {
                                    Dialog myDialog = new Dialog(MainActivity2.this);
                                    myDialog.setContentView(R.layout.bidwinner_dialog);
                                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    Button btnOk = myDialog.findViewById(R.id.btnOke_dialogWinner);
                                    TextView textView = myDialog.findViewById(R.id.txt_winner_dialogWinner);

                                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
                                    int bidprice = Integer.parseInt(bidPrice);
                                    String bidPiceWin = decimalFormat.format(bidprice);

                                    textView.setText(bidderName + " đã thắng với số tiền " + bidPiceWin + " đ");
                                    myDialog.setTitle("Thông báo");
                                    myDialog.setCancelable(false);
                                    myDialog.show();

                                    btnOk.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            myDialog.dismiss();
                                        }
                                    });
                                }

                            } else {
                                // Không có document trong collection Bidder
                            }
                        } else {
                            // Lỗi khi truy vấn collection Bidder
                        }
                    });
                } else {
                    // Document không tồn tại trong collection Allproduct
                }
            } else {
                // Lỗi khi truy vấn document trong collection Allproduct
            }
        });
    }
}