package com.example.first

import android.content.Context
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log.d
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker.OnDateChangedListener
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.time.LocalDateTime
import java.util.Date


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        TextOutput.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (event.getAction() === KeyEvent.KEYCODE_ENTER) { // Handle pressing "Enter" key here
                handled = true
                changeText()
            }
            handled
        }

        bigButton.setOnClickListener {
            changeText()
            it.hideKeyboard()
        }

        ResetTextButton.setOnClickListener {
            TextOutput.text = "Hello World :?>"
            NameTextField.setText("")
            it.hideKeyboard()
        }

        DatePickerDialog.setOnDateChangedListener{ view, year, monthOfYear, dayOfMonth ->

            var handled = false

            var date = Date()

            val current = LocalDateTime.now();

            val today = current.dayOfMonth
            val month = current.month
            val this_year = current.year

            d( " diff: " , )

            d(
                "Selected date",
                year.toString() + " : " + monthOfYear.toString() + " : " + dayOfMonth.toString()
            )

            handled

        }

    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    fun changeText() {
        TextOutput.text = NameTextField.text
    }
}
