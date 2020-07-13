package com.example.optimal_buzz.fragments


import androidx.lifecycle.ViewModel
import com.example.optimal_buzz.model.Drink
import com.example.optimal_buzz.charthelper.DrinkTimeManager
import com.example.optimal_buzz.charthelper.XLabelManager
import com.example.optimal_buzz.model.BACClock
import com.example.optimal_buzz.model.User
import com.example.optimal_buzz.util.DrinkUtil
import com.github.mikephil.charting.data.*
import java.util.*


class SessionViewModel : ViewModel() {

    val user = User()
    val xLabelManager = XLabelManager()
    var chartEntries: MutableList<Entry> = mutableListOf()
    private val dm = DrinkTimeManager()
    private val bacClock = BACClock(user, dm)
  //  var contextDrink: Drink =
     //   Drink(0F, 0F)
    //var suggestedWait: Float = 0F

   var drinkList = LinkedList<Drink>()


    fun initiateDrink() {
        var contextDrink = drinkList[0]
        dm.contextDrinkMoment.startDrink()
        //Initialize start time if necessary
        checkSetStartTime()
        var minDrinking = dm.contextDrinkMoment.startTimeMin - dm.initialDrinkTime
        var bac = DrinkUtil.calculateBAC(
            user.standardDrinksConsumed,
            minDrinking,
            user.weight,
            user.isFemale,
            user.isMetric
        )
        //Create a starting entry for the graph.//
        chartEntries.add(Entry(dm.contextDrinkMoment.startTime, bac))
        user.isDrinking = true

        var projectedNumDrinks = user.standardDrinksConsumed + DrinkUtil.accumulateStandardDrink(contextDrink)
        user.nextDrinkTime.value = DrinkUtil.suggestDrink(projectedNumDrinks, user.weight, user.isFemale, user.isMetric) * 60F

    }
    fun completeDrink() {
        var contextDrink = drinkList[0]
        dm.contextDrinkMoment.finishDrink()
        user.standardDrinksConsumed += DrinkUtil.accumulateStandardDrink(contextDrink)


        var minDrinking = dm.contextDrinkMoment.endTimeMin - dm.initialDrinkTime
        user.minDrinking = minDrinking
        var bac = DrinkUtil.calculateBAC(
            user.standardDrinksConsumed,
            minDrinking,
            user.weight,
            user.isFemale,
            user.isMetric
        )
        checkFinished()
        chartEntries.add(Entry(dm.contextDrinkMoment.endTime, bac))
        user.isDrinking = false

        var projectedNumDrinks = user.standardDrinksConsumed + DrinkUtil.accumulateStandardDrink(contextDrink)
        user.nextDrinkTime.value = DrinkUtil.suggestDrink(projectedNumDrinks, user.weight, user.isFemale, user.isMetric) * 60F
        drinkList.removeAt(0)
    }

private fun checkSetStartTime() {
        if (!user.hasStartedDrinking) {
            user.hasStartedDrinking = true
            dm.initialDrinkTime = dm.contextDrinkMoment.startTimeMin
        }
    }
private fun checkFinished() {
    if(!user.finishedOneDrink) {
        user.finishedOneDrink = true
        bacClock.start()
    }
}

}