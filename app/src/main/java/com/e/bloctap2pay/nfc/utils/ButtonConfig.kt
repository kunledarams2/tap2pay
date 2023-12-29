package com.e.bloctap2pay.nfc.utils

import android.util.Log

object ButtonConfig {

    var customFontCurrencyEditText: CustomFontCurrencyEditText? = null
    private var inputAmt = ""
    private var mListener: OnPinListener? = null


    fun init(customFontCurrencyEditText: CustomFontCurrencyEditText?) {
        this.customFontCurrencyEditText = customFontCurrencyEditText
    }

    fun getInputAmt(): String? {
        return inputAmt
    }

    fun input(number: String) {
        Log.d("LogInputAmount", inputAmt)
        //        String names = getInputAmt().toCharArray();
        inputAmt += if (inputAmt.contains(".") && number == ".") {
            Log.d("LogInputPoint", inputAmt)
            // deleteBy1();
            return
        } else {
            number
        }
        if (mListener != null) {
            mListener!!.onCodeCompleted(inputAmt)
        }
        Log.d("LogInputPoint", inputAmt)
        Log.d("LogInputPoint", GlobalUtil.currencyFormat(inputAmt.toDouble()).toString())
        val newImput = inputAmt.toDouble()
        if (newImput.toString().contains(".")) {
            val parts = newImput.toString().split("\\.".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val part1 = parts[0] // 004
            val part2 = parts[1] // 034556
            Log.d("LogInputPoint", part2)
            if (part2.length > 2) {
                deleteBy1()
                return
            }
        }
        customFontCurrencyEditText!!.setText(GlobalUtil.currencyFormat(inputAmt.toDouble()))
    }

    fun deleteBy1() {
        if (mListener != null) {
            mListener!!.onCodeNotCompleted(inputAmt)
        }
        val status: Boolean = inputAmt.isEmpty()/* StringUtils.isEmpty(inputAmt)*/
        inputAmt = if (status) "0.0" else inputAmt.substring(0, inputAmt.length - 1)
        customFontCurrencyEditText!!.setText(GlobalUtil.currencyFormat(inputAmt.toDouble()))
    }

    fun clearInput() {
        if (mListener != null) {
            mListener!!.onCodeNotCompleted(inputAmt)
        }
        inputAmt = ""
        customFontCurrencyEditText!!.setText(inputAmt)
    }

    fun setListener(listener: OnPinListener?) {
        mListener = listener
    }

    interface OnPinListener {
        fun onCodeCompleted(code: String?)
        fun onCodeNotCompleted(code: String?)
    }
}