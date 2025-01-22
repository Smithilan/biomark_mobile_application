package com.example.form;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;

@Dao
public interface ModelBuildingDao {

    @Query("SELECT * FROM model_building_data LIMIT 1")
    ModelBuildingData getModelBuildingData();

    @Query("SELECT * FROM model_building_data WHERE email = :email LIMIT 1")
    ModelBuildingData getModelBuildingDataByEmail(String email);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModelBuildingData(ModelBuildingData modelBuildingData);

    @Delete
    void delete(ModelBuildingData modelBuildingData);

    // Clears the profile data by setting certain fields to NULL for the given email
    @Query("UPDATE model_building_data SET dob = NULL, timeOfBirth = NULL, locationOfBirth = NULL, " +
            "bloodGroup = NULL, sex = NULL, height = NULL, ethnicity = NULL, eyeColor = NULL WHERE email = :email")
    void clearModelBuildingData(String email);
}

