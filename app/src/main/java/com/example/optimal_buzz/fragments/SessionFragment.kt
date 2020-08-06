package com.example.optimal_buzz.fragments


//import androidx.test.core.app.ApplicationProvider.getApplicationContext
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.optimal_buzz.R
import com.example.optimal_buzz.charthelper.GraphConstants
import com.example.optimal_buzz.charthelper.TimeXAxisValueFormatter
import com.example.optimal_buzz.databinding.SessionFragmentBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import pl.droidsonroids.gif.GifImageView


class SessionFragment : Fragment() {

    private lateinit var graph: LineChart
    private lateinit var lineDataSet: LineDataSet
    private lateinit var lineData: LineData
    private lateinit var valueFormatter: TimeXAxisValueFormatter
    private val model: SessionViewModel by activityViewModels()
    private lateinit var binding: SessionFragmentBinding
    private var numDrinks = 0

/*Fragment lifecycle callbacks*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<SessionFragmentBinding>(
            inflater,
            R.layout.session_fragment, container, false
        )
        /*Initializes several aspects of the chart.*/
        setupGraph()
        /*Button text context*/
        setDrinkStatus()
        /*Populate queue*/
        populateQueueLayout()

        updateXLabels()
    if(model.chartEntries.isNotEmpty()) {
        snapToEntry()
    }

         update()
        /*SETUP LISTENERS--------------------------------------------------------------------------*/
        binding.buttonAddDrink.setOnClickListener {
            toBeerWizard()

        }
        binding.buttonStartDrink.setOnClickListener {

            if (model.drinkList.isEmpty()) {
                drinkEmptyError()
            } else {
                when (binding.buttonStartDrink.text) {
                    "Start Drink" -> {
                        binding.buttonStartDrink.text = "Finish Drink"
                        fillBeerAnimation()
                        drink()
                        update()
                    }
                    else -> {
                        binding.buttonStartDrink.text = "Start Drink"
                       stopDrink()
                        update()
                    }
                }
            }
        }
        model.xLabelManager.xLabelClock.viewSignal.observe(viewLifecycleOwner, Observer {
            updateNumXLabels()
            valueFormatter.updateList(model.xLabelManager.xLabelList)
            graph.setVisibleXRangeMaximum(GraphConstants.SNAP_VISIBLE_X_RANGE_MAX)
      //      binding.textSuggestedTimer?.text = "Num labels: " + model.xLabelManager.xLabelList.size.toString() +
        //            "\n Starting time: " + model.xLabelManager.lowerBoundInitialTime.toString() + "weight: " + model.user.weight.toString()

            update()
        })
        model.user.currentBac.observe(viewLifecycleOwner, Observer {
            binding.textBac?.text = floatToBacString(model.user.currentBac.value)
        })
//        model.user.nextDrinkTime.observe(viewLifecycleOwner, Observer {
//
////        if(model.drinkList.isEmpty()) {
////            binding.textSuggestedTimer?.text = "Add a drink"
////        } else {
////            binding.textSuggestedTimer?.text = "Finish drink by: " + TFUtil.addHoursToTime(model.user.nextDrinkTime.value)
////        }
//
//        })
        model.yAxisMaximum.observe(viewLifecycleOwner, Observer {
        var yAxisLeft: YAxis = graph.axisLeft
        var yAxisRight: YAxis = graph.axisRight
        yAxisRight.isEnabled = false
        yAxisLeft.axisMaximum = model.yAxisMaximum.value!!
        yAxisLeft.axisMinimum = 0F
        graph.invalidate()
    })

        /*------------------------------------------------------------------------------------------*/

        return binding.root
    }

    @Override
    override fun onResume() {
        super.onResume()

        updateXLabels()
        update()
      //  binding.textSuggestedTimer?.text = "Num labels: " + model.xLabelManager.xLabelList.size.toString() +
        //        "\n Starting time: " + model.xLabelManager.lowerBoundInitialTime.toString() + "weight: " + model.user.weight.toString() + "x:" + x.toString()
    }

