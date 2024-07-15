package com.example.btlauction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivityWallet extends AppCompatActivity {

    TextView money_activityWallet ,recharge_activityWallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wallet);
        money_activityWallet = findViewById(R.id.money_activityWallet);
        recharge_activityWallet = findViewById(R.id.recharge_activityWallet);
    }
}