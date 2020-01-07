package com.tarotcard.sqlite

import android.provider.BaseColumns

object DBContract {
    class TarotCardEntry: BaseColumns {
        companion object {
            val TABLE_NAME = "tarot_cards"
            val COLUMN_ID = "id"
            val COLUMN_NAME = "name"
            val COLUMN_DESCRIPTION = "description"
        }
    }
}