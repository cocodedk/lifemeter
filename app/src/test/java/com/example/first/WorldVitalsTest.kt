package com.example.first

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import org.junit.Assert.assertEquals
import org.junit.Test

class WorldVitalsTest {

    private val utc = ZoneOffset.UTC

    @Test fun `deaths over a whole year equal the UN annual figure`() {
        val deaths = deathsSince(LocalDate.of(2023, 1, 1), Instant.parse("2024-01-01T00:00:00Z"), utc)

        assertEquals(61_651_608L, deaths)
    }

    @Test fun `deaths over half a year are half the annual figure`() {
        val deaths = deathsSince(LocalDate.of(2023, 1, 1), Instant.parse("2023-07-02T12:00:00Z"), utc)

        assertEquals(30_825_804L, deaths)
    }

    @Test fun `deaths across two years take the share of each year lived`() {
        val deaths = deathsSince(LocalDate.of(2022, 7, 2), Instant.parse("2024-01-01T00:00:00Z"), utc)

        assertEquals(92_876_235L, deaths)
    }

    @Test fun `years before 1950 use the 1950 figure`() {
        val deaths = deathsSince(LocalDate.of(1949, 1, 1), Instant.parse("1950-01-01T00:00:00Z"), utc)

        assertEquals(48_486_892L, deaths)
    }

    @Test fun `births per second in 2026 come from the UN annual figure`() {
        assertEquals(4.2017, birthsPerSecond(2026), 0.0001)
    }

    @Test fun `deaths per second in 2026 come from the UN annual figure`() {
        assertEquals(2.0179, deathsPerSecond(2026), 0.0001)
    }
}
