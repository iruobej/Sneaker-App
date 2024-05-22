package com.example.sneakerspot.ui.theme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import androidx.lifecycle.MutableLiveData

class TrainerViewModel(application: Application) : AndroidViewModel(application){
    private val repository: TrainerRepository
    val allTrainers: LiveData<List<Trainer>>
    init {
        val trainerDao = TrainerDatabase.getDatabase(application, viewModelScope).trainerDao()
        repository = TrainerRepository(trainerDao)
        allTrainers = repository.allTrainers

        //Pre populating the database
        viewModelScope.launch {
            TrainerDatabase.populateDatabase(trainerDao)
        }
    }

    fun insert(trainer: Trainer) = viewModelScope.launch {
        repository.insert(trainer)
    }
}
