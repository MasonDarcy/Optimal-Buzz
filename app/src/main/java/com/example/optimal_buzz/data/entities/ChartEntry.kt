package com.example.optimal_buzz.data.entities
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "chart_entries")
data class ChartEntry(
    @PrimaryKey()
    var key: Int,

    @ColumnInfo(name = "time")
    var x: Float,

    @ColumnInfo(name = "bac")
    var y: Float
)