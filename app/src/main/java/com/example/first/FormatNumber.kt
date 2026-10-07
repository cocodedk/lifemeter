package com.example.first

import java.text.NumberFormat
import java.util.Locale

/**
 * Shortens big numbers for a reader in [locale]: digits are grouped and decimals are separated the
 * way that locale does it (13,234 and 2.01 in English, 13.234 and 2,01 in Danish).
 * [billionsFormat] and [millionsFormat] are format strings with one `%f` (the app's
 * `number_billions` and `number_millions`), so the unit can be translated.
 */
fun formatNumber(n: Long, billionsFormat: String, millionsFormat: String, locale: Locale): String = when {
    n >= 1_000_000_000L -> billionsFormat.format(locale, n / 1_000_000_000.0)
    n >= 1_000_000L     -> millionsFormat.format(locale, n / 1_000_000.0)
    else                -> NumberFormat.getNumberInstance(locale).format(n)
}
