package com.example.motivationalsentencesapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.motivationalsentencesapp.ui.archive.ArchiveScreen
import com.example.motivationalsentencesapp.ui.background.BackgroundScreen
import com.example.motivationalsentencesapp.ui.favorites.FavoritesScreen
import com.example.motivationalsentencesapp.ui.home.HomeScreen
import com.example.motivationalsentencesapp.ui.main.MainViewModel
import com.example.motivationalsentencesapp.ui.navigation.BottomNavItem
import com.example.motivationalsentencesapp.ui.navigation.Routes
import com.example.motivationalsentencesapp.ui.onboarding.OnboardingScreen
import com.example.motivationalsentencesapp.ui.settings.SettingsScreen
import com.example.motivationalsentencesapp.ui.theme.MotivationalSentencesAppTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.motivationalsentencesapp.ui.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModel()

        override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            homeViewModel.uiState.value.isLoading
        }

        val quoteId = intent.getIntExtra(EXTRA_QUOTE_ID, -1)
        val quoteText = intent.getStringExtra(EXTRA_QUOTE_TEXT)
        val isFavorite = intent.getBooleanExtra(EXTRA_IS_FAVORITE, false)

        setContent {
            MotivationalSentencesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                                        MotivationalApp(
                        homeViewModel = homeViewModel,
                        quoteId = if (quoteId != -1) quoteId else null,
                        quoteText = quoteText,
                        isFavorite = isFavorite
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        recreate()
    }

    companion object {
        const val EXTRA_QUOTE_ID = "EXTRA_QUOTE_ID"
        const val EXTRA_QUOTE_TEXT = "EXTRA_QUOTE_TEXT"
        const val EXTRA_IS_FAVORITE = "EXTRA_IS_FAVORITE"
    }
}

@Composable
fun MotivationalApp(
    viewModel: MainViewModel = koinViewModel(),
    homeViewModel: HomeViewModel,
    quoteId: Int?,
    quoteText: String?,
    isFavorite: Boolean
) {
    val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsState()
    val navController = rememberNavController()

    val view = LocalView.current
    if (!view.isInEditMode) {
        LaunchedEffect(Unit) {
            val window = (view.context as android.app.Activity).window
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.hide(WindowInsetsCompat.Type.statusBars())
            insetsController.systemBarsBehavior =
                WindowCompat.getInsetsController(window, view).systemBarsBehavior
        }
    }

    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Archive,
        BottomNavItem.Background,
        BottomNavItem.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            val showBottomBar = currentDestination?.hierarchy?.any { dest ->
                bottomNavItems.any { item -> dest.route == item.route || dest.route?.startsWith(item.route + "?") == true }
            } == true

            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        val isSelected = currentDestination.hierarchy.any {
                            it.route == screen.route || it.route?.startsWith(screen.route + "?") == true
                        } == true
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title, maxLines = 1) },
                            selected = isSelected,
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
        }
    ) { innerPadding ->
        when (isOnboardingCompleted) {
            null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                val startDestination = if (isOnboardingCompleted == true) {
                    if (quoteId != null && quoteText != null) {
                        Routes.Home.withArgs(quoteId, quoteText, isFavorite)
                    } else {
                        Routes.Home.ROUTE_BASE
                    }
                } else {
                    Routes.ONBOARDING
                }

                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(Routes.ONBOARDING) {
                        OnboardingScreen(onOnboardingComplete = {
                            navController.navigate(Routes.Home.ROUTE_BASE) {
                                popUpTo(Routes.ONBOARDING) { inclusive = true }
                            }
                        })
                    }
                    composable(
                        route = Routes.Home.ROUTE_TEMPLATE,
                        arguments = listOf(
                            navArgument(Routes.Home.ARG_QUOTE_ID) { type = NavType.IntType; defaultValue = -1 },
                            navArgument(Routes.Home.ARG_QUOTE_TEXT) { type = NavType.StringType; nullable = true },
                            navArgument(Routes.Home.ARG_IS_FAVORITE) { type = NavType.BoolType; defaultValue = false }
                        )
                    ) { backStackEntry ->
                                                HomeScreen(
                            viewModel = homeViewModel
                        )
                    }
                    composable(Routes.Favorites.ROUTE) { FavoritesScreen() }
                    composable(Routes.Archive.ROUTE) { ArchiveScreen() }
                    composable(Routes.Settings.ROUTE) { SettingsScreen() }
                    composable(Routes.Background.ROUTE) { BackgroundScreen() }
                }
            }
        }
    }
}