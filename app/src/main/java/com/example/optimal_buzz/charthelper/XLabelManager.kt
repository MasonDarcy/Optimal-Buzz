package com.example.optimal_buzz.charthelper

import com.example.optimal_buzz.util.TFUtil
import org.joda.time.DateTime


class XLabelManager {

    companion object {
        const val INITIAL_X_LABEL_ADDED = 4
        const val NUM_INITIAL_LABELS = INITIAL_X_LABEL_ADDED + 1
    }

    val xLabelClock = LabelClock(this)
    var xLabelList: MutableList<String> = mutableListOf()
    var currentNumXLabels = NUM_INITIAL_LABELS
    private val initialDateTimeStamp = DateTime()
    val lowerBoundInitialDateTime = TFUtil.roundDownDateTime(initialDateTimeStamp)



    init {
        xLabelClock.start()
    }

}