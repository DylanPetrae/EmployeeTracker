package com.example.android.employeetracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChangePasswordActivity extends AppCompatActivity {

    String newPassword = "";

    private EditText editTextNewPassword;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);


        editTextNewPassword = (EditText) findViewById(R.id.new_password);

    }

    public void changePassword ( View view ) {

        String emailAddress = editTextNewPassword.getText().toString();
        String newPassword = editTextNewPassword.getText().toString();

        if(newPassword.equals("")){
            Toast.makeText(this, "Please enter a new password.", Toast.LENGTH_SHORT).show();
        }

        else {
            firebaseAuth = FirebaseAuth.getInstance();
            user = firebaseAuth.getCurrentUser();

            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ChangePasswordActivity.this, "Password Changed Successfully.", Toast.LENGTH_SHORT).show();
                                editTextNewPassword.setText("");
                            }
                        }
                    });
        }
    }

    public void logoutUser ( View view ) {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }




}
