package com.example.nwhacks2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class LoginPage extends AppCompatActivity {

    private boolean signUp = true;
    EditText username, password, email;
    Button loginOrSignup;
    public FirebaseFirestore ddb = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private String userName, userEmail, userPassword;
    TextView switcher;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        toolbar =  findViewById(R.id.manualentrytoolbar);
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        ddb = FirebaseFirestore.getInstance();

        username = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        switcher = (TextView) findViewById(R.id.switcher);
        loginOrSignup = (Button) findViewById(R.id.loginOrsignup);
    }

    @Override
    public void onStart() {

        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser(); //gets current user if any
        if(currentUser !=null){//checks if there is a current user

            updateUI(currentUser); //calls method to send Activity to the UserInterface Activity
        }
    }

    public void authenticateUser(View view){
        userName = username.getText().toString();
        userEmail = email.getText().toString();
        userPassword = password.getText().toString();

        if(signUp){
            mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(userName)
                                        .build();
                                mAuth.getCurrentUser().updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Log.i("Yes", "Successful");
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    initializeFields();
                                                    updateUI(user);
                                                }
                                            }
                                        });

                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(LoginPage.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });
        }else{
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginPage.this, "Log in Successful", Toast.LENGTH_SHORT).show();
                        updateUI(mAuth.getCurrentUser());
                    } else {
                        Toast.makeText(LoginPage.this, "Log in Unsuccessful", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    private void initializeFields(){
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("Name", userName);
        userInfo.put("Email", userEmail);

        Map<String, Map<String, Integer>> userInfo2 = new HashMap<>();
        userInfo2.put("Inventory", new HashMap<String, Integer>());

        ddb.collection("Users").document(userName).set(userInfo);
        ddb.collection("Users").document(userName).set(userInfo2, SetOptions.merge());

//        Map<String, List<String>> inventoryInfo = new HashMap<>();
//        inventoryInfo.put(userEmail, new ArrayList<String>());
//        ddb.collection("Inventory").add(new ArrayList<String>());


    }

    private void updateUI(FirebaseUser user){
        if(user == null){
            return;
        }
        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(intent);
    }

    public void switcher(View view){
        if(signUp){
            username.setVisibility(View.GONE);
            switcher.setText("Don't have an account? Sign up");
            loginOrSignup.setText("Log In");
            signUp = false;
        }else{
            username.setVisibility(View.VISIBLE);
            username.setCursorVisible(true);
            signUp = true;
            loginOrSignup.setText("Sign up");
            switcher.setText("Already have an account? Log in");
        }
    }
}
