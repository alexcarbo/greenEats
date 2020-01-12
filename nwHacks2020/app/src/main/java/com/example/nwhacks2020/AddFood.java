package com.example.nwhacks2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AddFood extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        toolbar =  findViewById(R.id.manualentrytoolbar);
        toolbar.setTitle("Add Food Items");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void goToSpeech(View view){
        Intent intent = new Intent(getApplicationContext(), Speech.class);
        startActivity(intent);
    }

    public void goToManualEntry(View view){
        Intent intent = new Intent(getApplicationContext(), ManualEntry.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}
