package com.example.optimal_buzz.fragments


import android.os.CountDownTimer
import android.os.Handler
import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.optimal_buzz.Beer
import com.github.mikephil.charting.data.Entry
import java.text.SimpleDateFormat
import java.util.*

class SessionViewModel() : ViewModel() {

var contextBeer: Beer = Beer(0F, 0F)

     /*User supplied data, initialized to default values*/
       var userWeight: Float = 0F
       var isFemale: Boolean = true
       var isMetric: Boolean = true
     /*------------------*/

     /*Suggested wait time for next drink*/
     var suggestedWait: Float = 0F
     /*----------------------------------*/

     /*Graph entry list*/
     var drinkData: MutableList<Entry> = mutableListOf()
     var timeData = MutableLiveData<Float>(1F)


var startTime: Long = 0
var timeInMilliseconds: Long = 0
var customHandler: Handler = Handler()


     fun getDateFromMillis(d: Long): String? {
          val df = SimpleDateFormat("HH:mm:ss")
          df.timeZone = TimeZone.getTimeZone("GMT")
          return df.format(d)
     }

     fun start() {
          startTime = SystemClock.uptimeMillis()
          customHandler.postDelayed(updateTimerThread, 0)
     }

     fun stop() {
          customHandler.removeCallbacks(updateTimerThread)
     }

     private val updateTimerThread: Runnable = object : Runnable {
          override fun run() {
               timeInMilliseconds = SystemClock.uptimeMillis() - startTime
               timeData.postValue(timeData.value?.plus(1))
               customHandler.postDelayed(this, 1000)
          }
     }






}