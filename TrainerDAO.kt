package com.example.sneakerspot.ui.theme

import android.util.Log
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Dao
import androidx.lifecycle.LiveData

@Dao
interface TrainerDao {
    @Query("SELECT * FROM trainers")
    fun getAllTrainers(): LiveData<List<Trainer>>
    @Insert
    suspend fun insertTrainer(trainer: Trainer)
    @Query("DELETE FROM trainers")
    suspend fun deleteAll()
}
