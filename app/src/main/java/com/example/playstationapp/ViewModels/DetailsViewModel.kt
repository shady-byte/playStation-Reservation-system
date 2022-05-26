package com.example.playstationapp.ViewModels

import android.os.Bundle
import androidx.lifecycle.ViewModel

class DetailsViewModel : ViewModel() {
    var startDate = ""
    var endDate = ""
    var time = ""
    var price = ""
    var buttonActivated = true

    fun saveDurableState(outState : Bundle) {
        outState.putBoolean("buttonActivated",buttonActivated)
        outState.putString("startDate",startDate)
        outState.putString("endDate",endDate)
        outState.putString("time",time)
        outState.putString("price",price)
    }
    fun restoreDurableState(savedState : Bundle) {
        buttonActivated = savedState.getBoolean("buttonActivated")
        startDate = savedState.getString("startDate").toString()
        endDate = savedState.getString("endDate").toString()
        time = savedState.getString("time").toString()
        price = savedState.getString("price").toString()
    }
}