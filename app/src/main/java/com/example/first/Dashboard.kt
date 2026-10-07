package com.example.first

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.os.ConfigurationCompat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Locale

/** The numbers on the main screen: finds their views once and fills them in. */
class Dashboard(private val activity: Activity) {

    private val daysValue: TextView = activity.findViewById(R.id.days_value)
    private val secondsValue: TextView = activity.findViewById(R.id.seconds_value)
    private val foodValue: TextView = activity.findViewById(R.id.food_value)
    private val deathsValue: TextView = activity.findViewById(R.id.deaths_value)
    private val sessionSecondsValue: TextView = activity.findViewById(R.id.session_seconds_value)
    private val sessionDeathsValue: TextView = activity.findViewById(R.id.session_deaths_value)
    private val sessionBirthsValue: TextView = activity.findViewById(R.id.session_births_value)
    private val horoscopeSymbol: TextView = activity.findViewById(R.id.horoscope_symbol)
    private val horoscopeName: TextView = activity.findViewById(R.id.horoscope_name)
    private val horoscopePlanets: TextView = activity.findViewById(R.id.horoscope_planets)
    private val sexText: TextView = activity.findViewById(R.id.sex_text)

    /** Everything that depends on the birth date and the calendar day. */
    fun showBirth(birth: LocalDate) {
        val today = LocalDate.now()
        val days = ChronoUnit.DAYS.between(birth, today)

        daysValue.text = fmt(days)
        foodValue.text = fmt(foodKg(days))

        val sign = getHoroscopeSign(birth.monthValue - 1, birth.dayOfMonth)
        horoscopeSymbol.text  = sign.symbol
        horoscopeName.text    = stringOrEmpty(sign.nameRes)
        horoscopePlanets.text = stringOrEmpty(sign.planetsRes)

        val hours = sexHours(birth, today)
        if (hours > 0) {
            sexText.text       = activity.getString(R.string.curiosities_sex_hours, fmt(hours))
            sexText.visibility = View.VISIBLE
        } else {
            sexText.visibility = View.GONE
        }

        showLive(birth)
    }

    /** The two counters that tick every second. */
    fun showLive(birth: LocalDate) {
        val now = Instant.now()
        val zone = ZoneId.systemDefault()
        secondsValue.text = fmt(secondsAlive(birth, now, zone))
        deathsValue.text  = fmt(deathsSince(birth, now, zone))
    }

    /** The "This session" rows, for [seconds] since the screen was opened. */
    fun showSession(seconds: Long) {
        val year = LocalDate.now().year
        sessionSecondsValue.text = fmt(seconds)
        sessionDeathsValue.text  = fmt(Math.round(seconds * deathsPerSecond(year)))
        sessionBirthsValue.text  = fmt(Math.round(seconds * birthsPerSecond(year)))
    }

    /** Numbers follow the language the app is shown in, so a Danish phone reads 13.234, not 13,234. */
    private fun fmt(n: Long): String = formatNumber(
        n,
        activity.getString(R.string.number_billions),
        activity.getString(R.string.number_millions),
        ConfigurationCompat.getLocales(activity.resources.configuration)[0] ?: Locale.getDefault(),
    )

    private fun stringOrEmpty(@StringRes id: Int): String = if (id == 0) "" else activity.getString(id)
}
