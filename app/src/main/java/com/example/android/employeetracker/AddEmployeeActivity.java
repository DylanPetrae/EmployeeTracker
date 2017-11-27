package com.example.android.employeetracker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AddEmployeeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private String TAG = AddEmployeeActivity.class.getSimpleName();
    private Context context;
    private int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_add);
        firebaseAuth = FirebaseAuth.getInstance();
        emailEditText = (EditText) findViewById(R.id.new_employee_email);
        passwordEditText = (EditText) findViewById(R.id.new_employee_password);
    }


    // Add Employee to database, using the data in the fields
    public void addEmployee ( View view ){

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Ensure email field is not empty.
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter an e-mail address.", Toast.LENGTH_SHORT).show();
            // Stop function
            return;
        }

        // Ensure password field is not empty.
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter a password.", Toast.LENGTH_SHORT).show();
            // Stop function
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // If task is successful, new user will be automatically logged in
                if (task.isSuccessful()) {
                    // Sign in success
                    Log.d(TAG, "createUserWithEmail:success");
                    context = getApplicationContext();
                    duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "Empoyee succesfully added", duration);
                    toast.show();
                    // sign out and sign back in as admin
                    firebaseAuth.signOut();
                    firebaseAuth.signInWithEmailAndPassword("admin@employeetracker.net,", "password");

                } else {
                    // Sign in fails
                    context = getApplicationContext();
                    duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "Failed to add employee. User with same email may exist.", duration);
                    toast.show();
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                }
            }
        });

    }


    public void gotoControlPanel ( View view ){

        Intent i = new Intent(this, AdminMainActivity.class);
        startActivity(i);
    }
}