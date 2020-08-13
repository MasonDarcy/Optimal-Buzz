package com.example.optimal_buzz.data.entities
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "drink_data")
data class DrinkData(
    @PrimaryKey()
    var key: Int,

    @ColumnInfo(name = "ml")
    var ml: Float,

    @ColumnInfo(name = "abv")
    var abv: Float
)