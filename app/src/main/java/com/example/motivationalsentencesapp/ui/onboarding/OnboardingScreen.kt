package com.example.motivationalsentencesapp.ui.onboarding

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.timePickerError) {
        uiState.timePickerError?.let { error ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = error,
                    duration = SnackbarDuration.Short
                )
                viewModel.clearTimePickerError()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Witaj!", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Ustaw preferencje powiadomień, aby rozpocząć.")
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Włącz powiadomienia")
                Switch(
                    checked = uiState.notificationEnabled,
                    onCheckedChange = viewModel::onNotificationEnabledChanged
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Ile powiadomień chcesz otrzymywać dziennie?")
            Slider(
                value = uiState.notificationCount.toFloat(),
                onValueChange = { viewModel.onNotificationCountChanged(it.toInt()) },
                valueRange = 1f..3f,
                steps = 1
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                uiState.notificationTimes.forEachIndexed { index, time ->
                    TimePickerRow(
                        index = index,
                        selectedTime = time,
                        onTimeSelected = { newTime ->
                            viewModel.onNotificationTimeChanged(index, newTime)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.onSavePreferences()
                    onOnboardingComplete()
                },
                enabled = uiState.isSaveEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Zapisz i kontynuuj")
            }
        }
    }
}

@Composable
private fun TimePickerRow(
    index: Int,
    selectedTime: String,
    onTimeSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val (hour, minute) = selectedTime.split(":").map { it.toInt() }

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, min ->
            onTimeSelected(String.format("%02d:%02d", hourOfDay, min))
        },
        hour,
        minute,
        true
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Powiadomienie #${index + 1}")
        Text(
            text = selectedTime,
            modifier = Modifier.clickable { timePickerDialog.show() },
            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
        )
    }
}
