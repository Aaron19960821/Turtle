package com.yucwang.turtle.Overview

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class OverviewHistoryListItemTest {

    val mUsageList: Array<Long> = arrayOf(60000, 120000, 6000000, 9000000, 45000000)
    val mDateList: Array<String> = arrayOf("2018-10-11", "2018-03-01", "2018-9-1", "2018-11-1", "2018-12-13")

    val mEncodingDateList = arrayOf("2018-10-11", "2018-03-01", "2018-09-01", "2018-11-01", "2018-12-13")
    val mDisplayUsageList = arrayOf("1min", "2min", "1h40min", "2h30min", "12h30min")

    @Test
    fun Test_OverViewHistoryListItemDatabaseEncoding() {
        for (i in mDateList.indices) {
            val item = OverviewHistoryListItem.getInstanceFromDatabase(mDateList.get(i), 0)
            assert(mEncodingDateList.get(i).equals(item.encodeDateForDataBase()))
        }
    }

    @Test
    fun Test_OverviewHistoryListItemUsageDisplay() {
        for (i in mUsageList.indices) {
            val item = OverviewHistoryListItem(Date(), mUsageList.get(i))
            assert(item.encodeUsageForDatabase() == mUsageList.get(i))
            assert(item.getFormattedUsage().equals(mDisplayUsageList.get(i)))
        }
    }
}