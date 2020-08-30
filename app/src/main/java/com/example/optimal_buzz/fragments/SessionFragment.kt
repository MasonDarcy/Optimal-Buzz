package com.example.optimal_buzz.fragments

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.optimal_buzz.R
import com.example.optimal_buzz.charthelper.GraphConstants
import com.example.optimal_buzz.charthelper.TimeXAxisValueFormatter
import com.example.optimal_buzz.databinding.SessionFragmentBinding
import com.example.optimal_buzz.util.DrinkUtil
import com.example.optimal_buzz.util.TFUtil
import com.example.optimal_buzz.viewmodels.SessionViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import pl.droidsonroids.gif.GifImageView
import timber.log.Timber

//@AndroidEntryPoint
class SessionFragment : Fragment() {

    private lateinit var graph: LineChart
    private lateinit var lineDataSet: LineDataSet
    private lateinit var lineData: LineData
    private lateinit var valueFormatter: TimeXAxisValueFormatter
    private val model: SessionViewModel by activityViewModels()
    private lateinit var binding: SessionFragmentBinding

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
        /*Populate queue*/
        populateQueueLayout()
        /*Set y*/
        manualYSet()
        /*update xlabels*/
        updateXLabels()

        /*OnCreate, snap to the newest entry so the user doesn't get lost.*/
        if(model.chartEntries.isNotEmpty()) {
        snapToEntry()
    }

       /*Set hint for user at top*/
        setHint()

         update()
        /*SETUP LISTENERS--------------------------------------------------------------------------*/
        binding.buttonAddDrink.setOnClickListener {
            toBeerWizard()
        }
        binding.buttonStartDrink.setOnClickListener {

            if (model.drinkList.isEmpty()) {
                drinkEmptyError()
                var test = model.gendertest.toString()
                Timber.i("Test from db: $test")
            } else {
                if(!model.user.isCurrentlyDrinking.value!!) {
                        fillBeerAnimation()
                        drink()
                        update()
                    } else  {
                       stopDrink()
                        update()
                    }
                }
            }
        binding.buttonUndo?.setOnClickListener {
            model.undo()
            setHint()
        }
       binding.buttonHome?.setOnClickListener {
           toHome()
       }
       /*redo
       binding.buttonRedo?.setOnClickListener {
          model.redo()
       } */

        model.xLabelManager.xLabelClock.viewSignal.observe(viewLifecycleOwner, Observer {
            updateNumXLabels()
            valueFormatter.updateList(model.xLabelManager.xLabelList)
            graph.setVisibleXRangeMaximum(GraphConstants.SNAP_VISIBLE_X_RANGE_MAX)
      //      binding.textSuggestedTimer?.text = "Num labels: " + model.xLabelManager.xLabelList.size.toString() +
        //            "\n Starting time: " + model.xLabelManager.lowerBoundInitialTime.toString() + "weight: " + model.user.weight.toString()

            update()
        })
        model.user.currentBac.observe(viewLifecycleOwner, Observer {
            binding.textBac?.text = "BAC: " + floatToBacString(model.user.currentBac.value)
        })
        model.user.nextDrinkTime.observe(viewLifecycleOwner, Observer {

        if(model.drinkList.isNotEmpty() && model.user.isCurrentlyDrinking.value!!) {
            var drinkMin = model.user.nextDrinkTime.value!!
            if(drinkMin >= 2){
            binding.textSuggestedTimer?.text = "Finish by " + TFUtil.addHoursToTime(drinkMin, model.dm.initialDrinkTimeStamp)
            } else {
                binding.textSuggestedTimer?.text = ""

            }
        } else {
            binding.textSuggestedTimer?.text = ""         //   binding.textSuggestedTimer?.text = "Finish drink by: " + model.user.nextDrinkTime.value!!

        }

       })
        model.yAxisMaximum.observe(viewLifecycleOwner, Observer {
        var yAxisLeft: YAxis = graph.axisLeft
        var yAxisRight: YAxis = graph.axisRight
        yAxisRight.isEnabled = false
        yAxisLeft.axisMaximum = model.yAxisMaximum.value!!
        yAxisLeft.axisMinimum = 0F
        graph.invalidate()
    })
        model.user.isCurrentlyDrinking.observe(viewLifecycleOwner, Observer {
    setDrinkStatus()
    })
        model.drinkListSignal.observe(viewLifecycleOwner, Observer {
        binding.linearlayoutDrinks.removeAllViews()
       populateQueueLayout()
            update()
       })

        /*------------------------------------------------------------------------------------------*/

