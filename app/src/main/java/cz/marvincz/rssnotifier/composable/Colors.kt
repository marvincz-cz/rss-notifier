package cz.marvincz.rssnotifier.composable

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

private val Teal200 = Color(0xFF80CBC4)
private val Teal500 = Color(0xFF009688)
private val Teal700 = Color(0xFF00796B)

private val Purple200 = Color(0xFFCE93D8)
private val Purple200Dark = Color(0xFF9C64A6)

private val Black54 = Color.Black.copy(alpha = 0.54f)
private val White54 = Color.White.copy(alpha = 0.54f)

private fun primary(dark: Boolean) = if (dark) Teal200 else Teal500
private fun primaryVariant() = Teal700
private fun secondary(dark: Boolean) = if (dark) Purple200Dark else Purple200

fun iconOnSurface(dark: Boolean) = if (dark) White54 else Black54

fun colors(dark: Boolean) = if (dark) darkColors(
    primary = primary(dark),
    primaryVariant = primaryVariant(),
    secondary = secondary(dark),
    secondaryVariant = secondary(dark)
) else lightColors(
    primary = primary(dark),
    primaryVariant = primaryVariant(),
    secondary = secondary(dark),
    secondaryVariant = secondary(dark)
)