package pl.amarosee.motywator.data.model

import androidx.annotation.DrawableRes

data class Background(
    val id: Int,
    @param:DrawableRes val resourceId: Int,
    val name: String
)
