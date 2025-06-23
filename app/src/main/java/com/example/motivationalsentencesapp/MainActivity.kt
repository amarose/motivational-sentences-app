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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.motivationalsentencesapp.ui.home.HomeScreen
import com.example.motivationalsentencesapp.ui.main.MainViewModel
import com.example.motivationalsentencesapp.ui.onboarding.OnboardingScreen
import com.example.motivationalsentencesapp.ui.theme.MotivationalSentencesAppTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotivationalSentencesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MotivationalApp()
                }
            }
        }
    }
}

object Routes {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
}

@Composable
fun MotivationalApp(viewModel: MainViewModel = koinViewModel()) {
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
                NavHost(
                    navController = navController,
                    startDestination = if (isOnboardingCompleted == true) Routes.HOME else Routes.ONBOARDING
                ) {
                    composable(Routes.ONBOARDING) {
                        OnboardingScreen(onOnboardingComplete = {
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.ONBOARDING) { inclusive = true }
                            }
                        })
                    }
                    composable(Routes.HOME) {
                        HomeScreen()
                    }
                }
            }
        }
    }
}