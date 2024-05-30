package com.example.puffnpoof

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.puffnpoof.Model.Doll
import com.example.puffnpoof.Model.Transaction
import com.example.puffnpoof.Model.User
import com.example.puffnpoof.adapter.recyclerview_transactionAdapter
import java.io.IOException
import java.io.InputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class GlobalVar : Application() {

    var activeUser: User? = null
    var OTPUser : User? = null
    var isTransaction = false

    // helper function
    // check numeric string
    fun isNumeric(toCheck: String): Boolean {
        return toCheck.all { char -> char.isDigit() }
    }

    //convert int into formatted string Indonesian currency
    fun formatIDPrice(price: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        format.maximumFractionDigits = 0
        return format.format(price)
    }

    //get local time in dd/mm/yyyy string format
    fun getCurrentLocalDateString(): String {
        val currentDateTime = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault()) // Adjusted format: dd/MM/yyyy hh:mm
        return formatter.format(currentDateTime)
    }

    fun enableActiveUser(user: User) {
        val instance = user
        activeUser = instance
    }

    fun enableOTPUser(user : User){
        OTPUser = user
    }

    fun getOTPPhoneNumber() : String{
        return OTPUser?.phoneNum ?: ""
    }


}