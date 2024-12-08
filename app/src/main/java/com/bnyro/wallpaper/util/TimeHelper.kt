package com.bnyro.wallpaper.util

import java.util.Calendar

object TimeHelper {
    fun isInTimeRange(currentTimeMillis: Long, startTimeMillis: Long, endTimeMillis: Long): Boolean {
        // start and end time are on two different days in this case
        if (endTimeMillis < startTimeMillis) {
           return currentTimeMillis <= endTimeMillis || currentTimeMillis >= startTimeMillis
        }

        return currentTimeMillis in startTimeMillis..endTimeMillis
    }

    fun timeTodayInMillis(): Long {
        val calendar = Calendar.getInstance()

        return (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)) * 60 * 1000L
    }
}