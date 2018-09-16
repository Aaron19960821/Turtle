package com.yucwang.turtle.Overview

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HistoryListDBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "historylist.db"
        const val DATABASE_COLUMN_DATE = "date"
        const val DATABASE_COLUMN_USAGE = "usage"
        const val DATABASE_TABLENAME = "historylist"

        const val SQL_CREATE_ENTRIES =
                "CREATE TABLE IF NOT EXISTS ${DATABASE_TABLENAME} (" +
                        "${DATABASE_COLUMN_DATE} TEXT PRIMARY KEY," +
                        "${DATABASE_COLUMN_USAGE} INTEGER)"
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(HistoryListDBHelper.SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int){
        // TODO: need to be designed.
    }
}