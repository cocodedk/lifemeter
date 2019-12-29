package com.example.first

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.time.*
import java.time.LocalDate.parse
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val millisecondsInFuture:Long = 999999999
        val countDownInterval:Long = 1000


        val timer = InnerCountDownCounter(millisecondsInFuture, countDownInterval, 0)
        timer.start()

        DatePickerDialog.setOnDateChangedListener{ view, year, monthOfYear, dayOfMonth ->

            val current = LocalDateTime.now()

            val secondsSinceBirth = dateToEpoch(current.year, current.monthValue, current.dayOfMonth) - dateToEpoch(year, monthOfYear+1, dayOfMonth)
            val days = secondsSinceBirth / 86400

            timer.setSecondsSinceBirth(secondsSinceBirth)

            Toast.makeText(this, "Scanned: " + timer.getSecondsSinceBirth(), Toast.LENGTH_LONG).show()
            if(current.year - year>= 15) {
                val Secounds = dateToEpoch(current.year, current.monthValue, current.dayOfMonth) - dateToEpoch(year+15, monthOfYear+1, dayOfMonth)
                val Days = Secounds / 86400
                val Sex = Days * 10 / 60
                setSexText("Du har haft sex i $Sex timer :D:D")
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
        SecondsTextField.setText(text)
    }

    fun setDeathSinceBirth(text: String) {
        DeathsSinceBirth.setText(text)
    }

    fun setDaysText(text: String) {
        DaysTextView.setText(text)
    }

    fun setSexText(text: String) {
        SexText.setText(text)
    }

    fun setFootConsumptionText(text: String) {
        FoodConsumptionText.setText(text)
    }

    fun setSecondsPassed(text: String) {
        SecondsPassedText.setText(text)
    }

    fun setDeathsSince(text: String) {
        DeathsSinceText.setText(text)
    }

    fun setBirthsSince(text: String) {
        BirtsSinceText.setText(text)
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
        countDownInterval: Long,
        private var secondsSinceBirth:Long
    ) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onFinish() {
            println("Timer Completed.")
        }

        override fun onTick(millisUntilFinished: Long) {
            val timePassed:Long = (this.millisInFuture - millisUntilFinished) / 1000
            setSecondsPassed("Tid mens du kigger paa skærmen: $timePassed")
            setDeathsSince("Dødsfald mens du kigger på skærmen: " +  Math.round(timePassed * 1.8) )
            setBirthsSince("Fødsler mens du kigger på skærmen: " +  Math.round(timePassed * 4.0) )

            if(this.secondsSinceBirth > 0) {
                timePassed + this.secondsSinceBirth
                setSecondsText("Du har levet for: ${timePassed + this.secondsSinceBirth} Sekunder")
                setDaysText("Du har levet for: ${(timePassed + this.secondsSinceBirth)/86400} Dage")
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

        fun getSecondsSinceBirth(): Long {
            return this.secondsSinceBirth
        }
    }
}
