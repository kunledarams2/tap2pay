package com.e.bloctap2pay.nfc.utils




import com.e.bloctap2pay.nfc.model.InitialParams
import com.e.bloctap2pay.nfc.utils.Constants.PrefKeys.ACCESS_TOKEN
import com.e.bloctap2pay.nfc.utils.Constants.PrefKeys.INITIAL_PARAMS
import com.e.bloctap2pay.nfc.utils.Constants.PrefKeys.RESET_PASSWORD_EMAIL
import com.e.bloctap2pay.nfc.utils.Constants.PrefKeys.TERMINAL_PARAMS
import com.e.bloctap2pay.nfc.utils.PrefsUtils
import javax.inject.Inject

class PrefsValueHelper @Inject constructor(private val prefsUtils: PrefsUtils) {

    var accessToken: String?
        get() = prefsUtils.getString(ACCESS_TOKEN, null)
        set(value) {
            prefsUtils.putString(ACCESS_TOKEN, value)
        }

    var passwordEmail: String?
        get() = prefsUtils.getString(RESET_PASSWORD_EMAIL, null)
        set(value) {
            prefsUtils.putString(RESET_PASSWORD_EMAIL, value)
        }

    var initParams: InitialParams?
        get() = prefsUtils.getPrefAsObject(INITIAL_PARAMS, InitialParams::class.java)
        set(value) {
            prefsUtils.remove(INITIAL_PARAMS)
            value?.let {
                prefsUtils.putObject(INITIAL_PARAMS, it)
            }
        }
/*
    var userCredentials: LoginRequest
        get() = prefsUtils.getPrefAsObject(USER_CREDENTIALS, LoginRequest::class.java)
        set(value) = prefsUtils.putObject(USER_CREDENTIALS, value)

    var loggedInUser: LoginResponseData?
        get() = prefsUtils.getPrefAsObject(LOGGED_IN_USER, LoginResponseData::class.java)
        set(value) {
            prefsUtils.remove(LOGGED_IN_USER)
            value?.let {
                prefsUtils.putObject(LOGGED_IN_USER, it)
            }
        }


    var terminalParams: TerminalParams?
        get() = prefsUtils.getPrefAsObject(TERMINAL_PARAMS, TerminalParams::class.java)
        set(value) {
            prefsUtils.remove(TERMINAL_PARAMS)
            value?.let {
                prefsUtils.putObject(TERMINAL_PARAMS, it)
            }
        }


    var terminalParamsProfile: Profile?
        get() = prefsUtils.getPrefAsObject(TERMINAL_PARAMS_PROFILE, Profile::class.java)
        set(value) {
            prefsUtils.remove(TERMINAL_PARAMS_PROFILE)
            value?.let {
                prefsUtils.putObject(TERMINAL_PARAMS_PROFILE, it)
            }
        }

    var organization: PurpleOrganization?
        get() = prefsUtils.getPrefAsObject(ORGANISATION_DETAILS, PurpleOrganization::class.java)
        set(value) {
            prefsUtils.remove(ORGANISATION_DETAILS)
            value?.let {
                prefsUtils.putObject(ORGANISATION_DETAILS, it)
            }
        }




    var accountDetails: AccountResponse?
        get() = prefsUtils.getPrefAsObject(ACCOUNT_DETAILS, AccountResponse::class.java)
        set(value) {
            prefsUtils.remove(ACCOUNT_DETAILS)
            value?.let {
                prefsUtils.putObject(ACCOUNT_DETAILS, it)
            }
        }

    var terminalAccountInfo: Account?
        get() = prefsUtils.getPrefAsObject(ACCOUNT_DETAILS_TERMINAL, Account::class.java)
        set(value) {
            prefsUtils.remove(ACCOUNT_DETAILS_TERMINAL)
            value?.let {
                prefsUtils.putObject(ACCOUNT_DETAILS_TERMINAL, it)
            }
        }

    var isLoggedIn: Boolean
        get() = prefsUtils.getBoolean(IS_LOGGED_IN, false)
        set(value) {
            prefsUtils.putBoolean(IS_LOGGED_IN, value)
        }*/
}
