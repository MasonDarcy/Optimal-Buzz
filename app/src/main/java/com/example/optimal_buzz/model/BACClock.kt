package com.example.optimal_buzz.model

import android.os.Handler
import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import com.example.optimal_buzz.charthelper.DrinkTimeManager
import com.example.optimal_buzz.charthelper.LabelClock
import com.example.optimal_buzz.util.DrinkUtil
import com.example.optimal_buzz.util.GraphUtil
import com.example.optimal_buzz.util.TFUtil
import org.joda.time.LocalTime

class BACClock(user: User) {

    companion object {
        const val THREE_SECONDS = 3000L
    }

    var customHandler: Handler = Handler()

    fun start() {
        customHandler.postDelayed(updateTimerThread, 0)

    }


    private val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
            var minDrinking = TFUtil.hmsFloatToMinutes(TFUtil.hms24ToFloat(LocalTime())) - DrinkTimeManager.initialDrinkTime
            user.currentBac.value = DrinkUtil.calculateBAC(user.standardDrinksConsumed, minDrinking, user.weight, user.isFemale, user.isMetric)

            customHandler.postDelayed(
                this,
                THREE_SECONDS
            )

        }
    }

}