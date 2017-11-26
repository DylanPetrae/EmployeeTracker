package com.example.android.employeetracker;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;


    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonLogout = (Button) findViewById(R.id.admin_logout_button);

        buttonLogout.setOnClickListener(this);

    }

    public void logoutUser( View view ){
        firebaseAuth.signOut();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void addEmployee( View view ){
        Intent i = new Intent( this, AddEmployeeActivity.class);
        startActivity(i);
    }

    public void changePassword ( View view ){
        Intent i = new Intent(this, ChangePasswordActivity.class);
        startActivity(i);
    }

    @Override
    public void onClick(View view){
        if(view == buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
//
    /*
    public void viewEmployeeList ( View view ){
        Intent i = new Intent(this, )
    }
    */
}
