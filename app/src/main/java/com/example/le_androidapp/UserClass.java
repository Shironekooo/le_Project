package com.example.le_androidapp;

public class UserClass {

    private String firstName;
    private String middleName;
    private String lastName;
    private String userAge;
    //private String userGender;
    private String contactNumber;
    private String userEmail;
    private String userPass;

    private String dataImage;

    /*public Integer getUserId() {
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

    /*public String getUserGender() {
        return userGender;
    }*/

    public String getContactNumber() {
        return contactNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPass() {
        return userPass;
    }

    public String getDataImage() {
        return dataImage;
    }

    public UserClass(String firstName, String middleName, String lastName, String userAge, String contactNumber, String userEmail, String userPass, String dataImage) {
       // this.userId = userId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.userAge = userAge;
       // this.userGender = userGender;
        this.contactNumber = contactNumber;
        this.userEmail = userEmail;
        this.userPass = userPass;
        this.dataImage = dataImage;
    }
}
