package com.example.optimal_buzz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.optimal_buzz.data.entities.ChartEntry
import com.example.optimal_buzz.data.entities.DrinkData
import com.example.optimal_buzz.data.entities.TimeData
import com.example.optimal_buzz.data.entities.User

@Dao
interface SessionDatabaseDao{

    /*User*/
    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM previous_session_data")
    fun getUser(): User?

    @Query("DELETE FROM previous_session_data")
    fun clearUser()



    /*Entries*/
    @Insert
    fun insertEntry(chartEntry: ChartEntry)

    @Query("DELETE FROM chart_entries")
    fun clearEntries()

    @Query("SELECT * FROM chart_entries")
    fun getAllEntries(): MutableList<ChartEntry?>


    /*TimeData*/
    @Insert
    fun insertTimeData(time: TimeData)

    @Query("DELETE FROM time_data")
    fun clearTimeData()

    @Query("SELECT * FROM time_data")
    fun getAllTimeData(): TimeData?

    /*Drink Data*/
    @Insert
    fun insertDrinkData(drinkData: DrinkData)

    @Query("DELETE FROM drink_data")
    fun clearDrinkData()

    @Query("SELECT * FROM drink_data")
    fun getAllDrinkData(): MutableList<DrinkData?>


}