package com.example.android.employeetracker;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private Button buttonLogIn;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private ProgressDialog progressDialog;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        // if employee is already logged in
        if(firebaseAuth.getCurrentUser() != null){
            // If user is already logged in and IS an admin
            if(firebaseAuth.getUid().equals("WsOdq6SUCOQjtE2HZG0epJeUJah2")){
                finish();
                startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
            }
            // If user is already logged in and NOT an admin
            else {
                finish();
                startActivity(new Intent(getApplicationContext(), EmployeeMainActivity.class));
            }
        }


        progressDialog = new ProgressDialog(this);

        //initializing views
        buttonLogIn = (Button) findViewById(R.id.button_login);
        editTextEmail = (EditText) findViewById(R.id.edit_text_email);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);

        buttonLogIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        if(view == buttonLogIn){
            userLogin();
        }
    }

    public void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();



        // If email address field is empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter an e-mail address.", Toast.LENGTH_SHORT).show();
            // Stop function
            return;
        }

        // If password field is empty
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter a password.", Toast.LENGTH_SHORT).show();
            // Stop function
            return;
        }

        // Show progress Dialog
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        //Checks to see if login details are valid
        //If they are, launch EmployeeMainActivity
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Dialog box for wrong login
                        AlertDialog alert;
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            if(firebaseAuth.getUid().equals("WsOdq6SUCOQjtE2HZG0epJeUJah2")) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
                            }
                            else{
                                finish();
                                startActivity(new Intent(getApplicationContext(), EmployeeMainActivity.class));
                            }
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    new ContextThemeWrapper(MainActivity.this, android.R.style.DeviceDefault_ButtonBar_AlertDialog));
                            builder.setCancelable(true);
                            builder.setTitle("Incorrect Login.");
                            builder.setMessage("Try again.");
                            builder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alert = builder.create();
                            alert.show();
                            editTextEmail.setText("");
                            editTextPassword.setText("");
                        }

                    }
                });

    }

}
