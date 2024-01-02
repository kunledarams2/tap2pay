package com.e.bloctap2pay.nfc.cashout

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.e.bloctap2pay.R
import com.e.bloctap2pay.databinding.FragmentTransactionStatusBinding
import com.e.bloctap2pay.nfc.model.TransactionResult
import com.e.bloctap2pay.nfc.utils.PrefsUtils

class TransactionStatusFragment : Fragment() {

    lateinit var transactionResult: TransactionResult
    lateinit var binding: FragmentTransactionStatusBinding
    lateinit var prefsUtils: PrefsUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            transactionResult = it.getParcelable("transactionResult")!!

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransactionStatusBinding.inflate(inflater)
        return binding.root /* inflater.inflate(R.layout.fragment_transaction_status, container, false)*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefsUtils = PrefsUtils.create(requireContext())
        binding.cancelBtn.navIcon.setImageResource(R.drawable.baseline_close_24)

        binding.cancelBtn.navName.text= "Close "
        binding.cancelBtn.customBtn.background = resources.getDrawable( R.drawable.oval_red_stroke)
        binding.cancelBtn.navName.setTextColor(Color.parseColor("#D93C48"))

        binding.cancelBtn.customBtn.setOnClickListener {

//            findNavController().navigate(R.id.action_transactionStatusFragment_to_dashboardFragment)
        }
        binding.printBtnCtn.setOnClickListener {
            startActivity(Intent(requireActivity(), prefsUtils.getString("clientActivity",null)!!::class.java))
            requireActivity().finish()

        }

        if (transactionResult.responseCode == "00"){
            binding.approveLayout.visibility =View.VISIBLE


        }else{
            Log.d("TransactionResult", transactionResult.responseDescription)
            binding.approveLayout.visibility =View.GONE
            binding.failLayout.visibility =View.VISIBLE
            binding.failReason.text = transactionResult.responseDescription

        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TransactionStatusFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}