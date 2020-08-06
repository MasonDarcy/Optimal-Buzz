package com.example.optimal_buzz.fragments


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.optimal_buzz.model.Drink
import com.example.optimal_buzz.charthelper.DrinkTimeManager
import com.example.optimal_buzz.charthelper.XLabelManager
import com.example.optimal_buzz.model.BACClock
import com.example.optimal_buzz.model.User
import com.example.optimal_buzz.util.DrinkUtil
import com.example.optimal_buzz.util.GraphUtil
import com.github.mikephil.charting.data.*
import org.joda.time.DateTime
import java.util.*



class SessionViewModel(state: SavedStateHandle) : ViewModel() {

    val user = User()
    val xLabelManager = XLabelManager()
    var chartEntries: MutableList<Entry> = mutableListOf()
    var drinkList = LinkedList<Drink>()
    var yAxisMaximum = MutableLiveData<Float>(0.1F)
    private val dm = DrinkTimeManager()
    private val bacClock = BACClock(user, dm)

    //Saved state testing
    private val savedStateHandle = state

    fun initiateDrink() {
        var contextDrink = drinkList.peek()

        dm.contextDrinkMoment.startDrink()
        //Initialize start time if necessary
        checkSetStartTime()
        //var minDrinking = dm.contextDrinkMoment.startTimeMin - dm.initialDrinkTime
        var minDrinking = dm.contextDrinkMoment.startTimeMin

        var bac = DrinkUtil.calculateBAC(
            user.standardDrinksConsumed,
            minDrinking,
            user.weight,
            user.isFemale,
            user.isMetric,
            dm,
            user
        )
        //Create a starting entry for the graph.//
        //chartEntries.add(Entry(dm.contextDrinkMoment.startTimeMin, bac))
        chartEntries.add(Entry(dm.contextDrinkMoment.startTimeMin / 5F, bac))
        yAxisMaximum.value = GraphUtil.resizeYAxis(chartEntries, yAxisMaximum.value!!)
        user.isDrinking = true

        var projectedNumDrinks = user.standardDrinksConsumed + DrinkUtil.accumulateStandardDrink(contextDrink)
        user.nextDrinkTime.value = DrinkUtil.suggestDrink(projectedNumDrinks, user.weight, user.isFemale, user.isMetric) * 60F

    }
    fun completeDrink() {
        var contextDrink = drinkList.peek()
        dm.contextDrinkMoment.finishDrink()
        user.standardDrinksConsumed += DrinkUtil.accumulateStandardDrink(contextDrink)


       // var minDrinking = dm.contextDrinkMoment.endTimeMin - dm.initialDrinkTime
        var minDrinking = dm.contextDrinkMoment.endTimeMin
        user.minDrinking = minDrinking
        var bac = DrinkUtil.calculateBAC(
            user.standardDrinksConsumed,
            minDrinking,
            user.weight,
            user.isFemale,
            user.isMetric,
            dm,
            user
        )
        checkFinished()
        chartEntries.add(Entry(dm.contextDrinkMoment.endTimeMin / 5F, bac))
        yAxisMaximum.value = GraphUtil.resizeYAxis(chartEntries, yAxisMaximum.value!!)
        user.isDrinking = false


        drinkList.remove()
        var projectedNumDrinks = user.standardDrinksConsumed + DrinkUtil.accumulateStandardDrink(contextDrink)
        user.nextDrinkTime.value = DrinkUtil.suggestDrink(projectedNumDrinks, user.weight, user.isFemale, user.isMetric) * 60F

    }

private fun checkSetStartTime() {
        if (!user.hasStartedDrinking) {
            user.hasStartedDrinking = true
       //     dm.initialDrinkTime = dm.contextDrinkMoment.startTimeMin
            dm.initialDrinkTimeStamp = DateTime()
        }
    }
private fun checkFinished() {
    if(!user.finishedOneDrink) {
        user.finishedOneDrink = true
        bacClock.start()
    }
}

}