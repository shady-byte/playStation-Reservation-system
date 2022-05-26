package com.example.playstationapp.Database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseOperations(var context: Context) :
        SQLiteOpenHelper(context,DataBase_Name,null, DataBase_Version) {

    companion object {
        const val DataBase_Name = "reservations.db"
        const val DataBase_Version = 1
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(DataBaseInfo.Sql_Create_Table_Reservations_Query)
        p0?.execSQL(DataBaseInfo.Sql_Create_Table_Availability_Query)
        addAllDevices(p0!!)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }


    fun insertReservation(deviceName: String,startTime: String ) {
        try {
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put(DataBaseInfo.ReservationsTableInfo.Column_1,deviceName)
            cv.put(DataBaseInfo.ReservationsTableInfo.Column_2,startTime)
            db.insert(DataBaseInfo.ReservationsTableInfo.Table_Name,null,cv)
            db.close()
        }catch (ex: Exception) {

        }
    }

    @SuppressLint("Recycle", "Range")
    fun getReservation(deviceName: String) : String? {
        try {
            val db = this.readableDatabase
            val query = DataBaseInfo.Sql_Fetch_Row_Table_Reservations_Query + "'$deviceName'"
            val result = db.rawQuery(query,null)
            return if(result!=null && result.count >0) {
                result.moveToFirst()
                val startTime = result.getString(result.getColumnIndex(DataBaseInfo.ReservationsTableInfo.Column_2))
                result.close()
                db.close()
                startTime
            }else {
                result.close()
                db.close()
                null
            }

        }catch (ex: Exception) {
        }
        return null
    }

    private fun addAllDevices(db: SQLiteDatabase) {
        try {
            val cv = ContentValues()
            for(i in 1..5) {
                cv.put(DataBaseInfo.DevicesTableInfo.Column_1,"ps$i")
                cv.put(DataBaseInfo.DevicesTableInfo.Column_2,1)
                db.insert(DataBaseInfo.DevicesTableInfo.Table_Name, null,cv)
            }

        }catch (ex: Exception) {
        }
    }

    @SuppressLint("Range")
    fun getDeviceAvailability(deviceName: String) : Int {
        try {
            val db = this.readableDatabase
            val query = DataBaseInfo.Sql_Fetch_Row_Table_Availability_Query + "'$deviceName'"
            val result = db.rawQuery(query, null)
            return if(result!=null && result.count>0) {
                result.moveToFirst()
                val avail = result.getInt(result.getColumnIndex(DataBaseInfo.DevicesTableInfo.Column_2))
                result.close()
                db.close()
                avail

            }else {
                result.close()
                db.close()
                -1
            }
        }catch (ex: Exception) {
        }
        return 0
    }

    fun updateDeviceAvailability(deviceName: String, avail:Boolean) {
        try {
            //val avail = getDeviceAvailability(deviceName)
            val db = this.writableDatabase
            val cv = ContentValues()
            if(avail) {
                cv.put(DataBaseInfo.DevicesTableInfo.Column_2,0)
            }else {
                cv.put(DataBaseInfo.DevicesTableInfo.Column_2,1)
            }
            db.update(DataBaseInfo.DevicesTableInfo.Table_Name,cv,
                "${DataBaseInfo.DevicesTableInfo.Column_1}='$deviceName'", null)
        }catch (ex: Exception) {
        }
    }

    fun deleteReservation(psName:String) {
        try {
            val db = this.writableDatabase
            db.delete(DataBaseInfo.ReservationsTableInfo.Table_Name,
                "${DataBaseInfo.ReservationsTableInfo.Column_1}='$psName'",null)
        }catch (ex: Exception) {
        }

    }
}