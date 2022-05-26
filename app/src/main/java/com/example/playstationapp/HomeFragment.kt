package com.example.playstationapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.example.playstationapp.LifeCycles.HomeActivityLifeCycle

class HomeFragment : Fragment(){
    private lateinit var ps1Button : Button
    private lateinit var ps2Button : Button
    private lateinit var ps3Button : Button
    private lateinit var ps4Button : Button
    private lateinit var ps5Button : Button
    private lateinit var layout1 : LinearLayout
    private lateinit var layout2 : LinearLayout
    private val lifeCycle by lazy { HomeActivityLifeCycle(requireContext(),lifecycle) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home,container,false)

        //variables initialization
        ps1Button = view.findViewById(R.id.playStation1_button)
        ps2Button = view.findViewById(R.id.playStation2_button)
        ps3Button = view.findViewById(R.id.playStation3_button)
        ps4Button = view.findViewById(R.id.playStation4_button)
        ps5Button = view.findViewById(R.id.playStation5_button)
        layout1 = view.findViewById(R.id.linearLayout)
        layout2 = view.findViewById(R.id.linearLayout2)

        //giving screen size to layouts
        lifeCycle.adjustLayoutsHeight(layout1,layout2,ps5Button)

        //buttons listeners
        ps1Button.setOnClickListener {
            handlePlayStationOne()
        }
        ps2Button.setOnClickListener {
            handlePlayStationTwo()
        }
        ps3Button.setOnClickListener {
            handlePlayStationThree()
        }
        ps4Button.setOnClickListener {
            handlePlayStationFour()
        }
        ps5Button.setOnClickListener {
            handlePlayStationFive()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        lifeCycle.checkButtonColor(ps1Button,"ps1")
        lifeCycle.checkButtonColor(ps2Button,"ps2")
        lifeCycle.checkButtonColor(ps3Button,"ps3")
        lifeCycle.checkButtonColor(ps4Button,"ps4")
        lifeCycle.checkButtonColor(ps5Button,"ps5")
    }

    private fun handlePlayStationOne() {
        when(lifeCycle.dataBaseInstance.getDeviceAvailability("ps1")) {
            0 -> {
                lifeCycle.navigateToDetailsActivity("1")
            }
            1 -> {
                lifeCycle.reservePlayStation(ps1Button,"ps1")
                lifeCycle.dataBaseInstance.updateDeviceAvailability("ps1",true) //device available
            }
            -1 -> {
            }
        }
    }
    private fun handlePlayStationTwo() {
        when(lifeCycle.dataBaseInstance.getDeviceAvailability("ps2")) {
            0 -> {
                lifeCycle.navigateToDetailsActivity("2")
            }
            1 -> {
                lifeCycle.reservePlayStation(ps2Button,"ps2")
                lifeCycle.dataBaseInstance.updateDeviceAvailability("ps2",true) //device available
            }
        }
    }
    private fun handlePlayStationThree() {
        when(lifeCycle.dataBaseInstance.getDeviceAvailability("ps3")) {
            0 -> {
                lifeCycle.navigateToDetailsActivity("3")
            }
            1 -> {
                lifeCycle.reservePlayStation(ps3Button,"ps3")
                lifeCycle.dataBaseInstance.updateDeviceAvailability("ps3",true) //device available
            }
        }
    }
    private fun handlePlayStationFour() {
        when(lifeCycle.dataBaseInstance.getDeviceAvailability("ps4")) {
            0 -> {
                lifeCycle.navigateToDetailsActivity("4")
            }
            1 -> {
                lifeCycle.reservePlayStation(ps4Button,"ps4")
                lifeCycle.dataBaseInstance.updateDeviceAvailability("ps4",true) //device available
            }
        }
    }
    private fun handlePlayStationFive() {
        when(lifeCycle.dataBaseInstance.getDeviceAvailability("ps5")) {
            0 -> {
                lifeCycle.navigateToDetailsActivity("5")
            }
            1 -> {
                lifeCycle.reservePlayStation(ps5Button,"ps5")
                lifeCycle.dataBaseInstance.updateDeviceAvailability("ps5",true) //device available
            }
        }
    }
}