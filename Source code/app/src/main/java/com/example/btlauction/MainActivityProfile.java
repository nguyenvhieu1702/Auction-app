package com.example.btlauction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivityProfile extends AppCompatActivity {
    TextView name_acitvityProfile, email_acitvityProfile;

    RelativeLayout yourAuction_activityProfile,favoriteAuction_activityProfile, btnSignout, changePass;
    Button editProfile_activiyProfile;
    ImageView back_activityProfile;

    CircleImageView avavtar_activityProfile;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String uidNow = currentUser.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);
        btnSignout=findViewById(R.id.btnSignout);
        changePass=findViewById(R.id.changePass);
        name_acitvityProfile = findViewById(R.id.name_activityProfile);
        email_acitvityProfile = findViewById(R.id.email_activityProfile);
        avavtar_activityProfile = findViewById(R.id.avavtar_activityProfile);

        mAuth = FirebaseAuth.getInstance();

        back_activityProfile = findViewById(R.id.back_activityProfile);

        back_activityProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityProfile.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signout();
            }
        });
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityProfile.this,MainActivityResetPass.class);
                startActivity(intent);
                finish();
            }
        });

        GetData();

        editProfile_activiyProfile = findViewById(R.id.editProfile_activityProfile);
        editProfile_activiyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityProfile.this,DetailProfile.class);
                startActivity(intent);
            }
        });

        yourAuction_activityProfile = findViewById(R.id.yourAuction_activityProfile);
        yourAuction_activityProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityProfile.this,MainActivityMyAuction.class);
                startActivity(intent);
            }
        });

        favoriteAuction_activityProfile = findViewById(R.id.favoriteAuction_activityProfile);
        favoriteAuction_activityProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityProfile.this,MainActivityfavotites.class);
                startActivity(intent);
            }
        });
    }

    private void Signout(){
        mAuth.signOut();
        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("gmail");
        editor.remove("pass");
        editor.apply();

        Intent intent =new Intent(MainActivityProfile.this,MainActivityLogin1.class);
        startActivity(intent);
        finish();
    }
    private void GetData(){
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
                                    String Email = document.getString("Email");
                                    String Avatar = document.getString("avatar_url");
                                    name_acitvityProfile.setText(NameCus);
                                    email_acitvityProfile.setText(Email);

                                    Glide.with(MainActivityProfile.this)
                                            .load(Avatar)
                                            .into(avavtar_activityProfile);
                                }
                            }
                        } else {

                        }
                    }
                });

    }
}
