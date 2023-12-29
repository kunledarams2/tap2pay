package com.e.bloctap2pay.nfc.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.annotation.NonNull
import kotlinx.parcelize.Parcelize
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//import kotlinx.android.parcel.Parcelize
//import kotlinx.android.parcel.Parcelize
import java.util.*

@Keep
//@Entity
@Parcelize
data class TransactionResult(
//    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Int = 0,
    var responseCode: String = "-99",
    var responseDescription: String = "Failed transaction",
    var authCode: String = "NA",
    var amount: Double = 0.0,
    var mid: String = "NA",
    var tid: String = "NA",
    var merchantName: String = "NA",
    var maskedPan: String = "NA",
    var expiryDate: String = "NA",
    var cardholderName: String = "NA",
    var refNumber: String = "NA",
    var stan: String = "NA",
    var transType: String = "NA",
    var tsi: String = "",
    var tvr: String = "",
    var aid: String = "",
    var transactionDate: Long = Date().time,
    var terminalID:String = "NA",
    var operatorID:String = "NA"
) : Parcelable

@Keep
data class RemoteResponse(
    val auth: String,
    val icc: String,
    val meaning: String,
    val resp: String
)

data class  CardDebitResponse (
    val status:String,
    val data:RemoteResponse,
    val message: String
        )


enum class CardAccountType constructor(val code: String) {
    Default("000000"), Savings("001000"), Current("002000"), Credit("003000")
}