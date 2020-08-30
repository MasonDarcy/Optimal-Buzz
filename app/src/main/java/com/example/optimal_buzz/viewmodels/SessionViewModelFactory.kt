package com.example.optimal_buzz.viewmodels
//
//import android.app.Application
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.optimal_buzz.data.SessionDao
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class SessionViewModelFactory(
//    private val dataSource: SessionDao,
//    private val application: Application) : ViewModelProvider.Factory {
//    @Suppress("unchecked_cast")
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(SessionViewModel::class.java)) {
//            return SessionViewModel(dataSource, application) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}