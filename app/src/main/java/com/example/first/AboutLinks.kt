package com.example.first

enum class AboutLink { Update, Website, Privacy, Source, Issues }

internal const val SITE_URL = "https://lifemeter.cocode.dk/"
internal const val PRIVACY_URL = "https://lifemeter.cocode.dk/privacy/"
internal const val REPO_URL = "https://github.com/cocodedk/lifemeter"

/** True while the app is listed on F-Droid; until then "See the latest version" opens the GitHub release. */
internal const val LIVE_ON_FDROID = true

/**
 * Where each About link goes, or null for a link that has no target (no privacy policy yet), which
 * the screen then leaves out. The app never checks for updates itself: the update link only opens
 * a page where the person can see the newest version.
 */
fun aboutUrl(
    link: AboutLink,
    applicationId: String,
    onFdroid: Boolean = LIVE_ON_FDROID,
    privacyUrl: String? = PRIVACY_URL,
): String? = when (link) {
    AboutLink.Update ->
        if (onFdroid) "https://f-droid.org/packages/$applicationId/" else "$REPO_URL/releases/latest"
    AboutLink.Website -> SITE_URL
    AboutLink.Privacy -> privacyUrl
    AboutLink.Source -> REPO_URL
    AboutLink.Issues -> "$REPO_URL/issues"
}
