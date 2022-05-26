package com.example.playstationapp.ViewModels

import androidx.lifecycle.ViewModel
import com.example.playstationapp.R

class HomeViewModel : ViewModel() {
    private var tabSelected = R.id.home

    fun setTabSelected(value : Int) {
        tabSelected = value
    }
    fun getTabSelected() : Int{
        return  tabSelected
    }
}