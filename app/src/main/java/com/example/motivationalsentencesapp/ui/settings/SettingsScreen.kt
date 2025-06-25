package com.example.motivationalsentencesapp.ui.settings

import android.annotation.SuppressLint
import android.app.TimePickerDialog
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.motivationalsentencesapp.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val showSaveConfirmation by viewModel.showSaveConfirmation.collectAsState()

    val context = LocalContext.current
    val savedMessage = stringResource(id = R.string.changes_saved)

    LaunchedEffect(showSaveConfirmation) {
        if (showSaveConfirmation) {
            Toast.makeText(context, savedMessage, Toast.LENGTH_SHORT).show()
            viewModel.onSaveConfirmationShown()
        }
    }

    if (uiState.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        SettingsContent(
            uiState = uiState,
            onNotificationsEnabledChanged = viewModel::onNotificationsEnabledChanged,
            onNotificationTimeChanged = viewModel::onNotificationTimeChanged,
            onNotificationQuantityChanged = viewModel::onNotificationQuantityChanged,
            onSave = viewModel::savePreferences
        )
    }
}

@Composable
private fun SettingsContent(
    uiState: SettingsUiState,
    onNotificationsEnabledChanged: (Boolean) -> Unit,
    onNotificationTimeChanged: (Int, String) -> Unit,
    onNotificationQuantityChanged: (Int) -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NotificationSettingsCard(
                notificationsEnabled = uiState.notificationsEnabled,
                onNotificationsEnabledChanged = onNotificationsEnabledChanged
            )

            QuantitySettingsCard(
                notificationQuantity = uiState.notificationQuantity,
                onNotificationQuantityChanged = onNotificationQuantityChanged
            )

            if (uiState.notificationsEnabled) {
                TimeSettingsCard(
                    notificationTimes = uiState.notificationTimes,
                    onNotificationTimeChanged = onNotificationTimeChanged
                )
            }
        }

        Button(
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(stringResource(R.string.save))
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
            Text(text = "Włącz powiadomienia", style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = onNotificationsEnabledChanged
            )
        }
    }
}

@Composable
private fun TimeSettingsCard(
    notificationTimes: List<String>,
    onNotificationTimeChanged: (Int, String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Czas powiadomień", style = MaterialTheme.typography.bodyLarge)
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
            Text(text = selectedTime, style = MaterialTheme.typography.bodyLarge)
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
                Text(text = "Ilość powiadomień dziennie", style = MaterialTheme.typography.bodyLarge)
                Text(text = notificationQuantity.toString(), style = MaterialTheme.typography.bodyLarge)
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
