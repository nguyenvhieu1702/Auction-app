package com.example.btlauction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityResetPass extends AppCompatActivity {
    EditText oldPass, newPass, newPass2;
    Button btnConfirm4;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_reset_pass);
        oldPass = findViewById(R.id.oldPass);
        newPass = findViewById(R.id.newPass);
        newPass2 = findViewById(R.id.newPass2);
        btnConfirm4 = findViewById(R.id.btnConfirm4);
        mAuth = FirebaseAuth.getInstance();
        btnConfirm4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Changepass();
            }
        });
    }

    private void Changepass() {
        String nPass1, nPass2, oPass1;
        oPass1 = oldPass.getText().toString();
        nPass1 = newPass.getText().toString();
        nPass2 = newPass2.getText().toString();
        if (TextUtils.isEmpty(oPass1)) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu cũ", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(nPass1)) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(nPass2)) {
            Toast.makeText(this, "Vui lòng nhập xác nhận lại mật khẩu ", Toast.LENGTH_SHORT).show();
            return;
        } else if (!nPass1.equals(nPass2)) {
            Toast.makeText(this, "Mật khẩu mới không trùng khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            mAuth.signInWithEmailAndPassword(mUser.getEmail(), oPass1)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mUser.updatePassword(nPass1)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(MainActivityResetPass.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(MainActivityResetPass.this, MainActivityLogin1.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(MainActivityResetPass.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(MainActivityResetPass.this, "Mật khẩu cũ không chính xác", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
