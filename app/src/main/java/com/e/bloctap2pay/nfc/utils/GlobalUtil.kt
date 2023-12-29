package com.e.bloctap2pay.nfc.utils

import android.provider.Settings
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object GlobalUtil {

    fun currencyFormat(number: Double): String? {
        val newFormat = NumberFormat.getCurrencyInstance(Locale.US) as DecimalFormat
        val symbols = newFormat.decimalFormatSymbols
        symbols.currencySymbol = "₦" // Don't use null.
        newFormat.decimalFormatSymbols = symbols
        return newFormat.format(number)
    }


}