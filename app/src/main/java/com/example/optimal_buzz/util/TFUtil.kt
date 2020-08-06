package com.example.optimal_buzz.util

import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat

object TFUtil {
    @JvmStatic

    fun datetimeToString(stamp: DateTime): String {
        var format = DateTimeFormat.forPattern("h:mm")
        return format.print(stamp)
    }


fun roundDownDateTime(date: DateTime): DateTime {
    var x = 0
    while(true) {
        var output = date.minusMinutes(1 * x)
        if(output.minuteOfHour().asText.toInt() % 5 == 0) {
            return output.withSecondOfMinute(0).withMillisOfSecond(0)
        }
        x++
    }
}

    fun getMinDuration(initial: DateTime?, end: DateTime): Float {
        var dur = Duration(initial, end)
        var min = dur.standardMinutes.toFloat()
        min += end.secondOfMinute().asText.toFloat() / 60F
        return min
    }
}