package com.example.optimal_buzz

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.optimal_buzz.data.entities.ChartEntry
import com.example.optimal_buzz.data.entities.User
import com.example.optimal_buzz.data.SessionDatabase
import com.example.optimal_buzz.data.SessionDatabaseDao
import com.example.optimal_buzz.data.entities.DrinkData
import com.example.optimal_buzz.data.entities.TimeData
import com.example.optimal_buzz.viewmodels.SessionViewModel
import com.example.optimal_buzz.model.Drink
import com.github.mikephil.charting.data.Entry
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val model: SessionViewModel by viewModels()
    private lateinit var sessionDao: SessionDatabaseDao
    private lateinit var db: SessionDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())
        Timber.i("onCreate called")
        if(savedInstanceState == null) {
            initializeDb()
            reinstatePreviousData()
            closeDb()
        }

    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Timber.i("OnSavedInstanceState called")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Timber.i("OnRestoreInstanceState called")
    }



    override fun onStart() {
        super.onStart()
        Timber.i("onStart Called")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume Called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause Called")
    }

    override fun onStop() {
        initializeDb()
        clearAndSetNewData()
        closeDb()
        super.onStop()

        Timber.i("onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy Called")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.i("onRestart Called")
    }

/*Database operations*/
private fun initializeDb() {
    //val context = InstrumentationRegistry.getInstrumentation().targetContext
    db = Room.databaseBuilder(this, SessionDatabase::class.java, "MasterDB")
        // Allowing main thread queries, just for testing.
        .allowMainThreadQueries()
        .build()
    sessionDao = db.sessionDatabaseDao
}

private fun closeDb() {
    db.close()
}

private fun clearAndSetNewData() {
    //clear
    clearAllDb()

    //Insert data
    insertUserData()
    insertEntryData()
    insertTimeData()
    insertDrinkData()

   // Timber.i("Inserted: " + sd.isFemale + sd.isMetric +"\n")
}
/*clear database*/
private fun clearAllDb() {
    sessionDao.clearUser()
    sessionDao.clearEntries()
    sessionDao.clearDrinkData()
    sessionDao.clearTimeData()
}
/*database insert subroutines*/
private fun insertUserData(){
    var sd = User(
        1,
        model.user.isFemale,
        model.user.isMetric,
        model.user.hasStartedDrinking,
        model.user.isCurrentlyDrinking,
        model.user.standardDrinksConsumed,
        model.user.finishedOneDrink,
        model.yAxisMaximum.value,
        model.user.weight,
        model.oldSession
    //    model.numDrinksInQueue
    )
    sessionDao.insertUser(sd)
    }
private fun insertEntryData() {
    for(x in 0 until model.chartEntries.size) {
        var dbEntry = ChartEntry(
            x,
            model.chartEntries[x].x,
            model.chartEntries[x].y
        )
        sessionDao.insertEntry(dbEntry)
    }

}
private fun insertTimeData(){
    var td = TimeData(
        1,
        model.zeroTime,
        model.dm.initialDrinkTimeStamp
    )
    sessionDao.insertTimeData(td)
}
private fun insertDrinkData(){
    for(x in 0 until model.drinkList.size) {
        var de = DrinkData(
            x,
            model.drinkList[x].ml,
            model.drinkList[x].ABV
        )
        sessionDao.insertDrinkData(de)
    }
}
/*database retrieval subroutines*/
private fun reinstatePreviousData() {
 retrieveUser()
 retrieveEntries()
 retrieveTimeData()
 retrieveDrinkData()
}
private fun retrieveTimeData(){
        var td: TimeData? = sessionDao.getAllTimeData()
        if (td != null) {
            model.zeroTime = td.zeroDate
            model.dm.contextDrinkMoment.initialStart = td.zeroDate
            model.xLabelManager.lowerBoundInitialDateTime = td.zeroDate
            model.dm.initialDrinkTimeStamp = td.firstDrinkDate
        }
    }
private fun retrieveEntries() {
    var entries: MutableList<ChartEntry?> = sessionDao.getAllEntries()
    if(entries != null) {
        for (entry in entries) {
            model.chartEntries.add(Entry(entry!!.x, entry.y))
        }
    }
}
private fun retrieveUser() {
        var user: User? = sessionDao.getUser()
        if (user != null) {
            model.user.isFemale = user.isFemale
            model.user.isMetric = user.isMetric
            model.user.hasStartedDrinking = user.hasStartedDrinking
            model.user.standardDrinksConsumed = user.currentDrinksConsumed
            model.user.finishedOneDrink = user.finishedOneDrink
            model.yAxisMaximum.value = user.yMax
            model.user.weight = user.weight
            model.user.isCurrentlyDrinking = user.isCurrentlyDrinking
            model.oldSession = user.firstLaunch
        }
    }
private fun retrieveDrinkData() {
    var dd: MutableList<DrinkData?> = sessionDao.getAllDrinkData()
    if(dd != null) {
        for (e in dd) {
            model.drinkList.add(Drink(e!!.ml, e.abv))
        }
    }
}

}
