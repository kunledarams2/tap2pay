package com.e.bloctap2pay.nfc.utils




import android.content.Context
import com.e.bloctap2pay.nfc.model.InitialParams
import com.e.bloctap2pay.nfc.network.RetrofitInstance
import com.e.bloctap2pay.nfc.utils.Constants.PrefKeys.ACCESS_TOKEN
import com.e.bloctap2pay.nfc.utils.Constants.PrefKeys.INITIAL_PARAMS
import com.e.bloctap2pay.nfc.utils.Constants.PrefKeys.TERMINAL_PARAMS
import com.e.bloctap2pay.nfc.utils.PrefsUtils
//import javax.inject.Inject

class PrefsValueHelper(context: Context) {

    private val prefsUtils: PrefsUtils

    init {
        prefsUtils = PrefsUtils.create(context)
    }

    var accessToken: String?
        get() = prefsUtils.getString(ACCESS_TOKEN, null)
        set(value) {
            prefsUtils.putString(ACCESS_TOKEN, value)
        }


    var initParams: InitialParams?
        get() = prefsUtils.getPrefAsObject(INITIAL_PARAMS, InitialParams::class.java)
        set(value) {
            prefsUtils.remove(INITIAL_PARAMS)
            value?.let {
                prefsUtils.putObject(INITIAL_PARAMS, it)
            }
        }
}
