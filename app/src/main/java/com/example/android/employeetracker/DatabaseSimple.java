package com.example.android.employeetracker;

import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import junit.framework.Test;

import java.io.Console;
import java.util.*;

/**
 * Created by scottsuarez on 11/25/17.
 */

class DatabaseSimple {

    HashMap<String,String> currentUsers;
    ArrayList<String> validDays;
    ArrayList<String> validTimes;
    GeoPoint geoDude;
    GeoPoint mostRecentLocation;
    public static GeoPoint myGeoPoint;


    DatabaseSimple()
    {

    }


    public void getListOfUsers()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        currentUsers = new HashMap<String,String>();
        DocumentReference docRef = db.collection("users").document("list");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String,Object> data = document.getData();
                    for (String key : data.keySet())
                    {
                        currentUsers.put(key, (String)data.get(key));
                    }
                } else {
                }
            }
        });

    }

    public void getListOfValidDays(String userID)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        validDays = new ArrayList<String>();

        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String,Object> data = document.getData();

                    if (!data.containsKey("location"))
                        return;

                    Map<String,Object> location = (Map<String,Object>) data.get("location");

                    for (String key : location.keySet())
                    {
                        validDays.add(key);
                    }
                } else {
                }
            }
        });

    }

    public void getListOfValidTimes(String userID,String d)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final String day = d;
        validTimes = new ArrayList<String>();

        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String,Object> data = document.getData();

                    if (!data.containsKey("location"))
                        return;

                    Map<String,Object> location = (Map<String,Object>) data.get("location");

                    if (!location.containsKey(day))
                        return;

                    Map<String,Object> locationDay = (Map<String,Object>) location.get(day);

                    for (String key : locationDay.keySet())
                    {
                        validTimes.add(key);
                    }
                } else {
                }
            }
        });

    }


    public void getGeoPoint(String userID, String d, String t)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final String day = d;
        final String time = t;
        final ArrayList<String> validDays = new ArrayList<String>();

        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String,Object> data = document.getData();

                    if (!data.containsKey("location"))
                        return;

                    Map<String,Object> location = (Map<String,Object>) data.get("location");

                    if (!location.containsKey(day))
                        return;

                    Map<String,Object> locationDay = (Map<String,Object>) location.get(day);

                    if (!locationDay.containsKey(time))
                        return;

                    ArrayList<String> locationDayTime = (ArrayList<String>) location.get(time);

                    geoDude = new GeoPoint(Double.parseDouble(locationDayTime.get(0)),Double.parseDouble(locationDayTime.get(1)));

                } else {
                }
            }
        });
    }

    public void getMostRecentLocation(String userID)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final ArrayList<String> validDays = new ArrayList<String>();

        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String,Object> data = document.getData();

                    if (!data.containsKey("lastUpdatedDay"))
                        return;

                    if (!data.containsKey("lastUpdatedMin"))
                        return;

                    String day = (String) data.get("lastUpdatedDay");
                    String time = (String) data.get("lastUpdatedMin");

                    if (!data.containsKey("location"))
                        return;

                    Map<String,Object> location = (Map<String,Object>) data.get("location");

                    if (!location.containsKey(day))
                        return;

                    Map<String,Object> locationDay = (Map<String,Object>) location.get(day);

                    if (!locationDay.containsKey(time))
                        return;

                    ArrayList<String> locationDayTime = (ArrayList<String>) location.get(time);

                    mostRecentLocation = new GeoPoint(Double.parseDouble(locationDayTime.get(0)),Double.parseDouble(locationDayTime.get(1)));

                } else {
                }
            }
        });
    }






}
