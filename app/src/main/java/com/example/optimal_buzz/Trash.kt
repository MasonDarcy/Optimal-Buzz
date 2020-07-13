package com.example.optimal_buzz

import android.widget.SeekBar

//fun formatTime(stamp: Float): String {
//    var output = stamp.toString()
//    output.dropLast(2)
//    var builder = StringBuilder(output)
//
//    builder.insert(output.length - 2, ':')
//    output = builder.toString()
//    return output
//}
//
//    /*Takes a dateTime object and returns a four digit float representing hours and minutes*/
//     fun getLowerBoundLabel(date: DateTime): Float {
//
//        var hours: Float = date.hourOfDay().asShortText.toFloat() * 100
//        var mins: Float = date.minuteOfHour().asShortText.toFloat()
//
//        return  getLowerMultFiveFloat(hours + mins)
//    }
//
//    /*Returns the first lower multiple of 5 float beneath the input float. Used to create the lower
//    * bound of the xLabel list. */
//    private fun getLowerMultFiveFloat(timeInit: Float): Float {
//        var timeCopy = timeInit
//
//        while(timeCopy % 5F > 0) {
//            /*Special case of 1 OClock*/
//            if(timeCopy == 1000F)
//                return 1255F
//
//            timeCopy--
//        }
//        return timeCopy
//    }
//
//    fun populateInitialXLabels(labelData: MutableList<String>, lowerBoundTimeLabel: Float): MutableList<String> {
//        labelData.add(formatTime(lowerBoundTimeLabel))
//        var currentItem = lowerBoundTimeLabel
//        for(i in 1..TimeXAxisValueFormatter.NUM_INITIAL_LABELS) {
//
//            var newItem = incrementLabel(currentItem)
//            currentItem = newItem
//            labelData.add(formatTime(newItem))
//        }
//
//        return labelData
//    }
//
//
//    private fun incrementLabel(f: Float): Float {
//        var output = f
//
//        when {
//            output == 1255F ->       { output = 100F }
//            output % 100F == 55F ->  { output+=45F }
//            else ->                  { output+=5F }
//        }
//
//        return output
//    }
//
//    /*Take a list of labels, add the necessary labels to update the time.*/
////    fun updateLabels(list: MutableList<String>): MutableList<String>{
//
//  //  }


//             Error toasting
////            val errorToast = Toast.makeText(activity?.applicationContext, viewModel.contextBeer.ml.toString(), Toast.LENGTH_SHORT)
////            errorToast.show()

//    private fun sentinelEntry() {
//        //Initialize a point so the graph loads / this is a workaround
//        if(viewModel.created == false) {
//            var graphEntry = BacEntity(0F, 0F)
//            var testEntry = Entry(graphEntry.time, graphEntry.bac)
//            viewModel.drinkData.add(testEntry)
//        }
//        viewModel.created = true
//    }
//
//private fun testAddGraphPoint() {
//    var graphEntry = BacEntity(5F, 5F)
//    var testEntry = Entry(graphEntry.time, graphEntry.bac)
//    viewModel.drinkData.add(testEntry)
//    lineDataSet.notifyDataSetChanged()
//    graph.notifyDataSetChanged()
//    graph.invalidate()
//}
//
//private fun testManyPoints() {
//    viewModel.populateWithSampleData()
//    lineDataSet.notifyDataSetChanged()
//    graph.notifyDataSetChanged()
//    graph.invalidate()
//}

//private fun entryFactory(time: MutableLiveData<Float>): Entry {
//        val timeValue: Float = time.value!!
//        return Entry(timeValue, 5F)
//    }

//                    var startTime = viewModel.graphData.initialDrinkTime.toString()
//                    var currentTime = viewModel.graphData.contextDrinkMoment.startTimeMin
//                    var bac = viewModel.testBac.toString()
//                    var consume = viewModel.user.standardDrinksConsumed.toString()
//                    var drinkingtime = viewModel.minDrinking2.toString()
//                    binding.textDrinklistPlaceholder.text = "Bac: $bac\n Drinks#:\n $consume \nTime drinking:$drinkingtime"

/*Testing msg-----------------------------------------*/
//                var testString =
//                    viewModel.graphData.xLabelList.size
//                val errorToast =
//                    Toast.makeText(activity?.applicationContext, "lol", Toast.LENGTH_SHORT)
//                errorToast.show()
// val testToast = Toast.makeText(activity?.applicationContext, lol, Toast.LENGTH_SHORT)
// testToast.show()
/*---------------------------------------------------*/
//                var time = TFUtil.hms24ToFloat(viewModel.graphData.lowerBound24Time)
//                var getLow = TFUtil.getLow524(time)
//                var min = TFUtil.hmFloatToMinutes(getLow)
//
//                    val errorToast =
//                    Toast.makeText(activity?.applicationContext, "$time $getLow $min", Toast.LENGTH_SHORT)
//                errorToast.show()

//var minDrinking = viewModel.graphData.contextDrinkMoment.endTimeMin - viewModel.graphData.initialDrinkTime
//var drink = DrinkUtil.accumulateStandardDrink(Drink())
//var bac = DrinkUtil.calculateBAC(drink,  minDrinking, viewModel.user.weight, viewModel.user.isFemale, viewModel.user.isMetric)
//val errorToast = Toast.makeText(activity?.applicationContext, "$minDrinking $drink $bac", Toast.LENGTH_LONG)
//errorToast.show()
////0.05 1.7 90

/*Viewport stuff*/

//        graph.zoomAndCenterAnimated(1.4f, 1f, x, y, graph.axisLeft.axisDependency, 1000L)
//        var y = viewModel.chartEntries.get(index).y
//        graph.centerViewToAnimated(x, y, graph.axisLeft.axisDependency, 1000L)
//        graph.setVisibleXRangeMinimum(0.5F
//        graph
//graph.axisLeft.addLimitLine(ll)


//binding.seekbarAbv?.setOnSeekBarChangeListener(object :
//    SeekBar.OnSeekBarChangeListener {
//    override fun onProgressChanged(seek: SeekBar,
//                                   progress: Int, fromUser: Boolean) {
//        // write custom code for progress is changed
//        var value = progress.toFloat() * 0.1F
//        binding.edittextAbv.text = "%.1f".format(value) + "%"
//
//    }
//
//    override fun onStartTrackingTouch(seek: SeekBar) {
//        // write custom code for progress is started
//
//    }
//
//    override fun onStopTrackingTouch(seek: SeekBar) {
//        // write custom code for progress is stopped
//        //toast
//
//    }
//})
/* ------------*/