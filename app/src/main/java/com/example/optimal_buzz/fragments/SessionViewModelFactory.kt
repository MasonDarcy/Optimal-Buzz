package com.example.optimal_buzz.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SessionViewModelFactory() : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SessionViewModel::class.java)) {
            return SessionViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}