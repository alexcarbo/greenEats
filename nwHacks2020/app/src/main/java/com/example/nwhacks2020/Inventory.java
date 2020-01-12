package com.example.nwhacks2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory extends AppCompatActivity {

    private FirebaseFirestore mStore;
    ListView listView;
    private FirebaseAuth mAuth;
    Map<String, Number> inventory;
    ArrayList<String> foodItemNames;
    CustomAdapter adapter;
    Toolbar toolbar;
    List<String> items;
    Button recommendRecipes;
    public SharedPreferences itemSharedPreferences;
    String jsonRecipes;
    AlertDialog.Builder builder;
    ArrayList<Boolean> checkedOrNot;
    public static String itemString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        toolbar =  findViewById(R.id.inventorytoolbar);
        toolbar.setTitle("My Fridge");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);

        mStore =FirebaseFirestore.getInstance();
        listView = (ListView) findViewById(R.id.inventorylistview);
        mAuth = FirebaseAuth.getInstance();

        items = new ArrayList<>();
        recommendRecipes = (Button) findViewById(R.id.recommendrecipes);
       DocumentReference docRef =mStore.collection("Users").document(mAuth.getCurrentUser().getDisplayName());
       docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(snapshot.exists()){
                    inventory = (HashMap<String,Number>) snapshot.get("Inventory");
                    foodItemNames = new ArrayList<>(inventory.keySet());
                    checkedOrNot = new ArrayList<>();
                    for(String name: foodItemNames){
                        checkedOrNot.add(false);
                    }
                    adapter = new CustomAdapter();
                    listView.setAdapter(adapter);
                    if(inventory.size() == 0){
                        listView.setVisibility(View.GONE);
                    }
                }

                recommendRecipes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StringBuilder totalItems =new StringBuilder ();
                        
                        for(String item: items){
                            String lowStr = item.toLowerCase().replace(" ","+");
                            totalItems.append(lowStr);
                            totalItems.append(",");
                        }
                        if (totalItems.length() > 0) {
                            totalItems.setLength(totalItems.length() - 1);
                        }

                        Log.i("megaString: ", totalItems.toString());


                        Intent intent = new Intent(getApplicationContext(), Recipes.class);
                        Log.i("string", totalItems.toString());
                        itemString = totalItems.toString();
                        itemSharedPreferences = getSharedPreferences("com.mendozae.teamflickr", Context.MODE_PRIVATE);
                        itemSharedPreferences.edit().putString("item", totalItems.toString()).apply();
                        startActivity(intent);
                    }
                });
           }
       });

    }

    public void goToAddFood(View view){
        Intent intent = new Intent(getApplicationContext(), AddFood.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }


    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return foodItemNames.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final int index = i;
            final ViewHolder viewHolder;
            if(view == null) {
                view = getLayoutInflater().inflate(R.layout.layout_inventory, null);
                viewHolder = new ViewHolder();
                viewHolder.editButton = (ImageView) view.findViewById(R.id.edit);
                viewHolder.deleteButton = (ImageView) view.findViewById(R.id.delete);
                viewHolder.checkBox = (CheckBox) view.findViewById((R.id.checkbox));
                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.editButton.setImageResource(R.drawable.ic_edit_black_24dp);
            viewHolder.deleteButton.setImageResource(R.drawable.ic_delete_black_24dp);
            TextView food = (TextView) view.findViewById(R.id.foodItem);

            food.setText(foodItemNames.get(index));

//            if(checkedOrNot.get(i)){
//                viewHolder.checkBox.setChecked(true);
//            }else{
//                viewHolder.checkBox.setChecked(false);
//            }


            viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ManualEntry.class);
                    startActivity(intent);
                }
            });

            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                            //Setting message manually and performing action on button click
                            builder.setMessage("Do you want to delete this entry ?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            mStore.collection("Users").document(mAuth.getCurrentUser().getDisplayName())
                                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        DocumentSnapshot snapshot = task.getResult();
                                                        Map<String, Long> foodEntries = (Map<String, Long>) snapshot.get("Inventory");
                                                        foodEntries.remove(foodItemNames.get(i));
                                                        mStore.collection("Users").document(mAuth.getCurrentUser().getDisplayName())
                                                                .update("Inventory", foodEntries);

                                                    }
                                                }
                                            });
                                            Toast.makeText(getApplicationContext(),"Successful",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //  Action for 'NO' Button
                                            dialog.cancel();
                                            Toast.makeText(getApplicationContext(),"Failed",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("AlertDialogExample");
                            alert.show();
                        }
                    });


            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkedOrNot.get(i)){
                        checkedOrNot.set(i,false);
                        viewHolder.checkBox.setChecked(false);
                        items.remove(foodItemNames.get(i));
                    }else{
                        checkedOrNot.set(i, true);
                        viewHolder.checkBox.setChecked(true);
                        items.add(foodItemNames.get(i));
                    }
                }
            });



            //imageView.setImageResource(IMAGES[i]);

            // textView_desc.setText(DESCRIPTIONS[i]);
            return view;
        }

        private class ViewHolder{
            public ImageView editButton;
            public ImageView deleteButton;
            public CheckBox checkBox;

        }
    }
}
