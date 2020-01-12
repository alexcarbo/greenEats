package com.example.nwhacks2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        toolbar =  findViewById(R.id.inventorytoolbar);
        toolbar.setTitle("Inventory");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor("#333333"));

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
                    }
                });
           }
       });

    }

    public void goToAddFood(View view){
        Intent intent = new Intent(getApplicationContext(), AddFood.class);
        startActivity(intent);
    }

    public void activateStringGetter(View view){
//        String text = editText.getText().toString();
//        try {
//            getJSONstring(text);
//        }catch(Exception e){
//            Log.i("Failed", "uhhhhhhh");
//        }
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

            TextView food = (TextView) view.findViewById(R.id.name);

            food.setText(foodItemNames.get(index));


            viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {

                }
            });

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(viewHolder.checkBox.isChecked()){
                        viewHolder.checkBox.setChecked(false);
                        items.remove(inventory.get(i));
                    }else{
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
