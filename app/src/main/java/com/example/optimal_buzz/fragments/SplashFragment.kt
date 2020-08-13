package com.example.optimal_buzz.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.optimal_buzz.R
import com.example.optimal_buzz.databinding.SplashFragmentBinding
import com.example.optimal_buzz.databinding.TitleFragmentBinding
import com.example.optimal_buzz.viewmodels.SessionViewModel
import timber.log.Timber

/**
 * A Splash [Fragment] subclass.
 */
class SplashFragment : Fragment() {

    private lateinit var binding: SplashFragmentBinding
    private val model: SessionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<SplashFragmentBinding>(
            inflater,
            R.layout.splash_fragment, container, false
        )

        if(!model.oldSession) {
            binding.buttonToSession.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.error_red, null))
        }

        /*Initialize Listeners*/
        binding.buttonToSession?.setOnClickListener {
            if(model.oldSession) {
                model.bacClock.start()
                toSession()
            } else {
                //TODO toast some error message
                errorToast()
            }
        }

        binding.buttonToTitle?.setOnClickListener {
            model.reset()
            toTitle()
        }


        return binding.root


    }

    private fun toSession() {
        view?.findNavController()?.navigate(
            SplashFragmentDirections.actionSplashFragmentToSessionFragment()
        )
    }

    private fun toTitle() {
        view?.findNavController()?.navigate(
            SplashFragmentDirections.actionSplashFragmentToTitleFragment()
        )
    }

private fun errorToast() {
                val errorToast =
                    Toast.makeText(activity?.applicationContext, "No saved data found", Toast.LENGTH_SHORT)
                errorToast.show()
}

}


