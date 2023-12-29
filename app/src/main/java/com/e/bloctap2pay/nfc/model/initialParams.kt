package com.e.bloctap2pay.nfc.model

import android.os.Environment

data class InitialParams(
    val deviceId:String,
    val terminalId:String,
    val secretKey:String,
    val crlPinKey:String,
    val appEnvironment: String,
)