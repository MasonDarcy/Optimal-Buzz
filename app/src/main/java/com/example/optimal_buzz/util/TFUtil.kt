package com.example.optimal_buzz.util

import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import java.text.DecimalFormat

object TFUtil {
    @JvmStatic

    fun timeToString(stamp: LocalTime): String {
        var format = DateTimeFormat.forPattern("h:mm")
        return format.print(stamp)
    }

    fun timeToStringAMPM(stamp: LocalTime): String {
        var format = DateTimeFormat.forPattern("h:mm a")
        return format.print(stamp)
    }


    fun hmsFloatToMinutes(f: Float): Float {
        var intCopy = f.toInt()
        var secondsToMinutes = (f % 100) / 60
        var minutesToMinutes = (intCopy / 100) % 100
        var hoursToMinutes: Int = (intCopy / 10000) * 60

        return secondsToMinutes + minutesToMinutes + hoursToMinutes

    }

    /*Convert 2:45 --> minutes*/
    fun hmFloatToMinutes(f: Float): Float {
        var copy = f
        var minutesToMinutes = copy % 100
        var hoursToMinutesInt: Int = (copy.toInt() / 100)

        return minutesToMinutes + hoursToMinutesInt.toFloat() * 60

    }

    fun hmTimeToFloat(stamp: LocalTime): Float {
        var format = DateTimeFormat.forPattern("hhmm")
        return format.print(stamp).toFloat()

    }

    fun getLow524(f: Float): Float {
        var temp: Int = f.toInt()
        temp /= 100
        while (temp % 5F > 0) {
            /*Special case of 1 OClock*/
            if (temp == 0)
                temp = 2355
            else {
                temp--
            }
        }

        return temp.toFloat()
    }

    fun hms24ToFloat(stamp: LocalTime): Float {
        var format = DateTimeFormat.forPattern("HHmmss")
        return format.print(stamp).toFloat()
    }

    fun getLow5(timeInit: Float): LocalTime {
        var timeCopy = timeInit

        while (timeCopy % 5F > 0) {
            /*Special case of 1 OClock*/
            if (timeCopy == 1000F)
                return LocalTime(12, 55)

            timeCopy--
        }
        var hour = timeCopy / 100
        var minute = timeCopy % 100

        return LocalTime(hour.toInt(), minute.toInt())
    }

    fun addHoursToTime(mins: Float?): String? {
        var currentStamp = LocalTime()
        var futureStamp = mins?.toInt()?.let { currentStamp.plusMinutes(it) }
        if (mins != null) {
            if (mins < 2F) {
                return "Anytime"
        }

        }
        return futureStamp?.let { timeToStringAMPM(it) }
    }
}