package com.example.playstationapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playstationapp.Constants.GlobalFunctions
import com.example.playstationapp.ViewModels.HomeViewModel

class RevenuesFragment : Fragment() {
    private lateinit var revenues : TextView
    private lateinit var hours : TextView
    private lateinit var reservations : TextView
    private lateinit var clearButton : Button

    //private val viewModel by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_revenues, container, false)

        //variables initialization
        revenues = view.findViewById(R.id.revenue_field)
        hours = view.findViewById(R.id.total_hours_field)
        reservations = view.findViewById(R.id.reservations_field)
        clearButton = view.findViewById(R.id.clear_button)

        val params = clearButton.layoutParams
        params.height = (GlobalFunctions.screenHeight * 0.12).toInt()
        clearButton.layoutParams = params

        //Listeners
        clearButton.setOnClickListener {
            clearStorage()
            buttonIsUnavailable()
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        loadFromSharedPreferences()
    }

    private fun setFieldsData(revenue: Int,time: String,reservation: Int) {
        revenues.text = if(revenue!=-1) "$revenue LE" else "0"
        hours.text = time
        reservations.text = if(reservation!=-1) reservation.toString()
            else "0"
    }

    private fun loadFromSharedPreferences() {
        val storage = requireContext().getSharedPreferences(GlobalFunctions.sharedPreferencesName,
            AppCompatActivity.MODE_PRIVATE)
        val revenue = storage.getInt(GlobalFunctions.revenues,-1)
        val time = storage.getInt(GlobalFunctions.time,-1)
        val reservations = storage.getInt(GlobalFunctions.reservations,-1)
        val hours =if(time!=-1) convertTime(time) else "0"
        setFieldsData(revenue,hours,reservations)
    }

    private fun convertTime(time: Int): String {
        val hours = time / (1000*60*60)
        val minutes = (time - 1000*60*60*hours) / (1000*60)
        var time = if(hours.toString().length < 2) "0$hours:" else "$hours:"
        time+= if(minutes.toString().length < 2) "0$minutes" else "$minutes"

        return time
    }

    private fun clearStorage() {
        val storage = requireContext().getSharedPreferences(GlobalFunctions.sharedPreferencesName,
            AppCompatActivity.MODE_PRIVATE)
        val editor = storage.edit()
        //editor.clear()
        editor.remove(GlobalFunctions.revenues)
        editor.remove(GlobalFunctions.time)
        editor.remove(GlobalFunctions.reservations)
        editor.apply()
    }

    private fun buttonIsUnavailable() {
        clearButton.isClickable = false
        clearButton.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.lightGrey))
    }
}