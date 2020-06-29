package com.example.optimal_buzz.charthelper

import com.example.optimal_buzz.util.TFUtil
import org.joda.time.LocalTime

/*Class that represents an abstract USER drink. Data is used to display on a chart.*/
class DrinkMoment(private val start: Float) {

    /*Variables representing starting and ending time.*/
    var startTime: Float = 0F
    var endTime: Float = 0F
    var startTimeMin: Float = 0F
    var endTimeMin: Float = 0F
    var startMinutes = TFUtil.hmFloatToMinutes(start)

    /*Initializes the "start" of the drink with a timestamp.*/
    fun startDrink() {
        var startStamp = LocalTime()
        startTimeMin = TFUtil.hmsFloatToMinutes(TFUtil.hms24ToFloat(startStamp))
        startTime = (TFUtil.hmsFloatToMinutes(TFUtil.hms24ToFloat(startStamp)) - startMinutes) / 5F
    }

    /*Initializes the "end" of a drink with a timestamp.*/
    fun finishDrink() {
        var endStamp = LocalTime()
        endTimeMin = TFUtil.hmsFloatToMinutes(TFUtil.hms24ToFloat(endStamp))
        endTime = (TFUtil.hmsFloatToMinutes(TFUtil.hms24ToFloat(endStamp)) - startMinutes) / 5F
    }


}