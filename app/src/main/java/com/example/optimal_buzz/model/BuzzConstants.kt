package com.example.optimal_buzz.model

object BuzzConstants {

    const val STANDARD_BEER_ML = 341F
    /*Body water constants for calculating BAC*/
    const val MALE_BW = 0.58F
    const val FEMALE_BW = 0.49F
    /*----------------------------------------*/

    /*Metabolism constants--------------------*/
    const val MALE_MB = 0.015F
    const val FEMALE_MB = 0.017F
    /*----------------------------------------*/

    const val BODY_WATER = 0.806F
    const val SWEDISH_NUM = 1.2F

    const val LB_TO_KG = 0.45359F
    /*Calculations will be done with one SD being 10 grams of ethanol.
    For example, in Canada, one SD is 13.6 grams and in the US it's 14 grams. */
    const val STANDARD_DRINK_GRAMS = 10F



}