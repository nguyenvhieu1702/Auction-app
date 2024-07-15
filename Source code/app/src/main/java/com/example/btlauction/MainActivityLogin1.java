package com.example.btlauction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivityLogin1 extends AppCompatActivity {

    EditText gmailEdit, passEdit;
    Button btnConfirm;
    FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ImageButton back_acitivtyLogin1;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    String UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login1);

        gmailEdit = findViewById(R.id.gmailEdit);
        passEdit = findViewById(R.id.passEdit);
        btnConfirm = findViewById(R.id.btnConfirm);
        back_acitivtyLogin1 = findViewById(R.id.back_activityLogin1);
        back_acitivtyLogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityLogin1.this, MainActivityLogin.class);
                startActivity(intent);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        //Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Lấy thông tin đăng nhập từ SharedPreferences
        String savedGmail = sharedPreferences.getString("gmail", "");
        String savedPass = sharedPreferences.getString("pass", "");

//        gmailEdit.setText(savedGmail);
//        passEdit.setText(savedPass);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String gmail, pass;
        gmail = gmailEdit.getText().toString();
        pass = passEdit.getText().toString();
        if (TextUtils.isEmpty(gmail)) {
            Toast.makeText(this, "Vui lòng nhập gmail ", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(gmail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivityLogin1.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    //Lưu thông tin đăng nhập vào SharedPreferences để sử dụng lại sau này
                    editor.putString("gmail", gmail);
                    editor.putString("pass", pass);
                    editor.apply();
                    Intent intent = new Intent(MainActivityLogin1.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(MainActivityLogin1.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
