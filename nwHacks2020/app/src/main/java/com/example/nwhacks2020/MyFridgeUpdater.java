package com.example.nwhacks2020;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

import io.opencensus.internal.StringUtils;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class MyFridgeUpdater {
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;

    MyFridgeUpdater() {
        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public void updateMyFridge(List<String> itemsToAdd) {
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

                            for(String item: itemsToAdd){
                                item = item.replaceAll("[0-9]","");
                                item = item.trim();
                                item = item.toLowerCase();

                                if(!item.isEmpty()) {
                                    Log.i("ingredient", item);
                                    if(!currentItems.containsKey(item)){
                                        myFridge.put(item, new Long(-1));
                                    }else{
                                        myFridge.put(item, currentItems.get(item));
                                    }
                                }
                            }
                            Log.i("size", Integer.toString(myFridge.size()));
                            mStore.collection("Users").document(mAuth.getCurrentUser().getDisplayName())
                                    .update("Inventory", myFridge);
                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
