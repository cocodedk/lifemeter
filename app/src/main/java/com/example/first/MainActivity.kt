package com.example.first

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.google.android.material.button.MaterialButton
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.text.NumberFormat
import java.util.Locale

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
    private lateinit var toolbarView: androidx.appcompat.widget.Toolbar
    private lateinit var mainScrollView: NestedScrollView
    private lateinit var selectedBirthDateText: TextView
    private lateinit var changeBirthDateButton: MaterialButton
    private lateinit var continueButton: MaterialButton
    private lateinit var resultsContainer: LinearLayout
    private lateinit var horoscopeSignText: TextView
    private lateinit var secondsTextField: TextView
    private lateinit var deathsSinceBirth: TextView
    private lateinit var daysTextView: TextView
    private lateinit var sexText: TextView
    private lateinit var foodConsumptionText: TextView
    private lateinit var secondsPassedText: TextView
    private lateinit var deathsSinceText: TextView
    private lateinit var birthsSinceText: TextView
    private var timer: InnerCountDownCounter? = null
    private var selectedBirthDate: LocalDate = LocalDate.now()
    private var resultsVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        toolbarView = findViewById(R.id.toolbar)
        mainScrollView = findViewById(R.id.main_scroll_view)
        selectedBirthDateText = findViewById(R.id.selected_birth_date_text)
        changeBirthDateButton = findViewById(R.id.change_birth_date_button)
        continueButton = findViewById(R.id.continue_button)
        resultsContainer = findViewById(R.id.results_container)
        horoscopeSignText = findViewById(R.id.horoscope_sign_text)
        secondsTextField = findViewById(R.id.seconds_text_field)
        deathsSinceBirth = findViewById(R.id.deaths_since_birth)
        daysTextView = findViewById(R.id.days_text_view)
        sexText = findViewById(R.id.sex_text)
        foodConsumptionText = findViewById(R.id.food_consumption_text)
        secondsPassedText = findViewById(R.id.seconds_passed_text)
        deathsSinceText = findViewById(R.id.deaths_since_text)
        birthsSinceText = findViewById(R.id.births_since_text)

        setSupportActionBar(toolbarView)

        selectedBirthDate = LocalDate.now()
        changeBirthDateButton.setOnClickListener {
            showBirthDatePicker()
        }
        continueButton.setOnClickListener {
            showResults()
        }

        resultsContainer.visibility = View.GONE
        renderSelectedBirthDate()
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }

    private fun showBirthDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedBirthDate = LocalDate.of(year, month + 1, dayOfMonth)
                renderSelectedBirthDate()
                if (resultsVisible) {
                    updateDashboard(requireNotNull(timer), year, month, dayOfMonth)
                }
            },
            selectedBirthDate.year,
            selectedBirthDate.monthValue - 1,
            selectedBirthDate.dayOfMonth
        ).show()
    }

    private fun renderSelectedBirthDate() {
        selectedBirthDateText.text = "Selected birthday: ${selectedBirthDate.dayOfMonth}/${selectedBirthDate.monthValue}/${selectedBirthDate.year}"
    }

    private fun showResults() {
        if (timer == null) {
            val millisecondsInFuture: Long = 999999999
            val countDownInterval: Long = 1000
            timer = InnerCountDownCounter(millisecondsInFuture, countDownInterval, 0).also { it.start() }
        }

        resultsVisible = true
        resultsContainer.visibility = View.VISIBLE
        updateDashboard(requireNotNull(timer), selectedBirthDate.year, selectedBirthDate.monthValue - 1, selectedBirthDate.dayOfMonth)
        mainScrollView.post { mainScrollView.smoothScrollTo(0, resultsContainer.top) }
    }

    private fun updateDashboard(
        timer: InnerCountDownCounter,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int
    ) {
        if (!resultsVisible) {
            return
        }

        val current = LocalDateTime.now()

        val secondsSinceBirth =
            dateToEpoch(current.year, current.monthValue, current.dayOfMonth) -
                dateToEpoch(year, monthOfYear + 1, dayOfMonth)
        val days = secondsSinceBirth / 86400

        timer.setSecondsSinceBirth(secondsSinceBirth)

        if (current.year - year >= 15) {
            val seconds =
                dateToEpoch(current.year, current.monthValue, current.dayOfMonth) -
                    dateToEpoch(year + 15, monthOfYear + 1, dayOfMonth)
            val daysAfterFifteen = seconds / 86400
            val sexHours = daysAfterFifteen / 6
            setSexText("Du har haft sex i $sexHours timer :D:D")
        } else {
            setSexText("")
        }

        val foodConsumption = days / 2
        setFootConsumptionText("Du har spist $foodConsumption kilo mad")
        val horoscope = getHoroscopeSign(monthOfYear, dayOfMonth)
        setHoroscopeSignText("Du er født i ${horoscope.symbol} ${horoscope.name}")
    }

    private fun setHoroscopeSignText(text: String) {
        horoscopeSignText.text = text
    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    fun setSecondsText(text: String) {
        secondsTextField.text = text
    }

    fun setDeathSinceBirth(text: String) {
        deathsSinceBirth.text = text
    }

    fun setDaysText(text: String) {
        daysTextView.text = text
    }

    fun setSexText(text: String) {
        sexText.text = text
    }

    private fun setFootConsumptionText(text: String) {
        foodConsumptionText.text = text
    }

    fun setSecondsPassed(text: String) {
        secondsPassedText.text = text
    }

    fun setDeathsSince(text: String) {
        deathsSinceText.text = text
    }

    fun setBirthsSince(text: String) {
        birthsSinceText.text = text
    }

    fun dateToEpoch(year: Int, month: Int, day: Int): Long {
        return LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
    }

    inner class InnerCountDownCounter(
        private val millisInFuture: Long,
        countDownInterval: Long,
        private var secondsSinceBirth: Long
    ) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onFinish() {
            println("Timer Completed.")
        }

        override fun onTick(millisUntilFinished: Long) {
            if (!resultsVisible) {
                return
            }

            val timePassed: Long = (this.millisInFuture - millisUntilFinished) / 1000
            setSecondsPassed("Tid mens du har kigget paa skærmen: $timePassed")
            setDeathsSince("Dødsfald mens du har kigget på skærmen: " + Math.round(timePassed * 1.8))
            setBirthsSince("Fødsler mens du har kigget på skærmen: " + Math.round(timePassed * 4.0))

            if (this.secondsSinceBirth > 0) {
                setSecondsText("Du har levet for: ${timePassed + this.secondsSinceBirth} Sekunder")
                setDaysText("Du har levet for: ${(timePassed + this.secondsSinceBirth) / 86400} Dage")
                setDeathSinceBirth("Døde siden du blev født: " + (timePassed + this.secondsSinceBirth) * 2)
            } else {
                setSecondsText("")
                setDaysText("")
                setDeathSinceBirth("")
            }
        }

        fun setSecondsSinceBirth(ssb: Long) {
            this.secondsSinceBirth = ssb
        }
    }
}
