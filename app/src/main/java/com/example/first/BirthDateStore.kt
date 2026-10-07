package com.example.first

import android.content.Context
import androidx.core.content.edit
import java.time.LocalDate

private const val PREFS_NAME = "lifemeter_prefs"
private const val PREFS_KEY_BIRTHDATE = "birthdate"

/** The one thing the app stores: the birth date, in its private preferences. */
class BirthDateStore(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun save(date: LocalDate) {
        prefs.edit { putString(PREFS_KEY_BIRTHDATE, date.toString()) }
    }

    fun load(): LocalDate? {
        val s = prefs.getString(PREFS_KEY_BIRTHDATE, null) ?: return null
        return try { LocalDate.parse(s) } catch (e: Exception) { null }
    }
}
