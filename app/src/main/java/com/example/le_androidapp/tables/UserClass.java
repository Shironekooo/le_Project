package com.example.le_androidapp.tables;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserClass {

    /*public String userId;*/
    public String firstName;
    public String middleName;
    public String lastName;
    public String userAge;

    public String userGender;
    public String contactNumber;

    public String dataImage;

    public UserClass() {
        // Required empty constructor for Firebase Realtime Database.
    }

    public UserClass(String userGender, String firstN, String middleN, String lastN, String age, String gender, String contactNo, String dataImage) {
    }

   /* public String getUserId() {
        return userId;
    }*/

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserAge() {
        return userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getDataImage() {
        return dataImage;
    }

    public UserClass(String firstName, String middleName, String lastName, String userAge, String userGender, String contactNumber, String dataImage) {
       // this.userId = userId();
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.userAge = userAge;
        this.userGender = userGender;
        this.contactNumber = contactNumber;
        this.dataImage = dataImage;
    }


    /*private String userId() {
        // create a reference to the Firebase Realtime Database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        // push a new child node to the "users" node in the database
        DatabaseReference userRef = reference.child("User Data").push();

        // return the unique key generated by the push() method
        return userRef.getKey();
    }*/
}
