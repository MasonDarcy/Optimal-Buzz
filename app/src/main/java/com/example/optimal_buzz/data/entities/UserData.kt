package com.example.optimal_buzz.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime

@Entity(tableName = "previous_session_data")
data class User(
    @PrimaryKey(autoGenerate = true)
    var identifier: Int = 1,

    @ColumnInfo(name = "isFemale")
    var isFemale: Boolean,

    @ColumnInfo(name = "isMetric")
    var isMetric: Boolean,

    @ColumnInfo(name = "hasStartedDrinking")
    var hasStartedDrinking: Boolean,

    @ColumnInfo(name = "isCurrentlyDrinking")
    var isCurrentlyDrinking: Boolean,

    @ColumnInfo(name = "drinksConsumed")
    var currentDrinksConsumed: Float,

    @ColumnInfo(name = "finishedOneDrink")
    var finishedOneDrink: Boolean,

    @ColumnInfo(name = "yMax")
    var yMax: Float?,

    @ColumnInfo(name = "weight")
    var weight: Float,

    @ColumnInfo(name = "firstLaunch")
    var firstLaunch: Boolean
)