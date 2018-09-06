package com.yucwang.turtle.Overview

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.text.format.Time
import java.util.*

class HistoryListDatabase(mContext: Context) {
    val mDBHelper = HistoryListDBHelper(mContext)

    // Add a new item to database
    fun insertHistoryList(historyListItem: OverviewHistoryListItem) {
        val database = mDBHelper.writableDatabase

        val values = ContentValues().apply {
            put(HistoryListDBHelper.DATABASE_COLUMN_DATE, historyListItem.encodeDateForDataBase())
            put(HistoryListDBHelper.DATABASE_COLUMN_USAGE, historyListItem.encodeUsageForDatabase())
        }

        val rowId = database.insert(HistoryListDBHelper.DATABASE_TABLENAME, null, values)
    }

    /**
     * Get all History list items from database
     * Will sort historylist with time decreasing order
     */
    fun getAllHistoryList(): Array<OverviewHistoryListItem> {
        val database = mDBHelper.readableDatabase

        val projection = arrayOf(HistoryListDBHelper.DATABASE_COLUMN_DATE, HistoryListDBHelper.DATABASE_COLUMN_USAGE)
        val sortOrder = "${HistoryListDBHelper.DATABASE_COLUMN_DATE} DESC"

        val cursor = database.query(
                HistoryListDBHelper.DATABASE_TABLENAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        )

        val historyList = Array<OverviewHistoryListItem>(getHistoryCount().toInt(), { OverviewHistoryListItem(Date(), Time()) })
        var current = 0
        with (cursor) {
            if (moveToFirst()) {
                while (!isAfterLast) {
                    val curDate = getString(cursor.getColumnIndexOrThrow(HistoryListDBHelper.DATABASE_COLUMN_DATE))
                    val curUsage = getInt(cursor.getColumnIndexOrThrow(HistoryListDBHelper.DATABASE_COLUMN_USAGE))
                    historyList[current++] = OverviewHistoryListItem.getInstanceFromDatabase(curDate, curUsage)
                    moveToNext()
                }
            }
        }
        return historyList
    }

    /**
     * Update the history list when the record of a day is existed.
     * Do not expose to the public.
     */
    private fun updateHistoryList(historyListItem: OverviewHistoryListItem, database: SQLiteDatabase) {
        val values = ContentValues()
        val constraints = "${HistoryListDBHelper.DATABASE_COLUMN_DATE} = ?"
        val whereArgs = arrayOf(historyListItem.encodeDateForDataBase())

        values.put(HistoryListDBHelper.DATABASE_COLUMN_DATE, historyListItem.encodeDateForDataBase())
        values.put(HistoryListDBHelper.DATABASE_COLUMN_USAGE, historyListItem.encodeUsageForDatabase())

        val rowAffected = database.update(
                HistoryListDBHelper.DATABASE_TABLENAME,
                values,
                constraints,
                whereArgs
        )

        assert(rowAffected == 1)
    }

    /**
     * Return the size of the history list
     */
    fun getHistoryCount(): Long {
        val database = mDBHelper.readableDatabase
        val result = DatabaseUtils.queryNumEntries(database, HistoryListDBHelper.DATABASE_TABLENAME)

        return result
    }
}