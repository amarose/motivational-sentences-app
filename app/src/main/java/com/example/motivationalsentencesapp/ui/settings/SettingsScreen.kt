package com.example.motivationalsentencesapp.ui.settings

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
    val showDuplicateTimeError by viewModel.showDuplicateTimeError.collectAsState()

    val context = LocalContext.current
    val savedMessage = stringResource(id = R.string.changes_saved)
    val duplicateTimeMessage = stringResource(id = R.string.duplicate_notification_time_error)

    LaunchedEffect(showSaveConfirmation) {
        if (showSaveConfirmation) {
            Toast.makeText(context, savedMessage, Toast.LENGTH_SHORT).show()
            viewModel.onSaveConfirmationShown()
        }
    }

    LaunchedEffect(showDuplicateTimeError) {
        if (showDuplicateTimeError) {
            Toast.makeText(context, duplicateTimeMessage, Toast.LENGTH_LONG).show()
            viewModel.onDuplicateTimeErrorShown()
        }
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        SettingsContent(
            uiState = uiState,
            onNotificationsEnabledChanged = viewModel::onNotificationsEnabledChanged,
            onNotificationTimeChanged = viewModel::onNotificationTimeChanged,
            onNotificationQuantityChanged = viewModel::onNotificationQuantityChanged,
            onColorSelected = viewModel::onColorSelected,
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
    onColorSelected: (Int) -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ColorPickerCard(
                    selectedColor = uiState.selectedTextColor,
                    onColorSelected = onColorSelected
                )
            }

            item {
                NotificationSettingsCard(
                    notificationsEnabled = uiState.notificationsEnabled,
                    onNotificationsEnabledChanged = onNotificationsEnabledChanged
                )
            }

            if (uiState.notificationsEnabled) {
                item {
                    NotificationQuantitySettingsCard(
                        notificationQuantity = uiState.notificationQuantity,
                        onNotificationQuantityChanged = onNotificationQuantityChanged
                    )
                }

                items(uiState.notificationQuantity) { index ->
                    NotificationTimeSettingsCard(
                        index = index,
                        time = uiState.notificationTimes.getOrElse(index) { "09:00" },
                        onNotificationTimeChanged = onNotificationTimeChanged
                    )
                }
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
private fun ColorPickerCard(selectedColor: Int, onColorSelected: (Int) -> Unit) {
    val colors = listOf(
        Color.White,
        Color.Black,
        Color.Red,
        Color.Green,
        Color.Blue,
        Color(0xFFFFD700) // Gold
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Kolor tekstu",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                colors.forEach { color ->
                    ColorCircle(
                        color = color,
                        isSelected = selectedColor == color.toArgb(),
                        onClick = { onColorSelected(color.toArgb()) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorCircle(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color)
            .clickable(onClick = onClick)
            .border(
                width = 2.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = if (color == Color.Black) Color.White else Color.Black
            )
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.enable_notifications),
                style = MaterialTheme.typography.bodyMedium
            )
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = onNotificationsEnabledChanged
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun NotificationTimeSettingsCard(
    index: Int,
    time: String,
    onNotificationTimeChanged: (Int, String) -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        val timeParts = try {
            time.split(":").map { it.toInt() }
        } catch (_: Exception) {
            listOf(9, 0) // Default time
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Powiadomienie ${index + 1}",
                style = MaterialTheme.typography.bodyMedium
            )
            Button(onClick = {
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        onNotificationTimeChanged(index, String.format("%02d:%02d", hour, minute))
                    },
                    timeParts.getOrElse(0) { 9 },
                    timeParts.getOrElse(1) { 0 },
                    true
                ).show()
            }) {
                Text(text = time)
            }
        }
    }
}

@Composable
private fun NotificationQuantitySettingsCard(
    notificationQuantity: Int,
    onNotificationQuantityChanged: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.notification_quantity),
                style = MaterialTheme.typography.bodyMedium
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { onNotificationQuantityChanged(maxOf(1, notificationQuantity - 1)) },
                    enabled = notificationQuantity > 1
                ) {
                    Text("-")
                }
                Text(
                    text = notificationQuantity.toString(),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Button(
                    onClick = { onNotificationQuantityChanged(minOf(3, notificationQuantity + 1)) },
                    enabled = notificationQuantity < 3
                ) {
                    Text("+")
                }
            }
        }
    }
}
