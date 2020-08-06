package com.example.optimal_buzz.model

class Drink(val ml: Float = BuzzConstants.STANDARD_BEER_ML, val ABV: Float = 0.05F) {

    override fun toString(): String {
        return ml.toString() + "ml " + ABV.toString()
    }

}