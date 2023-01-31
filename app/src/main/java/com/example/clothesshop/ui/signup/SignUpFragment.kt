package com.example.clothesshop.ui.signup

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
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.clothesshop.R
import com.example.clothesshop.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class SignUpFragment : Fragment() {



    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var signUpViewModel: SignUpViewModel
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val emailEditText = binding.emailSignUp
        val passwordEditTextFirst = binding.passwordSignUp
        val passwordEditTextSecond = binding.passwordSignUpRepeat
        val dataError = binding.tex
        val buttonEnd= binding.doLog

        signUpViewModel =
            ViewModelProvider(
                this,
                SignUpViewModelFactory()
            )[SignUpViewModel::class.java]


        val signupButton = binding.signup
        val loadingProgressBar = binding.loading
        buttonEnd.setOnClickListener {
           findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        signupButton.setOnClickListener {
            if (checkPasswordsComparability(passwordEditTextFirst, passwordEditTextSecond)) {
                waitStateUI(
                    loadingProgressBar,
                    emailEditText,
                    passwordEditTextFirst,
                    passwordEditTextSecond,
                    signupButton,
                    false
                )

                uiScope.launch(Dispatchers.IO) {
                    signUpViewModel.signUp(
                        emailEditText.text.toString(),
                        passwordEditTextSecond.text.toString()
                    )
                }


                signUpViewModel.signupResult.observe(viewLifecycleOwner,
                    Observer { signUpFormState ->
                        if (signUpFormState == null) {
                            return@Observer
                        }
                        if(signUpFormState) {
                            binding.searchLayout.visibility = View.GONE
                            binding.searchLayout3.visibility = View.GONE
                            binding.searchLayout1.visibility = View.GONE
                            loadingProgressBar.visibility = View.GONE
                            signupButton.visibility = View.GONE
                            //passwordEditTextSecond.visibility = View.GONE
                            //passwordEditTextFirst.visibility = View.GONE
                            //emailEditText.visibility = View.GONE
                            dataError.visibility = View.VISIBLE
                            //dataError.text = "Udało się założyć konto Potwierdź pocztę (kliknij  link w liście do poczty, który podałeś podczas logowania) i przejdź do logowania"
                            buttonEnd.visibility = View.VISIBLE
                        }else{

                            val appContext = context?.applicationContext
                            Toast.makeText(appContext, "NIE udało się zarejestować się ", Toast.LENGTH_LONG).show()
                            waitStateUI(
                                loadingProgressBar,
                                emailEditText,
                                passwordEditTextFirst,
                                passwordEditTextSecond,
                                signupButton,
                                true
                            )
//                            dataError.visibility = View.VISIBLE
//                            dataError.text = "NIE Hura"
//                            buttonEnd.visibility = View.VISIBLE
                        }
                    })
            } else {
                passwordEditTextSecond.error ="Hasła są różne" //getString("Nieprawidłowe dane")
            }
        }



        signUpViewModel.signupFormState.observe(viewLifecycleOwner,
            Observer { signUpFormState ->
                if (signUpFormState == null) {
                    return@Observer
                }
                signupButton.isEnabled = signUpFormState.isDataValid
                signUpFormState.emailError?.let {
                    emailEditText.error = getString(it)
                }
                signUpFormState.passwordFirstError?.let {
                    passwordEditTextFirst.error = getString(it)
                }
                signUpFormState.passwordSecondError?.let {
                    passwordEditTextSecond.error = getString(it)
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                signUpViewModel.signUpDataChanged(
                    emailEditText.text.toString(),
                    passwordEditTextFirst.text.toString(),
                    passwordEditTextSecond.text.toString()
                )
            }
        }
        emailEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditTextFirst.addTextChangedListener(afterTextChangedListener)
        passwordEditTextSecond.addTextChangedListener(afterTextChangedListener)

    }

    private fun waitStateUI(
        loadingProgressBar: ProgressBar,
        emailEditText: EditText,
        passwordEditTextFirst: EditText,
        passwordEditTextSecond: EditText,
        signupButton: Button,
        b: Boolean
    ) {
        if(!b) {
            loadingProgressBar.visibility = View.VISIBLE
        }else
        {
            loadingProgressBar.visibility = View.GONE
        }
        emailEditText.isEnabled = b
        passwordEditTextFirst.isEnabled = b
        passwordEditTextSecond.isEnabled = b
        signupButton.isEnabled = b
    }

    private fun checkPasswordsComparability(
        passwordEditTextFirst: EditText,
        passwordEditTextSecond: EditText
    ) = passwordEditTextFirst.text.toString() == passwordEditTextSecond.text.toString()

}



