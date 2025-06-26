package com.example.motivationalsentencesapp.data.model

data class Quote(
    val id: Int,
    val text: String,
    val isFavorite: Boolean = false
)
