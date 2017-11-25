package com.example.android.employeetracker;

import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
class MyLocationListener implements LocationListener{
    // variables
    private FirebaseFirestore db;
    GeoPoint geoPointLocation;
    private double latitude;
    private double longitude;
    FirebaseUser user;

    public MyLocationListener(FirebaseUser user) {
        db = FirebaseFirestore.getInstance();
        this.user = user;
    }



    public GeoPoint getLocation(){
        if(geoPointLocation != null)
            return geoPointLocation;
        else
            return null;
    }

    public void storeLocation(android.location.Location location){
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        geoPointLocation = new GeoPoint(latitude, longitude);
    }

    public void onLocationChanged(android.location.Location location){
        storeLocation(location);

        // Get user from Firebase
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    Map<String, Object> locationHashMap = new HashMap<>();

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            // Loop through users
                            for (DocumentSnapshot document : task.getResult()) {
                                // If user found
                                if (document.getId().equals(user.getEmail())) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                                    String currentDateandTime = sdf.format(new Date());

                                    // Update location
                                    locationHashMap.put(currentDateandTime, geoPointLocation);
                                    db.collection("users").document(document.getId()).update(locationHashMap);
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Error getting documents: " + task.getException());
                        }
                    }
                });
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
