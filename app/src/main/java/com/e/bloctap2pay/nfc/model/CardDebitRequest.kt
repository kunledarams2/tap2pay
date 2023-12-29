package com.e.bloctap2pay.nfc.model

data class CardDebitRequest(
    var serial_number :String,
    var field3:String,
    var field4:String,
    var field23:String,
    var field35:String,
    var field52:String,
    var unpredictable:String,
    var capabilities:String,
    var cryptogram:String,
    var tvr:String,
    var iad:String,
    var cvm:String,
    var atc:String,
    var aip:String,
    var filename:String,
    var account:String,

)