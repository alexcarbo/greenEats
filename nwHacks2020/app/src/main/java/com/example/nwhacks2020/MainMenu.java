package com.example.nwhacks2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void goToInventory(View view){
        Intent intent = new Intent(getApplicationContext(), Inventory.class);
        startActivity(intent);
    }

    public void goToAddFood(View view){
        Intent intent = new Intent(getApplicationContext(), AddFood.class);
        startActivity(intent);
    }

    public void goToBuyFood(View view){
        Intent intent = new Intent(getApplicationContext(), BuyFood.class);
        startActivity(intent);
    }

    public void goToScanReceipt(View view){
        Intent intent = new Intent(getApplicationContext(), Camera.class);
        startActivity(intent);
    }
}