        return binding.root
    }

    @Override
    override fun onResume() {
        super.onResume()
        updateXLabels()
        update()
    }

    /*Drinking functions-------------------------------------*/
    private fun drink() {
        model.initiateDrink()
        openBeerSound()
        setCurrentDrinkLabel()
        snapToEntry()
        setHint()
    }
    private fun stopDrink() {
        disableButtons()
        model.completeDrink()
        finishBeerSound()
        snapToEntry()
        finishBeerAnimation()
        object : CountDownTimer(2200, 2500) {
            override fun onFinish() {
                slideBeerAnimation()

                object : CountDownTimer(600, 2000) {
                    override fun onFinish() {
                        popDrinkView()
                        colorPhantomBeer()
                        setHint()
                        enableButtons()
                    }
                    override fun onTick(millisUntilFinished: Long) {}
                }.start()
            }
            override fun onTick(millisUntilFinished: Long) {}
        }.start()
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

    /*DrinkList representation functions-----------*/
    private fun populateQueueLayout() {
        var size = model.drinkList.size
        Timber.i("Number of drinks = $size")
        if(model.drinkList.isNotEmpty()) {
                for (x in 0..model.drinkList.size - 1) {
                    addDrinkView(x)
                }
        }
    }
    private fun addDrinkView(x: Int) {
        var gifView = GifImageView(activity)
        gifView.adjustViewBounds = true
        gifView.setPadding(0, 0, 0, 0)

        if(model.user.isCurrentlyDrinking.value!! && x == 0) {
                gifView.setImageResource(R.drawable.full_beer)
            } else {
                gifView.setImageResource(R.drawable.empty_beer)
            }
            gifView.setBackgroundResource(R.color.lightest)

    binding.linearlayoutDrinks?.addView(gifView)
    }

    /*GIF manipulation/animation*/
    private fun fillBeerAnimation() {
      var beerGif: GifImageView = binding.linearlayoutDrinks?.getChildAt(0) as GifImageView
      beerGif.setImageResource(R.drawable.full_beer)
    }
    private fun finishBeerAnimation() {
        var beerGif: GifImageView = binding.linearlayoutDrinks?.getChildAt(0) as GifImageView
        beerGif.setImageResource(R.drawable.drink_beer_anim)
    }
    private fun slideBeerAnimation() {
        var numChildren = binding.linearlayoutDrinks.childCount
        for(x in 0 .. numChildren - 1) {
            if(x == numChildren - 1) {
                var beerGif: GifImageView = binding.linearlayoutDrinks?.getChildAt(x) as GifImageView
                beerGif.setImageResource(R.drawable.beer_slide_loner)
            } else {
                var beerGif: GifImageView = binding.linearlayoutDrinks?.getChildAt(x) as GifImageView
                beerGif.setImageResource(R.drawable.beer_slide_cycle)
            }
        }
    }
    private fun colorPhantomBeer() {
        var numChildren = binding.linearlayoutDrinks.childCount
        if(numChildren != 0) {
            var beerGif: GifImageView = binding.linearlayoutDrinks?.getChildAt(numChildren - 1) as GifImageView
            beerGif.setImageResource(R.drawable.empty_beer)
        }

    }
    private fun popDrinkView() {
        binding.linearlayoutDrinks?.removeViewAt(0)
    }

    /*Navigation*/
    private fun toBeerWizard() {
        view?.findNavController()?.navigate(
            SessionFragmentDirections.actionSessionFragmentToBeerWizardFragment()
        )
    }
    private fun toHome() {
        view?.findNavController()?.navigate(
            SessionFragmentDirections.actionSessionFragmentToSplashFragment()
        )
    }
    /*---------------------------------------------------------*/

    /*GraphView commands--------------------------------------*/
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
    private fun manualYSet() {
        var yAxisLeft: YAxis = graph.axisLeft
        var yAxisRight: YAxis = graph.axisRight
        yAxisRight.isEnabled = false
        yAxisLeft.axisMaximum = model.yAxisMaximum.value!!
        yAxisLeft.axisMinimum = 0F
        graph.invalidate()
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
       if(model.user.isCurrentlyDrinking.value!!) {
           binding.buttonStartDrink.text = "Finish Drink"
           } else {
           binding.buttonStartDrink.text = "Start Drink"
        }
    }
    private fun setCurrentDrinkLabel() {
   //     var ml = model.drinkList.peek().ml.toString() + "ml"
   //     var abv = (model.drinkList.peek().ABV * 100F).toString() + "%"
   //     binding.textCurrentDrink.text = "$ml at $abv"
    }
    private fun setHint() {
        if (model.user.isCurrentlyDrinking.value!!) {
            binding.textMainHeader?.text = getString(R.string.main_header_finishdrink_hint)
        } else if (model.drinkList.isEmpty()) {
            binding.textMainHeader?.text = getString(R.string.main_header_add_hint)
        } else {
            binding.textMainHeader?.text = getString(R.string.main_header_startdrink_hint)
        }
    }
    private fun disableButtons() {
        binding.buttonStartDrink.isClickable = false
        binding.buttonAddDrink.isClickable = false
        binding.toolbarSession?.getChildAt(1)?.isClickable = false

        //Was for redo button
       // binding.toolbarSession?.getChildAt(1)?.isClickable = false
        binding.buttonAddDrink.background = context?.let { ContextCompat.getDrawable(it, R.drawable.button_style_session_disabled) }
        binding.buttonStartDrink.background = context?.let { ContextCompat.getDrawable(it, R.drawable.button_style_session_disabled) }


    }
    private fun enableButtons() {
        binding.buttonStartDrink.isClickable = true
        binding.buttonAddDrink.isClickable = true
        binding.toolbarSession?.getChildAt(1)?.isClickable = true
      //  binding.toolbarSession?.getChildAt(1)?.isClickable = true
        binding.buttonAddDrink.background = context?.let { ContextCompat.getDrawable(it, R.drawable.button_style_session) }
        binding.buttonStartDrink.background = context?.let { ContextCompat.getDrawable(it, R.drawable.button_style_session) }

    }
    /*-------------------------------------------------------*/
    /*Misc string formatting subroutines---------------------*/
    private fun floatToBacString(f: Float?): String {
        return String.format("%.5f", f)
    }


}
