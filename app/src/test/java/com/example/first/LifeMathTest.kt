package com.example.first

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import org.junit.Assert.assertEquals
import org.junit.Test

class LifeMathTest {

    private val utc = ZoneOffset.UTC

    @Test fun `seconds alive count the part of today that has already passed`() {
        val birth = LocalDate.of(2000, 1, 1)
        val noonNextDay = Instant.parse("2000-01-02T12:00:00Z")

        assertEquals(86_400L + 43_200L, secondsAlive(birth, noonNextDay, utc))
    }

    @Test fun `food is 1 point 7 kg per day`() {
        assertEquals(170L, foodKg(100))
    }

    @Test fun `no sex hours are counted before age 16`() {
        assertEquals(0L, sexHours(LocalDate.of(2000, 1, 1), LocalDate.of(2016, 1, 1)))
    }

    @Test fun `ages 16 to 25 use the 5 point 9 a month rate at 25 minutes each`() {
        assertEquals(266L, sexHours(LocalDate.of(2000, 1, 1), LocalDate.of(2025, 1, 1)))
    }
}
