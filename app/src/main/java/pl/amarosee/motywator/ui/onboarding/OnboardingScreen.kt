package pl.amarosee.motywator.ui.onboarding

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import org.koin.androidx.compose.koinViewModel
import pl.amarosee.motywator.ui.common.NotificationTimeButton

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
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
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Witaj!", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Ustaw swoje preferencje, aby rozpocząć.", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.weight(1f)
            .verticalScroll(rememberScrollState()),
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

                NotificationTimesStackedCard(
                    notificationQuantity = uiState.notificationQuantity,
                    notificationTimes = uiState.notificationTimes,
                    onNotificationTimeChanged = onNotificationTimeChanged
                )
                uiState.timePickerError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Włącz powiadomienia",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = onNotificationsEnabledChanged,
                modifier = Modifier.align(Alignment.CenterHorizontally)
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
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Ilość powiadomień", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(8.dp))
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
private fun NotificationTimesStackedCard(
    notificationQuantity: Int,
    notificationTimes: List<String>,
    onNotificationTimeChanged: (Int, String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Czas powiadomień",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            repeat(notificationQuantity) { index ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "#${index + 1}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    NotificationTimeButton(
                        time = notificationTimes.getOrNull(index) ?: "09:00",
                        onClick = { onNotificationTimeChanged(index, it) },
                        minWidth = 80.dp,
                        minHeight = 60.dp
                    )
                }
            }
        }
    }
}




