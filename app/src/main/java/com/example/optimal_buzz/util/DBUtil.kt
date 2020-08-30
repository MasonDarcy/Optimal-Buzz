package com.example.optimal_buzz.util

import com.example.optimal_buzz.data.SessionDatabase
import com.example.optimal_buzz.data.SessionDao
import com.example.optimal_buzz.data.entities.ChartEntry
import com.example.optimal_buzz.data.entities.DrinkData
import com.example.optimal_buzz.data.entities.TimeData
import com.example.optimal_buzz.data.entities.UserData
import com.example.optimal_buzz.model.Drink
import com.example.optimal_buzz.viewmodels.SessionViewModel
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.*
import timber.log.Timber

object DBUtil {
    @JvmStatic

    var databaseJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + databaseJob)

    fun endJob() {
        databaseJob.cancel()
    }

    fun closeDb(db: SessionDatabase) {
        db.close()
    }

    fun coroutineClearSetData(model: SessionViewModel, dao: SessionDao) {
        uiScope.launch {
            clearAndSetNewData(model, dao)
        }
    }

    fun coroutineRetrieveData(model: SessionViewModel, dao: SessionDao) {
        uiScope.launch {
            reinstatePreviousData(model, dao)
        }
    }

    private suspend fun clearAndSetNewData(model: SessionViewModel, dao: SessionDao) {
        //clear
        withContext(Dispatchers.IO) {
            clearAllDb(dao)
            //Insert data
            insertUserData(model, dao)
            insertEntryData(model, dao)
            insertTimeData(model, dao)
            insertDrinkData(model, dao)
        }
    }

    private suspend fun reinstatePreviousData(model: SessionViewModel, dao: SessionDao) {
        withContext(Dispatchers.IO) {
            retrieveUser(model, dao)
            retrieveEntries(model, dao)
            retrieveTimeData(model, dao)
            retrieveDrinkData(model, dao)
        }
    }

    /*clear database*/
    private fun clearAllDb(dao: SessionDao) {
        dao.clearUser()
        dao.clearEntries()
        dao.clearDrinkData()
        dao.clearTimeData()
    }

    /*database  subroutines*/
    private fun insertUserData(model: SessionViewModel, dao: SessionDao) {
        var sd = UserData(
            1,
            model.user.isFemale,
            model.user.isMetric,
            model.user.hasStartedDrinking,
            model.user.isCurrentlyDrinking.value!!,
            model.user.standardDrinksConsumed,
            model.user.finishedOneDrink,
            model.yAxisMaximum.value,
            model.user.weight,
            model.oldSession
        )
        dao.insertUser(sd)
        Timber.i("Inserted data")
    }

    private fun insertEntryData(model: SessionViewModel, sessionDao: SessionDao) {
        for (x in 0 until model.chartEntries.size) {
            var dbEntry = ChartEntry(
                x,
                model.chartEntries[x].x,
                model.chartEntries[x].y
            )
            sessionDao.insertEntry(dbEntry)
        }

    }

    private fun insertTimeData(model: SessionViewModel, dao: SessionDao) {
        var td = TimeData(
            1,
            model.zeroTime,
            model.dm.initialDrinkTimeStamp
        )
        dao.insertTimeData(td)
    }

    private fun insertDrinkData(model: SessionViewModel, dao: SessionDao) {
        for (x in 0 until model.drinkList.size) {
            var de = DrinkData(
                x,
                model.drinkList[x].ml,
                model.drinkList[x].ABV
            )
            dao.insertDrinkData(de)
        }
    }

    private fun retrieveTimeData(model: SessionViewModel, dao: SessionDao) {
        var td: TimeData? = dao.getAllTimeData()
        if (td != null) {
            model.zeroTime = td.zeroDate
            model.dm.contextDrinkMoment.initialStart = td.zeroDate
            model.xLabelManager.lowerBoundInitialDateTime = td.zeroDate
            model.dm.initialDrinkTimeStamp = td.firstDrinkDate
        }
    }

    private fun retrieveEntries(model: SessionViewModel, dao: SessionDao) {
        var entries: MutableList<ChartEntry?> = dao.getAllEntries()
        if (entries != null) {
            for (entry in entries) {
                model.chartEntries.add(Entry(entry!!.x, entry.y))
            }
        }
    }

    private fun retrieveUser(model: SessionViewModel, dao: SessionDao) {
        var user: UserData? = dao.getUser()
        if (user != null) {
            model.user.isFemale = user.isFemale
            model.user.isMetric = user.isMetric
            model.user.hasStartedDrinking = user.hasStartedDrinking
            model.user.standardDrinksConsumed = user.currentDrinksConsumed
            model.user.finishedOneDrink = user.finishedOneDrink
            model.yAxisMaximum.postValue(user.yMax)
            model.user.weight = user.weight
            model.user.isCurrentlyDrinking.postValue(user.isCurrentlyDrinking)
            model.oldSession = user.firstLaunch
        }
    }

    private fun retrieveDrinkData(model: SessionViewModel, dao: SessionDao) {
        var dd: MutableList<DrinkData?> = dao.getAllDrinkData()
        if (dd != null) {
            for (e in dd) {
                model.drinkList.add(Drink(e!!.ml, e.abv))
            }
        }
    }


    fun vanillaClearAndSetNewData(model: SessionViewModel, dao: SessionDao) {
        //clear
        clearAllDb(dao)
        //Insert data
        insertUserData(model, dao)
        insertEntryData(model, dao)
        insertTimeData(model, dao)
        insertDrinkData(model, dao)

    }

    fun vanillaReinstatePreviousData(model: SessionViewModel, dao: SessionDao) {

        retrieveUser(model, dao)
        retrieveEntries(model, dao)
        retrieveTimeData(model, dao)
        retrieveDrinkData(model, dao)

    }

}