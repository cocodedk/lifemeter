package com.example.first

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.os.ConfigurationCompat
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.Snackbar

/**
 * The About page: name and version, what the app does, privacy, links, credits and who made it.
 * Every link opens in the browser; nothing here uses the network itself.
 */
class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        HEADINGS.forEach { ViewCompat.setAccessibilityHeading(findViewById(it), true) }
        findViewById<TextView>(R.id.about_version).text =
            getString(R.string.about_version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
        findViewById<TextView>(R.id.about_update_hint).setText(
            if (LIVE_ON_FDROID) R.string.about_update_hint_fdroid else R.string.about_update_hint_github,
        )

        LINK_BUTTONS.forEach { (id, link) -> bindLink(id, link) }

        // Support slot (the Support phase of the cocode-apps standard): nothing visible until then.
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun bindLink(@IdRes buttonId: Int, link: AboutLink) {
        val button = findViewById<View>(buttonId)
        val language = ConfigurationCompat.getLocales(resources.configuration)[0]?.language ?: "en"
        val url = aboutUrl(link, BuildConfig.APPLICATION_ID, language)
        if (url == null) {
            button.visibility = View.GONE
        } else {
            button.setOnClickListener { open(url) }
        }
    }

    private fun open(url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
        } catch (e: ActivityNotFoundException) {
            Snackbar.make(findViewById(R.id.about_root), R.string.about_no_browser, Snackbar.LENGTH_LONG).show()
        }
    }

    private companion object {
        val HEADINGS = listOf(
            R.id.about_name_version_title,
            R.id.about_what_title,
            R.id.about_privacy_title,
            R.id.about_links_title,
            R.id.about_credits_title,
            R.id.about_made_by_title,
        )
        val LINK_BUTTONS = listOf(
            R.id.about_check_updates to AboutLink.Update,
            R.id.about_privacy_link to AboutLink.Privacy,
            R.id.about_website to AboutLink.Website,
            R.id.about_source to AboutLink.Source,
            R.id.about_report to AboutLink.Issues,
        )
    }
}
