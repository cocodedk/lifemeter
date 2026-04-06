package com.example.first

import org.junit.Assert.assertEquals
import org.junit.Test

class HoroscopeTest {
    // month parameter is 0-indexed (Jan=0, Feb=1, ..., Dec=11)

    @Test fun `aries - march 21`() {
        val r = getHoroscopeSign(2, 21)
        assertEquals("♈", r.symbol); assertEquals("Aries", r.name)
    }

    @Test fun `taurus - april 20`() {
        val r = getHoroscopeSign(3, 20)
        assertEquals("♉", r.symbol); assertEquals("Taurus", r.name)
    }

    @Test fun `gemini - june 1`() {
        val r = getHoroscopeSign(5, 1)
        assertEquals("♊", r.symbol); assertEquals("Gemini", r.name)
    }

    @Test fun `cancer - july 4`() {
        val r = getHoroscopeSign(6, 4)
        assertEquals("♋", r.symbol); assertEquals("Cancer", r.name)
    }

    @Test fun `leo - august 10`() {
        val r = getHoroscopeSign(7, 10)
        assertEquals("♌", r.symbol); assertEquals("Leo", r.name)
    }

    @Test fun `virgo - september 1`() {
        val r = getHoroscopeSign(8, 1)
        assertEquals("♍", r.symbol); assertEquals("Virgo", r.name)
    }

    @Test fun `libra - october 10`() {
        val r = getHoroscopeSign(9, 10)
        assertEquals("♎", r.symbol); assertEquals("Libra", r.name)
    }

    @Test fun `scorpio - november 1`() {
        val r = getHoroscopeSign(10, 1)
        assertEquals("♏", r.symbol); assertEquals("Scorpio", r.name)
    }

    @Test fun `sagittarius - december 1`() {
        val r = getHoroscopeSign(11, 1)
        assertEquals("♐", r.symbol); assertEquals("Sagittarius", r.name)
    }

    @Test fun `capricorn - january 15`() {
        val r = getHoroscopeSign(0, 15)
        assertEquals("♑", r.symbol); assertEquals("Capricorn", r.name)
    }

    @Test fun `aquarius - february 1`() {
        val r = getHoroscopeSign(1, 1)
        assertEquals("♒", r.symbol); assertEquals("Aquarius", r.name)
    }

    @Test fun `pisces - march 15`() {
        val r = getHoroscopeSign(2, 15)
        assertEquals("♓", r.symbol); assertEquals("Pisces", r.name)
    }

    @Test fun `boundary - aries starts march 21`() {
        assertEquals("Pisces", getHoroscopeSign(2, 20).name)
        assertEquals("Aries",  getHoroscopeSign(2, 21).name)
    }

    @Test fun `boundary - capricorn spans dec-jan`() {
        assertEquals("Capricorn", getHoroscopeSign(11, 22).name)
        assertEquals("Capricorn", getHoroscopeSign(0, 19).name)
        assertEquals("Aquarius",  getHoroscopeSign(0, 20).name)
    }

    @Test fun `invalid month returns empty result`() {
        val r = getHoroscopeSign(12, 1)
        assertEquals("", r.symbol)
        assertEquals("", r.name)
    }

    @Test fun `boundary - aquarius ends feb 18`() {
        assertEquals("Aquarius", getHoroscopeSign(1, 18).name)
        assertEquals("Pisces",   getHoroscopeSign(1, 19).name)
    }
}
