package com.example.optimal_buzz.commands

import androidx.lifecycle.MutableLiveData
import com.example.optimal_buzz.model.Drink
import com.example.optimal_buzz.model.User
import com.example.optimal_buzz.util.DrinkUtil
import com.github.mikephil.charting.data.Entry
import java.util.*

class FinishDrinkAction(var e: Entry, var drink: Drink, var el: MutableList<Entry>, var u: User, var dl: LinkedList<Drink>, var dls: MutableLiveData<Boolean>): Action() {


    override fun undo() {

        if(el.isNotEmpty()) {
            /*Removes the last entry from the list.*/
            el.removeAt(el.size - 1)
            /*Remove the alcohol from the user.*/
            u.standardDrinksConsumed -= DrinkUtil.accumulateStandardDrink(drink)
            /*UserData is now drinking again*/
            u.isCurrentlyDrinking.value = !u.isCurrentlyDrinking.value!!
            /* put the drink back on the stack*/
            dl.addFirst(drink)
            /*Signal that the drink list has changed*/
            dls.value = !dls.value!!
        }
    }

    override fun redo() {
        /*Removes the last entry from the list.*/
        el.add(e)
        /*Remove the alcohol from the user.*/
        u.standardDrinksConsumed += DrinkUtil.accumulateStandardDrink(drink)
        /*UserData is now drinking again*/
        u.isCurrentlyDrinking.value = !u.isCurrentlyDrinking.value!!
        /* put the drink back on the stack*/
        dl.removeFirst()
        /*Signal that the drink list has changed*/
        dls.value = !dls.value!!
    }

}