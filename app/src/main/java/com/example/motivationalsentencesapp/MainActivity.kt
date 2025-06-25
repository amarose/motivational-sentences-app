package com.example.motivationalsentencesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.motivationalsentencesapp.ui.mainscreen.MainScreen
import com.example.motivationalsentencesapp.ui.navigation.Routes
import com.example.motivationalsentencesapp.ui.main.MainViewModel
import com.example.motivationalsentencesapp.ui.onboarding.OnboardingScreen
import com.example.motivationalsentencesapp.ui.theme.MotivationalSentencesAppTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        val quoteId = intent.getIntExtra(EXTRA_QUOTE_ID, -1)
        val quoteText = intent.getStringExtra(EXTRA_QUOTE_TEXT)
        val quoteAuthor = intent.getStringExtra(EXTRA_QUOTE_AUTHOR)
        val isFavorite = intent.getBooleanExtra(EXTRA_IS_FAVORITE, false)

        setContent {
            MotivationalSentencesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MotivationalApp(
                        viewModel = koinViewModel(),
                        quoteId = if (quoteId != -1) quoteId else null,
                        quoteText = quoteText,
                        quoteAuthor = quoteAuthor,
                        isFavorite = isFavorite
                    )
                }
            }
        }
    }

    companion object {
        const val EXTRA_QUOTE_ID = "EXTRA_QUOTE_ID"
        const val EXTRA_QUOTE_TEXT = "EXTRA_QUOTE_TEXT"
        const val EXTRA_QUOTE_AUTHOR = "EXTRA_QUOTE_AUTHOR"
        const val EXTRA_IS_FAVORITE = "EXTRA_IS_FAVORITE"
    }
}



@Composable
fun MotivationalApp(
    viewModel: MainViewModel = koinViewModel(),
    quoteId: Int?,
    quoteText: String?,
    quoteAuthor: String?,
    isFavorite: Boolean
) {
    val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsState()
    val navController = rememberNavController()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        when (isOnboardingCompleted) {
            null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                val startDestination = if (isOnboardingCompleted == true) {
                    if (quoteId != null && quoteText != null && quoteAuthor != null) {
                        Routes.Home.withArgs(quoteId, quoteText, quoteAuthor, isFavorite)
                    } else {
                        Routes.MAIN
                    }
                } else {
                    Routes.ONBOARDING
                }

                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    composable(Routes.ONBOARDING) {
                        OnboardingScreen(onOnboardingComplete = {
                            navController.navigate(Routes.MAIN) {
                                popUpTo(Routes.ONBOARDING) { inclusive = true }
                            }
                        })
                    }
                    composable(Routes.MAIN) {
                        MainScreen()
                    }
                    composable(
                        route = Routes.Home.ROUTE_TEMPLATE,
                        arguments = listOf(
                            navArgument(Routes.Home.ARG_QUOTE_ID) { type = NavType.IntType },
                            navArgument(Routes.Home.ARG_QUOTE_TEXT) { type = NavType.StringType },
                            navArgument(Routes.Home.ARG_QUOTE_AUTHOR) { type = NavType.StringType },
                            navArgument(Routes.Home.ARG_IS_FAVORITE) { type = NavType.BoolType }
                        )
                    ) {
                        MainScreen()
                    }
                }
            }
        }
    }
}