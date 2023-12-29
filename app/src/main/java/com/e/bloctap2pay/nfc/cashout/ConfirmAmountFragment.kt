package com.e.bloctap2pay.nfc.cashout

import android.app.ProgressDialog
import android.content.Intent
import android.content.Intent.getIntent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.os.RemoteException
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.e.bloctap2pay.R
import com.e.bloctap2pay.databinding.FragmentConfirmAmountBinding
import com.e.bloctap2pay.nfc.model.EmvCard
import com.e.bloctap2pay.nfc.utils.Constants
import com.e.bloctap2pay.nfc.utils.GlobalUtil
//import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

import java.text.SimpleDateFormat
import java.util.*
//import javax.inject.Inject


//@AndroidEntryPoint
open class ConfirmAmountFragment : Fragment() {

    lateinit var binding: FragmentConfirmAmountBinding
    lateinit var amountEnter:String
    lateinit var accountTypeSelected:String

    private lateinit var mutableLiveData: MutableLiveData<String>

    private var isSupport = false

    private var rrn: String = System.currentTimeMillis().toString().reversed().take(12)
   private var bundle=Bundle()


//    @Inject
//    lateinit var blocApiService: BlocApiService


    /**
     * Tag comm
     */
    private var mTagcomm: IsoDep? = null

    /**
     * Emv Card
     */
    private var mCard: EmvCard? = null

    /**
     * Boolean to indicate exception
     */
    private var mException = false

    /**
     * IsoDep provider
     */
//    private val mProvider: Provider = Provider()

    /**
     * Emv card
     */
    private var mReadCard: EmvCard? = null

    private var mDialog: ProgressDialog? = null

    private lateinit var lastAts: ByteArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bundle = it
            amountEnter = it.getString("Amount")!!
            accountTypeSelected= it.getString(Constants.ACCOUNT_TYPE)!!
        }

//        rrn = if (Constants.UNIQUE_ID != null && Constants.UNIQUE_ID.length == 12) Constants.UNIQUE_ID else rrn

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentConfirmAmountBinding.inflate(inflater)
        return binding.root /*inflater.inflate(R.layout.fragment_confirm_amount, container, false)*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn.customBtn.setOnClickListener {

            findNavController().navigate(R.id.action_confirmAmountFragment_to_amountFragment, bundle)
        }

        binding.amount.text= GlobalUtil.currencyFormat(amountEnter.toDouble()).toString()
        binding.accType.text = accountTypeSelected
        mutableLiveData = MutableLiveData()

        binding.btnCtn.setOnClickListener {
            findNavController().navigate(R.id.action_confirmAmountFragment_to_tapPayFragment, bundle)

        }

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ConfirmAmountFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


}