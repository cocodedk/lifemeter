package com.example.first

import org.junit.Assert.assertEquals
import org.junit.Test

class HoroscopeTest {
    // month parameter is 0-indexed (Jan=0, Feb=1, ..., Dec=11)

    @Test fun `aries - march 21`() {
        val r = getHoroscopeSign(2, 21)
        assertEquals("♈", r.symbol); assertEquals(R.string.sign_aries, r.nameRes)
    }

    @Test fun `taurus - april 20`() {
        val r = getHoroscopeSign(3, 20)
        assertEquals("♉", r.symbol); assertEquals(R.string.sign_taurus, r.nameRes)
    }

    @Test fun `gemini - june 1`() {
        val r = getHoroscopeSign(5, 1)
        assertEquals("♊", r.symbol); assertEquals(R.string.sign_gemini, r.nameRes)
    }

    @Test fun `cancer - july 4`() {
        val r = getHoroscopeSign(6, 4)
        assertEquals("♋", r.symbol); assertEquals(R.string.sign_cancer, r.nameRes)
    }

    @Test fun `leo - august 10`() {
        val r = getHoroscopeSign(7, 10)
        assertEquals("♌", r.symbol); assertEquals(R.string.sign_leo, r.nameRes)
    }

    @Test fun `virgo - september 1`() {
        val r = getHoroscopeSign(8, 1)
        assertEquals("♍", r.symbol); assertEquals(R.string.sign_virgo, r.nameRes)
    }

    @Test fun `libra - october 10`() {
        val r = getHoroscopeSign(9, 10)
        assertEquals("♎", r.symbol); assertEquals(R.string.sign_libra, r.nameRes)
    }

    @Test fun `scorpio - november 1`() {
        val r = getHoroscopeSign(10, 1)
        assertEquals("♏", r.symbol); assertEquals(R.string.sign_scorpio, r.nameRes)
    }

    @Test fun `sagittarius - december 1`() {
        val r = getHoroscopeSign(11, 1)
        assertEquals("♐", r.symbol); assertEquals(R.string.sign_sagittarius, r.nameRes)
    }

    @Test fun `capricorn - january 15`() {
        val r = getHoroscopeSign(0, 15)
        assertEquals("♑", r.symbol); assertEquals(R.string.sign_capricorn, r.nameRes)
    }

    @Test fun `aquarius - february 1`() {
        val r = getHoroscopeSign(1, 1)
        assertEquals("♒", r.symbol); assertEquals(R.string.sign_aquarius, r.nameRes)
    }

    @Test fun `pisces - march 15`() {
        val r = getHoroscopeSign(2, 15)
        assertEquals("♓", r.symbol); assertEquals(R.string.sign_pisces, r.nameRes)
    }

    @Test fun `boundary - aries starts march 21`() {
        assertEquals(R.string.sign_pisces, getHoroscopeSign(2, 20).nameRes)
        assertEquals(R.string.sign_aries,  getHoroscopeSign(2, 21).nameRes)
    }

    @Test fun `boundary - capricorn spans dec-jan`() {
        assertEquals(R.string.sign_capricorn, getHoroscopeSign(11, 22).nameRes)
        assertEquals(R.string.sign_capricorn, getHoroscopeSign(0, 19).nameRes)
        assertEquals(R.string.sign_aquarius,  getHoroscopeSign(0, 20).nameRes)
    }

    @Test fun `invalid month returns empty result`() {
        val r = getHoroscopeSign(12, 1)
        assertEquals("", r.symbol)
        assertEquals(0, r.nameRes)
        assertEquals(0, r.planetsRes)
    }

    @Test fun `boundary - aquarius ends feb 18`() {
        assertEquals(R.string.sign_aquarius, getHoroscopeSign(1, 18).nameRes)
        assertEquals(R.string.sign_pisces,   getHoroscopeSign(1, 19).nameRes)
    }
}
