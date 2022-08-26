package com.kylecorry.oneironaut.ui

import android.content.Context
import android.text.format.DateUtils
import java.time.ZonedDateTime

class FormatService(private val context: Context) {
//    fun formatTime(
//        time: LocalTime,
//        includeSeconds: Boolean = true,
//        includeMinutes: Boolean = true
//    ): String {
//        val amPm = !prefs.use24HourTime
//        return if (amPm) {
//            time.format(DateTimeFormatter.ofPattern("h${if (includeMinutes) ":mm" else ""}${if (includeSeconds) ":ss" else ""} a"))
//        } else {
//            time.format(DateTimeFormatter.ofPattern("H${if (includeMinutes) ":mm" else ""}${if (includeSeconds) ":ss" else ""}"))
//        }
//    }

    fun formatDate(
        date: ZonedDateTime,
        includeWeekDay: Boolean = true,
        abbreviateMonth: Boolean = false
    ): String {
        return DateUtils.formatDateTime(
            context,
            date.toEpochSecond() * 1000,
            DateUtils.FORMAT_SHOW_DATE or (if (includeWeekDay) DateUtils.FORMAT_SHOW_WEEKDAY else 0) or DateUtils.FORMAT_SHOW_YEAR or (if (abbreviateMonth) DateUtils.FORMAT_ABBREV_MONTH else 0)
        )
    }

}