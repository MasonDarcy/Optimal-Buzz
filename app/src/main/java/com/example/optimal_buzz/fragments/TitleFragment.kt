package com.example.optimal_buzz.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.optimal_buzz.R
import com.example.optimal_buzz.databinding.TitleFragmentBinding

/**
 * A Title [Fragment] subclass. Takes arguments from the user and passes them to Session Fragment.
 */
class TitleFragment : Fragment() {

    private lateinit var binding: TitleFragmentBinding
    private val model: SessionViewModel by activityViewModels()
    private val weightErrorMsg: String = "Enter your weight!"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<TitleFragmentBinding>(
            inflater,
            R.layout.title_fragment, container, false
        )

        /*Set default radio buttons checked*/
        setDefaultRadioOptions()

        /*Initialize Listeners*/
        binding.buttonSession?.setOnClickListener {
            grabAndSetUserParameters()
        }


        return binding.root
    }


    private fun grabAndSetUserParameters() {
        if (binding.editWeight.text.toString().isEmpty()) {
            showWeightError()

        } else {
            /*Declare and assign user parameters from view objects.*/
            val userWeight: Float = binding.editWeight.text.toString().toFloat()
            val isFemale: Boolean = binding.radioFemale.isChecked
            val isMetric: Boolean = binding.radioMetric.isChecked

            //Setting user object variables
            model.user.weight = userWeight
            model.user.isFemale = isFemale
            model.user.isMetric = isMetric


            /*Navigate to session fragment and pass user parameters.*/
            toSession()

        }
    }

    private fun toSession() {

        view?.findNavController()?.navigate(
            TitleFragmentDirections.actionTitleFragmentToSessionFragment()

        )
    }

    private fun showWeightError() {
        val errorToast =
            Toast.makeText(activity?.applicationContext, weightErrorMsg, Toast.LENGTH_SHORT)
        errorToast.show()
    }

    private fun setDefaultRadioOptions() {
        binding.radioFemale.isChecked = true
        binding.radioMetric.isChecked = true
    }

}


