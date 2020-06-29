package com.example.optimal_buzz.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.optimal_buzz.R
import com.example.optimal_buzz.charthelper.TimeXAxisValueFormatter
import com.example.optimal_buzz.databinding.SessionFragmentBinding
import com.github.mikephil.charting.charts.*
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


/**
 * A simple [Fragment] subclass.
 */
class SessionFragment : Fragment() {

    private lateinit var graph: LineChart
    private lateinit var lineDataSet: LineDataSet
    private lateinit var lineData: LineData
    private lateinit var valueFormatter: TimeXAxisValueFormatter
    private val viewModel: SessionViewModel by activityViewModels()
    private lateinit var binding: SessionFragmentBinding


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
        /*SETUP LISTENERS--------------------------------------------------------------------------*/
        binding.buttonAddDrink.setOnClickListener {
            toBeerWizard()
        }

        binding.buttonStartDrink.setOnClickListener {
            when (binding.buttonStartDrink.text) {
                "Start Drink" -> {
                    binding.buttonStartDrink.text = "Finish Drink"
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
        viewModel.xLabelManager.xLabelClock.viewSignal.observe(viewLifecycleOwner, Observer {
            updateNumXLabels()
            valueFormatter.updateList(viewModel.xLabelManager.xLabelList)
            update()
        })

        viewModel.user.currentBac.observe(viewLifecycleOwner, Observer {
        binding.textBac?.text = viewModel.user.currentBac.value.toString()

        })


        /*------------------------------------------------------------------------------------------*/
        return binding.root
    }


    /*Drinking functions-------------------------------------*/
    private fun drink() {
        viewModel.initiateDrink()
    }
    private fun stopDrink() {
        viewModel.completeDrink()
    }
    /*-------------------------------------------------------*/

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
                viewModel.xLabelManager.xLabelList
            )
        graph.xAxis.valueFormatter = valueFormatter
        //1. Create a DataSet object
        lineDataSet = LineDataSet(viewModel.chartEntries, "Drinks")
        //2. Create LineData object and pass lineDataSet
        lineData = LineData(lineDataSet)
        //3.Pass the LineData object to the view
        graph.data = lineData
        setAxes()
        lineDataSet.notifyDataSetChanged()
        graph.notifyDataSetChanged()
        graph.invalidate()
    }
    private fun setAxes() {
        setXAxisParams()
        setYAxisParams()
    }
    private fun setXAxisParams() {
        var xAxis: XAxis = graph.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisMaximum = viewModel.xLabelManager.currentNumXLabels.toFloat() - 1
        xAxis.axisMinimum = 0F
        xAxis.labelCount = viewModel.xLabelManager.currentNumXLabels
        xAxis.granularity = 1F
        graph.setVisibleXRangeMaximum(5F)
        lineDataSet.notifyDataSetChanged()
        graph.notifyDataSetChanged()
        graph.invalidate()
    }
    private fun setYAxisParams() {
        var yAxis: YAxis = graph.axisLeft
        yAxis.axisMaximum = 0.1F
        yAxis.axisMinimum = 0F
        graph.invalidate()
    }
    private fun updateNumXLabels() {
        var xAxis = graph.xAxis
        xAxis.labelCount = viewModel.xLabelManager.currentNumXLabels
        xAxis.axisMaximum = viewModel.xLabelManager.currentNumXLabels.toFloat() - 1

    }
    private fun update() {
        lineDataSet.notifyDataSetChanged()
        graph.notifyDataSetChanged()
        graph.invalidate()
    }
    /*-------------------------------------------------------*/
    /*Button text command------------------------------------*/
    private fun setDrinkStatus() {
        when (viewModel.user.isDrinking) {
            true -> binding.buttonStartDrink.text = "Finish Drink"
            false -> binding.buttonStartDrink.text = "Start Drink"
        }
    }
}
