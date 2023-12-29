package com.e.bloctap2pay.nfc.cashout

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.e.bloctap2pay.R

import com.e.bloctap2pay.databinding.FragmentAmountBinding
import com.e.bloctap2pay.nfc.utils.ButtonConfig
import com.e.bloctap2pay.nfc.utils.Constants


class AmountFragment : Fragment() {

    lateinit var binding: FragmentAmountBinding
    lateinit var accountTypeSelected: String
    private var bundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bundle = it
            accountTypeSelected = it.getString(Constants.ACCOUNT_TYPE)!!

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAmountBinding.inflate(inflater)
        return binding.root /*inflater.inflate(R.layout.fragment_amount, container, false)*/
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ButtonConfig.init(binding.edtAmount)
        if (bundle.containsKey("Amount")) {
            ButtonConfig.clearInput()
            ButtonConfig.input(bundle.getString("Amount")!!)
        }

        //Function to block Keypad

        //Function to block Keypad
        binding.edtAmount.setOnTouchListener { v, _ ->
            v.performClick()
            true
        }

        //Clear field by 1 function

        //Clear field by 1 function
        binding.buttonBackSpace.setOnClickListener { v ->
            if (ButtonConfig.getInputAmt()!!.isNotEmpty()
                && (ButtonConfig.getInputAmt()!!.length != 1)
            ) {
                Log.d("amountButtonConfig", ButtonConfig.getInputAmt().toString())
                ButtonConfig.deleteBy1()
            } else {
                ButtonConfig.clearInput()
//                amountButtonConfig.input("0");
                //binding.edtAmount.setText("0.0");
            }
        }

        //Clear field function

        //Clear field function
//        binding.buttonClear.setOnClickListener { v -> ButtonConfig.clearInput() }
        initKeyViews()


        binding.backBtn.customBtn.setOnClickListener {
            findNavController().navigate(R.id.action_amountFragment_to_accountTypeFragment)
        }

        binding.btnCtn.setOnClickListener {
            val amountEnter = ButtonConfig.getInputAmt()
            if (amountEnter!!.isNotEmpty()) {
                bundle.putString("Amount", amountEnter)
                findNavController().navigate(
                    R.id.action_amountFragment_to_confirmAmountFragment,
                    bundle
                )
            }
        }
    }

    companion object {
        lateinit var selAccountType: String

        @JvmStatic
        fun newInstance(accountType: String) =
            AmountFragment().apply {
                arguments = Bundle().apply {
                }
                selAccountType = accountType
            }
    }


    private val mOnKeyClickListener = View.OnClickListener { v: View? ->
        if (v is Button) {
            val string = v.text.toString()
            Log.d("InputNumber", string)
            if (string.isEmpty()) {
                return@OnClickListener
            }
            Log.d("AmountEnter", ButtonConfig.getInputAmt().toString())
            if (ButtonConfig.getInputAmt() == null ||
                ButtonConfig.getInputAmt().equals("0") ||
                ButtonConfig.getInputAmt().equals(".")
            ) {
                binding.button0.isEnabled = false
                binding.buttonPoint.isEnabled = false
                return@OnClickListener
            }
            binding.button0.isEnabled = true
            ButtonConfig.input(string)
            if (ButtonConfig.getInputAmt()!!.toDouble() % 1000 != 0.0) {
                binding.buttonPoint.isEnabled = true
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

}