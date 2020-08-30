package com.example.optimal_buzz.global
import android.app.Application
import android.content.Context
import com.example.optimal_buzz.data.SessionDatabase
import com.example.optimal_buzz.data.SessionDao
import dagger.hilt.android.qualifiers.ApplicationContext

//@HiltAndroidApp
class SessionApplication: Application() {

   // @InstallIn(ApplicationComponent::class)
   // @Module
    object DBModule {
     //   @Provides
        fun provideSessionDao(@ApplicationContext appContext: Context) : SessionDao {
            return SessionDatabase.getInstance(appContext).sessionDatabaseDao
        }

      //  @Provides
        fun provideSessionDb(@ApplicationContext appContext: Context) : SessionDatabase {
            return SessionDatabase.getInstance(appContext)
        }

    }

}