package com.example.optimal_buzz.util



import com.github.mikephil.charting.data.Entry
import org.joda.time.DateTime
import kotlin.math.roundToInt

object GraphUtil {
    @JvmStatic
    fun incrementLabel(list: MutableList<String>, initialStamp: DateTime, currentStamp: DateTime) {
        list.clear()
        var goalTime = TFUtil.roundDownDateTime(currentStamp).plusMinutes(25)
        var x = 0
        var finishedFlag = false

        while(!finishedFlag) {
            var time = initialStamp.plusMinutes(5 * x)
            x++
            list.add(TFUtil.datetimeToString(time))
            if(time == goalTime) {
                finishedFlag = true
            }

        }

    }

    fun resizeYAxis(list: MutableList<Entry>, currentMax: Float): Float {
        var largest = currentMax
        for (x in 0 until list.size) {
            if (list[x].y >= largest)
                largest = list[x].y
        }
        return if(largest == currentMax) {
            currentMax
        } else {
            largest + 0.02F
        }

    }


}