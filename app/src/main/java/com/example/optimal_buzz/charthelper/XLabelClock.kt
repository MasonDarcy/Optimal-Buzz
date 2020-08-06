package com.example.optimal_buzz.charthelper

import android.os.Handler
import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import com.example.optimal_buzz.util.GraphUtil
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.LocalTime

/*Class description: holds a timer object that will intermittently signal that the xlabels have changed. */
class LabelClock(xLabelData: XLabelManager) {

    companion object {
      const val UPDATE_INTERVAL_MILLI = 1000L
    }

    /*Clock code---------------------------------------------------------------*/
    /*Timer data-----------------------------------------*/
    var startTime: Long = 0
    var timeInMilliseconds: Long = 0
    var customHandler: Handler = Handler()
    var isActive: Boolean = false
    var viewSignal: MutableLiveData<Boolean> = MutableLiveData(false)
    fun start() {
        startTime = SystemClock.uptimeMillis()
        customHandler.postDelayed(updateTimerThread, 0)
        isActive = true
    }
    fun stop() {
        customHandler.removeCallbacks(updateTimerThread)
        isActive = false
    }
    private val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime
            GraphUtil.incrementLabel(xLabelData.xLabelList, xLabelData.lowerBoundInitialDateTime, DateTime())
            signal()
            xLabelData.currentNumXLabels = xLabelData.xLabelList.size
            customHandler.postDelayed(
                this,
                UPDATE_INTERVAL_MILLI
            )
        }
    }
    /*Signal an observer in the controller that a change has been made.*/
    private fun signal() {
        if (viewSignal.equals(true)) {
            viewSignal.postValue(false)
        } else {
            viewSignal.postValue(true)
        }
    }
/*--------------------------------------------------------------------------*/

}