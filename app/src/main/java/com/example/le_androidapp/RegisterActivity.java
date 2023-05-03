package com.example.le_androidapp;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.example.le_androidapp.tables.History;
import com.example.le_androidapp.tables.ReadData;
import com.example.le_androidapp.tables.UserClass;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    ImageView uploadImage;
    String imageURL, selectedGender;
    Button addbtn;
    EditText firstName, middleName, lastName, userAge, contactNumber;
    RadioGroup genderRadioGroup;

    RadioButton radioButtonFemale, radioButtonMale;
    Uri uri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        uploadImage = findViewById(R.id.uploadImg);
        addbtn = findViewById(R.id.addbtn);
        firstName = findViewById(R.id.userFirst);
        middleName = findViewById(R.id.userMiddle);
        lastName = findViewById(R.id.userLast);
        userAge = findViewById(R.id.userAge);
        contactNumber = findViewById(R.id.contactNumber);
        radioButtonFemale = findViewById(R.id.female);
        radioButtonMale = findViewById(R.id.male);

        genderRadioGroup = findViewById(R.id.groupGender);
        genderRadioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            switch (checkedId) {
                case R.id.female:
                    // Female option is selected
                    selectedGender = "Female";
                    break;
                case R.id.male:
                    // Male option is selected
                    selectedGender = "Male";
                    break;
                default:
                    // No option is selected
                    break;
            }
        });


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        uri = data.getData();
                        uploadImage.setImageURI(uri);

                    } else {
                        Toast.makeText(RegisterActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        uploadImage.setOnClickListener(view -> {
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            photoPicker.setType("image/*");
            activityResultLauncher.launch(photoPicker);
        });


        addbtn.setOnClickListener(v -> {
            saveData();
            Intent intent = new Intent(RegisterActivity.this, PrimaryActivity.class);
            startActivity(intent);
        });

    }

    public void saveData() {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {

            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isComplete()) ;
            Uri urlImage = uriTask.getResult();
            imageURL = urlImage.toString();
            uploadData();
            dialog.dismiss();
        }).addOnFailureListener(e -> dialog.dismiss());
    }

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference("User Data");
    DatabaseReference readDataRef = database.getReference("Read Data");
    DatabaseReference historyRef = database.getReference("History");

    public void uploadData() {

        String userId = usersRef.push().getKey();
        String firstN = firstName.getText().toString();
        String middleN = middleName.getText().toString();
        String lastN = lastName.getText().toString();
        String age = userAge.getText().toString();
        String contactNo = contactNumber.getText().toString();

        UserClass newUser = new UserClass(userId, firstN, middleN, lastN, age, selectedGender, contactNo, imageURL);

        // Save to User Data reference
        usersRef.child(userId).setValue(newUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Saved to User Data", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, Objects.requireNonNull(e.getMessage()), Toast.LENGTH_SHORT).show());

        // Save to ReadData reference
        ReadData newReadData = new ReadData(userId);
        readDataRef.child(userId).setValue(newReadData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                    }
                }).addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());

        // Save to History reference
        History newHistory = new History(userId);
        historyRef.child(userId).setValue(newHistory)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                    }
                }).addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());

    }
}