package com.example.optimal_buzz.viewmodels


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.optimal_buzz.model.Drink
import com.example.optimal_buzz.charthelper.DrinkTimeManager
import com.example.optimal_buzz.charthelper.XLabelManager
import com.example.optimal_buzz.commands.Action
import com.example.optimal_buzz.commands.AddDrinkAction
import com.example.optimal_buzz.commands.FinishDrinkAction
import com.example.optimal_buzz.commands.StartDrinkAction
import com.example.optimal_buzz.data.SessionDao
import com.example.optimal_buzz.global.SessionApplication
import com.example.optimal_buzz.model.BACClock
import com.example.optimal_buzz.model.User
import com.example.optimal_buzz.util.DBUtil
import com.example.optimal_buzz.util.DrinkUtil
import com.example.optimal_buzz.util.GraphUtil
import com.example.optimal_buzz.util.TFUtil
import com.github.mikephil.charting.data.*
import kotlinx.coroutines.*
import org.joda.time.DateTime
import timber.log.Timber
import java.util.*


class SessionViewModel (application: Application) : AndroidViewModel(application) {

    private val dao = SessionApplication.DBModule.provideSessionDao(application)
    val db = SessionApplication.DBModule.provideSessionDb(application)
    var gendertest: Boolean? = false


//    private var databaseJob = Job()
//    private val uiScope = CoroutineScope(Dispatchers.Main + databaseJob)

//    fun retrieveData() {
//        uiScope.launch {
//        var nullTest = getUserGenderFromDb()
//        Timber.i("Nulltest = $nullTest")
//        if(nullTest != null) {
//            gendertest = nullTest
//        }
//        }
//    }

//    private fun saveData() {
//        Timber.i("saveData  called")
//
//        uiScope.launch {
//        saveUserData()
//        }
//    }

//    private suspend fun saveUserData() {
//        Timber.i("Save user data called")
//         withContext(Dispatchers.IO) {
//            DBUtil.clearAndSetNewData(this@SessionViewModel, dao)
//        }
//    }


//    private suspend fun getUserGenderFromDb(): Boolean? {
//        return withContext(Dispatchers.IO) {
//            DBUtil.getGender(dao)
//        }
//}

    /*UserData information*/
    val user = User()
    /*BAC calculations*/
    var zeroTime = TFUtil.roundDownDateTime(DateTime())
    val dm = DrinkTimeManager(zeroTime)
    val bacClock = BACClock(user, dm)
    /* Chart data */
    var chartEntries: MutableList<Entry> = mutableListOf()
    val xLabelManager = XLabelManager(zeroTime)
    var yAxisMaximum = MutableLiveData<Float>(0.1F)
    /*Drink containers*/
    var drinkList = LinkedList<Drink>()
    var drinkListSignal = MutableLiveData<Boolean>(false)
    /*Flag for database*/
    var oldSession = false
    /*Stack of abstract user actions for undo/redo functionality*/
    var actionHistory = LinkedList<Action>()
    /*var redoCandidates = LinkedList<Action>()*/

    fun addDrink(d: Drink) {
        drinkList.add(d)
        actionHistory.addFirst(AddDrinkAction(d, drinkList, drinkListSignal))
    }
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
        /*Add graph entry*/
        var newEntry = Entry(dm.contextDrinkMoment.startTimeMin / 5F, bac)
        chartEntries.add(newEntry)
        /*Adjust y-axis if necessary*/
        yAxisMaximum.value = GraphUtil.resizeYAxis(chartEntries, yAxisMaximum.value!!)
        user.isCurrentlyDrinking.value = true

        /*Add action to history stack*/
        actionHistory.addFirst(StartDrinkAction(newEntry, contextDrink, chartEntries, user, drinkList, drinkListSignal))
//        if(redoCandidates.isNotEmpty()) {
//            for(x in redoCandidates) {
//                if(x is FinishDrinkAction || x is StartDrinkAction) {
//                    redoCandidates.remove(x)
//                }
//            }
//        }
        var projectedNumDrinks = user.standardDrinksConsumed + DrinkUtil.accumulateStandardDrink(contextDrink)

        user.nextDrinkTime.value = DrinkUtil.suggestDrink(projectedNumDrinks, user.weight, user.isFemale, user.isMetric) * 60F

    }
    fun completeDrink() {
        var contextDrink = drinkList.peek()
        dm.contextDrinkMoment.finishDrink()
        user.standardDrinksConsumed += DrinkUtil.accumulateStandardDrink(contextDrink)


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

        user.currentBac.value = bac
        checkFinished()
        var newEntry = Entry(dm.contextDrinkMoment.endTimeMin / 5F, bac)
        chartEntries.add(newEntry)
        yAxisMaximum.value = GraphUtil.resizeYAxis(chartEntries, yAxisMaximum.value!!)
        user.isCurrentlyDrinking.value = false
        drinkList.remove()

        /*Add action to history stack*/
        actionHistory.addFirst(FinishDrinkAction(newEntry, contextDrink, chartEntries, user, drinkList, drinkListSignal))
//        if(redoCandidates.isNotEmpty()) {
//        for(x in redoCandidates) {
//            if(x is FinishDrinkAction || x is StartDrinkAction) {
//                redoCandidates.remove(x)
//            }
//        }
   //     }

        /*Drink predictions-----------------------------------------------------------------------------------*/
        var projectedNumDrinks = user.standardDrinksConsumed + DrinkUtil.accumulateStandardDrink(contextDrink)
        user.nextDrinkTime.value = DrinkUtil.suggestDrink(projectedNumDrinks, user.weight, user.isFemale, user.isMetric) * 60F

    }

fun undo() {
    if(actionHistory.isNotEmpty()) {
        actionHistory[0].undo()
    //    redoCandidates.addFirst(actionHistory[0])
        actionHistory.removeFirst()
    }
}
private fun checkSetStartTime() {
        if (!user.hasStartedDrinking) {
            user.hasStartedDrinking = true
            dm.initialDrinkTimeStamp = DateTime()
        }
    }
private fun checkFinished() {
    if(!user.finishedOneDrink) {
        user.finishedOneDrink = true
        bacClock.start()
    }
}
fun reset() {
        chartEntries.clear()
        user.hasStartedDrinking = false
        user.standardDrinksConsumed = 0F
        user.isCurrentlyDrinking.value = false
        user.finishedOneDrink = false
        drinkList.clear()
        var now = TFUtil.roundDownDateTime(DateTime())
        zeroTime = now
        dm.contextDrinkMoment.initialStart = now
        xLabelManager.lowerBoundInitialDateTime = now
        dm.initialDrinkTimeStamp = null
        yAxisMaximum.value = 0.1F

    }
//override fun onCleared() {
//    saveData()
//    Timber.i("On cleared called")
//    databaseJob.cancel()
//    super.onCleared()
//
//    }

}


//fun redo() {
//    if(redoCandidates.isNotEmpty()) {
//        redoCandidates[0].redo()
//        actionHistory.addFirst(redoCandidates[0])
//        redoCandidates.removeFirst()
//    }
//}