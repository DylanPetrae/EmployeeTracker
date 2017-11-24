package com.example.android.employeetracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Date;


public class EmployeeMainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;

    private TextView textViewUserEmail;

    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main);


        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        // If user is NOT logged in
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));

        }


        //Intialize Instace of FireStore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Start getting our user data ready
        HashMap<String, Object> userMap = new HashMap<String, Object>();
        HashMap<String,String>  dateLoc  = new HashMap<String, String>();

        userMap.put("email", user.getEmail());
        userMap.put("lastUpdated", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        userMap.put("lastUpdatedDay",new SimpleDateFormat("yyyy.MM.dd").format(new Date()));
        dateLoc.put(new SimpleDateFormat("yyyy.MM.dd").format(new Date()), "GPS LOCATION HERE");

        userMap.put("location",dateLoc);
        //userMap.put("temp","temp");

        //update to 'users' data
        db.collection("users").document(user.getUid()).update(userMap);


        textViewUserEmail = (TextView) findViewById(R.id.welcomeUser);

        textViewUserEmail.setText("Welcome "+user.getEmail());

        buttonLogout = (Button) findViewById(R.id.employeeLogoutButton);

        buttonLogout.setOnClickListener(this);


    }

    @Override
    public void onClick(View view){
        if(view == buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