//    @Override
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState);
//
//        if (savedInstanceState != null) {
//            //Restore the fragment's state here
//        }
//    }
//    @Override
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//
//        //Save the fragment's state here
//    }


    /*Drinking functions-------------------------------------*/
    private fun drink() {
        model.initiateDrink()
        openBeerSound()
        setCurrentDrinkLabel()
        snapToEntry()
        binding.textSuggestedTimer?.text = model.chartEntries[0].x.toString()
    }
    private fun stopDrink() {
        model.completeDrink()
        finishBeerSound()
        snapToEntry()
        popDrinkView()
        binding.textSuggestedTimer?.text = model.chartEntries[1].x.toString()

    }
    /*-------------------------------------------------------*/


   /* Sound functions */
    private fun openBeerSound() {
       val mp: MediaPlayer =
           MediaPlayer.create(activity, R.raw.open_can)
       mp.start()
   }

    private fun finishBeerSound() {
        val mp: MediaPlayer =
            MediaPlayer.create(activity, R.raw.finished_drink)
        mp.start()
    }


    /*Error check*/
    private fun drinkEmptyError() {
        val errorString = "Add a drink!"
        val errorToast =
        Toast.makeText(activity?.applicationContext, errorString, Toast.LENGTH_SHORT)
        errorToast.show()
    }

    /*Drink linked list Representation functions-----------*/
    private fun populateQueueLayout() {
        if(model.drinkList.isNotEmpty()) {
                for (x in 0..model.drinkList.size - 1) {
                    addDrinkView()
                }
        }
    }

    private fun addDrinkView() {
        var gifView = GifImageView(activity)
        gifView.adjustViewBounds = true

            gifView.setPadding(0, 0, 0, 0)
            if(model.user.isDrinking && numDrinks == 0) {
                gifView.setImageResource(R.drawable.full_beer)
            } else {
                gifView.setImageResource(R.drawable.empty_beer)

            }
            gifView.setBackgroundResource(R.color.lightest)


        binding.linearlayoutDrinks?.addView(gifView)
    numDrinks++
    }
    private fun fillBeerAnimation() {
      var beerGif: GifImageView = binding.linearlayoutDrinks?.getChildAt(0) as GifImageView
      beerGif.setImageResource(R.drawable.full_beer)
    }
    private fun popDrinkView() {
        binding.linearlayoutDrinks?.removeViewAt(0)
    }

    /*Navigation function to move to the BeerWizard fragment.*/
    private fun toBeerWizard() {
        view?.findNavController()?.navigate(
            SessionFragmentDirections.actionSessionFragmentToBeerWizardFragment()
        )
    }
    /*---------------------------------------------------------*/

    /*Graph view commands--------------------------------------*/
    private fun setupGraph() {
        graph = binding.graphBac as LineChart
        valueFormatter =
            TimeXAxisValueFormatter(
                model.xLabelManager.xLabelList
            )
        graph.xAxis.valueFormatter = valueFormatter
        //1. Create a DataSet object
        lineDataSet = LineDataSet(model.chartEntries, "Drink times")
        //2. Create LineData object and pass lineDataSet
        lineData = LineData(lineDataSet)
        //3.Pass the LineData object to the view
        graph.data = lineData

        graph.isDragDecelerationEnabled = true
        graph.dragDecelerationFrictionCoef = GraphConstants.DRAG_FRICTION_COEFFICIENT

        setHighlight()
        setAxes()
        setLabelStyling()
        setDescription()
        setLimitLine()
    }
    private fun setAxes() {
        setXAxisParams()
        setYAxisParams()
    }
    private fun setXAxisParams() {
        var xAxis: XAxis = graph.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisMaximum = model.xLabelManager.currentNumXLabels.toFloat() - 1
        xAxis.axisMinimum = 0F
        xAxis.labelCount = model.xLabelManager.xLabelList.size
        xAxis.granularity = GraphConstants.X_AXIS_GRANULARITY
        graph.setVisibleXRangeMaximum(GraphConstants.INITIAL_VISIBLE_X_RANGE_MAX)
     //   xAxis.textColor = Color.WHITE
        lineDataSet.notifyDataSetChanged()
        graph.notifyDataSetChanged()
        graph.invalidate()
    }
    private fun setYAxisParams() {
        var yAxisLeft: YAxis = graph.axisLeft
        var yAxisRight: YAxis = graph.axisRight
        yAxisRight.isEnabled = false
        yAxisLeft.axisMaximum = GraphConstants.INITIAL_Y_AXIS_MAX
        yAxisLeft.axisMinimum = 0F
        graph.invalidate()
    }
    private fun updateNumXLabels() {
        var xAxis = graph.xAxis
        xAxis.labelCount = model.xLabelManager.xLabelList.size
        xAxis.axisMaximum = model.xLabelManager.currentNumXLabels.toFloat() - 1

    }
    private fun setDescription() {
        var chartDescription = Description()
        chartDescription.text = "BAC"
        graph.description = chartDescription
    }
    private fun setLabelStyling() {
        lineDataSet.setDrawValues(false)
    }
    private fun setHighlight() {
        lineDataSet.isHighlightEnabled = false
        lineDataSet.setDrawHighlightIndicators(false)
     //   lineDataSet.highLightColor = BLACK
    }
    private fun setLimitLine() {
        val ll = LimitLine(0.05F)
        ll.lineWidth = GraphConstants.LIMIT_LINE_WIDTH
       // ll.lineColor = GraphConstants.LIMIT_LINE_COLOR
        ll.lineColor = Color.BLACK
        var yAxis = graph.axisLeft
        yAxis.addLimitLine(ll)
    }
    private fun update() {
        lineDataSet.notifyDataSetChanged()
        graph.notifyDataSetChanged()
        graph.invalidate()
    }
    private fun snapToEntry() {
        var index = model.chartEntries.size - 1
        var x = model.chartEntries.get(index).x
        graph.setVisibleXRangeMaximum(GraphConstants.SNAP_WIDTH_X_AXIS)
        graph.moveViewToX(x - GraphConstants.SNAP_WIDTH_X_AXIS / 2)
        graph.setVisibleXRangeMaximum(GraphConstants.SNAP_VISIBLE_X_RANGE_MAX)
    }
     private fun updateXLabels() {
        model.xLabelManager.currentNumXLabels = model.xLabelManager.xLabelList.size
        updateNumXLabels()
        valueFormatter.updateList(model.xLabelManager.xLabelList)
    }
    /*-------------------------------------------------------*/
    /*Misc Label View functions------------------------------------*/
    private fun setDrinkStatus() {
        when (model.user.isDrinking) {
            true -> {binding.buttonStartDrink.text = "Finish Drink"

                        }
            false -> binding.buttonStartDrink.text = "Start Drink"
        }
    }
    private fun setCurrentDrinkLabel() {
   //     var ml = model.drinkList.peek().ml.toString() + "ml"
   //     var abv = (model.drinkList.peek().ABV * 100F).toString() + "%"
   //     binding.textCurrentDrink.text = "$ml at $abv"
    }
    /*-------------------------------------------------------*/
    /*Misc string formatting subroutines---------------------*/
    private fun floatToBacString(f: Float?): String {
        return String.format("%.5f", f)
    }


}
