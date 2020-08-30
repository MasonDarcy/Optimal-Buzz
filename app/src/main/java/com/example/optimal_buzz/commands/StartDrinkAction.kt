package com.example.optimal_buzz.commands

import androidx.lifecycle.MutableLiveData
import com.example.optimal_buzz.model.Drink
import com.example.optimal_buzz.model.User
import com.github.mikephil.charting.data.Entry
import java.util.*

class StartDrinkAction(var e: Entry, var drink: Drink, var el: MutableList<Entry>, var u: User, var dl: LinkedList<Drink>, var dls: MutableLiveData<Boolean>): Action() {


    override fun undo() {
        /*Removes the last entry from the list.*/
        el.removeAt(el.size - 1)
        /*UserData is now drinking again*/
        u.isCurrentlyDrinking.value = !u.isCurrentlyDrinking.value!!
        /*Signal that the drink list has changed (this is just to redraw the queue)*/
        dls.value = !dls.value!!
    }

    override fun redo() {
        /*UserData is now drinking again*/
        el.add(e)
        u.isCurrentlyDrinking.value = !u.isCurrentlyDrinking.value!!
        /*Signal that the drink list has changed (this is just to redraw the queue)*/
        dls.value = !dls.value!!
    }

}