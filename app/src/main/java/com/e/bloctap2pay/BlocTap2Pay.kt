package com.e.bloctap2pay

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Patterns
import android.widget.Toast
import com.e.bloctap2pay.nfc.model.InitialParams
import com.e.bloctap2pay.nfc.utils.Constants.PrefKeys.INITIAL_PARAMS
import com.e.bloctap2pay.nfc.utils.PrefsUtils
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

class BlocTap2Pay @Inject constructor(
    private val context: Context,
    ) {



    fun initialClient(
        deviceId: String,
        clrPinKey: String,
        terminalId: String,
        secretKey: String,
        appEnvironment: String,
    ) {
        if (deviceId.isNotEmpty() && clrPinKey.isNotEmpty() && terminalId.isNotEmpty() && secretKey.isNotEmpty()) {
//            prefsUtils = PrefsUtils()
            val initialParams = InitialParams(
                deviceId = deviceId,
                terminalId =  terminalId,
                secretKey = secretKey,
                crlPinKey = clrPinKey,
                appEnvironment = appEnvironment
            )
//            prefsUtils.putObject(INITIAL_PARAMS, initialParams)
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(INITIAL_PARAMS,initialParams )
            context.startActivity(intent)


        }

    }
}