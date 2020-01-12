package com.example.nwhacks2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class ManualEntry extends AppCompatActivity {

    FirebaseFirestore mStore;
    FirebaseAuth mAuth;
    EditText foodItem, expirationDate;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        toolbar =  findViewById(R.id.manualentrytoolbar);
        toolbar.setTitle("Manual Entry");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        foodItem = (EditText) findViewById(R.id.foodinput);
        expirationDate = (EditText) findViewById(R.id.expirydateinput);
    }

    public void submitEntry(View view){

        if(foodItem.getText().toString().equals("") || expirationDate.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Please fill out the fields above", Toast.LENGTH_SHORT);
            return;
        }

        mStore.collection("Users").document(mAuth.getCurrentUser().getDisplayName())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String foodItemString = foodItem.getText().toString();
                    String expirationDateString = expirationDate.getText().toString();
                    DocumentSnapshot snapshot = task.getResult();
                    Map<String, Long> inventory = (Map<String, Long>) snapshot.get("Inventory");
                    inventory.put(foodItemString, Long.parseLong(expirationDateString));
                    mStore.collection("Users").document(mAuth.getCurrentUser().getDisplayName())
                                .update("Inventory", inventory);
                    goBackToLastScreen();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please Submit Again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    private void goBackToLastScreen(){
        onSupportNavigateUp();
    }


}
