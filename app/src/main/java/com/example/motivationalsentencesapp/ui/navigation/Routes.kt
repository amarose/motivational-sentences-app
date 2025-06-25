package com.example.motivationalsentencesapp.ui.navigation

object Routes {
    const val ONBOARDING = "onboarding"
    const val MAIN = "main"

    object Home {
        const val ROUTE_BASE = "home"
        const val ARG_QUOTE_TEXT = "quoteText"
        const val ARG_QUOTE_AUTHOR = "quoteAuthor"
        val ROUTE_TEMPLATE = "$ROUTE_BASE?$ARG_QUOTE_TEXT={$ARG_QUOTE_TEXT}&$ARG_QUOTE_AUTHOR={$ARG_QUOTE_AUTHOR}"

        fun withArgs(text: String, author: String): String {
            return "$ROUTE_BASE?$ARG_QUOTE_TEXT=$text&$ARG_QUOTE_AUTHOR=$author"
        }
    }

    object Favorites {
        const val ROUTE = "favorites"
    }

    object Archive {
        const val ROUTE = "archive"
    }

    object Settings {
        const val ROUTE = "settings"
    }
}
