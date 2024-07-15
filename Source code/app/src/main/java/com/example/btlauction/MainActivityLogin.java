package com.example.btlauction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityLogin extends AppCompatActivity {

    TextView textView, textView1, textView2;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String savedGmail = sharedPreferences.getString("gmail", "");
        String savedPass = sharedPreferences.getString("pass", "");

        if (!TextUtils.isEmpty(savedGmail) && !TextUtils.isEmpty(savedPass)) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(savedGmail, savedPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(MainActivityLogin.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        } else {
            setContentView(R.layout.activity_main_login);

            textView = findViewById(R.id.loginBtn);
            textView1 = findViewById(R.id.registerBtn);
            textView2 = findViewById(R.id.resetPassBtn);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivityLogin.this, MainActivityLogin1.class);
                    startActivity(intent);
                }
            });

            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivityLogin.this, MainActivityDKI.class);
                    startActivity(intent);
                }
            });

            textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivityLogin.this, MainActivityGmail.class);
                    startActivity(intent);
                }
            });
        }
    }
}
