package com.example.optimal_buzz.charthelper

import com.example.optimal_buzz.util.GraphUtil
import com.example.optimal_buzz.util.TFUtil
import org.joda.time.LocalTime


object DrinkTimeManager {
    /*A 24 hour timestamp rounded down to the lower bound to function as a 0 for the chart*/
    private var startTime = TFUtil.getLow524(TFUtil.hms24ToFloat(LocalTime()))

    /*Represents the time in minutes when the first drink is started as a baseline.*/
    var initialDrinkTime: Float = 0F
    var contextDrinkMoment = DrinkMoment(startTime)
}

