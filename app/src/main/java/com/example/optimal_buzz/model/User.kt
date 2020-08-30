package com.example.optimal_buzz.model

import androidx.lifecycle.MutableLiveData


class User(var weight: Float = 0F, var isFemale: Boolean = true, var isMetric: Boolean = true) {
    var isCurrentlyDrinking: MutableLiveData<Boolean> = MutableLiveData(false)
    var standardDrinksConsumed = 0F
    var hasStartedDrinking: Boolean = false
    var finishedOneDrink: Boolean = false
    var currentBac: MutableLiveData<Float> = MutableLiveData(0F)
    var nextDrinkTime: MutableLiveData<Float> = MutableLiveData(0F)
    var minDrinking = 0F

}