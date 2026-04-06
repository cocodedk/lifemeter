package com.example.first

import org.junit.Assert.assertEquals
import org.junit.Test

class FormatNumberTest {

    @Test fun `small number below 1000 no separator`() {
        assertEquals("999", formatNumber(999L))
    }

    @Test fun `exactly 1000 uses comma`() {
        assertEquals("1,000", formatNumber(1_000L))
    }

    @Test fun `number below 1 million uses commas`() {
        assertEquals("123,456", formatNumber(123_456L))
    }

    @Test fun `exactly 1 million uses M with one decimal`() {
        assertEquals("1.0M", formatNumber(1_000_000L))
    }

    @Test fun `2300000 formats as 2 point 3M`() {
        assertEquals("2.3M", formatNumber(2_300_000L))
    }

    @Test fun `exactly 1 billion uses B with two decimals`() {
        assertEquals("1.00B", formatNumber(1_000_000_000L))
    }

    @Test fun `1140000000 formats as 1 point 14B`() {
        assertEquals("1.14B", formatNumber(1_140_000_000L))
    }
}
