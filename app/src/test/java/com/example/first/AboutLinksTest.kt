package com.example.first

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AboutLinksTest {
    private val id = "dk.cocode.lifemeter"

    @Test fun `update opens the F-Droid page while the app is live there`() {
        assertEquals(
            "https://f-droid.org/packages/dk.cocode.lifemeter/",
            aboutUrl(AboutLink.Update, id, "en", onFdroid = true),
        )
    }

    @Test fun `update opens the latest GitHub release before the app is on F-Droid`() {
        assertEquals(
            "https://github.com/cocodedk/lifemeter/releases/latest",
            aboutUrl(AboutLink.Update, id, "en", onFdroid = false),
        )
    }

    @Test fun `the app ships as live on F-Droid`() {
        assertEquals(
            "https://f-droid.org/packages/dk.cocode.lifemeter/",
            aboutUrl(AboutLink.Update, id, "en"),
        )
    }

    @Test fun `privacy opens the English policy in English`() {
        assertEquals("https://lifemeter.cocode.dk/privacy/", aboutUrl(AboutLink.Privacy, id, "en"))
    }

    @Test fun `privacy opens the Danish policy in Danish`() {
        assertEquals("https://lifemeter.cocode.dk/da/privacy/", aboutUrl(AboutLink.Privacy, id, "da"))
    }

    @Test fun `privacy falls back to the English policy in a language the site lacks`() {
        assertEquals("https://lifemeter.cocode.dk/privacy/", aboutUrl(AboutLink.Privacy, id, "fa"))
    }

    @Test fun `privacy has no link when there is no policy`() {
        assertNull(aboutUrl(AboutLink.Privacy, id, "da", privacyUrl = null))
    }

    @Test fun `website opens the English site in English`() {
        assertEquals("https://lifemeter.cocode.dk/", aboutUrl(AboutLink.Website, id, "en"))
    }

    @Test fun `website opens the Danish site in Danish`() {
        assertEquals("https://lifemeter.cocode.dk/da/", aboutUrl(AboutLink.Website, id, "da"))
    }

    @Test fun `website falls back to the English site in a language the site lacks`() {
        assertEquals("https://lifemeter.cocode.dk/", aboutUrl(AboutLink.Website, id, "fa"))
    }

    @Test fun `source opens the repository in every language`() {
        assertEquals("https://github.com/cocodedk/lifemeter", aboutUrl(AboutLink.Source, id, "da"))
    }

    @Test fun `issues opens the issue tracker in every language`() {
        assertEquals("https://github.com/cocodedk/lifemeter/issues", aboutUrl(AboutLink.Issues, id, "da"))
    }
}
