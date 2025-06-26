package com.example.motivationalsentencesapp.ui.background

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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.motivationalsentencesapp.data.model.Background
import org.koin.androidx.compose.koinViewModel

@Composable
fun BackgroundScreen(
    viewModel: BackgroundViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(uiState.backgrounds) { background ->
            BackgroundItem(
                background = background,
                isSelected = background.resourceId == uiState.selectedBackgroundResId,
                onSelected = { viewModel.onBackgroundSelected(background.resourceId) }
            )
        }
    }
}

@Composable
fun BackgroundItem(
    background: Background,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    val context = LocalContext.current
    val imageBitmap = remember(background.resourceId) {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeResource(context.resources, background.resourceId, options)

        options.inSampleSize = calculateInSampleSize(options, 150, 300)

        options.inJustDecodeBounds = false
        BitmapFactory.decodeResource(context.resources, background.resourceId, options).asImageBitmap()
    }

    Box(
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
        Image(
            bitmap = imageBitmap,
            contentDescription = background.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

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
