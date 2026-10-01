package com.example.first

import java.time.Instant
import java.time.LocalDate
import java.time.Year
import java.time.ZoneId
import kotlin.math.roundToLong

/** World deaths from [birth] to [now]: each year's UN total times the share of it lived. */
fun deathsSince(birth: LocalDate, now: Instant, zone: ZoneId): Long =
    eventsSince(WORLD_DEATHS, birth, now, zone)

fun birthsPerSecond(year: Int): Double = perSecond(WORLD_BIRTHS, year)

fun deathsPerSecond(year: Int): Double = perSecond(WORLD_DEATHS, year)

// Years outside the table (before 1950, after 2030) reuse the nearest year's figure.
private fun annual(table: LongArray, year: Int): Long =
    table[year.coerceIn(FIRST_YEAR, LAST_YEAR) - FIRST_YEAR]

private fun perSecond(table: LongArray, year: Int): Double =
    annual(table, year).toDouble() / (Year.of(year).length() * 86_400L)

private fun eventsSince(table: LongArray, birth: LocalDate, now: Instant, zone: ZoneId): Long {
    val from = birth.atStartOfDay(zone).toInstant().epochSecond
    val to = now.epochSecond
    var total = 0.0
    for (year in birth.year..now.atZone(zone).year) {
        val yearStart = LocalDate.of(year, 1, 1).atStartOfDay(zone).toInstant().epochSecond
        val yearEnd = LocalDate.of(year + 1, 1, 1).atStartOfDay(zone).toInstant().epochSecond
        val lived = maxOf(0L, minOf(to, yearEnd) - maxOf(from, yearStart))
        total += annual(table, year) * lived.toDouble() / (yearEnd - yearStart)
    }
    return total.roundToLong()
}
