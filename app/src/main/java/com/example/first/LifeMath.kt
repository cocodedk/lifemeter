package com.example.first

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.math.roundToLong

// Food: FAO Food Balance Sheets 2023 supply is 2.07 kg per person per day; the UNEP Food Waste
// Index 2024 puts waste at 19% of food available to consumers, which leaves about 1.7. It is a
// world average over all ages and leaves out drinking water and most drinks.
private const val FOOD_KG_PER_DAY = 1.7

// Sex: mean occasions in 4 weeks from Natsal-3 (Britain, 2010-12), Table 18, sexes averaged and
// converted to a month, as (age it applies from, occasions per month). Natsal covers ages 16-74;
// the last band is carried on beyond that. https://www.natsal.ac.uk
private val OCCASIONS_PER_MONTH =
    listOf(16 to 5.9, 25 to 5.6, 35 to 4.4, 45 to 4.1, 55 to 3.1, 65 to 2.0)

// Mean length of a whole session, foreplay to finish: Frappier et al., PLoS ONE 2013 (21 couples).
private const val MINUTES_PER_OCCASION = 25
private const val DAYS_PER_MONTH = 30.4375
private const val OLDEST_AGE = 200

/** Seconds from midnight on the birth date (the birth time is unknown) to [now]. */
fun secondsAlive(birth: LocalDate, now: Instant, zone: ZoneId): Long =
    now.epochSecond - birth.atStartOfDay(zone).toInstant().epochSecond

fun foodKg(days: Long): Long = (days * FOOD_KG_PER_DAY).roundToLong()

fun sexHours(birth: LocalDate, today: LocalDate): Long {
    var occasions = 0.0
    OCCASIONS_PER_MONTH.forEachIndexed { i, (fromAge, perMonth) ->
        val toAge = OCCASIONS_PER_MONTH.getOrNull(i + 1)?.first ?: OLDEST_AGE
        occasions += daysInAgeBand(birth, today, fromAge, toAge) / DAYS_PER_MONTH * perMonth
    }
    return (occasions * MINUTES_PER_OCCASION / 60).roundToLong()
}

private fun daysInAgeBand(birth: LocalDate, today: LocalDate, fromAge: Int, toAge: Int): Long {
    val bandEnd = birth.plusYears(toAge.toLong())
    val end = if (bandEnd.isBefore(today)) bandEnd else today
    return maxOf(0L, ChronoUnit.DAYS.between(birth.plusYears(fromAge.toLong()), end))
}
