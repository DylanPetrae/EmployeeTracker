package com.example.android.employeetracker;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.employeetracker.AdminMainActivity;
import com.example.android.employeetracker.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;


public class EmployeeListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //private TextView textViewEmployeeList;

    private ListView listViewEmployeeList;

    boolean clickable = false;
    int depth;
    String employeeID;
    String employeeDay;

    private Button buttonControlPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_employee);
        depth=0;

        //textViewEmployeeList = (TextView) findViewById(R.id.textViewEmployeeList);
        listViewEmployeeList = (ListView) findViewById(R.id.listView1);
        listViewEmployeeList.setOnItemClickListener(this);


        populateUsers();
        

        buttonControlPanel = (Button) findViewById(R.id.control_panel_button);

    }

    public void populateUsers()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document("list");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String,Object> data = document.getData();
                    ArrayList<String> array = new ArrayList<String>();

                    StringBuilder temp = new StringBuilder();
                    for (String key : data.keySet()) {
                        temp.append(key);
                        temp.append(" \n");
                        temp.append((String) data.get(key));

                        array.add(temp.toString());
                        temp.setLength(0);

                    }
                    setAdapter(array.toArray(new String[array.size()]));


                } else {
                }
            }
        });
    }

    public void populateDates(String userID)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String,Object> data = document.getData();
                    ArrayList<String> validDays = new ArrayList<String>();

                    if (!data.containsKey("location")) {
                        String[] array = new String[1];
                        array[0] = "No valid days";
                        setAdapter(array);
                        clickable = false;
                        return;
                    }

                    Map<String,Object> location = (Map<String,Object>) data.get("location");

                    for (String key : location.keySet())
                    {
                        validDays.add(key);
                    }

                    setAdapter(validDays.toArray(new String[validDays.size()]));
                } else {
                }
            }
        });
    }

    public void populateTimes(String userID,String date) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final String day = date;


        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String, Object> data = document.getData();
                    ArrayList<String> validTimes = new ArrayList<String>();

                    if (!data.containsKey("location"))
                        return;

                    Map<String, Object> location = (Map<String, Object>) data.get("location");

                    if (!location.containsKey(day))
                        return;

                    Map<String, Object> locationDay = (Map<String, Object>) location.get(day);

                    for (String key : locationDay.keySet()) {
                        validTimes.add(key);
                    }
                    setAdapter(validTimes.toArray(new String[validTimes.size()]));
                } else {
                }
            }
        });
    }

    public void getGeoPoint(String userID, String d, String t) {
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
                    Map<String, Object> data = document.getData();

                    if (!data.containsKey("location"))
                        return;

                    Map<String, Object> location = (Map<String, Object>) data.get("location");

                    if (!location.containsKey(day))
                        return;

                    Map<String, Object> locationDay = (Map<String, Object>) location.get(day);

                    if (!locationDay.containsKey(time))
                        return;

                    GeoPoint locationDayTime = (GeoPoint) locationDay.get(time);
                    DatabaseSimple.myGeoPoint = locationDayTime;

                    startMapsActivity();

                } else {
                }
            }
        });
    }

    public void startMapsActivity()
    {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


    public void setAdapter(String[] array)
    {
        listViewEmployeeList.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                array));
        listViewEmployeeList.invalidateViews();

        clickable = true;

        //listViewEmployeeList.updateViewLayout();
    }

    public void gotoControlPanel(View view) {

        Intent i = new Intent(this, AdminMainActivity.class);
        startActivity(i);
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        if(!clickable)
            return;
        clickable= false;

        Adapter myAdapter = listViewEmployeeList.getAdapter();

        if(depth ==0)
        {


            String item = (String) myAdapter.getItem(position);
            employeeID = item.substring(0,item.indexOf(' '));

            populateDates(employeeID);

            //no dates avalible for user
        }

        if(depth == 1)
        {
            employeeDay = (String) myAdapter.getItem(position);

            populateTimes(employeeID, employeeDay);
        }

        if(depth >= 2)
        {
            String time = (String) myAdapter.getItem(position);

            getGeoPoint(employeeID,employeeDay,time);
        }

        depth++;





    }
}