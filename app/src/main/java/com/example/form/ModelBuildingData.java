package com.example.form;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "model_building_data")
public class ModelBuildingData {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String email;
    private String dob;
    private String timeOfBirth;
    private String locationOfBirth;
    private String bloodGroup;
    private String sex;
    private String height;
    private String ethnicity;
    private String eyeColor;

    // Constructor including email and other fields
    public ModelBuildingData( String email, String dob, String timeOfBirth, String locationOfBirth,
                             String bloodGroup, String sex, String height, String ethnicity, String eyeColor) {
        this.email = email;
        this.dob = dob;
        this.timeOfBirth = timeOfBirth;
        this.locationOfBirth = locationOfBirth;
        this.bloodGroup = bloodGroup;
        this.sex = sex;
        this.height = height;
        this.ethnicity = ethnicity;
        this.eyeColor = eyeColor;
    }

    // Getters and Setters for email and other fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getTimeOfBirth() {
        return timeOfBirth;
    }

    public void setTimeOfBirth(String timeOfBirth) {
        this.timeOfBirth = timeOfBirth;
    }

    public String getLocationOfBirth() {
        return locationOfBirth;
    }

    public void setLocationOfBirth(String locationOfBirth) {
        this.locationOfBirth = locationOfBirth;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }
}
