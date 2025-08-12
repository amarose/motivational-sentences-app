package pl.amarosee.motywator.ui.background

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import pl.amarosee.motywator.data.model.Background
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel

@Composable
fun BackgroundScreen(
    viewModel: BackgroundViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCheckmark by remember { mutableStateOf(false) }
    var checkmarkBackgroundId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is BackgroundViewEffect.BackgroundChanged -> {
                    checkmarkBackgroundId = uiState.selectedBackgroundResId
                    showCheckmark = true
                    delay(1500)
                    showCheckmark = false
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 50.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(uiState.backgrounds.size) { index ->
            val background = uiState.backgrounds[index]
            BackgroundItem(
                background = background,
                isSelected = background.resourceId == uiState.selectedBackgroundResId,
                showCheckmark = showCheckmark && checkmarkBackgroundId == background.resourceId,
                onSelected = { viewModel.onBackgroundSelected(background.resourceId) }
            )
        }
    }
    }
}

@Composable
private fun BackgroundItem(
    background: Background,
    isSelected: Boolean,
    showCheckmark: Boolean = false,
    onSelected: () -> Unit
) {
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    
    val checkmarkAlpha by animateFloatAsState(
        targetValue = if (showCheckmark) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "checkmark_alpha"
    )
    
    val checkmarkScale by animateFloatAsState(
        targetValue = if (showCheckmark) 1f else 0.5f,
        animationSpec = tween(durationMillis = 300),
        label = "checkmark_scale"
    )

    LaunchedEffect(background.resourceId) {
        withContext(Dispatchers.IO) {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeResource(context.resources, background.resourceId, options)

            options.inSampleSize = calculateInSampleSize(options, 150, 300)

            options.inJustDecodeBounds = false
            imageBitmap = BitmapFactory.decodeResource(context.resources, background.resourceId, options)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .aspectRatio(9f / 16f)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onSelected() }
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium
                    )
                } else {
                    Modifier
                }
            )
    ) {
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!.asImageBitmap(),
                contentDescription = background.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            CircularProgressIndicator()
        }
        
        // Animowany checkmark overlay
        if (showCheckmark) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.Black.copy(alpha = 0.6f),
                        shape = MaterialTheme.shapes.medium
                    )
                    .alpha(checkmarkAlpha),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier
                        .scale(checkmarkScale)
                        .background(
                            Color.Green,
                            shape = CircleShape
                        )
                        .padding(8.dp)
                )
            }
        }
    }
}

@Suppress("SameParameterValue")
private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}
