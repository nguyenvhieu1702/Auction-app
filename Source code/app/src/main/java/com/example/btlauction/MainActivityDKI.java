package com.example.btlauction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivityDKI extends AppCompatActivity {
    EditText gmailEdit,passEdit,pass2Edit;
    Button btnConfirm;
    FirebaseAuth mAuth;

    ImageButton back_activityDki;
    String Email,passw,passw2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dki);
        gmailEdit = findViewById(R.id.gmailEdit);
        passEdit = findViewById(R.id.passEdit);
        pass2Edit = findViewById(R.id.pass2Edit);
        btnConfirm = findViewById(R.id.btnConfirm);
        mAuth = FirebaseAuth.getInstance();
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resigter();
            }
        });

        back_activityDki = findViewById(R.id.back_activityDki);
        back_activityDki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void resigter() {
        Email = gmailEdit.getText().toString();
        passw = passEdit.getText().toString();
        passw2 = pass2Edit.getText().toString();
        if(TextUtils.isEmpty(Email)){
            Toast.makeText(this, "Vui lòng nhập Gmail", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(passw)){
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(passw2)) {
            Toast.makeText(this, "Vui lòng nhập xác nhận lại mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!passw.equals(passw2)) {
            Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(Email,passw2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivityDKI.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                    SaveUID();
                    Intent intent = new Intent(MainActivityDKI.this,MainActivityLogin1.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivityDKI.this,"Đăng ký chưa thành công",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void SaveUID() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();

        Map<String, Object> User = new HashMap<>();
        User.put("Uid", uid);
        User.put("Email", Email);
        db.collection("CurrentUser").add(User)
                .addOnSuccessListener(documentReference -> {
                    Log.d("TAG", "Product added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w("TAG", "Error adding product", e);
                });
    }

}