package com.example.first

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Locale

private const val PREFS_NAME = "lifemeter_prefs"
private const val PREFS_KEY_BIRTHDATE = "birthdate"

data class HoroscopeResult(val symbol: String, val name: String, val planets: String)

fun getHoroscopeSign(month: Int, day: Int): HoroscopeResult {
    val m = month + 1
    return when {
        (m == 3 && day >= 21) || (m == 4 && day <= 19)  -> HoroscopeResult("♈", "Aries", "Mars")
        (m == 4 && day >= 20) || (m == 5 && day <= 20)  -> HoroscopeResult("♉", "Taurus", "Venus · Moon")
        (m == 5 && day >= 21) || (m == 6 && day <= 20)  -> HoroscopeResult("♊", "Gemini", "Mercury")
        (m == 6 && day >= 21) || (m == 7 && day <= 22)  -> HoroscopeResult("♋", "Cancer", "Moon · Jupiter")
        (m == 7 && day >= 23) || (m == 8 && day <= 22)  -> HoroscopeResult("♌", "Leo", "Sun")
        (m == 8 && day >= 23) || (m == 9 && day <= 22)  -> HoroscopeResult("♍", "Virgo", "Mercury")
        (m == 9 && day >= 23) || (m == 10 && day <= 22) -> HoroscopeResult("♎", "Libra", "Venus · Saturn")
        (m == 10 && day >= 23) || (m == 11 && day <= 21)-> HoroscopeResult("♏", "Scorpio", "Mars · Pluto")
        (m == 11 && day >= 22) || (m == 12 && day <= 21)-> HoroscopeResult("♐", "Sagittarius", "Jupiter")
        (m == 12 && day >= 22) || (m == 1 && day <= 19) -> HoroscopeResult("♑", "Capricorn", "Saturn · Mars")
        (m == 1 && day >= 20) || (m == 2 && day <= 18)  -> HoroscopeResult("♒", "Aquarius", "Saturn · Uranus")
        (m == 2 && day >= 19) || (m == 3 && day <= 20)  -> HoroscopeResult("♓", "Pisces", "Jupiter · Neptune · Venus")
        else -> HoroscopeResult("", "", "")
    }
}

fun formatNumber(n: Long): String = when {
    n >= 1_000_000_000L -> "%.2fB".format(n / 1_000_000_000.0)
    n >= 1_000_000L     -> "%.1fM".format(n / 1_000_000.0)
    else                -> NumberFormat.getNumberInstance(Locale.US).format(n)
}

