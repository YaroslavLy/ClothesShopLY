package com.example.clothesshop.ui.order

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.clothesshop.data.order.OrderRepository
import com.example.clothesshop.databinding.FragmentOrderSummaryBinding
import com.example.clothesshop.model.Order
import com.example.clothesshop.model.ProductBasket
import com.example.clothesshop.ui.basket.BasketViewModel
import com.example.clothesshop.utils.Constants
import com.example.clothesshop.utils.parsers.PriceParser
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class OrderSummaryFragment : Fragment() {


    // todo move to constants
    private val CARD_NUMBER_TOTAL_SYMBOLS = 19 // size of pattern 0000-0000-0000-0000

    private val CARD_NUMBER_TOTAL_DIGITS = 16 // max numbers of digits in pattern: 0000 x 4

    private val CARD_NUMBER_DIVIDER_MODULO =
        5 // means divider position is every 5th symbol beginning with 1

    private val CARD_NUMBER_DIVIDER_POSITION =
        CARD_NUMBER_DIVIDER_MODULO - 1 // means divider position is every 4th symbol beginning with 0

    private val CARD_NUMBER_DIVIDER = '-'

    private val CARD_DATE_TOTAL_SYMBOLS = 5 // size of pattern MM/YY

    private val CARD_DATE_TOTAL_DIGITS = 4 // max numbers of digits in pattern: MM + YY

    private val CARD_DATE_DIVIDER_MODULO =
        3 // means divider position is every 3rd symbol beginning with 1

    private val CARD_DATE_DIVIDER_POSITION =
        CARD_DATE_DIVIDER_MODULO - 1 // means divider position is every 2nd symbol beginning with 0

    private val CARD_DATE_DIVIDER = '/'

    private val CARD_CVC_TOTAL_SYMBOLS = 3


    private var _binding: FragmentOrderSummaryBinding? = null
    private val binding get() = _binding!!


    private val orderViewModel: OrderViewModel by activityViewModels()
    private var data = ArrayList<ProductBasket>()
    private var textNames = ""
    private var textPrices = ""
    private val basketViewModel by viewModels<BasketViewModel> ()

    override fun onResume() {
        super.onResume()
        binding.blikSummary.text = "Podaj Kod BliK."
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: OrderSummaryFragmentArgs by navArgs()
        val delivery = args.delivery
        var typePay = args.payType
        val address = args.address
        var notEmpty = false

        setPaymentVisibility(typePay)

        binding.nameDelivery.text = delivery

        cardNumberChange()
        cardDateChange()
        cardCVCChange()

        //orderViewModel = OrderViewModel(OrderRepository())

        basketViewModel.getProductsFromBasket()
//        basketViewModel.basketProductFormState.observe(
//            viewLifecycleOwner,
//            Observer { productBasket ->
//                if (productBasket == null) {
//                    return@Observer
//                }
//                updateUiWithProduct(productBasket)
//            })



        binding.buttonAccept.setOnClickListener {

            if (notEmpty) {


                findNavController().popBackStack()
                findNavController().popBackStack()
                findNavController().popBackStack()

                return@setOnClickListener
            }

            when (typePay) {
                Constants.KEY_PAY_CARD -> {
                    binding.cardNumberEditText.visibility = View.VISIBLE
                    binding.card.visibility = View.VISIBLE
                    val cardNumber = binding.cardNumberEditText.text.length
                    val cardDate = binding.cardDateEditText.text.length
                    val cardCVC = binding.cardCVCEditText.text.length
                    if (cardNumber == 19 && cardDate == 5 && cardCVC == 3)
                        notEmpty = true
                    else {
                        if (cardNumber != 19) {
                            binding.cardNumberEditText.error = "Błędny format numeru karty bankowej"
                        }
                        if (cardDate != 5) {
                            binding.cardDateEditText.error = "Błędny format Daty"
                        }
                        if (cardCVC != 3) {
                            binding.cardCVCEditText.error = "Błędny format CVC"
                        }
                    }

                }
                Constants.KEY_PAY_BLIK -> {
                    if (binding.blikSummaryEditText.text.length == 6)
                        notEmpty = true
                    else
                        binding.blikSummaryEditText.error = "Blik kod musi składać się z 6 cyfr "
                }
                Constants.KEY_PAY_CASH -> {
                    notEmpty = true
                }
            }

            if (notEmpty) {

                // todo #1 add order to DB and remove all  items in Cart
                orderViewModel.saveOrder(
                    Order(
                        address = address,
                        sumPrice = binding.priceSumDelivery.text as String,
                        ifPaid = typePay != Constants.KEY_PAY_CASH,
                        dateOrder = Date(),
                        products = data
                    )
                )

                binding.cardNumberEditText.visibility = View.GONE
                binding.card.visibility = View.GONE

                binding.blikSummary.visibility = View.VISIBLE
                binding.blikSummaryEditText.visibility = View.GONE

                binding.localSummary.visibility = View.GONE
                binding.cashPriceAdd.visibility = View.GONE

                binding.blikSummary.text =
                    "Zamówienie zostało podtwierdzone, kliknij Do Sklepu, aby zobaczyć zamówienie"

                binding.buttonAccept.text = "Do Sklepu"

                binding.seekBar.progress = 4
            }
        }


    }

    private fun setPaymentVisibility(typePay: Int) {
        when (typePay) {
            Constants.KEY_PAY_CARD -> {
                binding.cardNumberEditText.visibility = View.VISIBLE
                binding.card.visibility = View.VISIBLE

                binding.blikSummary.visibility = View.GONE
                binding.blikSummaryEditText.visibility = View.GONE

                binding.localSummary.visibility = View.GONE
                binding.cashPriceAdd.visibility = View.GONE
            }
            Constants.KEY_PAY_BLIK -> {
                binding.cardNumberEditText.visibility = View.GONE
                binding.card.visibility = View.GONE

                binding.blikSummary.visibility = View.VISIBLE
                binding.blikSummaryEditText.visibility = View.VISIBLE

                binding.localSummary.visibility = View.GONE
                binding.cashPriceAdd.visibility = View.GONE
            }
            Constants.KEY_PAY_CASH -> {
                binding.cardNumberEditText.visibility = View.GONE
                binding.card.visibility = View.GONE

                binding.blikSummary.visibility = View.GONE
                binding.blikSummaryEditText.visibility = View.GONE

                binding.localSummary.visibility = View.VISIBLE
                binding.cashPriceAdd.visibility = View.VISIBLE
            }
        }
    }

    private fun containBasketProduct(product: ProductBasket): Boolean {
        for (pr in data) {
            if (pr.code.equals(product.code)) {
                return true
            }
        }
        return false
    }

    private fun updateUiWithProduct(product: ProductBasket) {
        if (!containBasketProduct(product)) {
            data.add(product)

            textNames = if (binding.nameProduct.text == "") {
                product.name.toString()
            } else {
                textNames + "\n" + product.name.toString()
            }

            if (binding.priceProduct.text == "") {
                textPrices = product.price.toString()
            } else {
                textPrices = textPrices + "\n\n" + product.price.toString()
            }

            binding.nameProduct.text = textNames
            binding.priceProduct.text = textPrices
            updatePriceData()
        }


    }

    private fun updatePriceData() {
        val listPrice = mutableListOf<String>()
        for (s in data) {
            s.price?.let { listPrice.add(it) }
        }
        listPrice.add(binding.priceDelivery.text as String)
        if (binding.cashPriceAdd.visibility == View.VISIBLE)
            listPrice.add(binding.priceDeliveryCash.text as String)
        binding.priceSumDelivery.text = PriceParser.sumPrise(listPrice)
    }

    private fun cardCVCChange() {
        binding.cardCVCEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                sm: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                val s = sm as Editable
                if (s.length > CARD_CVC_TOTAL_SYMBOLS) {
                    s.delete(CARD_CVC_TOTAL_SYMBOLS, s.length)
                }
            }
        })
    }

    private fun cardDateChange() {
        binding.cardDateEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (!isInputCorrect(
                        s as Editable,
                        CARD_DATE_TOTAL_SYMBOLS,
                        CARD_DATE_DIVIDER_MODULO,
                        CARD_DATE_DIVIDER
                    )
                ) {
                    s.replace(
                        0,
                        s.length,
                        concatString(
                            getDigitArray(s, CARD_DATE_TOTAL_DIGITS),
                            CARD_DATE_DIVIDER_POSITION,
                            CARD_DATE_DIVIDER
                        )
                    )
                }
            }
        })
    }

    private fun cardNumberChange() {
        binding.cardNumberEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (!isInputCorrect(
                        s as Editable,
                        CARD_NUMBER_TOTAL_SYMBOLS,
                        CARD_NUMBER_DIVIDER_MODULO,
                        CARD_NUMBER_DIVIDER
                    )
                ) {
                    s.replace(
                        0,
                        s.length,
                        concatString(
                            getDigitArray(s, CARD_NUMBER_TOTAL_DIGITS),
                            CARD_NUMBER_DIVIDER_POSITION,
                            CARD_NUMBER_DIVIDER
                        )
                    )
                }
            }
        })
    }


    private fun isInputCorrect(
        s: Editable,
        size: Int,
        dividerPosition: Int,
        divider: Char
    ): Boolean {
        var isCorrect = s.length <= size
        for (i in 0 until s.length) {
            isCorrect = if (i > 0 && (i + 1) % dividerPosition == 0) {
                isCorrect and (divider == s[i])
            } else {
                isCorrect and Character.isDigit(s[i])
            }
        }
        return isCorrect
    }

    private fun concatString(digits: CharArray, dividerPosition: Int, divider: Char): String? {
        val formatted = StringBuilder()
        for (i in digits.indices) {
            if (digits[i].code != 0) {
                formatted.append(digits[i])
                if (i > 0 && i < digits.size - 1 && (i + 1) % dividerPosition == 0) {
                    formatted.append(divider)
                }
            }
        }
        return formatted.toString()
    }

    private fun getDigitArray(s: Editable, size: Int): CharArray {
        val digits = CharArray(size)
        var index = 0
        var i = 0
        while (i < s.length && index < size) {
            val current = s[i]
            if (Character.isDigit(current)) {
                digits[index] = current
                index++
            }
            i++
        }
        return digits
    }


}