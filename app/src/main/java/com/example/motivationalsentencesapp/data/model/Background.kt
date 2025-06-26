package com.example.motivationalsentencesapp.data.model

import androidx.annotation.DrawableRes

data class Background(
    val id: Int,
    @DrawableRes val resourceId: Int,
    val name: String
)
