package com.e.bloctap2pay.nfc.cashout

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.e.bloctap2pay.R

import com.e.bloctap2pay.databinding.FragmentTransactionPinBinding
import com.e.bloctap2pay.nfc.network.BlocApiService
import com.e.bloctap2pay.nfc.model.CardDebitRequest
import com.e.bloctap2pay.nfc.model.EmvCard
import com.e.bloctap2pay.nfc.model.TransactionResult
import com.e.bloctap2pay.nfc.network.RetrofitInstance
import com.e.bloctap2pay.nfc.utils.*
//import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.math.BigDecimal
//import javax.inject.Inject


class TransactionPinFragment : Fragment(), TextWatcher {

    lateinit var binding:FragmentTransactionPinBinding
    private val editTextArray: ArrayList<EditText> = ArrayList(NUM_OF_DIGITS)
    private var numTemp=String()
    private val args: TransactionPinFragmentArgs by navArgs()
    private var bundle=Bundle()
    private val mProvider: Provider = Provider()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransactionPinBinding.inflate(inflater)
        return binding.root /*inflater.inflate(R.layout.fragment_transaction_pin, container, false)*/
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //init buttons
        initKeyViews()
        val nfcData: EmvCard = args.cardData
        Log.d("Tag", nfcData.toString())

        for (i in 1..4) {
            val digitView = binding.root.findViewWithTag<EditText>("digit$i")
            digitView?.setOnTouchListener { v, _ ->
                v.performClick()
                true
            }
        }

        //create array

        for (index in 0 until (binding.codeLayout.childCount)) {
            val view: View = binding.codeLayout.getChildAt(index)
            if (view is EditText) {
                editTextArray.add(index, view)
                editTextArray[index].addTextChangedListener(this)

            }
        }

        editTextArray[0].requestFocus()

        // Set up backspace button functionality
        binding.buttonBackSpace.setOnClickListener {
            editTextArray.firstOrNull { it.hasFocus() }?.let { currentEditText ->
                val currentIndex = editTextArray.indexOf(currentEditText)
                if (currentIndex > 0) {
                    val previousEditText = editTextArray[currentIndex - 1]
                    previousEditText.requestFocus()
                    previousEditText.setSelection(previousEditText.length())
                }
            }
        }

