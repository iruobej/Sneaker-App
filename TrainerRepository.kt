package com.example.sneakerspot.ui.theme

import androidx.lifecycle.LiveData
import com.example.sneakerspot.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.util.Log

class TrainerRepository(private val trainerDao: TrainerDao) {
    val allTrainers: LiveData<List<Trainer>> = trainerDao.getAllTrainers()

    suspend fun insert(trainer: Trainer) {
        Log.d("TrainerRepository", "Inserting trainer: ${trainer.name}")
        trainerDao.insertTrainer(trainer)
    }
    fun populateDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            // Add sample trainers.
            val trainer1 = Trainer(name = "New Balance 2002r Navy", imageRes = R.drawable.new_balance_2002r_navy, price = "£110")
            val trainer2 = Trainer(name = "Jordan 4 Bred", imageRes = R.drawable.jordan_4_bred, price = "£250")
            val trainer3 = Trainer(name = "Jordan 4 Black Cat", imageRes = R.drawable.jordan_4_black_cat, price = "£210")
            trainerDao.insertTrainer(trainer1)
            trainerDao.insertTrainer(trainer2)
            trainerDao.insertTrainer(trainer3)
        }
    }
}
