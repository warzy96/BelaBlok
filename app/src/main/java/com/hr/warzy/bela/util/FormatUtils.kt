package com.hr.warzy.bela.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * If the day is:
 * - today: 12:30 PM (or Today if [TimestampTextType.MESSAGE])
 * - yesterday: Yesterday
 * - max 7 days ago: Sat, Mar 7
 * - this year: Mar 6
 * - previous year: Mar 5 2017
 *
 * In case of [TimestampTextType.HELP_OTHERS] every return value (except today) is appended with " at TIME_TEXT"
 */
fun toTimestampText(date: Date): String {
    val now = Calendar.getInstance()
    val dateCalendar = Calendar.getInstance().apply { time = date }
    val daysBetween = calculateDaysBetween(now, dateCalendar)

    if (daysBetween == 0) return "Today"

    return when {
        daysBetween == 1 -> "Yesterday"
        daysBetween < 7 -> SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(date)
        else -> {
            val currentCalendar: Calendar = GregorianCalendar().apply { time = now.time }
            val requestedCalendar: Calendar = GregorianCalendar().apply { time = date }

            if (currentCalendar[Calendar.YEAR] == requestedCalendar[Calendar.YEAR]) SimpleDateFormat("MMM d", Locale.getDefault()).format(date)
            else SimpleDateFormat("MMM d yyyy", Locale.getDefault()).format(date)
        }
    }
}

/**
 * This function calculates the days between two [Calendar] instances if they are within one year apart.
 * If instances are more than one year apart we return [Int.MAX_VALUE]. In that case timestamps are shown instead of days.
 *
 * We have to do it this way since we can't rely on milliseconds because they produce the wrong result in cases such as:
 * Monday 23:59 - Tuesday 00:00 -> This would return 0 days and it should be 1.
 */
private fun calculateDaysBetween(now: Calendar, past: Calendar): Int {
    val yearDiff = (now[Calendar.YEAR] - past[Calendar.YEAR])
    return when {
        yearDiff > 1 -> Int.MAX_VALUE // Don't care about days
        // How far off we are in this year + how many days were left in the past year
        yearDiff == 1 -> now[Calendar.DAY_OF_YEAR] + (past.getActualMaximum(Calendar.DAY_OF_YEAR) - past[Calendar.DAY_OF_YEAR])
        // We are in the same year, calculate the difference in days
        else -> now[Calendar.DAY_OF_YEAR] - past[Calendar.DAY_OF_YEAR]
    }
}