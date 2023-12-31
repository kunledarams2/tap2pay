package com.e.bloctap2pay

import android.app.ProgressDialog
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.e.bloctap2pay.nfc.BlocApiService
import com.e.bloctap2pay.nfc.cashout.TapPayFragmentDirections
import com.e.bloctap2pay.nfc.cashout.TransactionPinFragment
import com.e.bloctap2pay.nfc.model.CardDebitRequest
import com.e.bloctap2pay.nfc.model.EmvCard
import com.e.bloctap2pay.nfc.model.InitialParams
import com.e.bloctap2pay.nfc.model.TransactionResult
import com.e.bloctap2pay.nfc.parser.EmvParser
import com.e.bloctap2pay.nfc.utils.*
import com.e.bloctap2pay.nfc.utils.Constants.PrefKeys.INITIAL_PARAMS
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.math.BigDecimal
import javax.inject.Inject

@AndroidEntryPoint
class BlocMainActivity : AppCompatActivity() {

    lateinit var navHostFragment: NavHostFragment
    private var mTagcomm: IsoDep? = null
    private var mCard: EmvCard? = null
    private var mException = false
    private val mProvider: Provider = Provider()
    private var mReadCard: EmvCard? = null

    private var mDialog: ProgressDialog? = null
    private var bundle = Bundle()

    @Inject
    lateinit var prefsUtils: PrefsUtils
    private var initialParams: InitialParams?=null


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bloc_activity_main)
        if (intent.extras != null) {
            initialParams = intent.extras!!.getParcelable(INITIAL_PARAMS, InitialParams::class.java)!!
            prefsUtils.putObject(INITIAL_PARAMS, initialParams)
        }


         navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.d("Destination", destination.label.toString())

            if (destination.label.toString() == "TapPayFragment") {
                Log.d("Arguments", arguments.toString())
                bundle = arguments!!
            }
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        Log.d("TagAction", intent?.action.toString())


        if (NfcAdapter.ACTION_TECH_DISCOVERED == intent?.action) {
            // Handle NFC intent, extract data if needed
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            // val dataFromNFC = extractDataFromNFC(tag)

            // Read card data
            if (tag != null) {

                mTagcomm = IsoDep.get(tag)
                if (mTagcomm == null) {
                    /* CroutonUtils.display(
                         this,
                         getText(R.string.error_communication_nfc),
                         CroutonUtils.CoutonColor.BLACK
                     )*/
                    return
                }
                mException = false
                CoroutineScope(Dispatchers.IO).launch {

                    try {
                        mReadCard = null
                        // Open connection
                        mTagcomm!!.connect()
//                        lastAts = getAts(mTagcomm!!)!!
                        mProvider.setmTagCom(mTagcomm)


                        val parser = EmvParser(mProvider, true)

                        mCard = parser.readEmvCard()

//                mCard?.atrDescription = extractAtsDescription(lastAts)
                    } catch (e: IOException) {
                        mException = true
                    } finally {
                        // close tagcomm
                        IOUtils.closeQuietly(mTagcomm)
                    }
                    withContext(Dispatchers.Main) {
//                        mDialog!!.hide()
                        if (mCard != null) {


                            // Pass data to the fragment
                            val navController = findNavController(R.id.nav_host_fragment)
                            val currentDestinationId = navController.currentDestination?.id

                            if (currentDestinationId == R.id.tapPayFragment) {


                                val action =
                                    TapPayFragmentDirections.actionTapPayFragmentToTransactionPinFragment(
                                        bundle.getString("Amount").toString(),
                                        bundle.getString(Constants.ACCOUNT_TYPE).toString(),
                                        mCard!!,initialParams!!

                                    )
                                navController.navigate(action)
                            }

                        }
                        Log.d("mCard", mCard.toString())
                    }
                }
            }


        }

    }


/*      fun processPayment( pin:String, initialParams: InitialParams, card: EmvCard, amount:String) {

        val transactionResult = TransactionResult()
        val tags: HashMap<*, *> = mProvider.tags
//
        val cardDebitRequest = CardDebitRequest(
            serial_number = initialParams.deviceId, //
            field3 = "001000",
            field4 = TransactionPinFragment.getField4(BigDecimal(amount).toString() + "00"),
            field23 = "001"*//*padLeft(mCard.pan,3, '0')!!*//*,
            field35 = card.track2 *//* tags["57"].toString()*//*,
            field52 = PinEnc.encryptPinBlock(
                pin,
                card.cardNumber,
                initialParams.crlPinKey
            ),
            unpredictable = tags["9F37"].toString(),
            capabilities = "2028C0",
            cryptogram = tags["9F26"].toString(),
            tvr = tags["95"].toString(),
            iad = tags["9F10"].toString(),
            cvm = "010002",
            atc = tags["9F36"].toString(),
            aip = tags["82"].toString(),
            filename = tags["84"].toString(),
            account = "Savings"
        )
        Log.d("RequestCard", cardDebitRequest.toString())


        transactionResult.amount = amount.toDouble()
        transactionResult.mid = ""
        transactionResult.tid = *//*model.tid*//*initialParams.terminalId
        transactionResult.merchantName = ""

        transactionResult.maskedPan = StringUtils.processPan(card.cardNumber)

        transactionResult.expiryDate = TransactionPinFragment.getExpiryDate(card.track2)
        transactionResult.cardholderName =
            "${card.holderLastname} ${card.holderFirstname}" *//*StringUtils.hexStringToUTF8String(response.CardHolderName)*//*
        transactionResult.refNumber = ""
        transactionResult.stan = ""
        transactionResult.transType = "PURCHASE"
        transactionResult.tsi = ""
        transactionResult.tvr = card.tvr
        transactionResult.aid = card.aip
        transactionResult.operatorID = "NA"

//        binding.keyPadWrapper.visibility = View.GONE
//        binding.processPaymentWrapper.visibility = View.VISIBLE


        CoroutineScope(Dispatchers.IO).launch {
            val debitResponse = blocApiService.performCardDebit(cardDebitRequest)
            withContext(Dispatchers.Main) {
                if (debitResponse.isSuccessful) {
                    Log.d("Response", debitResponse.body().toString())
                    if (debitResponse.body()!!.success) {
                        val result = debitResponse.body()!!.entity

                        transactionResult.authCode = result!!.auth
                        transactionResult.responseCode = result!!.resp
                        transactionResult.responseDescription = result!!.meaning
                    } else {
                        transactionResult.authCode = "NA"
                        transactionResult.responseCode = "NA"
                        transactionResult.responseDescription = debitResponse.body()!!.message
                    }
                    bundle.putParcelable("transactionResult", transactionResult)
                    findNavController(R.id.nav_host_fragment).navigate(
                        R.id.action_transactionPinFragment_to_transactionStatusFragment,
                        bundle
                    )
                }
            }
        }
    }*/
}