        binding.btnCtn.setOnClickListener {
            val pin = "${binding.digit1.text}${binding.digit2.text}${binding.digit3.text}${binding.digit4.text}"
            Log.d("Pin",pin)
            if (pin.length ==4){
                processPayment( pin)
            }

        }

    }

    override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
        numTemp = s.toString()
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(s: Editable) {

        (0 until editTextArray.size)
            .forEach { i ->
                if (s === editTextArray[i].editableText) {

                    if (s.isBlank()) {
                        return
                    }
                    if (s.length >= 2) {//if more than 1 char
                        val newTemp = s.toString().substring(s.length - 1, s.length)
                        if (newTemp != numTemp) {
                            editTextArray[i].setText(newTemp)
                        } else {
                            editTextArray[i].setText(s.toString().substring(0, s.length - 1))
                        }
                    } else if (i != editTextArray.size - 1) { //not last char
                        editTextArray[i + 1].requestFocus()
                        editTextArray[i + 1].setSelection(editTextArray[i + 1].length())
                        return
                    } else

                    //will verify code the moment the last character is inserted and all digits have a number
                        verifyCode(testCodeValidity())


                }
            }


    }

    private fun testCodeValidity(): String {
        var verificationCode = ""
        for (i in editTextArray.indices) {
            val digit = editTextArray[i].text.toString().trim { it <= ' ' }
            verificationCode += digit
        }
        if (verificationCode.trim { it <= ' ' }.length == NUM_OF_DIGITS) {
            return verificationCode
        }
        return ""
    }

    /** Set the edittext views to be editable / uneditable
     *
     */
    private fun enableCodeEditTexts(enable: Boolean) {
        for (i in 0 until editTextArray.size)
            editTextArray[i].isEnabled = enable
    }

    private fun verifyCode(verificationCode: String) {
        if (verificationCode.isNotEmpty()) {
            enableCodeEditTexts(false)
            //Your code
        }
    }



    private val mOnKeyClickListener = View.OnClickListener { v: View? ->

        val clickedButton = v as Button
        val clickedDigit = clickedButton.text.toString()


        editTextArray.firstOrNull { it.hasFocus() }?.let { currentEditText ->
            // Append the clicked digit to the currently focused EditText


            val currentText = currentEditText.text.toString()
            val newText = "$currentText$clickedDigit"
            Log.d("DigitClick", clickedDigit)

            currentEditText.setText(clickedDigit)
//            numTemp = clickedDigit
//            binding.digit1.setText(clickedDigit)
//            currentEditText.requestLayout()
            Log.d("DigitClick", currentEditText.text.toString())

            // Move focus to the next EditText, if available
            val currentIndex = editTextArray.indexOf(currentEditText)
            if (currentIndex < editTextArray.size - 1) {
                val nextEditText = editTextArray[currentIndex + 1]
                nextEditText.requestFocus()
                nextEditText.setSelection(nextEditText.length())
            }
        }

    }

    private fun initKeyViews() {
        binding.button1.setOnClickListener(mOnKeyClickListener)
        binding.button2.setOnClickListener(mOnKeyClickListener)
        binding.button3.setOnClickListener(mOnKeyClickListener)
        binding.button4.setOnClickListener(mOnKeyClickListener)
        binding.button5.setOnClickListener(mOnKeyClickListener)
        binding.button6.setOnClickListener(mOnKeyClickListener)
        binding.button7.setOnClickListener(mOnKeyClickListener)
        binding.button8.setOnClickListener(mOnKeyClickListener)
        binding.button9.setOnClickListener(mOnKeyClickListener)
        binding.button0.setOnClickListener(mOnKeyClickListener)
        binding.buttonPoint.setOnClickListener(mOnKeyClickListener)
    }

    private  fun processPayment( pin:String) {

        val transactionResult = TransactionResult()
        val tags: HashMap<*, *> = mProvider.tags
//        Log.d("RequestCard","${ args.cardData.cardNumber} ->clrpinKey ${prefsValueHelper.terminalParams!!.clrpinkey}")
        val cardDebitRequest = CardDebitRequest(
            serial_number = args.initParams.deviceId, //
            field3 = "001000",
            field4 = getField4(BigDecimal(args.amount).toString() + "00"),
            field23 = "001"/*padLeft(mCard.pan,3, '0')!!*/,
            field35 = args.cardData.track2 /* tags["57"].toString()*/,
            field52 = PinEnc.encryptPinBlock(
                pin,
                args.cardData.cardNumber,
                args.initParams.crlPinKey
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


        transactionResult.amount = args.amount.toDouble()
        transactionResult.mid = ""
        transactionResult.tid = /*model.tid*/args.initParams.terminalId
        transactionResult.merchantName = ""

        transactionResult.maskedPan = StringUtils.processPan(args.cardData.cardNumber)

        transactionResult.expiryDate = getExpiryDate(args.cardData.track2)
        transactionResult.cardholderName =
            "${args.cardData.holderLastname} ${args.cardData.holderFirstname}" /*StringUtils.hexStringToUTF8String(response.CardHolderName)*/
        transactionResult.refNumber = ""
        transactionResult.stan = ""
        transactionResult.transType = "PURCHASE"
        transactionResult.tsi = ""
        transactionResult.tvr = args.cardData.tvr
        transactionResult.aid = args.cardData.aip
        transactionResult.operatorID = "NA"

        binding.keyPadWrapper.visibility = View.GONE
        binding.processPaymentWrapper.visibility = View.VISIBLE

//        blocMainActivity.processPayment()

        CoroutineScope(Dispatchers.IO).launch {
            val debitResponse = RetrofitInstance.blocApiService(requireContext()).performCardDebit(cardDebitRequest)
            withContext(Dispatchers.Main) {
                if (debitResponse.isSuccessful) {
                    Log.d("Response", debitResponse.body().toString())
                    if (debitResponse.body()!!.success) {
                        val result = debitResponse.body()!!.entity

                        transactionResult.authCode = result!!.auth
                        transactionResult.responseCode = result.resp
                        transactionResult.responseDescription = result.meaning
                    } else {
                        transactionResult.authCode = "NA"
                        transactionResult.responseCode = "NA"
                        transactionResult.responseDescription = debitResponse.body()!!.message
                    }
                    bundle.putParcelable("transactionResult", transactionResult)
                    findNavController().navigate(R.id.action_transactionPinFragment_to_transactionStatusFragment,
                        bundle
                    )
                }
            }
        }
    }

    companion object {
        const val NUM_OF_DIGITS = 4

        fun getExpiryDate(track2Data: String): String {
            val indexOfToken = track2Data.indexOf("D")
            val indexOfExpiryDate = indexOfToken + 1
            val lengthOfExpiryDate = 4
            return track2Data.substring(indexOfExpiryDate, indexOfExpiryDate + lengthOfExpiryDate)
        }


        fun getField4(amountStr: String): String {
            var amountStr = amountStr
            val index = amountStr.indexOf(".")
            if (amountStr.substring(index + 1, amountStr.length).length < 2) {
                amountStr = amountStr + "0"
            }
            amountStr = amountStr.replace(".", "")
            val amtlen = amountStr.length
            val amtBuilder = StringBuilder()
            if (amtlen < 12) {
                for (i in 0 until 12 - amtlen) {
                    amtBuilder.append("0")
                }
            }
            amtBuilder.append(amountStr)
            amountStr = amtBuilder.toString()
            return amountStr
        }

        @JvmStatic
        fun newInstance() =
            TransactionPinFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


}