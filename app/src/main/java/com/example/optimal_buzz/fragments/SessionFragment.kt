package com.example.optimal_buzz.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.optimal_buzz.BacEntity
import com.example.optimal_buzz.Beer
import com.example.optimal_buzz.R

import com.example.optimal_buzz.databinding.SessionFragmentBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet




/**
 * A simple [Fragment] subclass.
 */
class SessionFragment : Fragment() {
    private lateinit var viewModelFactory: SessionViewModelFactory
    private lateinit var beer: Beer
    private lateinit var graph: LineChart
    private lateinit var dataSet: LineDataSet
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

        /*Initialize ViewModelFactory*/
        viewModelFactory =
            SessionViewModelFactory()

        //viewModel = ViewModelProvider(this, viewModelFactory).get(SessionViewModel::class.java)

        /*Graph Initialization*/
        graph = binding.graphBac as LineChart


        setupGraph(viewModel.drinkData)

        /*UI updates*/
        updateWait()

        /*Listener callbacks-------------------------------*/
        binding.buttonAddDrink.setOnClickListener {
            //  initializeBeer()
            toBeerWizard()
        }

        binding.buttonStartDrink.setOnClickListener {
            startClock()
        }
        viewModel.timeData.observe(viewLifecycleOwner, Observer {
            viewModel.drinkData.add(entryFactory(viewModel.timeData))
            dataSet.notifyDataSetChanged()
            graph.notifyDataSetChanged()
            graph.invalidate()
        })
        /*------------------------------------------------------*/

        return binding.root
    }

    /*Testing use of args*/
    private fun updateWait() {
        binding.textSuggestedTimer?.text = viewModel.suggestedWait.toString()
    }

    /*Create a generic drink object for testing.*/
    private fun initializeBeer() {
        beer = Beer()
    }

    private fun stopClock() {
        viewModel.stop()
    }

    private fun startClock() {
            val errorToast = Toast.makeText(activity?.applicationContext, viewModel.contextBeer.ml.toString(), Toast.LENGTH_SHORT)
            errorToast.show()

        viewModel.start()

    }

    private fun toBeerWizard() {
        view?.findNavController()?.navigate(
            SessionFragmentDirections.actionSessionFragmentToBeerWizardFragment()
        )
    }


    /*Process of  inputting data into graph.*/
    private fun setupGraph(drinkList: MutableList<Entry>) {
        //List of entry object
        //var testList: MutableList<Entry> = mutableListOf()
        //My data object

        //var graphEntry = BacEntity(0F, 0F)

        //Wrapping BAC object in an Entry<> object

        //Add a pair of values
        // var testEntry = Entry(graphEntry.time, graphEntry.bac)
        //drinkList.add(testEntry)
        //Create a DataSet object
        dataSet = LineDataSet(drinkList, "Drinks")
        //Finally, add your LineDataSet to a LineData object -- this object holds all data / allows styling
        var lineData: LineData = LineData(dataSet)
        //Pass the LineData object to the view
        graph.data = lineData


        /*Need to call these two functions to update the graph*/
        // dataSet.notifyDataSetChanged()
        //graph.notifyDataSetChanged()
        /*----------------------------------------------------*/

        /*Redraws the graph*/
        graph.invalidate()

        setXAxisParams()
        setYAxisParams()
    }

    private fun entryFactory(time: MutableLiveData<Float>): Entry {
        val timeValue: Float = time.value!!
        return Entry(timeValue, 5F)
    }

    private fun initializeGraph() {
        binding.textBacLabel.text = User.weight.toString()
    }

    private fun setXAxisParams() {
        var xAxis: XAxis = graph.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisMaximum = 20F
        graph.invalidate()
    }

    private fun setYAxisParams() {
        var yAxis: YAxis = graph.axisLeft
        yAxis.axisMaximum = 10F
        graph.invalidate()

    }
}

