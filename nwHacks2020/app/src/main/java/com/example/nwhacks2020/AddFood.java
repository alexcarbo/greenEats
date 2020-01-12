package com.example.nwhacks2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AddFood extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
    }

    public void goToSpeech(View view){
        Intent intent = new Intent(getApplicationContext(), Speech.class);
        startActivity(intent);
    }

    public void goToScanReceipt(View view){
        Intent intent = new Intent(getApplicationContext(), Camera.class);
        startActivity(intent);
    }
}
