package com.example.motivationalsentencesapp.ui.onboarding

import android.Manifest
import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.onPermissionResult(isGranted)
        }
    )

    val uiState by viewModel.uiState.collectAsState()
    val launchPermissionRequest by viewModel.launchPermissionRequest.collectAsState()

    LaunchedEffect(launchPermissionRequest) {
        if (launchPermissionRequest) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    LaunchedEffect(uiState.onboardingComplete) {
        if (uiState.onboardingComplete) {
            onOnboardingComplete()
        }
    }

    OnboardingContent(
        uiState = uiState,
        onNotificationEnabledChanged = viewModel::onNotificationEnabledChanged,
        onNotificationQuantityChanged = viewModel::onNotificationQuantityChanged,
        onNotificationTimeChanged = viewModel::onNotificationTimeChanged,
        onSave = viewModel::onSavePreferences
    )
}

@Composable
private fun OnboardingContent(
    uiState: OnboardingUiState,
    onNotificationEnabledChanged: (Boolean) -> Unit,
    onNotificationQuantityChanged: (Int) -> Unit,
    onNotificationTimeChanged: (Int, String) -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Witaj!", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Ustaw swoje preferencje, aby rozpocząć.", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NotificationSettingsCard(
                notificationsEnabled = uiState.notificationEnabled,
                onNotificationsEnabledChanged = onNotificationEnabledChanged
            )

            if (uiState.notificationEnabled) {
                QuantitySettingsCard(
                    notificationQuantity = uiState.notificationQuantity,
                    onNotificationQuantityChanged = onNotificationQuantityChanged
                )

                TimeSettingsCard(
                    notificationTimes = uiState.notificationTimes,
                    onNotificationTimeChanged = onNotificationTimeChanged,
                    timePickerError = uiState.timePickerError
                )
            }
        }

        Button(
            onClick = onSave,
            enabled = uiState.isSaveEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Zapisz i kontynuuj")
        }
    }
}

@Composable
private fun NotificationSettingsCard(
    notificationsEnabled: Boolean,
    onNotificationsEnabledChanged: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Włącz powiadomienia", style = MaterialTheme.typography.bodyMedium)
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = onNotificationsEnabledChanged
            )
        }
    }
}

@Composable
private fun QuantitySettingsCard(
    notificationQuantity: Int,
    onNotificationQuantityChanged: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Ilość powiadomień dziennie", style = MaterialTheme.typography.bodyMedium)
                Text(text = notificationQuantity.toString(), style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Slider(
                value = notificationQuantity.toFloat(),
                onValueChange = { onNotificationQuantityChanged(it.toInt()) },
                valueRange = 1f..3f,
                steps = 1
            )
        }
    }
}

@Composable
private fun TimeSettingsCard(
    notificationTimes: List<String>,
    onNotificationTimeChanged: (Int, String) -> Unit,
    timePickerError: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Czas powiadomień", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            notificationTimes.forEachIndexed { index, time ->
                TimePickerRow(
                    index = index,
                    selectedTime = time,
                    onTimeSelected = { newTime ->
                        onNotificationTimeChanged(index, newTime)
                    }
                )
            }
            timePickerError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun TimePickerRow(
    index: Int,
    selectedTime: String,
    onTimeSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val (hour, minute) = try {
        selectedTime.split(":").map { it.toInt() }
    } catch (_: Exception) {
        listOf(9, 0) // Default time if format is incorrect
    }

    val timePickerDialog = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            onTimeSelected(String.format("%02d:%02d", selectedHour, selectedMinute))
        },
        hour,
        minute,
        true
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Powiadomienie #${index + 1}", style = MaterialTheme.typography.bodyMedium)
        TextButton(onClick = { timePickerDialog.show() }) {
            Text(text = selectedTime, style = MaterialTheme.typography.bodyMedium)
        }
    }
}


