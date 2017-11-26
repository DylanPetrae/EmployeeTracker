package com.example.android.employeetracker;

import android.app.Activity;
import android.content.Context;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
class MyLocationListener implements LocationListener{
    // variables
    private FirebaseFirestore db;

    private GeoPoint geoPointLocation;
    private double latitude;
    private double longitude;

    private HashMap<String,Object> userMap;

    private Activity activity;

    private TextView textViewLatitude;
    private TextView textViewLongitude;

    FirebaseUser user;

    public MyLocationListener(FirebaseUser user, HashMap<String,Object> userMap, Activity activity) {
        db = FirebaseFirestore.getInstance();
        this.user = user;
        this.userMap = userMap;
        this.activity = activity;
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

        // Set UI Lat/Lon text
        if(getLocation() != null){
            textViewLatitude = (TextView) activity.findViewById(R.id.latitudeText);
            textViewLatitude.setText(Double.toString(getLocation().getLatitude()));
            textViewLongitude = (TextView) activity.findViewById(R.id.longitudeText);
            textViewLongitude.setText(Double.toString(getLocation().getLongitude()));
        }

        // Get user from Firebase

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    Map<String, Object> locationHashMap = new HashMap<>();

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            // Loop through users

                            HashMap<String,Object>  dateMin  = new HashMap<String, Object>();
                            HashMap<String,Object>  minLoc   = new HashMap<String,Object>();

                            Date now = new Date();
                            userMap.put("lastUpdated", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(now));
                            userMap.put("lastUpdatedDay",new SimpleDateFormat("yyyy.MM.dd").format(now));
                            userMap.put("lastUpdatedMin", new SimpleDateFormat("HH.mm").format(now));

                            minLoc.put(new SimpleDateFormat("HH.mm").format(now), geoPointLocation);
                            dateMin.put(new SimpleDateFormat("yyyy.MM.dd").format(now), minLoc);

                            userMap.put("location",dateMin);

                            db.collection("users").document(user.getUid()).set(userMap, SetOptions.merge());

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
