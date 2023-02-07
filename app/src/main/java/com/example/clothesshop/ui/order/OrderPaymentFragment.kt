package com.example.clothesshop.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.clothesshop.R
import com.example.clothesshop.databinding.FragmentOrderBinding
import com.example.clothesshop.databinding.FragmentOrderPaymentBinding
import com.example.clothesshop.ui.basket.BasketFragmentDirections
import com.example.clothesshop.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderPaymentFragment : Fragment() {

    private var _binding: FragmentOrderPaymentBinding? = null
    private val binding get() = _binding!!

    var localeList = mutableListOf(
        "RUCH ul. Wały Dwernickiego 2 42-217 Częstochowa",
        "Żabka ul. Aleja Wolności 18 42-217 Częstochowa",
        "UP 17 ul. Śląska 19 42-217 Częstochowa",
        "Lewiatan ul. Wolności 45 42-242 Rędziny",
        "ABC ul. Józefa Mireckiego 35 42-208 Częstochowa",
        "Orlen ul. Śląska 77 42-217 Częstochowa"
    )

    var paList = mutableListOf(
        "InPost CZE11N ul. Dwernickiego 2 42-217 Częstochowa",
        "InPost CZE34N ul. Wolności 18 42-217 Częstochowa",
        "InPost CZE76N ul. Śląska 19 42-217 Częstochowa",
        "InPost CZE88N ul. Wolności 45 42-242 Rędziny",
        "InPost CZE12N ul. Józefa 35 42-208 Częstochowa",
        "InPost CZE17N ul. Śląska 77 42-217 Częstochowa"
    )

    var courierList = mutableListOf(
        "FedEx",
        "UPS",
        "InPost",
    )

    var checkedDeliveryId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOrderPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: OrderPaymentFragmentArgs by navArgs()


        initSpinnerLocale()
        initSpinnerParcelLocker()
        initSpinnerCourier()

        binding.next.setOnClickListener {
            checkedDeliveryId = binding.postOptions.checkedRadioButtonId
            var deliverySendText = ""
            when (checkedDeliveryId) {
                binding.courierButton.id -> deliverySendText =
                    "Kurier " + binding.courierRadioGroup.selectedItem.toString()
                binding.parcelLocker.id -> deliverySendText =
                    "Paczkomat " + binding.parcelLockerInfo.selectedItem.toString()
                binding.localeRadioButton.id -> deliverySendText =
                    "Odbiór w punkcie - " + binding.localeSelectSpinner.selectedItem.toString()
            }
            var typePaySend = 0
            val checkedPay = binding.paymentOptions.checkedRadioButtonId
            when (checkedPay) {
                binding.payRadioButtonCard.id -> typePaySend = Constants.KEY_PAY_CARD
                binding.payRadioButtonBgik.id -> typePaySend = Constants.KEY_PAY_BLIK
                binding.payRadioButtonInPlace.id -> typePaySend = Constants.KEY_PAY_CASH
            }

            val address = args.orderAdres
            val action =
                OrderPaymentFragmentDirections.actionOrderPaymentFragmentToOrderSummaryFragment(
                    deliverySendText,
                    typePaySend,
                    address
                )
            Navigation.findNavController(view).navigate(action)

            //findNavController().navigate(R.id.action_orderPaymentFragment_to_orderSummaryFragment)
        }

        checkedDeliveryId = binding.postOptions.checkedRadioButtonId

        postSelect()
        paymentSelect()
    }

    private fun initSpinnerCourier() {
        val spinner2 = binding.courierRadioGroup
        if (spinner2 != null) {
            val adapter2 = context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item, courierList
                )
            }
            spinner2.adapter = adapter2
        }
    }


    private fun paymentSelect() {
        binding.payRadioButtonCard.setOnClickListener {
            binding.payCardInfo.visibility = View.VISIBLE
            binding.payInPlaceInfo.visibility = View.GONE
            binding.payBgikInfo.visibility = View.GONE
        }

        binding.payRadioButtonBgik.setOnClickListener {
            binding.payCardInfo.visibility = View.GONE
            binding.payInPlaceInfo.visibility = View.GONE
            binding.payBgikInfo.visibility = View.VISIBLE
        }
        binding.payRadioButtonInPlace.setOnClickListener {
            binding.payCardInfo.visibility = View.GONE
            binding.payInPlaceInfo.visibility = View.VISIBLE
            binding.payBgikInfo.visibility = View.GONE
        }
    }

    private fun initSpinnerParcelLocker() {
        val spinner2 = binding.parcelLockerInfo
        if (spinner2 != null) {
            val adapter2 = context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item, paList
                )
            }
            spinner2.adapter = adapter2
        }
    }

    private fun initSpinnerLocale() {
        val spinner = binding.localeSelectSpinner
        if (spinner != null) {
            val adapter = context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item, localeList
                )
            }
            spinner.adapter = adapter
        }
    }


    private fun postSelect() {
        binding.courierButton.setOnClickListener {
            binding.courierRadioGroup.visibility = View.VISIBLE
            binding.parcelLockerInfo.visibility = View.GONE
            binding.localeSelectSpinner.visibility = View.GONE

        }
        binding.parcelLocker.setOnClickListener {
            binding.parcelLockerInfo.visibility = View.VISIBLE
            binding.courierRadioGroup.visibility = View.GONE
            binding.localeSelectSpinner.visibility = View.GONE
        }
        binding.localeRadioButton.setOnClickListener {
            binding.parcelLockerInfo.visibility = View.GONE
            binding.courierRadioGroup.visibility = View.GONE
            binding.localeSelectSpinner.visibility = View.VISIBLE
        }
    }

}