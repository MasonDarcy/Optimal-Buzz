package com.example.optimal_buzz.charthelper

import com.example.optimal_buzz.util.TFUtil
import org.joda.time.DateTime
import org.joda.time.LocalTime


class DrinkTimeManager {

    /*DateStamp rounded down, serving as 0 for calculations on the chart.*/
    var startTimeNew = TFUtil.roundDownDateTime(DateTime())
    /*DateStamp of the first drink started.*/
    var initialDrinkTimeStamp: DateTime? = null


    var contextDrinkMoment = DrinkMoment(startTimeNew)


}

