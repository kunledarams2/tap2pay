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
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.e.bloctap2pay.nfc.cashout.TapPayFragmentDirections
import com.e.bloctap2pay.nfc.model.EmvCard
import com.e.bloctap2pay.nfc.model.InitialParams
import com.e.bloctap2pay.nfc.parser.EmvParser
import com.e.bloctap2pay.nfc.utils.*
import com.e.bloctap2pay.nfc.utils.Constants.PrefKeys.INITIAL_PARAMS
//import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils
//import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.io.IOUtils
import java.io.IOException
//import javax.inject.Inject


class BlocMainActivity : AppCompatActivity() {

    lateinit var navHostFragment: NavHostFragment
    private var mTagcomm: IsoDep? = null
    private var mCard: EmvCard? = null
    private var mException = false
    private val mProvider: Provider = Provider()
    private var mReadCard: EmvCard? = null

    private var mDialog: ProgressDialog? = null
    private var bundle = Bundle()


    lateinit var prefsUtils: PrefsUtils
    private var initialParams: InitialParams?=null
    lateinit var clientActivityName:String
    private var mNfcUtils: NFCUtils? = null


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bloc_activity_main)
        prefsUtils = PrefsUtils.create(this)
        if (intent.extras != null) {
            initialParams = intent.extras!!.getParcelable(INITIAL_PARAMS, InitialParams::class.java)!!
            prefsUtils.putObject(INITIAL_PARAMS, initialParams)
//            clientActivityName = intent.extras!!.getString("clientActivity")!!
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

        mNfcUtils = NFCUtils(this)
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

                    }
                }
            }


        }

    }


    override fun onResume() {
        super.onResume()
        mNfcUtils!!.enableDispatch()

    }

}