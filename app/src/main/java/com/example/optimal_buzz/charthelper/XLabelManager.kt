package com.example.optimal_buzz.charthelper

import com.example.optimal_buzz.util.GraphUtil
import com.example.optimal_buzz.util.TFUtil
import org.joda.time.LocalTime

class XLabelManager {

    companion object {
        const val INITIAL_X_LABEL_ADDED = 4
        const val NUM_INITIAL_LABELS = INITIAL_X_LABEL_ADDED + 1
    }

     val xLabelClock = LabelClock(this)
    var xLabelList: MutableList<String> = mutableListOf()
    var currentNumXLabels = NUM_INITIAL_LABELS
    val lowerBoundInitialTime = TFUtil.getLow5(TFUtil.hmTimeToFloat(LocalTime.now()))

    init {
        xLabelClock.start()
        GraphUtil.initializeXLabels(xLabelList, lowerBoundInitialTime)
    }

}