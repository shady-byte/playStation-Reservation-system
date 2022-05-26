package com.example.playstationapp.Constants

import java.util.*

object GlobalFunctions {
    var screenWidth : Int = 0
    var screenHeight : Int = 0
    var sharedPreferencesName = ""
    const val hourPricePs4 = 20
    const val time = "Time"
    const val revenues = "Revenues"
    const val reservations = "Reservations"
    const val passKey = "PassKey"
    const val firstRun = "FirstRun"

    fun getCurrentTime(): Date {
        return Date()
    }


}