package com.e.bloctap2pay.nfc.cashout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.e.bloctap2pay.R


class TapPayFragment : Fragment() {


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
        return inflater.inflate(R.layout.fragment_tap_pay, container, false)
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