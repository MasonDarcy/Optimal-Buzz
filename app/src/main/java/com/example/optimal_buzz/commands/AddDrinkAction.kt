package com.example.optimal_buzz.commands

import androidx.lifecycle.MutableLiveData
import com.example.optimal_buzz.model.Drink
import java.util.*

class AddDrinkAction(var drink: Drink, var dl: LinkedList<Drink>, var dls: MutableLiveData<Boolean>): Action() {

    override fun undo() {
        /*Removes the last entry from the list.*/
       dl.removeLast()
       dls.value = !dls.value!!
    }

    override fun redo() {
        dl.addFirst(drink)
        dls.value = !dls.value!!
    }

}