package pl.amarosee.motywator.ui.common

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@SuppressLint("DefaultLocale")
@Composable
fun NotificationTimeButton(
    time: String,
    onClick: (String) -> Unit,
    minWidth: Dp = 60.dp,
    minHeight: Dp = 60.dp
) {
    val context = LocalContext.current
    val (hour, minute) = try {
        time.split(":").map { it.toInt() }
    } catch (_: Exception) {
        listOf(9, 0)
    }
    OutlinedButton(
        onClick = {
            TimePickerDialog(
                context,
                { _, h, m -> onClick(String.format("%02d:%02d", h, m)) },
                hour,
                minute,
                true
            ).show()
        },
        modifier = androidx.compose.ui.Modifier
            .height(minHeight)
            .defaultMinSize(minWidth = minWidth, minHeight = minHeight)
    ) {
        Text(time)
    }
}
