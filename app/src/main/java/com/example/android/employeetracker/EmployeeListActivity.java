package com.example.android.employeetracker;

import android.content.Intent;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class EmployeeListActivity extends AppCompatActivity {

    private TextView textViewEmployeeList;

    private Button buttonControlPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_employee);

        textViewEmployeeList = (TextView) findViewById(R.id.textViewEmployeeList);

        buttonControlPanel = (Button) findViewById(R.id.control_panel_button);

    }

    public void gotoControlPanel(View view) {

        Intent i = new Intent(this, AdminMainActivity.class);
        startActivity(i);
    }
}