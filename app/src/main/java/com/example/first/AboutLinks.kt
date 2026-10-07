package com.example.first

enum class AboutLink { Update, Website, Privacy, Source, Issues }

internal const val SITE_URL = "https://lifemeter.cocode.dk/"

/** Languages the site has a home page and a privacy page for, at `<site>/<code>/` and `<site>/<code>/privacy/`. */
internal val SITE_LANGUAGES = setOf("da")

/** A site page in the app's language, or the English page when the site has no pages in that language. */
internal fun sitePage(language: String, path: String = ""): String =
    if (language in SITE_LANGUAGES) "$SITE_URL$language/$path" else "$SITE_URL$path"

internal const val REPO_URL = "https://github.com/cocodedk/lifemeter"

/** True while the app is listed on F-Droid; until then "See the latest version" opens the GitHub release. */
internal const val LIVE_ON_FDROID = true

/**
 * Where each About link goes, or null for a link that has no target (no privacy policy yet), which
 * the screen then leaves out. The website and privacy pages follow [language] (a code such as "da"
 * from the app's current locale). The app never checks for updates itself: the update link only
 * opens a page where the person can see the newest version.
 */
fun aboutUrl(
    link: AboutLink,
    applicationId: String,
    language: String,
    onFdroid: Boolean = LIVE_ON_FDROID,
    privacyUrl: String? = sitePage(language, "privacy/"),
): String? = when (link) {
    AboutLink.Update ->
        if (onFdroid) "https://f-droid.org/packages/$applicationId/" else "$REPO_URL/releases/latest"
    AboutLink.Website -> sitePage(language)
    AboutLink.Privacy -> privacyUrl
    AboutLink.Source -> REPO_URL
    AboutLink.Issues -> "$REPO_URL/issues"
}
