package com.e.bloctap2pay.nfc.cashout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import androidx.navigation.fragment.findNavController

import com.e.bloctap2pay.R
import com.e.bloctap2pay.databinding.FragmentAccountTypeBinding
import com.e.bloctap2pay.nfc.utils.AccountType
import com.e.bloctap2pay.nfc.utils.Constants


class AccountTypeFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentAccountTypeBinding
    lateinit var bundle: Bundle

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_type, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bundle = Bundle()
        binding.cardSaving.setOnClickListener(this)
        binding.cardCurrent.setOnClickListener(this)
        binding.backBtn.customBtn.setOnClickListener(this)


    }


    companion object {

        @JvmStatic
        fun newInstance() =
            AccountTypeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onClick(p0: View?) {
       when(p0!!.id){
           R.id.cardSaving->{
               bundle.putString(Constants.ACCOUNT_TYPE, AccountType.Savings.name)
               findNavController().navigate(R.id.action_accountTypeFragment_to_amountFragment,bundle)
           }
           R.id.cardCurrent->{
               bundle.putString(Constants.ACCOUNT_TYPE, AccountType.Current.name)
               findNavController().navigate(R.id.action_accountTypeFragment_to_amountFragment,bundle)
           }
           R.id.back_btn->{
//               findNavController().navigate(R.id.action_accountTypeFragment_to_dashboardFragment)
           }
       }
    }
}