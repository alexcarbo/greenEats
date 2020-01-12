package com.example.nwhacks2020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginPage extends AppCompatActivity {

    private boolean signUp = true;
    EditText username, password, email;
    Button loginOrSignup;
    public FirebaseFirestore ddb = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    TextView switcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        username = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        switcher = (TextView) findViewById(R.id.switcher);
        loginOrSignup = (Button) findViewById(R.id.loginOrsignup);
    }

    public void authenticateUser(View view){

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
