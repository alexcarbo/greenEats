package com.example.nwhacks2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nwhacks2020.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import static android.Manifest.permission.*;
import static java.sql.Types.NULL;

public class Speech extends AppCompatActivity {

    // Replace below with your own subscription key
    private static String speechSubscriptionKey = "210804fbd4a140a7aa9c2ec68872d83d";
    // Replace below with your own service region (e.g., "westus").
    private static String serviceRegion = "westus2";

    FirebaseFirestore mStore;
    FirebaseAuth mAuth;

    Toolbar toolbar;
    Button speech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        toolbar =  findViewById(R.id.inventorytoolbar);
        speech = (Button) findViewById(R.id.button);
        toolbar.setTitle("Voice Entry");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Note: we need to request the permissions
        int requestCode = 5; // unique code for the permission request
        ActivityCompat.requestPermissions(Speech.this, new String[]{RECORD_AUDIO, INTERNET}, requestCode);
        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    public void onSpeechButtonClicked(View v) {
        ImageView ear = (ImageView) this.findViewById(R.id.ear);

        TextView txt = (TextView) this.findViewById(R.id.hello); // 'hello' is the ID of your text view
        TextView examples = (TextView) this.findViewById(R.id.hello2);

        if(ear.getVisibility()==View.VISIBLE){
            txt.setVisibility(View.VISIBLE);
            examples.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            speech.setVisibility(View.VISIBLE);
            ear.setVisibility(View.VISIBLE);
        }else{
            txt.setVisibility(View.VISIBLE);
            examples.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            speech.setVisibility(View.VISIBLE);
            ear.setVisibility(View.VISIBLE);
        }
        ear.bringToFront();
        ear.setImageResource(R.drawable.ic_hearing_grey_24dp);
        try {
            SpeechConfig config = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
            assert(config != null);

            SpeechRecognizer reco = new SpeechRecognizer(config);
            assert(reco != null);

            Future<SpeechRecognitionResult> task = reco.recognizeOnceAsync();
            assert(task != null);

            // Note: this will block the UI thread, so eventually, you want to
            //        register for the event (see full samples)
            SpeechRecognitionResult result = task.get();
            assert(result != null);

            if (result.getReason() == ResultReason.RecognizedSpeech) {
//                Log.i("result", result.toString());
                String str = result.toString();
                str = str.toLowerCase();
                String[] data = str.split("add");
                List<String> ingredients = new ArrayList<>(Arrays.asList(data));
                if (ingredients.size() > 1) {
                    ingredients.remove(0); //some random result id thing
                    String lastIng = ingredients.get(ingredients.size()-1);
                    ingredients.remove(ingredients.size()-1);
                    ingredients.add(lastIng.replace(".", ""));

                    for (int i = 0 ; i < ingredients.size() ; i++) {
                        ingredients.set(i, ingredients.get(i).substring(1, ingredients.get(i).length()-1)); //deletes first and last char
                        ingredients.set(i, ingredients.get(i).replace(",",""));
//                        ingredients.set(i, ingredients.get(i).replace(" ","+"));
                        Log.i("ingredient:", ingredients.get(i));
                    }
                }

                StringBuilder strDisplay = new StringBuilder();
                for (String ingredient : ingredients) {
                    strDisplay.append(ingredient);
                    strDisplay.append(", ");
                }
                strDisplay.setLength(strDisplay.length()-2);

                examples.setVisibility(View.GONE);
                ear.setVisibility(View.GONE);
                txt.setText("Your inputted ingredients: " + strDisplay.toString());

                mStore.collection("Users").document(mAuth.getCurrentUser().getDisplayName())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot snapshot = task.getResult();
                                    Map<String, Long> myFridge = (Map<String,Long>) snapshot.get("Inventory");

                                    mStore.collection("Food").document("Food")
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot currentItemsSnapshot = task.getResult();
                                            Map<String, Long> currentItems = (Map<String, Long>) currentItemsSnapshot.get("MyFridge");

                                            for(String ingredient: ingredients){
                                                Log.i("ingredient", ingredient);
                                                if(!currentItems.containsKey(ingredient)){
                                                    myFridge.put(ingredient, new Long(-1));
                                                }else{
                                                    myFridge.put(ingredient, currentItems.get(ingredient));
                                                }
                                            }

                                            mStore.collection("Users").document(mAuth.getCurrentUser().getDisplayName())
                                                    .update("Inventory", myFridge);
                                        }
                                    });

                                }
                                else{
                                    Log.i("failed", "failed");
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

            }
            else {
                txt.setText("Error recognizing. Did you update the subscription info?" + System.lineSeparator() + result.toString());
            }

            reco.close();
        } catch (Exception ex) {
            Log.e("SpeechSDKDemo", "unexpected " + ex.getMessage());
            assert(false);
        }
    }

}