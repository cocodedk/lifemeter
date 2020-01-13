package com.tarotcard.sqlite

import android.provider.BaseColumns

object DBContract {
    class TarotCardEntry: BaseColumns {
        companion object {
            const val TABLE_NAME = "tarot_cards"
            const val COLUMN_ID = "id"
            const val COLUMN_NAME = "name"
            const val COLUMN_DESCRIPTION = "description"
            const val COLUMN_MEANING = "meaning"
        }
    }
}