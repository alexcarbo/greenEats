package com.example.nwhacks2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.nwhacks2020.R;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import static android.Manifest.permission.*;

public class Speech extends AppCompatActivity {

    // Replace below with your own subscription key
    private static String speechSubscriptionKey = "210804fbd4a140a7aa9c2ec68872d83d";
    // Replace below with your own service region (e.g., "westus").
    private static String serviceRegion = "westus2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        // Note: we need to request the permissions
        int requestCode = 5; // unique code for the permission request
        ActivityCompat.requestPermissions(Speech.this, new String[]{RECORD_AUDIO, INTERNET}, requestCode);
    }

    public void onSpeechButtonClicked(View v) {
        TextView txt = (TextView) this.findViewById(R.id.hello); // 'hello' is the ID of your text view

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
                txt.setText(result.toString());
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
                        ingredients.set(i, ingredients.get(i).replace(" ","+"));
                        Log.i("ingredient:", ingredients.get(i));
                    }
                }

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