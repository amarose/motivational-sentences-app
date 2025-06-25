package com.example.motivationalsentencesapp.ui.mainscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.motivationalsentencesapp.ui.archive.ArchiveScreen
import com.example.motivationalsentencesapp.ui.favorites.FavoritesScreen
import com.example.motivationalsentencesapp.ui.home.HomeScreen
import com.example.motivationalsentencesapp.ui.navigation.BottomNavItem
import com.example.motivationalsentencesapp.ui.navigation.Routes
import com.example.motivationalsentencesapp.ui.settings.SettingsScreen

@Composable
fun MainScreen(quoteText: String?, quoteAuthor: String?) {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Archive,
        BottomNavItem.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        val startDestination = if (quoteText != null && quoteAuthor != null) {
            Routes.Home.withArgs(quoteText, quoteAuthor)
        } else {
            Routes.Home.ROUTE_BASE
        }

        NavHost(
            navController, 
            startDestination = startDestination, 
            Modifier.padding(innerPadding)
        ) {
            composable(
                route = Routes.Home.ROUTE_TEMPLATE,
                arguments = listOf(
                    navArgument(Routes.Home.ARG_QUOTE_TEXT) { type = NavType.StringType; nullable = true },
                    navArgument(Routes.Home.ARG_QUOTE_AUTHOR) { type = NavType.StringType; nullable = true }
                )
            ) { HomeScreen() }
            composable(Routes.Favorites.ROUTE) { FavoritesScreen() }
            composable(Routes.Archive.ROUTE) { ArchiveScreen() }
            composable(Routes.Settings.ROUTE) { SettingsScreen() }
        }
    }
}
