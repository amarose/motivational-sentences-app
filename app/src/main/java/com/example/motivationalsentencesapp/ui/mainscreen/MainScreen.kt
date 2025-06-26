package com.example.motivationalsentencesapp.ui.mainscreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
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
import com.example.motivationalsentencesapp.R
import com.example.motivationalsentencesapp.ui.main.MainViewModel
import com.example.motivationalsentencesapp.ui.navigation.Routes
import org.koin.androidx.compose.koinViewModel
import com.example.motivationalsentencesapp.ui.background.BackgroundScreen
import com.example.motivationalsentencesapp.ui.settings.SettingsScreen

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val uiState by mainViewModel.uiState.collectAsState()
    val context = LocalContext.current

    val batterySettingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        mainViewModel.checkBatteryOptimizations()
    }

    if (uiState.showBatteryOptimizationDialog) {
        AlertDialog(
            onDismissRequest = { mainViewModel.onBatteryOptimizationDialogDismissed() },
            title = { Text(
                textAlign = TextAlign.Center,
                text = stringResource(R.string.battery_optimization_title)) },
            text = { Text(
                textAlign = TextAlign.Center,
                text = stringResource(R.string.battery_optimization_description)) },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { mainViewModel.onBatteryOptimizationDialogDismissed() }) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(Modifier.width(16.dp))
                    Button(
                        onClick = {
                            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                                data = "package:${context.packageName}".toUri()
                            }
                            batterySettingsLauncher.launch(intent)
                            mainViewModel.onBatteryOptimizationDialogDismissed()
                        }
                    ) {
                        Text(stringResource(R.string.go_to_settings))
                    }
                }
            }
        )
    }
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Archive,
        BottomNavItem.Background,
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
                        label = { Text(screen.title, maxLines = 1) },
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
        NavHost(
            navController,
            startDestination = Routes.Home.ROUTE_BASE,
            Modifier.padding(innerPadding)
        ) {
            composable(
                route = Routes.Home.ROUTE_TEMPLATE,
                arguments = listOf(
                    navArgument(Routes.Home.ARG_QUOTE_ID) { type = NavType.IntType; defaultValue = -1 },
                    navArgument(Routes.Home.ARG_QUOTE_TEXT) { type = NavType.StringType; nullable = true },
                    navArgument(Routes.Home.ARG_QUOTE_AUTHOR) { type = NavType.StringType; nullable = true },
                    navArgument(Routes.Home.ARG_IS_FAVORITE) { type = NavType.BoolType; defaultValue = false }
                )
            ) {
                HomeScreen()
            }
            composable(Routes.Favorites.ROUTE) { FavoritesScreen() }
            composable(Routes.Archive.ROUTE) { ArchiveScreen() }
            composable(Routes.Settings.ROUTE) { SettingsScreen() }
            composable(Routes.Background.ROUTE) { BackgroundScreen() }
        }
    }
}
