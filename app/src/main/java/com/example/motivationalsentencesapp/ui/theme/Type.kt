package com.example.motivationalsentencesapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.motivationalsentencesapp.R
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
private val LucidaCalligraphy = FontFamily(
    Font(R.font.lucida_calligraphy, FontWeight.Normal)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = LucidaCalligraphy,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 60.sp,
        letterSpacing = 0.7.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = LucidaCalligraphy,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp
    ),
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,S
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)