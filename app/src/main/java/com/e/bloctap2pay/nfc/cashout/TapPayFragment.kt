package com.e.bloctap2pay.nfc.cashout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.e.bloctap2pay.R
import com.e.bloctap2pay.databinding.FragmentTapPayBinding


class TapPayFragment : Fragment() {
    lateinit var binding:FragmentTapPayBinding
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
        binding = FragmentTapPayBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn.customBtn.setOnClickListener {
            findNavController().navigate(R.id.action_tapPayFragment_to_confirmAmountFragment)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TapPayFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}