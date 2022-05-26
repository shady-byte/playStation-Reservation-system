package com.example.playstationapp.LifeCycles

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import com.example.playstationapp.Constants.GlobalFunctions
import com.example.playstationapp.Database.DataBaseOperations
import com.example.playstationapp.DetailsActivity
import com.example.playstationapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivityLifeCycle(private val context : Context,private val lifecycle: Lifecycle): DefaultLifecycleObserver {

    val dataBaseInstance = DataBaseOperations(context)

    init {
        lifecycle.addObserver(this)
    }

    fun navigateToDetailsActivity(ps: String) {
        val intent = Intent(context, DetailsActivity::class.java)
        intent.putExtra("playStationName",ps)
        context.startActivity(intent)
    }

    fun reservePlayStation(ps: Button, deviceName: String) {
        val startTime = GlobalFunctions.getCurrentTime()
        dataBaseInstance.insertReservation(deviceName, startTime.toString())
        setButtonReserved(ps)
    }

    fun adjustLayoutsHeight(layout1: LinearLayout,layout2:LinearLayout,psButton:Button) {
        layout1.layoutParams.height = (GlobalFunctions.screenHeight * 0.12).toInt()
        layout2.layoutParams.height = (GlobalFunctions.screenHeight * 0.12).toInt()
        val params = psButton.layoutParams
        params.height = (GlobalFunctions.screenHeight * 0.12).toInt()
        psButton.layoutParams = params
    }

    fun checkButtonColor(ps:Button,psName:String) {

        when(dataBaseInstance.getDeviceAvailability(psName)) {
           0 -> { setButtonReserved(ps) }
           1 -> { setButtonAvailable(ps) }
           -1 -> { Log.d("DetailsActivity","no $psName found with this name") }
        }
    }

    private fun setButtonReserved(ps:Button) =
        ps.setBackgroundColor(ContextCompat.getColor(context, R.color.lightRed))
    private fun setButtonAvailable(ps:Button) =
        ps.setBackgroundColor(ContextCompat.getColor(context, R.color.lightGreen))

}