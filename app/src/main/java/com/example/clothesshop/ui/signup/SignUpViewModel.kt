package com.example.clothesshop.ui.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothesshop.R
import com.example.clothesshop.data.singup.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val signUpRepository: SignUpRepository) : ViewModel() {

    private val _signupForm = MutableLiveData<SignUpFormState>()
    val signupFormState: LiveData<SignUpFormState> = _signupForm

    private val _signupResult = MutableLiveData<Boolean>()
    val signupResult: LiveData<Boolean> = _signupResult


    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                // run read - write (ex. write read DB)
            }

            withContext(Dispatchers.Default){
                // run важкі math  operation
            }
        }
    }


     fun signUp(username: String, password: String){
        //_signupResult.value =
            signUpRepository.signUp(username,password).onEach { resource ->
                when (resource) {
                    true -> { //is Resource.Error
                        _signupResult.value = resource//_categoryForm.value = resource.data!!
                    }
                    else -> {
                        _signupResult.value = resource
                        //Log.w(TAG, resource.error!!)
                    }
                }
            }.launchIn(viewModelScope)

    }

    fun signUpDataChanged(username: String, passwordFirst: String,passwordSecond: String) {
        if (!isUserNameValid(username)) {
            _signupForm.value = SignUpFormState(emailError =  R.string.invalid_username)
        } else if (!isPasswordValid(passwordFirst)) {
            _signupForm.value = SignUpFormState(passwordFirstError = R.string.invalid_password)
        } else if(!isPasswordValid(passwordSecond)) {
            _signupForm.value = SignUpFormState(passwordSecondError = R.string.invalid_password)
        }
        else{
            _signupForm.value = SignUpFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

}