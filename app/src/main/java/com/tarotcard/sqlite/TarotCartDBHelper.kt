package com.tarotcard.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

import java.util.ArrayList

class TarotCardDBHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertTarotCard(tarotCard: TarotCardModel): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DBContract.TarotCardEntry.COLUMN_ID, tarotCard.id)
        values.put(DBContract.TarotCardEntry.COLUMN_NAME, tarotCard.name)
        values.put(DBContract.TarotCardEntry.COLUMN_DESCRIPTION, tarotCard.description)

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DBContract.TarotCardEntry.TABLE_NAME, null, values)

        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteTarotCard(id: Int): Boolean {
        // Gets the data repository in write mode
        val db = writableDatabase
        // Define 'where' part of query.
        val selection = DBContract.TarotCardEntry.COLUMN_ID + " LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(id.toString())
        // Issue SQL statement.
        db.delete(DBContract.TarotCardEntry.TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun readTarotCard(id: Int): ArrayList<TarotCardModel> {
        val users = ArrayList<TarotCardModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.TarotCardEntry.TABLE_NAME + " WHERE " + DBContract.TarotCardEntry.COLUMN_ID + "='" + id + "'", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var name: String
        var age: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                name = cursor.getString(cursor.getColumnIndex(DBContract.TarotCardEntry.COLUMN_NAME))
                age = cursor.getString(cursor.getColumnIndex(DBContract.TarotCardEntry.COLUMN_DESCRIPTION))

                users.add(TarotCardModel(id, name, age))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return users
    }

    fun readAllTarotCards(): ArrayList<TarotCardModel> {
        val tarotCards = ArrayList<TarotCardModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.TarotCardEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var id: Int
        var name: String
        var age: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                id = cursor.getInt(cursor.getColumnIndex(DBContract.TarotCardEntry.COLUMN_ID))
                name = cursor.getString(cursor.getColumnIndex(DBContract.TarotCardEntry.COLUMN_NAME))
                age = cursor.getString(cursor.getColumnIndex(DBContract.TarotCardEntry.COLUMN_DESCRIPTION))

                tarotCards.add(TarotCardModel(id, name, age))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return tarotCards
    }

    fun populateTable() {
        insertTarotCard(
            TarotCardModel(1, "The World", "The World card in the tarot deck has a dancing figure at the center. The dancing figure on the card has one leg crossed over the other and holds a wand in either hand. She symbolizes balance and evolution in movement. The fulfillment and unity that she represents is not one that is static, but ever-changing, dynamic and eternal.\n" +
                    "\n" +
                    "The green wreath of flowers that surrounds the central figure is a symbol of success, while the red ribbons that wrap around it are reminiscent of infinity. There are four figures on each corner of the card - and they are the same ones that are in the Wheel of Fortune. The four figures represent Scorpio, Leo, Aquarius and Taurus - representative of the four corners of the universe, the four elements, and the four evangelicals. Together, they symbolize the harmony between all of their energies.")
        )
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "TarotCards.db"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.TarotCardEntry.TABLE_NAME + " (" +
                    DBContract.TarotCardEntry.COLUMN_ID + " TEXT PRIMARY KEY," +
                    DBContract.TarotCardEntry.COLUMN_NAME + " TEXT," +
                    DBContract.TarotCardEntry.COLUMN_DESCRIPTION + " TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.TarotCardEntry.TABLE_NAME
    }

}