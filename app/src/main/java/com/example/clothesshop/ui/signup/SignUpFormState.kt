package com.example.clothesshop.ui.signup

//first second
data class SignUpFormState (val emailError: Int? = null,
                           val passwordFirstError: Int? = null,
                            val passwordSecondError: Int? = null,
                           val isDataValid: Boolean = false)