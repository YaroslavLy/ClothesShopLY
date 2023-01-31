package com.example.clothesshop.ui.order

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.clothesshop.R
import com.example.clothesshop.databinding.FragmentBasketBinding
import com.example.clothesshop.databinding.FragmentOrderBinding


class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameCheck()
        numberCheck()
        emailCheck()
        streetNumberCheck()
        postNumberCheck()
        placeCheck()


        binding.next.setOnClickListener {
            if (ifValidEnterData()) {
                var sendText = ""
                sendText =
                    binding.name.text.toString() + "\n" + binding.number.text.toString() + "\n" +
                            binding.email.text.toString() + "\n" + binding.streetNumber.text.toString() + "\n" +
                            binding.postNumber.text.toString() + "\n" + binding.place.text.toString()

                val action =
                    OrderFragmentDirections.actionOrderFragmentToOrderPaymentFragment(sendText)
                Navigation.findNavController(view).navigate(action)
                //findNavController().navigate(R.id.action_orderFragment_to_orderPaymentFragment)
            }
        }
    }



    private fun ifValidEnterData(): Boolean {

        //check name
        val name = binding.name.text.toString().trimEnd(' ')
        if (name.isEmpty() || name.contains("[0-9!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex()) || !name.contains(
                ' '
            )
        ) {
            binding.nameLayout.error = "Nieprawidłowe dane (Format: \"Imie Nazwisko\")"
            return false
        }

        //check number
        val number = binding.number.text.toString().trimEnd(' ')
        if (number.isEmpty() || number.contains("[A-Za-z!\"#$%&'()*,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex()) ||
            number.contains(' ')
        ){
            binding.numberLayout.error = "Nieprawidłowe dane (Format: \"+470123456789\")"
            return false
        }

        //check e-mail
        val email =binding.email.text.toString().trimEnd(' ')
        if(email.isEmpty() || !email.contains("@") || !Patterns.EMAIL_ADDRESS.matcher(email).matches() ){
            binding.emailLayout.error = "Nieprawidłowe dane (Format: \"xxxxx@xxx.xxx\")"
            return false
        }

        //check street-number
        val streetNumber = binding.streetNumber.text.toString().trimEnd(' ')
        if (streetNumber.isEmpty() || streetNumber.contains("[!\"#$%&'()*+,.:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex()) || !streetNumber.contains(
                ' '
            )
        ) {
            binding.streetNumberLayout.error = "Nieprawidłowe dane (Format: \"Ulica 2223\")"
            return false
        }

        //check post number
        val postNumber = binding.postNumber.text.toString()
        if (postNumber.isEmpty())
         {
            binding.postNumberLayout.error = "Nieprawidłowe dane (Format: \"xxxx\")"
            return false
        }


        //check place
        val place = binding.place.text.toString().trimEnd(' ')
        if (place.isEmpty() || place.contains("[0-9!\"#$%&'()*+,-. /:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex())
        ) {
            binding.placeLayout.error = "Nieprawidłowe dane (Format: \"Miejscowość\")"
            return false
        }

        return true
    }

    private fun nameCheck() {
        binding.name.addTextChangedListener(object : TextWatcher {

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
                binding.nameLayout.isErrorEnabled = false
            }
        })
    }
    private fun numberCheck() {
        binding.number.addTextChangedListener(object : TextWatcher {

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
                binding.numberLayout.isErrorEnabled = false
            }
        })
    }

    private fun emailCheck() {
        binding.email.addTextChangedListener(object : TextWatcher {

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
                binding.emailLayout.isErrorEnabled = false
            }
        })
    }


    private fun streetNumberCheck() {
        binding.streetNumber.addTextChangedListener(object : TextWatcher {

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
                binding.streetNumberLayout.isErrorEnabled = false
            }
        })
    }

    private fun postNumberCheck() {
        binding.postNumber.addTextChangedListener(object : TextWatcher {

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
                binding.postNumberLayout.isErrorEnabled = false
            }
        })
    }


    private fun placeCheck() {
        binding.place.addTextChangedListener(object : TextWatcher {

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
                binding.placeLayout.isErrorEnabled = false
            }
        })
    }

}