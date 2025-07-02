package com.example.motivationalsentencesapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class Quote(
    @PrimaryKey val id: Int,
    val text: String,
    val isFavorite: Boolean = false,
    val wasDisplayed: Boolean = false
)
