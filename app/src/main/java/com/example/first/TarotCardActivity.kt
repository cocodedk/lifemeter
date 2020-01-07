package com.example.first

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tarotcard.sqlite.TarotCardDBHelper

class TarotCardActivity : AppCompatActivity() {

    lateinit var tarotCardDBHelper: TarotCardDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarot_card)

        tarotCardDBHelper = TarotCardDBHelper(this)
        tarotCardDBHelper.populateTable()

    }
}
