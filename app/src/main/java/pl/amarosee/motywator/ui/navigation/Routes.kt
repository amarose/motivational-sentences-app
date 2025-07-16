package pl.amarosee.motywator.ui.navigation

object Routes {
    const val ONBOARDING = "onboarding"
    const val MAIN = "main"

    object Home {
        const val ROUTE_BASE = "home"
        const val ARG_QUOTE_ID = "quoteId"
        const val ARG_QUOTE_TEXT = "quoteText"
        const val ARG_IS_FAVORITE = "isFavorite"
        val ROUTE_TEMPLATE = "$ROUTE_BASE?$ARG_QUOTE_ID={$ARG_QUOTE_ID}&$ARG_QUOTE_TEXT={$ARG_QUOTE_TEXT}&$ARG_IS_FAVORITE={$ARG_IS_FAVORITE}"

        fun withArgs(id: Int, text: String, isFavorite: Boolean): String {
            return "$ROUTE_BASE?$ARG_QUOTE_ID=$id&$ARG_QUOTE_TEXT=$text&$ARG_IS_FAVORITE=$isFavorite"
        }
    }

    object Favorites {
        const val ROUTE = "favorites"
    }

    object Archive {
        const val ROUTE = "archive"
    }

    object Background {
        const val ROUTE = "background"
    }

    object Settings {
        const val ROUTE = "settings"
    }
}
