package com.example.btlauction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityGmail extends AppCompatActivity {
    Button btnConfirm3;
    EditText quenmkEdit;
    ImageButton back_activityGmail;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gmail);
        quenmkEdit = findViewById(R.id.quenmkEdit);
        btnConfirm3 = findViewById(R.id.btnConfirm3);
        mAuth = FirebaseAuth.getInstance();
        btnConfirm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgot();
            }
        });

        back_activityGmail = findViewById(R.id.back_activityGmail);
        back_activityGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void forgot() {
        String gmailAddress;
        gmailAddress = quenmkEdit.getText().toString();
        if (TextUtils.isEmpty(gmailAddress)) {
            Toast.makeText(this, "Vui lòng nhập gmail ", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.sendPasswordResetEmail(gmailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivityGmail.this,"Đã gửi đến gmail của bạn",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivityGmail.this,MainActivityLogin1.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivityGmail.this,"Không tồn tại gmail này ",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}