package com.example.optimal_buzz.fragments

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.optimal_buzz.R
import com.example.optimal_buzz.databinding.BeerWizardFragmentBinding
import com.example.optimal_buzz.model.Drink



/**
 * A simple [Fragment] subclass.
 * Use the [BeerWizardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BeerWizardFragment : Fragment() {

    private lateinit var binding: BeerWizardFragmentBinding
    private val model: SessionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<BeerWizardFragmentBinding>(
            inflater,
            R.layout.beer_wizard_fragment, container, false
        )

        binding.seekbarBeerMl?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                // write custom code for progress is changed
//                when(seek.progress) {
//                    0 -> binding.textBeerMl.text = "248ml"
//                    1 -> binding.textBeerMl.text = "354ml"
//                    2 -> binding.textBeerMl.text = "473ml"
//                    3 -> binding.textBeerMl.text = "567ml"
//                    4 -> binding.textBeerMl.text = "750ml"
//                    5 -> binding.textBeerMl.text = "1892ml"
//                }

                binding.textBeerMl.text = mapProgressToMl(progress).toString() + "ml"

            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started

            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
              //toast

            }
        })



        binding.buttonSelectDrink.setOnClickListener {
            toSession()
        }

        return binding.root
    }

    private fun toSession() {
       addDrink()
        slideGlassSound()
        view?.findNavController()?.navigate(
           BeerWizardFragmentDirections.actionBeerWizardFragmentToSessionFragment()
        )

    }

    private fun addDrink() {
        var ml = mapProgressToMl(binding.seekbarBeerMl.progress)
        var abv: Float = (binding.edittextAbv.text.toString().toFloat()) / 100F
        model.drinkList.add(Drink(ml.toFloat(), abv))
    }


    private fun mapProgressToMl(progress: Int): Int {
        return when(progress) {
            0 -> 248
            1 -> 354
            2 -> 473
            3 -> 567
            4 -> 750
            5 -> 1892
            else -> 473
        }

    }

    private fun slideGlassSound() {
        val mp: MediaPlayer =
            MediaPlayer.create(activity, R.raw.empty_glass_sound)
        mp.start()
    }

}