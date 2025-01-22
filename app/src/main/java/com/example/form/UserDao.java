package com.example.form;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM User WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("UPDATE User SET fullName = NULL, dob = NULL, mothersMaidenName = NULL, bestFriendName = NULL, " +
            "childhoodPetName = NULL, phoneNumber = NULL, password = NULL WHERE email = :email")
    void clearUserProfileData(String email);

    // Method for verifying recovery information
    @Query("SELECT * FROM User WHERE fullName = :fullName AND dob = :dob AND " +
            "(mothersMaidenName = :mothersMaidenName OR bestFriendName = :bestFriendName OR " +
            "childhoodPetName = :childhoodPetName OR phoneNumber = :phoneNumber) LIMIT 1")
    User verifyRecoveryInfo(String fullName, String dob, String mothersMaidenName,
                            String bestFriendName, String childhoodPetName, String phoneNumber);
}
