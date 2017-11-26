package com.example.android.employeetracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.employeetracker.AdminMainActivity;
import com.example.android.employeetracker.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class ViewPhotoActivity extends AppCompatActivity {

    private EditText editTextEmployeeId;

    private ImageView imageViewEmployeePhoto;

    private Button buttonControlPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        editTextEmployeeId = (EditText) findViewById(R.id.employee_id);

        imageViewEmployeePhoto = (ImageView) findViewById(R.id.imageViewEmployeePhoto);

        buttonControlPanel = (Button) findViewById(R.id.control_panel_button);

    }


    public void downloadPhoto(View view){
        String employeeId = editTextEmployeeId.getText().toString();

        if(employeeId.matches("")) {
            Toast.makeText(this, "Please enter an employee ID.", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            FirebaseStorage storage = FirebaseStorage.getInstance();

            // Reference to an image file in Firebase Storage
            StorageReference storageRef = storage.getReference();

            // Create a child reference
            // imagesRef now points to "images"
            StorageReference imagesRef = storageRef.child("images/");

            StorageReference employeePhotoRef = imagesRef.child(employeeId + ".jpg");

            // Load the image using Glide
            Glide.with(this /* context */)
                    .using(new FirebaseImageLoader())
                    .load(employeePhotoRef)
                    .into(imageViewEmployeePhoto);
        }
    }

    public void gotoControlPanel ( View view ){

        Intent i = new Intent(this, AdminMainActivity.class);
        startActivity(i);
    }
}