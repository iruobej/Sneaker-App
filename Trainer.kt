package com.example.sneakerspot.ui.theme

import androidx.room.PrimaryKey
import androidx.room.Entity
import com.example.sneakerspot.R

@Entity(tableName = "trainers")
data class Trainer (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imageRes: Int,
    val price: String
)
