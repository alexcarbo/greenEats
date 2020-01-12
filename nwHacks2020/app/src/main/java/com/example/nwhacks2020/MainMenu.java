package com.example.nwhacks2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        toolbar =  findViewById(R.id.manualentrytoolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

    }

    public void goToInventory(View view){
        Intent intent = new Intent(getApplicationContext(), Inventory.class);
        startActivity(intent);
    }

    public void goToAddFood(View view){
        Intent intent = new Intent(getApplicationContext(), AddFood.class);
        startActivity(intent);
    }

    public void goToScanReceipt(View view){
        Intent intent = new Intent(getApplicationContext(), Camera.class);
        startActivity(intent);
    }

    public void goToExpiringSoon(View view){
        Intent intent = new Intent(getApplicationContext(), ExpiringSoon.class);
        startActivity(intent);
    }


}
