package com.example.nwhacks2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Recipes extends AppCompatActivity {

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> recipeURLs = new ArrayList<>();
    String jstring;
    ListView recipes;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        recipes = (ListView) findViewById(R.id.recipes);
        adapter = new CustomAdapter();
        recipes.setAdapter(adapter);
        String totalItems = getSharedPreferences("com.example.nwhacks2020", Context.MODE_PRIVATE).getString("item", "Default");

        try {
            getJSONstring(totalItems);
            JSONObject jobj = new JSONObject(totalItems);

            JSONArray jrecipes =  jobj.getJSONArray("recipes");

            for (int i = 0; i < jrecipes.length(); i++){
                JSONObject recipe = (JSONObject)jrecipes.get(i);
                titles.add((String)recipe.get("title"));
                recipeURLs.add((String)recipe.get("href"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void getJSONstring(String ingredients) {
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
                        jstring = response;

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return titles.size();
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

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(recipeURLs.get(i));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });

            //imageView.setImageResource(IMAGES[i]);

            // textView_desc.setText(DESCRIPTIONS[i]);
            return view;
        }


    }
>>>>>>> 6f33955103c2c0d677e17a61b127086eddad1d9d
}
