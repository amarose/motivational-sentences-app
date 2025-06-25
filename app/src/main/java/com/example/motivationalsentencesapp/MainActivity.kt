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



        val quoteText = intent.getStringExtra(EXTRA_QUOTE_TEXT)
        val quoteAuthor = intent.getStringExtra(EXTRA_QUOTE_AUTHOR)

        setContent {
            MotivationalSentencesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MotivationalApp(
                        viewModel = koinViewModel(),
                        quoteText = quoteText,
                        quoteAuthor = quoteAuthor
                    )
                }
            }
        }
    }

    companion object {
        const val EXTRA_QUOTE_TEXT = "EXTRA_QUOTE_TEXT"
        const val EXTRA_QUOTE_AUTHOR = "EXTRA_QUOTE_AUTHOR"
    }
}



@Composable
fun MotivationalApp(
    viewModel: MainViewModel = koinViewModel(),
    quoteText: String?,
    quoteAuthor: String?
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
                    Routes.MAIN
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
                        MainScreen(quoteText = quoteText, quoteAuthor = quoteAuthor)
                    }
                }
            }
        }
    }
}