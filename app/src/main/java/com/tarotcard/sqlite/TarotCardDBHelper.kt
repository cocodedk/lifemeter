package com.tarotcard.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

import java.util.ArrayList

import CSVFile
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.example.first.R
import java.io.File
import java.io.InputStream

class TarotCardDBHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DROP_TABLE)
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
            db.execSQL(SQL_CREATE_TABLE)
            return ArrayList()
        }

        var name: String
        var age: String
        var meaning: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                name = cursor.getString(cursor.getColumnIndex(DBContract.TarotCardEntry.COLUMN_NAME))
                age = cursor.getString(cursor.getColumnIndex(DBContract.TarotCardEntry.COLUMN_DESCRIPTION))
                meaning = cursor.getString(cursor.getColumnIndex(DBContract.TarotCardEntry.COLUMN_MEANING))

                users.add(TarotCardModel(id, name, age, meaning))
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
            db.execSQL(SQL_CREATE_TABLE)
            return ArrayList()
        }

        var id: Int
        var name: String
        var description: String
        var meaning: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                id = cursor.getInt(cursor.getColumnIndex(DBContract.TarotCardEntry.COLUMN_ID))
                name = cursor.getString(cursor.getColumnIndex(DBContract.TarotCardEntry.COLUMN_NAME))
                description = cursor.getString(cursor.getColumnIndex(DBContract.TarotCardEntry.COLUMN_DESCRIPTION))
                meaning = cursor.getString(cursor.getColumnIndex(DBContract.TarotCardEntry.COLUMN_MEANING))

                tarotCards.add(TarotCardModel(id, name, description, meaning))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return tarotCards
    }

    fun populateTable() {

        val tarotCards =  ArrayList<Array<String>>()

        val file: File = File("res/raw/tarot_cards.csv")

        Log.d("Log", "File open")

        tarotCards.add(arrayOf(
            "",
            "",
            ""))


    }

    fun getCount() : Int{
        var cursor: Cursor? = null;
        val db = writableDatabase
        try{
            cursor = db.rawQuery(SQL_GET_COUNT, null)
        }catch (e: SQLiteException) {
            return 0
        }

        var c: Int = 0
        if(cursor!!.moveToFirst()) {
            c = cursor.getInt(cursor.getColumnIndex("c"))
        }
        cursor.close()
        return c
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "TarotCards.db"

        private val SQL_CREATE_TABLE =
            "CREATE TABLE " + DBContract.TarotCardEntry.TABLE_NAME + " (" +
                    DBContract.TarotCardEntry.COLUMN_ID + " TEXT PRIMARY KEY," +
                    DBContract.TarotCardEntry.COLUMN_NAME + " TEXT," +
                    DBContract.TarotCardEntry.COLUMN_DESCRIPTION + " TEXT," +
                    DBContract.TarotCardEntry.COLUMN_MEANING + " TEXT)"

        private val SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + DBContract.TarotCardEntry.TABLE_NAME

        private val SQL_GET_COUNT = "SELECT COUNT(*) as c FROM ${DBContract.TarotCardEntry.TABLE_NAME}"
    }

}