package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.R
import com.example.motivationalsentencesapp.data.model.Background

class GetAvailableBackgroundsUseCaseImpl : GetAvailableBackgroundsUseCase {
    override fun invoke(): List<Background> {
        return listOf(
            Background(id = 1, resourceId = R.drawable.background_1, name = "Abstract 1"),
            Background(id = 2, resourceId = R.drawable.background_2, name = "Abstract 2"),
//            Background(id = 3, resourceId = R.drawable.background_3, name = "Abstract 3"),
//            Background(id = 4, resourceId = R.drawable.background_4, name = "Abstract 4"),
//            Background(id = 5, resourceId = R.drawable.background_5, name = "Abstract 5"),
        )
    }
}
