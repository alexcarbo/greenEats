package com.example.nwhacks2020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecipeRecommender extends AppCompatActivity {

    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_recommender);
        editText = (EditText) findViewById(R.id.editText);
    }

    public void activateStringGetter(View view){
        String text = editText.getText().toString();
        try {
            getJSONstring(text);
        }catch(Exception e){
            Log.i("Failed", "uhhhhhhh");
        }
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


}
