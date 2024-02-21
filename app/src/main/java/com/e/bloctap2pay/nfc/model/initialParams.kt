package com.e.bloctap2pay.nfc.model

import android.os.Environment
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InitialParams(
    val deviceId:String,
    val terminalId:String,
    val publicKey:String,
    val crlPinKey:String,

): Parcelable