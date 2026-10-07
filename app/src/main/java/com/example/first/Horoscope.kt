package com.example.first

import androidx.annotation.StringRes

/**
 * A zodiac sign. [symbol] is a Unicode glyph that needs no translation; [nameRes] and [planetsRes]
 * are string resources, or 0 for a date that falls in no sign.
 */
data class HoroscopeResult(
    val symbol: String,
    @StringRes val nameRes: Int,
    @StringRes val planetsRes: Int,
)

private val NO_SIGN = HoroscopeResult("", 0, 0)

fun getHoroscopeSign(month: Int, day: Int): HoroscopeResult {
    val m = month + 1
    return when {
        (m == 3 && day >= 21) || (m == 4 && day <= 19)  -> HoroscopeResult("♈", R.string.sign_aries, R.string.planets_aries)
        (m == 4 && day >= 20) || (m == 5 && day <= 20)  -> HoroscopeResult("♉", R.string.sign_taurus, R.string.planets_taurus)
        (m == 5 && day >= 21) || (m == 6 && day <= 20)  -> HoroscopeResult("♊", R.string.sign_gemini, R.string.planets_gemini)
        (m == 6 && day >= 21) || (m == 7 && day <= 22)  -> HoroscopeResult("♋", R.string.sign_cancer, R.string.planets_cancer)
        (m == 7 && day >= 23) || (m == 8 && day <= 22)  -> HoroscopeResult("♌", R.string.sign_leo, R.string.planets_leo)
        (m == 8 && day >= 23) || (m == 9 && day <= 22)  -> HoroscopeResult("♍", R.string.sign_virgo, R.string.planets_virgo)
        (m == 9 && day >= 23) || (m == 10 && day <= 22) -> HoroscopeResult("♎", R.string.sign_libra, R.string.planets_libra)
        (m == 10 && day >= 23) || (m == 11 && day <= 21)-> HoroscopeResult("♏", R.string.sign_scorpio, R.string.planets_scorpio)
        (m == 11 && day >= 22) || (m == 12 && day <= 21)-> HoroscopeResult("♐", R.string.sign_sagittarius, R.string.planets_sagittarius)
        (m == 12 && day >= 22) || (m == 1 && day <= 19) -> HoroscopeResult("♑", R.string.sign_capricorn, R.string.planets_capricorn)
        (m == 1 && day >= 20) || (m == 2 && day <= 18)  -> HoroscopeResult("♒", R.string.sign_aquarius, R.string.planets_aquarius)
        (m == 2 && day >= 19) || (m == 3 && day <= 20)  -> HoroscopeResult("♓", R.string.sign_pisces, R.string.planets_pisces)
        else -> NO_SIGN
    }
}
