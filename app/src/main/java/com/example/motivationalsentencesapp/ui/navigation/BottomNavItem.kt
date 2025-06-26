package com.example.motivationalsentencesapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = Routes.Home.ROUTE_BASE,
        title = "Start",
        icon = Icons.Default.Home
    )

    object Favorites : BottomNavItem(
        route = Routes.Favorites.ROUTE,
        title = "Ulubione",
        icon = Icons.Default.Favorite
    )

    object Archive : BottomNavItem(
        route = Routes.Archive.ROUTE,
        title = "Archiwum",
        icon = Icons.AutoMirrored.Filled.List
    )

    object Background : BottomNavItem(
        route = Routes.Background.ROUTE,
        title = "Tło",
        icon = Icons.Default.Image
    )

    object Settings : BottomNavItem(
        route = Routes.Settings.ROUTE,
        title = "Ustawienia",
        icon = Icons.Default.Settings
    )
}
