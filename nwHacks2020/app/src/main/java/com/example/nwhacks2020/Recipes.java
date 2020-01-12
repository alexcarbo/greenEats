package com.example.nwhacks2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Recipes extends AppCompatActivity {

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> recipeURLs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        String totalItems = getSharedPreferences("com.example.nwhacks2020", Context.MODE_PRIVATE).getString("item", "Default");

        try {
            JSONObject jstring = new JSONObject(totalItems);

            JSONArray jrecipes =  jstring.getJSONArray("recipes");

            for (int i = 0; i < jrecipes.length(); i++){
                JSONObject recipe = (JSONObject)jrecipes.get(i);
                titles.add((String)recipe.get("title"));
                recipeURLs.add((String)recipe.get("href"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



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
}
