package com.example.nwhacks2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class Recipes extends AppCompatActivity {

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> recipeURLs = new ArrayList<>();
    String jstring;
    ListView recipes;
    CustomAdapter adapter;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        toolbar =  findViewById(R.id.manualentrytoolbar);
        toolbar.setTitle("Recipes");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recipes = (ListView) findViewById(R.id.recipes);

        String totalItems = getSharedPreferences("com.example.nwhacks2020", Context.MODE_PRIVATE).getString("item", "mam");
        totalItems = Inventory.itemString;

            Log.i("BEFORE", totalItems);
            getJSONstring(totalItems);

            Log.i("AFTER", jstring + " hello");


//        try {

//            JSONObject jobj = new JSONObject(jstring);
//
//            JSONArray jrecipes =  jobj.getJSONArray("recipes");
//
//            Log.i("lol", "HELLLLLLOOOOOOOOOOOO");
//
//            for (int i = 0; i < 3; i++){
//                JSONObject recipe = (JSONObject)jrecipes.get(i);
//                titles.add((String)recipe.get("title"));
//                recipeURLs.add((String)recipe.get("href"));
//            }

//        } catch (JSONException e) {
//            e.printStackTrace();
   }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;

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
                        //Log.i("Response is: ", response.substring(0,500))
                        jstring = response.trim().substring(1,response.length()-1);
                        jstring = StringEscapeUtils.unescapeJava(jstring);
                        Log.i("STRING", jstring);

//                        String[] data = response.split("next");
//                        List<String> recipies = new ArrayList<>(Arrays.asList(data));
//                        if (recipies.size() > 1) recipies.remove(0);
//
//                        for (int i = 0; i < recipies.size(); i++){
//                            String[] set = recipies.get(i).split(url);
//                            titles.add(set[0]);
//                            recipeURLs.add(set[1]);
//                        }
//
//                        Log.i("HELLLO", titles.get(1));


//                        try {

//
                            Gson gson = new Gson();

//                        JsonReader reader = new JsonReader(new StringReader(jstring));
//                        reader.setLenient(true);


                        JsonObject jsonObject = new JsonParser().parse(jstring).getAsJsonObject();

                        JsonArray array = jsonObject.getAsJsonArray("results");

                        for (JsonElement result: array){
                            JsonObject resultObject = result.getAsJsonObject();
                            String title = resultObject.get("title").getAsString();
                            String url = resultObject.get("href").getAsString();

                            titles.add(title);
                            recipeURLs.add(url);
                            Log.i("TITLE", title);
                        }

                            //Object ob = gson.fromJson(input, MyObject.class);

                            //MyObject o = (MyObject) ob;

                            //Log.i("urls", o.getResults().get(0).href);
//
//                            JSONObject jobj = new JSONObject(jstring);
//
//                            JSONArray jrecipes =  jobj.getJSONArray("recipes");
//
//                            Log.i("lol", "HELLLLLLOOOOOOOOOOOO");
//
//                            for (int i = 0; i < 3; i++){
//                                JSONObject recipe = (JSONObject)jrecipes.get(i);
//                                titles.add((String)recipe.get("title"));
//                                recipeURLs.add((String)recipe.get("href"));
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                        adapter = new CustomAdapter();
                        recipes.setAdapter(adapter);
                        recipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Uri uri = Uri.parse(recipeURLs.get(i));
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        });

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERROR", error.toString());
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

            if(view == null){

                LayoutInflater mInflater = (LayoutInflater) getApplicationContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                view = mInflater.inflate(R.layout.layout_recipes, null);

                TextView recipe = (TextView) view.findViewById(R.id.recipe);
                recipe.setText(titles.get(i));

            }






            //imageView.setImageResource(IMAGES[i]);

            // textView_desc.setText(DESCRIPTIONS[i]);
            return view;
        }





    }

    public class MyObject{
        //List<Recipe> results;
        String title;
        String version;
        String href;

        MyObject(){

        }
        MyObject(List<Recipe> r, String t, String v, String h){
            //results = r;
            title = t;
            version = v;
            href = h;
        }

//        public void setResults(List<Recipe> results) {
//            this.results = results;
//        }
//
//        public List<Recipe> getResults() {
//            return results;
//        }

        public void setHref(String href) {
            this.href = href;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getTitle() {
            return title;
        }

        public String getHref() {
            return href;
        }

        public String getVersion() {
            return version;
        }
    }

    public class Recipe{
        String title;
        String href;
        String ingredients;
        String thumbnail;

        Recipe(){

        }

        Recipe(String t, String url, String i, String thumb){
            title = t;
            href = url;
            ingredients = i;
            thumbnail = thumb;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getHref() {
            return href;
        }

        public String getTitle() {
            return title;
        }

        public String getIngredients() {
            return ingredients;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setIngredients(String ingredients) {
            this.ingredients = ingredients;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }
    }


}
