package com.example.clothesshop.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clothesshop.data.CategoryRepository
import com.example.clothesshop.data.SignUpRepository
import com.example.clothesshop.ui.category.CategoryViewModel


class SignUpViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignUpViewModel(signUpRepository = SignUpRepository()) as T
    }
}
