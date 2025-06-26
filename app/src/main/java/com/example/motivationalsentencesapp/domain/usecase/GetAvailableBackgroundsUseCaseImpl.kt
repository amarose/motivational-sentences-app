package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.R
import com.example.motivationalsentencesapp.data.model.Background

class GetAvailableBackgroundsUseCaseImpl : GetAvailableBackgroundsUseCase {
    override fun invoke(): List<Background> {
        return listOf(
            Background(id = 1, resourceId = R.drawable.img1, name = "Abstract 1"),
            Background(id = 2, resourceId = R.drawable.img2, name = "Abstract 2"),
            Background(id = 3, resourceId = R.drawable.img3, name = "Abstract 3"),
            Background(id = 4, resourceId = R.drawable.img4, name = "Abstract 4"),
            Background(id = 5, resourceId = R.drawable.img5, name = "Abstract 5"),
            Background(id = 6, resourceId = R.drawable.img6, name = "Abstract 6"),
            Background(id = 7, resourceId = R.drawable.img7, name = "Abstract 7"),
            Background(id = 8, resourceId = R.drawable.img8, name = "Abstract 8"),
            Background(id = 9, resourceId = R.drawable.img9, name = "Abstract 9"),
            Background(id = 10, resourceId = R.drawable.img10, name = "Abstract 10"),
            Background(id = 11, resourceId = R.drawable.img11, name = "Abstract 11"),
            Background(id = 12, resourceId = R.drawable.img12, name = "Abstract 12"),
            Background(id = 13, resourceId = R.drawable.img13, name = "Abstract 13"),
            Background(id = 14, resourceId = R.drawable.img14, name = "Abstract 14"),
            Background(id = 15, resourceId = R.drawable.img15, name = "Abstract 15"),
            Background(id = 16, resourceId = R.drawable.img16, name = "Abstract 16"),
            Background(id = 17, resourceId = R.drawable.img17, name = "Abstract 17"),
            Background(id = 18, resourceId = R.drawable.img18, name = "Abstract 18"),
            Background(id = 19, resourceId = R.drawable.img19, name = "Abstract 19"),
            Background(id = 20, resourceId = R.drawable.img20, name = "Abstract 20"),
            Background(id = 21, resourceId = R.drawable.img21, name = "Abstract 21"),
            Background(id = 22, resourceId = R.drawable.img22, name = "Abstract 22"),
            Background(id = 23, resourceId = R.drawable.img23, name = "Abstract 23"),
            Background(id = 24, resourceId = R.drawable.img24, name = "Abstract 24"),
            Background(id = 25, resourceId = R.drawable.img25, name = "Abstract 25"),
            Background(id = 26, resourceId = R.drawable.img26, name = "Abstract 26"),
            Background(id = 27, resourceId = R.drawable.img27, name = "Abstract 27"),
            Background(id = 28, resourceId = R.drawable.img28, name = "Abstract 28"),
            Background(id = 29, resourceId = R.drawable.img29, name = "Abstract 29"),
            Background(id = 30, resourceId = R.drawable.img30, name = "Abstract 30"),
        )
    }
}
