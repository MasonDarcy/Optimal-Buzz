package com.example.optimal_buzz.model

import android.os.Handler
import com.example.optimal_buzz.charthelper.DrinkTimeManager
import com.example.optimal_buzz.util.DrinkUtil
import com.example.optimal_buzz.util.TFUtil
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.LocalTime
import timber.log.Timber

class BACClock(user: User, dm: DrinkTimeManager) {

    companion object {
        const val THREE_SECONDS = 3000L
    }


    var customHandler: Handler = Handler()

    fun start() {
        customHandler.postDelayed(updateTimerThread, 0)
    }



    private val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
Timber.i("running")
            user.minDrinking = TFUtil.getMinDuration(dm.initialDrinkTimeStamp, DateTime())
            user.currentBac.value = DrinkUtil.calculateBAC(user.standardDrinksConsumed, user.minDrinking, user.weight, user.isFemale, user.isMetric, dm, user)

//            var projectedNumDrinks = user.standardDrinksConsumed + DrinkUtil.accumulateStandardDrink(Drink())
//            user.nextDrinkTime.value = DrinkUtil.suggestDrink(projectedNumDrinks, user.weight, user.isFemale, user.isMetric) * 60F

            customHandler.postDelayed(
                this,
                THREE_SECONDS
            )

        }
    }



}