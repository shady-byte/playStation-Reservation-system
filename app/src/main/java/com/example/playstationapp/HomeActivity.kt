package com.example.playstationapp

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowInsets
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playstationapp.Constants.GlobalFunctions
import com.example.playstationapp.ViewModels.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.nio.charset.Charset
import javax.crypto.SecretKey
import javax.microedition.khronos.opengles.GL

class HomeActivity : AppCompatActivity() {
    private val bottomNavBar by lazy { findViewById<BottomNavigationView>(R.id.bottom_navigation_view)}
    private val viewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]}
    private var alert : AlertDialog?=null
    private var key : SecretKey?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_PlayStationApp)
        setContentView(R.layout.activity_home)

        setFragment(viewModel.getTabSelected())

        //variables initialize
        GlobalFunctions.sharedPreferencesName = this.packageName + ".psStoredData"
        getScreenSize()

        bottomNavBar.setOnItemSelectedListener {
            setFragment(it.itemId)
            viewModel.setTabSelected(it.itemId)
            true
        }
    }

    override fun onResume() {
        super.onResume()
        if(checkFirstRun()) {
            savePassCodeKey()
            showConfirmationDialog()
        }
        if(!loadPassCodeKey())
            showConfirmationDialog()
    }

    override fun onPause() {
        super.onPause()
        alert?.cancel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("TabSelected",viewModel.getTabSelected())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.setTabSelected(savedInstanceState.getInt("TabSelected"))
        setFragment(viewModel.getTabSelected())
    }

    private fun getScreenSize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val size = windowManager.currentWindowMetrics
            val insets = windowManager.currentWindowMetrics.
            windowInsets.getInsets(WindowInsets.Type.systemBars())
            GlobalFunctions.screenHeight = size.bounds.height() - insets.bottom - insets.top
            GlobalFunctions.screenWidth = size.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            GlobalFunctions.screenWidth = displayMetrics.widthPixels
            GlobalFunctions.screenHeight  = displayMetrics.heightPixels
        }
    }

    private fun setFragment(itemId: Int) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        when(itemId) {
            R.id.home -> {
                fragmentTransaction.replace(R.id.fragment_layout,HomeFragment())
            }
            R.id.revenues -> {
                fragmentTransaction.replace(R.id.fragment_layout,RevenuesFragment())
            }
        }
        fragmentTransaction.commit()
    }

    private fun showConfirmationDialog() {
        val dialogLayout = layoutInflater.inflate(R.layout.confirmation_dialog_layout,null)
        val passCodeField = dialogLayout.findViewById<EditText>(R.id.pass_code_field)
        alert = AlertDialog.Builder(this)
            .setTitle("Enter pass code")
            .setPositiveButton("Send",null)
            .setView(dialogLayout)
            .setCancelable(false)
            .show()

        val button = alert?.getButton(AlertDialog.BUTTON_POSITIVE)
        button?.setOnClickListener {
            if(passCodeField.text.isNotEmpty()) {
                if(confirmKey(passCodeField.text.toString())) {
                    saveFirstRun()
                    alert?.cancel()
                }
            }
        }
    }

    private fun confirmKey(value: String): Boolean {
        val storage = getSharedPreferences(GlobalFunctions.sharedPreferencesName, MODE_PRIVATE)
        val code = storage.getString(GlobalFunctions.passKey, null)
        return code == value
    }

    private fun savePassCodeKey() {
        val storage = getSharedPreferences(GlobalFunctions.sharedPreferencesName, MODE_PRIVATE)
        val editor = storage.edit()
        editor.putString(GlobalFunctions.passKey,"1910")
        editor.apply()
    }

    private fun loadPassCodeKey(): Boolean {
        val storage = getSharedPreferences(GlobalFunctions.sharedPreferencesName, MODE_PRIVATE)
        val code = storage.getString(GlobalFunctions.passKey, null)
        return code!=null
    }

    private fun checkFirstRun(): Boolean {
        val storage = getSharedPreferences(GlobalFunctions.sharedPreferencesName, MODE_PRIVATE)
        return storage.getBoolean(GlobalFunctions.firstRun, true)
    }

    private fun saveFirstRun() {
        val storage = getSharedPreferences(GlobalFunctions.sharedPreferencesName, MODE_PRIVATE)
        val editor =storage.edit()
        editor.putBoolean(GlobalFunctions.firstRun,false)
        editor.apply()
    }

}
