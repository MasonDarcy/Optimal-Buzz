package com.example.optimal_buzz.util


import com.example.optimal_buzz.charthelper.XLabelManager
import org.joda.time.LocalTime

object GraphUtil {
    @JvmStatic

    fun initializeXLabels(list: MutableList<String>, lowerBound: LocalTime) {
        list.add(TFUtil.timeToString(lowerBound))
        var lowCopy = lowerBound
        for (i in 1..XLabelManager.INITIAL_X_LABEL_ADDED) {

            lowCopy = lowCopy.plusMinutes(5)
            list.add(TFUtil.timeToString(lowCopy))
        }

    }

    fun incrementLabel(list: MutableList<String>, timestamp: LocalTime) {
        var timestamp = timestamp.plusMinutes(5 * (list.size))
        list.add(TFUtil.timeToString(timestamp))
    }

}