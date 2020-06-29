package com.example.optimal_buzz.charthelper


import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter


class TimeXAxisValueFormatter(var labelData: MutableList<String>): ValueFormatter() {

    var list = labelData

    fun updateList(l: MutableList<String>) {
        list = l
    }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
          return list.getOrNull(value.toInt()) ?: value.toString()

    }

}


