package com.example.playstationapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playstationapp.Constants.GlobalFunctions
import com.example.playstationapp.Database.DataBaseOperations
import com.example.playstationapp.ViewModels.DetailsViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailsActivity : AppCompatActivity() {
    private val releaseButton by lazy { findViewById<Button>(R.id.release_button)}
    private val cancelButton by lazy { findViewById<TextView>(R.id.cancel_reservation)}
    private val startTimeField by lazy { findViewById<TextView>(R.id.start_time_field)}
    private val endTimeField by lazy { findViewById<TextView>(R.id.end_time_field)}
    private val totalHoursField by lazy { findViewById<TextView>(R.id.hours_field)}
    private val totalPriceField by lazy { findViewById<TextView>(R.id.price_field)}

    private val viewModel by lazy {
        ViewModelProvider(this)[DetailsViewModel::class.java]}

    private lateinit var startDate : Date
    private val psName = "PlayStation"
    private var dataBaseInstance = DataBaseOperations(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_PlayStationApp)
        setContentView(R.layout.activity_details)

        //getting intent extra
        val psNumber =  intent.getStringExtra("playStationName")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "$psName $psNumber"

        //variables initialize
        getLayoutsHeight()
        val reservationTime = dataBaseInstance.getReservation("ps$psNumber")
        if(reservationTime!=null) {
            stringToDate(reservationTime)
            startTimeField.text = viewModel.startDate
        }

        releaseButton.setOnClickListener {
            viewModel.buttonActivated = false
            setButtonUnAvailable()
            calculatePrice()
            dataBaseInstance.deleteReservation("ps$psNumber")
            dataBaseInstance.updateDeviceAvailability("ps$psNumber",false) //device not available
        }

        cancelButton.setOnClickListener {
            if(viewModel.buttonActivated) {
                dataBaseInstance.deleteReservation("ps$psNumber")
                dataBaseInstance.updateDeviceAvailability("ps$psNumber",false) //device not available
                onBackPressed()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveDurableState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.restoreDurableState(savedInstanceState)
        setReservationDetails()
        if(!viewModel.buttonActivated)
            setButtonUnAvailable()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SimpleDateFormat")
    private fun stringToDate(dateString: String) {
        val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
        val date = format.parse(dateString)
        if (date != null) {
            startDate = date
        }
        viewModel.startDate = DateFormat.format("hh:mm a", date).toString()
    }

    private fun calculatePrice() {
        val endDate = GlobalFunctions.getCurrentTime()
        val endTime = DateFormat.format("hh:mm a",endDate)
        val timeDiff = endDate.time.minus(startDate.time)
        val hours = timeDiff / (1000*60*60)
        val minutes = (timeDiff - 1000*60*60*hours) / (1000*60)
        var totalTime = "0$hours:"
        totalTime += if(minutes.toString().length<2)
            "0$minutes"
        else
            "$minutes"
        val value = (hours + (minutes.toFloat()/60))*GlobalFunctions.hourPricePs4

        viewModel.endDate = endTime.toString()
        viewModel.time = totalTime
        viewModel.price = "${value.toInt()} LE"
        setReservationDetails()
        saveTOSharedPreferences(timeDiff,value.toInt())
    }

    @SuppressLint("SetTextI18n")
    private fun setReservationDetails() {
        startTimeField.text = viewModel.startDate
        endTimeField.text = viewModel.endDate
        totalHoursField.text = viewModel.time
        totalPriceField.text = viewModel.price

    }

    private fun setButtonUnAvailable() {
       releaseButton.isClickable = viewModel.buttonActivated
       releaseButton.setBackgroundColor(ContextCompat.getColor(this,R.color.lightGrey))
    }


    private fun getLayoutsHeight() {
        val params2 = releaseButton.layoutParams
        params2.height = (GlobalFunctions.screenHeight * 0.12).toInt()
        releaseButton.layoutParams = params2
    }

    private fun saveTOSharedPreferences(time: Long,price: Int) {
        val list = loadFromSharedPreferences()
        val storage = getSharedPreferences(GlobalFunctions.sharedPreferencesName, MODE_PRIVATE)
        val editor = storage.edit()
        editor.putInt(GlobalFunctions.revenues, if(list[0]!=-1) list[0]+price else price)
        editor.putInt(GlobalFunctions.time, if(list[1]!=-1) list[1]+time.toInt() else time.toInt())
        editor.putInt(GlobalFunctions.reservations,if(list[2]!=-1) ++list[2] else 1)
        editor.apply()
    }

    private fun loadFromSharedPreferences() : ArrayList<Int> {
        val storage = getSharedPreferences(GlobalFunctions.sharedPreferencesName, MODE_PRIVATE)
        val list = ArrayList<Int>()
        list.add(storage.getInt(GlobalFunctions.revenues,-1))
        list.add(storage.getInt(GlobalFunctions.time,-1))
        list.add(storage.getInt(GlobalFunctions.reservations,-1))

        return list
    }
}