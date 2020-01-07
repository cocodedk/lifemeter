package com.example.first

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.first.ui.login.LoginActivity
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

        launchLogin()

        val millisecondsInFuture:Long = 999999999
        val countDownInterval:Long = 1000

        val timer = InnerCountDownCounter(millisecondsInFuture, countDownInterval, 0)
        timer.start()

        DatePickerDialog.setOnDateChangedListener{ view, year, monthOfYear, dayOfMonth ->

            val current = LocalDateTime.now()

            val secondsSinceBirth = dateToEpoch(current.year, current.monthValue, current.dayOfMonth) - dateToEpoch(year, monthOfYear+1, dayOfMonth)
            val days = secondsSinceBirth / 86400

            timer.setSecondsSinceBirth(secondsSinceBirth)

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
            setHoroscopeSignText("Du er født i " + getHoroscopeSign(monthOfYear, dayOfMonth))

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_tarotCards -> {
                val intent = Intent(this, TarotCardActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    fun getHoroscopeSign(month: Int, day: Int): String {
        
        val localMonth = month + 1
        
        var sign = ""
        
        if( (localMonth == 3 && day >= 21) || (localMonth == 4 && day <= 19) ) {
            sign =  "Aries, planet Mars"
        }
        else if( (localMonth == 4 && day >= 20) || (localMonth == 5 && day <= 20) ) {
            sign =  "Taurus, planets Venus & the Moon "
        }
        else if( (localMonth == 5 && day >= 21) || (localMonth == 6 && day <= 20) ) {
            sign =  "Gemini, planet Mercury"
        }
        else if( (localMonth == 6 && day >= 21) || (localMonth == 7 && day <= 22) ) {
            sign =  "Cancer, planets the Moon & Jupiter"
        }
        else if( (localMonth == 7 && day >= 23) || (localMonth == 8 && day <= 22) ) {
            sign =  "Leo, Star Sun"
        }
        else if( (localMonth == 8 && day >= 23) || (localMonth == 9 && day <= 22) ) {
            sign =  "Virgo, planet Mercury "
        }
        else if( (localMonth == 9 && day >= 23) || (localMonth == 10 && day <= 22) ) {
            sign =  "Libra, planet Venus and Saturn"
        }
        else if( (localMonth == 10 && day >= 23) || (localMonth == 11 && day <= 21) ) {
            sign =  "Scorpio, planets Mars & Pluto"
        }
        else if( (localMonth == 11 && day >= 22) || (localMonth == 12 && day <= 21) ) {
            sign =  "Sagittarius, planet Jupiter"
        }
        else if( (localMonth == 12 && day >= 22) || (localMonth == 1 && day <= 19) ) {
            sign =  "Capricorn, planets Saturn & Mars"
        }
        else if( (localMonth == 1 && day >= 20) || (localMonth == 2 && day <= 18) ) {
            sign =  "Aquarius, planets Saturn and Uranus"
        }
        else if( (localMonth == 2 && day >= 19) || (localMonth == 3 && day <= 20) ) {
            sign =  "Pisces, planets Jupiter, Neptune, Venus"
        }
        return sign
    }

    private fun setHoroscopeSignText(text: String) {
        HoroscopeSignText.setText(text)
    }

    private fun launchLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
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

    private fun setFootConsumptionText(text: String) {
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
            setSecondsPassed("Tid mens du har kigget paa skærmen: $timePassed")
            setDeathsSince("Dødsfald mens du har kigget på skærmen: " +  Math.round(timePassed * 1.8) )
            setBirthsSince("Fødsler mens du har kigget på skærmen: " +  Math.round(timePassed * 4.0) )

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
