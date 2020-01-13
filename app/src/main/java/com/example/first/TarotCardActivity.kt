package com.example.first

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.tarotcard.sqlite.TarotCardDBHelper
import kotlinx.android.synthetic.main.activity_tarot_card.*


class TarotCardActivity : AppCompatActivity() {

    lateinit var tarotCardDBHelper: TarotCardDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarot_card)

        tarotCardDBHelper = TarotCardDBHelper(this)
        tarotCardDBHelper.populateTable()

        TarotCardCountText.setText(tarotCardDBHelper.getCount().toString())

        Toast.makeText(applicationContext, "OnCreate", Toast.LENGTH_SHORT).show()
    }



}
