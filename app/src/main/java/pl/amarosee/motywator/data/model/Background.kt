package pl.amarosee.motywator.data.model

import androidx.annotation.DrawableRes

data class Background(
    val id: Int,
    @DrawableRes val resourceId: Int,
    val name: String
)
