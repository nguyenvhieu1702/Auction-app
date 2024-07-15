package com.example.btlauction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailProfile extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth =  FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String uidNow = currentUser.getUid();
    EditText editTextBirthday, editTextName, editTexNumber,edittextEmail;

    CircleImageView circleImageView;

    Button buttonConfirm;

    RadioGroup radioGroup;

    ImageButton buttonBack;

    LinearLayout linearLayout;
    private String mBirthday;

    private String Gender;

    RadioButton radioButtonNam;
    RadioButton radioButtonNu ;
    RadioButton radioButtonKhac;
    Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    final int PICK_IMAGE_REQUEST = 71;
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_profile);
        editTextBirthday = findViewById(R.id.birthday_activityDetailProfile);
        editTextName = findViewById(R.id.name_activityDetailProfile);
        editTexNumber = findViewById(R.id.phone_activityDetailProfile);
        edittextEmail = findViewById(R.id.email_activityDetailProfile);
        linearLayout = findViewById(R.id.avavtarChange_activityDetailProfile);
        circleImageView = findViewById(R.id.ImageAvatar_activityDetailProfile);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        getBirthDay();
        GetInfo();
        buttonConfirm = findViewById(R.id.buttonComfirm_activityDetailProfile);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateData();
            }
        });
        buttonBack = findViewById(R.id.back_activityDetailProfile);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailProfile.this, MainActivityProfile.class);
                startActivity(intent);
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });


        radioGroup = findViewById(R.id.radio_group_gender);
        radioButtonNam = findViewById(R.id.radio_male);
        radioButtonNu = findViewById(R.id.radio_female);
        radioButtonKhac = findViewById(R.id.radio_other);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == radioButtonNam.getId()) {
                    Gender = "Nam";
                } else if (checkedId == radioButtonNu.getId()) {
                    Gender = "Nữ";
                } else if (checkedId == radioButtonKhac.getId()) {
                    Gender = "Khác";
                }
            }
        });
    }
    void GetInfo(){
        db.collection("CurrentUser")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String uid = document.getString("Uid");
                                if (uid.equals(uidNow)) {
                                    String Email = document.getString("Email");
                                    String Name = document.getString("Name");
                                    String Phone = document.getString("Phone");
                                    String Birthday = document.getString("Birthday");
                                    String Gender = document.getString("Gender");
                                    String Avatar = document.getString("avatar_url");
                                    Glide.with(DetailProfile.this)
                                            .load(Avatar)
                                            .into(circleImageView);
                                    editTextBirthday.setText(Birthday);
                                    editTextName.setText(Name);
                                    editTexNumber.setText(Phone);
                                    edittextEmail.setText(Email);

                                    if(Gender == null){
                                        radioButtonKhac.setChecked(true);
                                    }else{
                                        if(Gender.equals("Nam")){
                                            radioButtonNam.setChecked(true);
                                        }else if (Gender.equals("Nữ")){
                                            radioButtonNu.setChecked(true);
                                        }else{
                                            radioButtonKhac.setChecked(true);
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
    void UpdateData() {
        UpdateAvatar();
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
                                    CollectionReference currentUserRef = db.collection("CurrentUser");
                                    DocumentReference docRef = currentUserRef.document(document_id);

                                    Map<String, Object> User = new HashMap<>();
                                    User.put("Name", editTextName.getText().toString());
                                    User.put("Phone", editTexNumber.getText().toString());
                                    User.put("Birthday",  mBirthday);
                                    User.put("Email",edittextEmail.getText().toString());
                                    User.put("Gender",Gender);

                                    docRef.update(User)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Dialog myDialog = new Dialog(DetailProfile.this);
                                                    myDialog.setContentView(R.layout.dialogsuccess_layout);
                                                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    Button btnOk = myDialog.findViewById(R.id.btnOke_dialog);
                                                    myDialog.setTitle("Thông báo");
                                                    myDialog.setCancelable(false);
                                                    myDialog.getWindow().setDimAmount(0.8f);
                                                    myDialog.show();

                                                    btnOk.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            myDialog.dismiss();
                                                            Intent intent = new Intent(DetailProfile.this,MainActivityProfile.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });
                                                    Log.d("TAG", "Dữ liệu đã được cập nhật thành công");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Dialog myDialog = new Dialog(DetailProfile.this);
                                                    myDialog.setContentView(R.layout.dialogfail_layout);
                                                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    Button btnOk = myDialog.findViewById(R.id.btnOke_dialogfail);
                                                    myDialog.setTitle("Thông báo");
                                                    myDialog.setCancelable(false);
                                                    myDialog.getWindow().setDimAmount(0.8f);
                                                    myDialog.show();

                                                    btnOk.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            myDialog.dismiss();
                                                        }
                                                    });
                                                    Log.w("TAG", "Không thể cập nhật dữ liệu", e);
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
    void getBirthDay(){
        editTextBirthday.setOnClickListener(v -> {
            // Lấy ngày tháng năm hiện tại
            Calendar currentDate = Calendar.getInstance();
            int year = currentDate.get(Calendar.YEAR);
            int month = currentDate.get(Calendar.MONTH);
            int day = currentDate.get(Calendar.DAY_OF_MONTH);

            // Mở DatePickerDialog để cho người dùng chọn ngày tháng năm sinh
            DatePickerDialog datePickerDialog = new DatePickerDialog(DetailProfile.this, (view, selectedYear, selectedMonth, selectedDay) -> {
                // Cập nhật giá trị của EditText với ngày tháng năm được chọn
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(selectedYear, selectedMonth, selectedDay);
                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
                mBirthday = dateFormat.format(selectedDate.getTime());
                editTextBirthday.setText(mBirthday);
            }, year, month, day);

            // Thiết lập giá trị tối thiểu và tối đa cho DatePickerDialog
            datePickerDialog.getDatePicker().setMinDate(0);
            datePickerDialog.getDatePicker().setMaxDate(currentDate.getTimeInMillis());

            // Hiển thị DatePickerDialog
            datePickerDialog.show();
        });
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    void UpdateAvatar() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("avatar/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    // Update avatar URL in Firestore
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("CurrentUser")
                                            .whereEqualTo("Uid", uidNow)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            String document_id = document.getId();
                                                            DocumentReference userRef = db.collection("CurrentUser").document(document_id);

                                                            userRef.update("avatar_url", imageUrl)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            // Avatar URL updated successfully
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            // Handle failure to update avatar URL
                                                                        }
                                                                    });
                                                        }
                                                    } else {
                                                        // Handle failure to retrieve document
                                                    }
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure to retrieve image URL
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            // Handle failure to upload image
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                circleImageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
