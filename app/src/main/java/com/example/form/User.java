package com.example.form;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String fullName;
    private String dob;
    private String mothersMaidenName;
    private String bestFriendName;
    private String childhoodPetName;
    private String phoneNumber;
    private String email;
    private String password;

    public User(String fullName, String dob, String mothersMaidenName, String bestFriendName,
                String childhoodPetName, String phoneNumber, String email, String password) {
        this.fullName = fullName;
        this.dob = dob;
        this.mothersMaidenName = mothersMaidenName;
        this.bestFriendName = bestFriendName;
        this.childhoodPetName = childhoodPetName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    // Getter and Setter methods
    public int getId() { return id; }

    public String getFullName() { return fullName; }

    public String getDob() { return dob; }

    public String getMothersMaidenName() { return mothersMaidenName; }

    public String getBestFriendName() { return bestFriendName; }

    public String getChildhoodPetName() { return childhoodPetName; }

    public String getPhoneNumber() { return phoneNumber; }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public void setId(int id) { this.id = id; }

    public void setPassword(String password) { this.password = password; }

    public void setEmail(String email){this.email = email;}
}