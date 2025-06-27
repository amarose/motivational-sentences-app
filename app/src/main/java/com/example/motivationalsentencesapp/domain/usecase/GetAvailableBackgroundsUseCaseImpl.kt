package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.R
import com.example.motivationalsentencesapp.data.model.Background

class GetAvailableBackgroundsUseCaseImpl : GetAvailableBackgroundsUseCase {
    override fun invoke(): List<Background> {
        return listOf(
            Background(id = 1, resourceId = R.mipmap.img1, name = "Abstract 1"),
            Background(id = 2, resourceId = R.mipmap.img2, name = "Abstract 2"),
            Background(id = 3, resourceId = R.mipmap.img3, name = "Abstract 3"),
            Background(id = 4, resourceId = R.mipmap.img4, name = "Abstract 4"),
            Background(id = 5, resourceId = R.mipmap.img5, name = "Abstract 5"),
            Background(id = 6, resourceId = R.mipmap.img6, name = "Abstract 6"),
            Background(id = 7, resourceId = R.mipmap.img7, name = "Abstract 7"),
            Background(id = 8, resourceId = R.mipmap.img8, name = "Abstract 8"),
            Background(id = 9, resourceId = R.mipmap.img9, name = "Abstract 9"),
            Background(id = 10, resourceId = R.mipmap.img10, name = "Abstract 10"),
            Background(id = 11, resourceId = R.mipmap.img11, name = "Abstract 11"),
            Background(id = 12, resourceId = R.mipmap.img12, name = "Abstract 12"),
//            Background(id = 13, resourceId = R.mipmap.img13, name = "Abstract 13"),
            Background(id = 14, resourceId = R.mipmap.img14, name = "Abstract 14"),
            Background(id = 15, resourceId = R.mipmap.img15, name = "Abstract 15"),
            Background(id = 16, resourceId = R.mipmap.img16, name = "Abstract 16"),
            Background(id = 17, resourceId = R.mipmap.img17, name = "Abstract 17"),
            Background(id = 18, resourceId = R.mipmap.img18, name = "Abstract 18"),
            Background(id = 19, resourceId = R.mipmap.img19, name = "Abstract 19"),
            Background(id = 20, resourceId = R.mipmap.img20, name = "Abstract 20"),
            Background(id = 21, resourceId = R.mipmap.img21, name = "Abstract 21"),
            Background(id = 22, resourceId = R.mipmap.img22, name = "Abstract 22"),
            Background(id = 23, resourceId = R.mipmap.img23, name = "Abstract 23"),
            Background(id = 24, resourceId = R.mipmap.img24, name = "Abstract 24"),
            Background(id = 25, resourceId = R.mipmap.img25, name = "Abstract 25"),
            Background(id = 26, resourceId = R.mipmap.img26, name = "Abstract 26"),
            Background(id = 27, resourceId = R.mipmap.img27, name = "Abstract 27"),
            Background(id = 28, resourceId = R.mipmap.img28, name = "Abstract 28"),
            Background(id = 29, resourceId = R.mipmap.img29, name = "Abstract 29"),
//            Background(id = 30, resourceId = R.mipmap.img30, name = "Abstract 30"),
        )
    }
}
