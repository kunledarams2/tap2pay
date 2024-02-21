package com.e.bloctap2pay

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import com.e.bloctap2pay.nfc.model.InitialParams
import com.e.bloctap2pay.nfc.utils.Constants.PrefKeys.INITIAL_PARAMS
import com.e.bloctap2pay.nfc.utils.PrefsUtils
import java.security.PublicKey

//import javax.inject.Inject

class BlocTap2Pay(private val context: Context) {


    fun initialClient(
        deviceId: String,
        clrPinKey: String,
        terminalId: String,
        publicKey:  String,

    ) {
        if (deviceId.isNotEmpty() && clrPinKey.isNotEmpty() && terminalId.isNotEmpty() && publicKey.isNotEmpty()) {
            val initialParams = InitialParams(
                deviceId = deviceId,
                terminalId = terminalId,
                publicKey = publicKey,
                crlPinKey = clrPinKey,

            )
          val prefsUtils=   PrefsUtils.create(context)
            prefsUtils.putString("clientActivity", getCurrentActivityName(context))

            val intent = Intent(context, BlocMainActivity::class.java)
            intent.putExtra(INITIAL_PARAMS, initialParams)
//            intent.putExtra("clientActivity", getCurrentActivityName(context))
            context.startActivity(intent)


        }

    }

    // Inside any class that has access to a Context object
    fun getCurrentActivityName(context: Context): String {
        if (context is Activity) {
            // If the context is an activity, get its class name
            return context.javaClass.simpleName
        } else if (context is ContextWrapper && context.baseContext is Activity) {
            // If the context is wrapped, get the base context's class name
            return (context.baseContext as Activity).javaClass.simpleName
        }

        // Return a default value or handle the case when the context is not an activity
        return "UnknownActivity"
    }

}