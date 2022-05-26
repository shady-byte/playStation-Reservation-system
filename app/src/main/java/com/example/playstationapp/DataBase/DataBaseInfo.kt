package com.example.playstationapp.Database

object DataBaseInfo {
    //table reservations create query
    const val Sql_Create_Table_Reservations_Query = "CREATE TABLE ${ReservationsTableInfo.Table_Name} (" +
            "${ReservationsTableInfo.Column_1} TEXT PRIMARY KEY," +
            "${ReservationsTableInfo.Column_2} TEXT)"
    //table Availability create query
    const val Sql_Create_Table_Availability_Query = "CREATE TABLE ${DevicesTableInfo.Table_Name} (" +
            "${DevicesTableInfo.Column_1} TEXT PRIMARY KEY," +
            "${DevicesTableInfo.Column_2} INTEGER)"

    //table reservations delete query
    const val Sql_Delete_Table_Reservations_Query = "DROP TABLE IF EXISTS ${ReservationsTableInfo.Table_Name}"
    //table Availability delete query
    const val Sql_Delete_Table_Availability_Query = "DROP TABLE IF EXISTS ${DevicesTableInfo.Table_Name}"

    //table reservations fetch query
    const val Sql_Fetch_Row_Table_Reservations_Query =
        "SELECT ${ReservationsTableInfo.Column_2} FROM ${ReservationsTableInfo.Table_Name} WHERE ${ReservationsTableInfo.Column_1}="
    //table availability fetch query
    const val Sql_Fetch_Row_Table_Availability_Query =
        "SELECT ${DevicesTableInfo.Column_2} FROM ${DevicesTableInfo.Table_Name} WHERE ${DevicesTableInfo.Column_1}="


    object ReservationsTableInfo {
        const val Table_Name = "playstation_reservations"
        const val Column_1 = "deviceNumber"
        const val Column_2 = "startTime"
    }

    object DevicesTableInfo {
        const val Table_Name = "playstation_Availability"
        const val Column_1 = "deviceNumber"
        const val Column_2 = "availability"
    }
}