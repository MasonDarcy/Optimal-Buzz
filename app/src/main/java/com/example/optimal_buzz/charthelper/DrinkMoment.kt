package com.example.optimal_buzz.charthelper

import com.example.optimal_buzz.util.TFUtil
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.LocalTime

/*Class that represents an abstract USER drink. Data is used to display on a chart.*/
class DrinkMoment(zeroedStart: DateTime) {

 /*Starting and ending times in minutes.*/
    var startTimeMin: Float = 0F
    var endTimeMin: Float = 0F

    /*DateTime representing the initial start time of the session*/
    var initialStart = zeroedStart

    /*Initializes the "start" of the drink with a timestamp.*/
    fun startDrink() {
        var currentDrinkStart = DateTime()
        var duration = Duration(initialStart, currentDrinkStart )
        startTimeMin = duration.standardMinutes.toFloat()
        startTimeMin += currentDrinkStart.secondOfMinute().asText.toFloat() / 300F
    }

    /*Initializes the "end" of a drink with a timestamp.*/
        fun finishDrink() {
        var currentDrinkEnd = DateTime()
        var duration = Duration(initialStart, currentDrinkEnd )
        endTimeMin = duration.standardMinutes.toFloat()
        endTimeMin+= currentDrinkEnd.secondOfMinute().asText.toFloat() / 300F
            }

}