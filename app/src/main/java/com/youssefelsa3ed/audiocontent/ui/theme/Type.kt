package com.youssefelsa3ed.audiocontent.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.youssefelsa3ed.audiocontent.R

val ibmPlexSansArabic = FontFamily(
    Font(R.font.ibmplexsansarabic_extralight, FontWeight.ExtraLight),
    Font(R.font.ibmplexsansarabic_light, FontWeight.Light),
    Font(R.font.ibmplexsansarabic_regular, FontWeight.Normal),
    Font(R.font.ibmplexsansarabic_medium, FontWeight.Medium),
    Font(R.font.ibmplexsansarabic_bold, FontWeight.Bold),
    Font(R.font.ibmplexsansarabic_semibold, FontWeight.SemiBold),
    Font(R.font.ibmplexsansarabic_thin, FontWeight.Thin)
)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.SemiBold
    ),
    bodySmall = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.Light
    ),
    titleLarge = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.Bold
    ),
    titleMedium = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.Medium
    ),
    titleSmall = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.Light
    ),
    labelSmall = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.ExtraLight
    ),
    labelMedium = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.Medium
    ),
    labelLarge = TextStyle(
        fontFamily = ibmPlexSansArabic,
        fontWeight = FontWeight.SemiBold
    )
)