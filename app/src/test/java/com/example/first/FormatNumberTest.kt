package com.example.first

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

class FormatNumberTest {

    private val danish = Locale.forLanguageTag("da-DK")

    // The English formats from strings.xml (number_billions, number_millions).
    private fun format(n: Long) = formatNumber(n, "%.2fB", "%.1fM", Locale.US)

    @Test fun `the billions unit comes from the format string`() {
        assertEquals("1.50 mia.", formatNumber(1_500_000_000L, "%.2f mia.", "%.1f mio.", Locale.US))
    }

    @Test fun `the millions unit comes from the format string`() {
        assertEquals("2.5 mio.", formatNumber(2_500_000L, "%.2f mia.", "%.1f mio.", Locale.US))
    }

    @Test fun `english groups thousands with a comma`() {
        assertEquals("13,234", format(13_234L))
    }

    @Test fun `danish groups thousands with a dot`() {
        assertEquals("13.234", formatNumber(13_234L, "%.2fB", "%.1fM", danish))
    }

    @Test fun `english separates decimals with a point`() {
        assertEquals("2.01B", format(2_010_000_000L))
    }

    @Test fun `danish separates decimals with a comma`() {
        assertEquals("2,01B", formatNumber(2_010_000_000L, "%.2fB", "%.1fM", danish))
    }

    @Test fun `small number below 1000 no separator`() {
        assertEquals("999", format(999L))
    }

    @Test fun `exactly 1000 uses comma`() {
        assertEquals("1,000", format(1_000L))
    }

    @Test fun `number below 1 million uses commas`() {
        assertEquals("123,456", format(123_456L))
    }

    @Test fun `exactly 1 million uses M with one decimal`() {
        assertEquals("1.0M", format(1_000_000L))
    }

    @Test fun `2300000 formats as 2 point 3M`() {
        assertEquals("2.3M", format(2_300_000L))
    }

    @Test fun `exactly 1 billion uses B with two decimals`() {
        assertEquals("1.00B", format(1_000_000_000L))
    }

    @Test fun `1140000000 formats as 1 point 14B`() {
        assertEquals("1.14B", format(1_140_000_000L))
    }
}
