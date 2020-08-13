package com.example.optimal_buzz.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime

@Entity(tableName = "time_data")
data class TimeData(
    @PrimaryKey(autoGenerate = true)
    var identifier: Int = 1,

    @ColumnInfo(name = "initialDateStamp")
    var zeroDate: DateTime,

    @ColumnInfo(name = "firstDrinkDateStamp")
    var firstDrinkDate: DateTime?
)