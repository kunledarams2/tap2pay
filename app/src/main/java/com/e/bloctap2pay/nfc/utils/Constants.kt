package com.e.bloctap2pay.nfc.utils

import com.e.bloctap2pay.BuildConfig


object Constants {
    object PrefKeys {
        const val ACCESS_TOKEN = "sessionId"
        const val IS_LOGGED_IN = "IS_LOGGED_IN"
        const val USER_CREDENTIALS = "USER_CREDENTIALS"
        const val LOGGED_IN_USER = "LOGGED_IN_USER"
        const val RESET_PASSWORD_EMAIL = "RESET_PASSWORD_EMAIL"
        const val ACCOUNT_DETAILS = "ACCOUNT_DETAIL"
        const val TERMINAL_PARAMS_PROFILE = "TERMINAL PARAMS PROFILE"
        const val TERMINAL_PARAMS = "TERMINAL PARAMS"
        const val ORGANISATION_DETAILS = "ORGANISATION"
        const val ACCOUNT_DETAILS_TERMINAL = "ACCOUNT_DETAIL_TERMINAL"
        const val INITIAL_PARAMS = "TERMINAL PARAMS"

    }

    object Misc {
//        const val FILE_PROVIDER_AUTHORITY = "${BuildConfig.APPLICATION_ID}.fileprovider"
        const val LIMIT = 40
        const val DEFAULT_FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val DEFAULT_PHOTO_EXTENSION = ".jpg"
        const val PERMISSION_CAMERA_REQUEST = 1
        const val USER_OBJECT = "USER_OBJECT"
        const val CLUSTER_COORDINATE = "Cluster Coordinate"
        const val CLUSTER_ADDRESS = "Cluster Address"
        const val DEFAULT_KML_EXTENSION = ".kml"
    }

    object FormatPattern {
        const val MYSQL_DATE = "yyyy-MM-dd"
        const val DISPLAY_DATE = "dd MMMM yyyy"
        const val API_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

    object Database {
        const val DATABASE_NAME = "tradebuza-database.db"
    }

    object Camera {
        const val RATIO_4_3_VALUE = 4.0 / 3.0
        const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    object APIDataKeys {
        const val SESSION_ID = "sessionId"
    }




    object APIResponseCodes {
        const val CODE_INVALID_SESSION_ERROR = 106
    }

    const val INTENT_EXTRA_AMOUNT_KEY = "amount"
    const val ACCOUNT_TYPE = "ACCOUNT_TYPE"
    const val UNIQUE_ID = "UNIQUE_ID"
    const val INTENT_EXTRA_ACCOUNT_TYPE = "00"

    const val KEY_TRANSACTION_PROFILE_CODE = "profile_code"
    const val KEY_AGENT_USERNAME = "username"
    const val KEY_AGENT_ADDRESS = "agent_address"
    const val PRINTER_DATA_TRANSACTION_STATUS_CODE = "printer_status_code"
    const val PRINTER_DATA_TRXN_STATUS_KEY = "trans_status_code"
    const val PAYMENT_NUMBER_ARGUMENT_NAME = "payment_number"
    const val PRINTER_DATA_AMOUNT_KEY = "printer_amount"
    const val PRINTER_DATA_MASKED_PAN_KEY = "masked_pan"
    const val PRINTER_DATA_RRN_KEY = "rrn"
    const val PRINTER_DATA_STAN_KEY = "stan"
    const val PRINTER_DATA_TERMINAL_ID_KEY = "terminal_key"
    const val CUSTOM_TRANSACTION_TYPE = "custom_transaction_type"
    const val TERMINAL_ID = "termial_id"
    const val TRANSACTION_RESPONSE = "transaction_response"
    const val TRANSACTION_TYPE = "transaction_type"
    const val KEY_CUSTOMER_DETAILS = "customer_details"
    const val PRODUCT_LIST = "product_list"
    const val CHANNEL_TYPE = "ChannelType"
    const val ENVIRONMENT = "env"
    const val CARD_PROCESSOR = "processor"
    var AGENT_CUS_ID = "agent_cus_id"
    var SUCCESS_CODE = "00"
    var FAILED_CODE = "01"
    var CANCELLED_CODE = "02"
}