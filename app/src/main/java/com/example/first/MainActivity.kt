package com.example.first

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.time.*
import java.time.LocalDate.parse
import java.time.format.DateTimeFormatter
import java.util.Date


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val millisecondsInFuture:Long = 999999999
        val countDownInterval:Long = 1000

        val timer = InnerCountDownCounter(millisecondsInFuture, countDownInterval)
        timer.start()

        DatePickerDialog.setOnDateChangedListener{ view, year, monthOfYear, dayOfMonth ->

            var handled = false

            var date = Date()

            val current = LocalDateTime.now();
            val today = current.dayOfMonth
            val month = current.monthValue
            val this_year = current.year

            val seconds = dateToEpoch(this_year, month, today) - dateToEpoch(year, monthOfYear+1, dayOfMonth)
            val days = seconds / 86400

            setSecondsText("Du har levet for: $seconds Sekunder")
            setDaysText("Du har levet for: $days Dage")

            if(this_year - year>= 15) {
                val mSecounds = dateToEpoch(this_year, month, today) - dateToEpoch(year+15, monthOfYear+1, dayOfMonth)
                val mDays = mSecounds / 86400
                val mSex = mDays * 10
                setSexText("Du har haft sex i $mSex minutter :D:D")
            } else {
                setSexText("")
            }

            val foodConsumption = days/2
            setFootConsumptionText("Du har spist $foodConsumption kilo mad")

        }

    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    fun setSecondsText(text: String) {
        SecondsTextField.setText(text);
    }

    fun setDaysText(text: String) {
        DaysTextView.setText(text)
    }

    fun setSexText(text: String) {
        SexText.setText(text);
    }

    fun setFootConsumptionText(text: String) {
        FoodConsumptionText.setText(text)
    }

    fun setSecondsPassed(text: String) {
        SecondsPassedText.setText(text);
    }

    fun setDeathsSince(text: String) {
        DeathsSinceText.setText(text);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateToEpoch(year: Int, month: Int, day: Int): Long {

        val parsedMonth = when(month){
            in 0..9 -> "0$month"
            else -> (month).toString()
        }

        val parsedDay = when(day){
            in 0..9 -> "0${day.toString()}"
            else -> day.toString()
        }

        val l = parse("$parsedDay-$parsedMonth-$year", DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        return l.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
    }

    inner class InnerCountDownCounter(
        private val millisInFuture: Long,
        countDownInterval: Long
    ) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onFinish() {
            println("Timer Completed.")
        }

        override fun onTick(millisUntilFinished: Long) {
            setSecondsPassed("Tid passeret mens du kigger paa skaermen  : " +  (this.millisInFuture - millisUntilFinished) / 1000)
            setDeathsSince("Antal døde mens du kigger på skærmen : " +  Math.round((this.millisInFuture - millisUntilFinished) * 1.8 / 1000) )
        }
    }
}
