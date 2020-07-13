package com.example.optimal_buzz.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.optimal_buzz.model.Drink
import com.example.optimal_buzz.R
import com.example.optimal_buzz.databinding.DrinkTypeSelectorFragmentBinding




class DrinkTypeSelectorFragment : Fragment() {

    private lateinit var binding: DrinkTypeSelectorFragmentBinding
    private val model: SessionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<DrinkTypeSelectorFragmentBinding>(
            inflater,
            R.layout.drink_type_selector_fragment, container, false
        )

        binding.buttonToSession.setOnClickListener {
            toSession()
        }

        binding.textBeer.setOnClickListener {
        toBeerWizard()
        }

//        model.contextDrink =
//            Drink(10F, 10F)

        return binding.root
    }


    private fun toSession() {
      view?.findNavController()?.navigate(
            DrinkTypeSelectorFragmentDirections.actionDrinkTypeSelectionFragmentToSessionFragment()
        )
    }

    private fun toBeerWizard() {
        view?.findNavController()?.navigate(
            DrinkTypeSelectorFragmentDirections.actionDrinkTypeSelectorFragmentToBeerWizardFragment()
        )

    }


}