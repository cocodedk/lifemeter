package com.example.first

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AboutLinksTest {
    private val id = "dk.cocode.lifemeter"

    @Test fun `update opens the F-Droid page while the app is live there`() {
        assertEquals(
            "https://f-droid.org/packages/dk.cocode.lifemeter/",
            aboutUrl(AboutLink.Update, id, onFdroid = true),
        )
    }

    @Test fun `update opens the latest GitHub release before the app is on F-Droid`() {
        assertEquals(
            "https://github.com/cocodedk/lifemeter/releases/latest",
            aboutUrl(AboutLink.Update, id, onFdroid = false),
        )
    }

    @Test fun `the app ships as live on F-Droid`() {
        assertEquals(
            "https://f-droid.org/packages/dk.cocode.lifemeter/",
            aboutUrl(AboutLink.Update, id),
        )
    }

    @Test fun `privacy opens the privacy policy`() {
        assertEquals("https://lifemeter.cocode.dk/privacy/", aboutUrl(AboutLink.Privacy, id))
    }

    @Test fun `privacy has no link when there is no policy`() {
        assertNull(aboutUrl(AboutLink.Privacy, id, privacyUrl = null))
    }

    @Test fun `website source and issues links`() {
        assertEquals("https://lifemeter.cocode.dk/", aboutUrl(AboutLink.Website, id))
        assertEquals("https://github.com/cocodedk/lifemeter", aboutUrl(AboutLink.Source, id))
        assertEquals("https://github.com/cocodedk/lifemeter/issues", aboutUrl(AboutLink.Issues, id))
    }
}
