package com.example.optimal_buzz

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.optimal_buzz.data.SessionDatabase
import com.example.optimal_buzz.data.SessionDao
import com.example.optimal_buzz.global.SessionApplication
import com.example.optimal_buzz.util.DBUtil
import com.example.optimal_buzz.viewmodels.SessionViewModel
import timber.log.Timber

//@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val model: SessionViewModel by viewModels()
    private lateinit var dao: SessionDao
 //   private lateinit var db: SessionDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dao = SessionApplication.DBModule.provideSessionDao(this)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())
        Timber.i("onCreate called")
       if(savedInstanceState == null) {
                initializeDb()
                Timber.i ("Inside savedInstanceConditional block")
                DBUtil.coroutineRetrieveData(model, dao)
        }
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
        super.onStop()
        DBUtil.coroutineClearSetData(model, dao)
        Timber.i("onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        object : CountDownTimer(1000, 1000) {
            override fun onFinish() {
            DBUtil.endJob()
            }
            override fun onTick(millisUntilFinished: Long) {}
        }.start()
        Timber.i("onDestroy Called")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.i("onRestart Called")
    }

/*Database operations*/
private fun initializeDb() {
//    db = Room.databaseBuilder(this, SessionDatabase::class.java, "MasterDB")
//        .allowMainThreadQueries()
//        .build()
//    db = SessionApplication.DBModule.provideSessionDb(this)
//    dao = SessionApplication.DBModule.provideSessionDao(this)
}

}
