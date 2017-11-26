package com.example.android.employeetracker;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Date;

public class EmployeeMainActivity extends AppCompatActivity implements View.OnClickListener{

    // Firebase methods
    private FirebaseAuth firebaseAuth;

    // Location methods
    LocationManager locationManager;
    LocationListener locationListener;

    private FirebaseUser user;

    private TextView textViewUserEmail;
  
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main);

        firebaseAuth = FirebaseAuth.getInstance();

        // If user is NOT logged in
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        // Take user to camera immediately after log in
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 0);

        user = firebaseAuth.getCurrentUser();

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

        // Location
        // TODO: Check if GPS is enables. Else, request permission
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener(user);

        try {
            // Store GPS every 5 minutes (300000 milliseconds)
            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,5000, 0, locationListener);
        }
        catch (SecurityException e) {
            //ERROR
            System.err.println("ERROR: Couldn't retrieve GPS.");
        }

    }

    public FirebaseUser getUser(){
        return user;
    }

    @Override
    public void onClick(View view){
        if(view == buttonLogout){
            firebaseAuth.signOut();
            locationManager.removeUpdates(locationListener);
            locationManager = null;
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Store image to database

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap map = (Bitmap) extras.get("data");

            //Store image to bitmap
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            map.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] stream = baos.toByteArray();

            // Create reference to filepath
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference userImageRef = storageRef.child("images/" + firebaseAuth.getCurrentUser().getUid() + ".jpg");

            // Upload to Firebase Storage
            UploadTask uploadTask = userImageRef.putBytes(stream);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("myTag", "picture not sent to firebase");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("myTag", "picture sent to firebase");
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });
        }
    }
}
