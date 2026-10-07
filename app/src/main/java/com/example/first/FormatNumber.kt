package com.example.first

import java.text.NumberFormat
import java.util.Locale

/**
 * Shortens big numbers. [billionsFormat] and [millionsFormat] are format strings with one `%f`
 * (the app's `number_billions` and `number_millions`), so the unit can be translated.
 */
fun formatNumber(n: Long, billionsFormat: String, millionsFormat: String): String = when {
    n >= 1_000_000_000L -> billionsFormat.format(n / 1_000_000_000.0)
    n >= 1_000_000L     -> millionsFormat.format(n / 1_000_000.0)
    else                -> NumberFormat.getNumberInstance(Locale.US).format(n)
}
