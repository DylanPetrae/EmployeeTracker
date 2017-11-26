package com.example.android.employeetracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;


public class EmployeeListActivity extends AppCompatActivity {

    private TextView textViewEmployeeList;

    private Button buttonControlPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_employee);

        textViewEmployeeList = (TextView) findViewById(R.id.textViewEmployeeList);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document("list");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Map<String,Object> data = document.getData();
                    for (String key : data.keySet()) {
                        textViewEmployeeList.append("\n");
                        textViewEmployeeList.append(key);
                        textViewEmployeeList.append("\n");
                        textViewEmployeeList.append((String) data.get(key));
                        textViewEmployeeList.append("\n");
                    }

                } else {
                }
            }
        });

        buttonControlPanel = (Button) findViewById(R.id.control_panel_button);

    }

    public void gotoControlPanel(View view) {

        Intent i = new Intent(this, AdminMainActivity.class);
        startActivity(i);
    }
}