package com.example.optimal_buzz.fragments


import androidx.lifecycle.ViewModel
import com.example.optimal_buzz.model.Drink
import com.example.optimal_buzz.charthelper.DrinkTimeManager
import com.example.optimal_buzz.charthelper.XLabelManager
import com.example.optimal_buzz.model.User
import com.example.optimal_buzz.util.DrinkUtil
import com.github.mikephil.charting.data.*


class SessionViewModel : ViewModel() {

    val user = User()
   // private val drinkTimeManager = DrinkTimeManager()
    val xLabelManager = XLabelManager()
    var chartEntries: MutableList<Entry> = mutableListOf()

    var contextDrink: Drink =
        Drink(0F, 0F)
    var suggestedWait: Float = 0F



    fun initiateDrink() {
        contextDrink = Drink()
        DrinkTimeManager.contextDrinkMoment.startDrink()
        //Initialize start time if necessary
        checkSetStartTime()
        var minDrinking = DrinkTimeManager.contextDrinkMoment.startTimeMin - DrinkTimeManager.initialDrinkTime
        var bac = DrinkUtil.calculateBAC(
            user.standardDrinksConsumed,
            minDrinking,
            user.weight,
            user.isFemale,
            user.isMetric
        )
        //Create a starting entry for the graph.//
        chartEntries.add(Entry(DrinkTimeManager.contextDrinkMoment.startTime, bac))
    }
    fun completeDrink() {
        DrinkTimeManager.contextDrinkMoment.finishDrink()
        user.standardDrinksConsumed += DrinkUtil.accumulateStandardDrink(contextDrink)
        var minDrinking = DrinkTimeManager.contextDrinkMoment.endTimeMin - DrinkTimeManager.initialDrinkTime
        user.minDrinking = minDrinking
        var bac = DrinkUtil.calculateBAC(
            user.standardDrinksConsumed,
            minDrinking,
            user.weight,
            user.isFemale,
            user.isMetric
        )
        checkFinished()
        chartEntries.add(Entry(DrinkTimeManager.contextDrinkMoment.endTime, bac))
    }
    private fun checkSetStartTime() {
        if (!user.hasStartedDrinking) {
            user.hasStartedDrinking = true
            DrinkTimeManager.initialDrinkTime = DrinkTimeManager.contextDrinkMoment.startTimeMin
        }
    }

private fun checkFinished() {
    if(!user.finishedOneDrink) {
        user.finishedOneDrink = true
        user.bacClock.start()
    }
}

}