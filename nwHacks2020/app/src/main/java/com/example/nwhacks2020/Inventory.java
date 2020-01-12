package com.example.nwhacks2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Inventory extends AppCompatActivity {

    private FirebaseFirestore mStore;
    ListView listView;
    private FirebaseAuth mAuth;
    ArrayList<String> inventory;
    CustomAdapter adapter;
    Toolbar toolbar;
    List<String> items;
    Button recommendRecipes;
    public SharedPreferences itemSharedPreferences;

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
                    inventory = (ArrayList<String>) snapshot.get("Inventory");
                    adapter = new CustomAdapter();
                    listView.setAdapter(adapter);
                    if(inventory.size() == 0){
                        listView.setVisibility(View.GONE);
                    }
                }

                recommendRecipes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String totalItems="";
                        for(String item: items){

                        }
                        
                        Intent intent = new Intent(getApplicationContext(), Recipes.class);
                        itemSharedPreferences = getSharedPreferences("com.mendozae.teamflickr", Context.MODE_PRIVATE);
                        itemSharedPreferences.edit().putString("item", totalItems).apply();
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


    public void getJSONstring(String ingredients) throws IOException {
        String url = "https://sarvan13.api.stdlib.com/nwhacks@dev/foodrec/?ingredients=" + ingredients;
        Log.i("url", url);

//        URL url = new URL("https://sarvan13.api.stdlib.com/nwhacks@dev/foodrec/?ingredients=" + ingredients);

//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestMethod("GET");
//
//        con.setConnectTimeout((3000));
//
//
//        int status = con.getResponseCode();
//
//        Reader streamReader = null;
//
//        if (status > 299) {
//            streamReader = new InputStreamReader(con.getErrorStream());
//        } else {
//            streamReader = new InputStreamReader(con.getInputStream());
//        }
//
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuffer content = new StringBuffer();
//        while ((inputLine = in.readLine()) != null) {
//            content.append(inputLine);
//        }
//        in.close();
//
//        Log.i("string", inputLine);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.i("Response is: ", response.substring(0,500));
                        try {
                            onFinish(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("Failed", "ohhhhhhh nooooo");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);



    }

    public void onFinish(String response) throws JSONException {
        JSONObject jrecipe = new JSONObject(response);


    }


    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return inventory.size();
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

            food.setText(inventory.get(index));


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
                        items.add(inventory.get(i));
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