class MainActivity : AppCompatActivity() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mainScrollView: NestedScrollView
    private lateinit var heroSection: LinearLayout
    private lateinit var dateCompactRow: LinearLayout
    private lateinit var firstLaunchContent: LinearLayout
    private lateinit var selectedBirthDateText: TextView
    private lateinit var changeBirthDateButton: MaterialButton
    private lateinit var setBirthDateButton: MaterialButton
    private lateinit var resultsContainer: LinearLayout
    private lateinit var daysValue: TextView
    private lateinit var secondsValue: TextView
    private lateinit var foodValue: TextView
    private lateinit var deathsValue: TextView
    private lateinit var sessionSecondsValue: TextView
    private lateinit var sessionDeathsValue: TextView
    private lateinit var sessionBirthsValue: TextView
    private lateinit var horoscopeSymbol: TextView
    private lateinit var horoscopeName: TextView
    private lateinit var horoscopePlanets: TextView
    private lateinit var sexText: TextView

    private var timer: CountDownTimer? = null
    private var selectedBirthDate: LocalDate? = null
    private var sessionStartTime: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        bindViews()

        selectedBirthDate = loadBirthDate()

        if (selectedBirthDate != null) {
            showReturningUserState()
        } else {
            showFirstLaunchState()
        }

        setBirthDateButton.setOnClickListener { showBirthDatePicker() }
        changeBirthDateButton.setOnClickListener { showBirthDatePicker() }
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            showBirthDatePicker()
        }
    }

    override fun onResume() {
        super.onResume()
        if (selectedBirthDate != null) {
            sessionStartTime = System.currentTimeMillis() / 1000
            startTimer()
        }
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
        timer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    private fun bindViews() {
        swipeRefreshLayout    = findViewById(R.id.swipe_refresh_layout)
        mainScrollView        = findViewById(R.id.main_scroll_view)
        heroSection           = findViewById(R.id.hero_section)
        dateCompactRow        = findViewById(R.id.date_compact_row)
        firstLaunchContent    = findViewById(R.id.first_launch_content)
        selectedBirthDateText = findViewById(R.id.selected_birth_date_text)
        changeBirthDateButton = findViewById(R.id.change_birth_date_button)
        setBirthDateButton    = findViewById(R.id.set_birth_date_button)
        resultsContainer      = findViewById(R.id.results_container)
        daysValue             = findViewById(R.id.days_value)
        secondsValue          = findViewById(R.id.seconds_value)
        foodValue             = findViewById(R.id.food_value)
        deathsValue           = findViewById(R.id.deaths_value)
        sessionSecondsValue   = findViewById(R.id.session_seconds_value)
        sessionDeathsValue    = findViewById(R.id.session_deaths_value)
        sessionBirthsValue    = findViewById(R.id.session_births_value)
        horoscopeSymbol       = findViewById(R.id.horoscope_symbol)
        horoscopeName         = findViewById(R.id.horoscope_name)
        horoscopePlanets      = findViewById(R.id.horoscope_planets)
        sexText               = findViewById(R.id.sex_text)
    }

    private fun showFirstLaunchState() {
        heroSection.visibility        = View.VISIBLE
        dateCompactRow.visibility     = View.GONE
        firstLaunchContent.visibility = View.VISIBLE
        resultsContainer.visibility   = View.GONE
    }

    private fun transitionToReturningLayout() {
        heroSection.visibility        = View.GONE
        dateCompactRow.visibility     = View.VISIBLE
        firstLaunchContent.visibility = View.GONE
    }

    private fun showReturningUserState() {
        transitionToReturningLayout()
        resultsContainer.visibility = View.VISIBLE
        renderSelectedBirthDate()
        updateDashboard()
    }

    private fun showBirthDatePicker() {
        val current = selectedBirthDate ?: LocalDate.of(LocalDate.now().year - 30, 1, 1)
        DatePickerDialog(
            this,
            { _, year, month, day ->
                selectedBirthDate = LocalDate.of(year, month + 1, day)
                saveBirthDate(selectedBirthDate!!)
                renderSelectedBirthDate()
                if (resultsContainer.visibility != View.VISIBLE) {
                    transitionToReturningLayout()
                    revealDashboard()
                } else {
                    updateDashboard()
                }
                sessionStartTime = System.currentTimeMillis() / 1000
                startTimer()
            },
            current.year,
            current.monthValue - 1,
            current.dayOfMonth
        ).show()
    }

    private fun renderSelectedBirthDate() {
        val d = selectedBirthDate ?: return
        selectedBirthDateText.text = "%02d / %02d / %d".format(d.dayOfMonth, d.monthValue, d.year)
    }

    private fun revealDashboard() {
        resultsContainer.visibility   = View.VISIBLE
        resultsContainer.alpha        = 0f
        resultsContainer.translationY = 32f * resources.displayMetrics.density
        resultsContainer.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(300)
            .start()
        updateDashboard()
        mainScrollView.post { mainScrollView.smoothScrollTo(0, resultsContainer.top) }
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(Long.MAX_VALUE / 2, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val timePassed = System.currentTimeMillis() / 1000 - sessionStartTime
                val year = LocalDate.now().year
                sessionSecondsValue.text = formatNumber(timePassed)
                sessionDeathsValue.text  = formatNumber(Math.round(timePassed * deathsPerSecond(year)))
                sessionBirthsValue.text  = formatNumber(Math.round(timePassed * birthsPerSecond(year)))
                updateLiveStats()
            }
            override fun onFinish() {}
        }.also { it.start() }
    }

    private fun updateDashboard() {
        val birth = selectedBirthDate ?: return
        val today = LocalDate.now()
        val days = ChronoUnit.DAYS.between(birth, today)

        daysValue.text = formatNumber(days)
        foodValue.text = formatNumber(foodKg(days))

        val sign = getHoroscopeSign(birth.monthValue - 1, birth.dayOfMonth)
        horoscopeSymbol.text  = sign.symbol
        horoscopeName.text    = sign.name
        horoscopePlanets.text = sign.planets

        val hours = sexHours(birth, today)
        if (hours > 0) {
            sexText.text       = getString(R.string.curiosities_sex_hours, formatNumber(hours))
            sexText.visibility = View.VISIBLE
        } else {
            sexText.visibility = View.GONE
        }

        updateLiveStats()
    }

    private fun updateLiveStats() {
        val birth = selectedBirthDate ?: return
        val now = Instant.now()
        val zone = ZoneId.systemDefault()
        secondsValue.text = formatNumber(secondsAlive(birth, now, zone))
        deathsValue.text  = formatNumber(deathsSince(birth, now, zone))
    }

    private fun saveBirthDate(date: LocalDate) {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(PREFS_KEY_BIRTHDATE, date.toString())
            .apply()
    }

    private fun loadBirthDate(): LocalDate? {
        val s = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(PREFS_KEY_BIRTHDATE, null) ?: return null
        return try { LocalDate.parse(s) } catch (e: Exception) { null }
    }
}
