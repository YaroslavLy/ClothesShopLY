package com.example.clothesshop.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.clothesshop.data.login.LoginRepository
import com.example.clothesshop.data.Result

import com.example.clothesshop.R
import com.example.clothesshop.model.LoggedInUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) :
    ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            loginRepository.login(username, password).collect { result ->
                when (result) {
                    is Result.Success -> {
                        val t = result.data as LoggedInUser
                        _loginResult.postValue(
                            LoginResult(success = LoggedInUserView(displayName = t.displayName))
                        )
                    }
                    is Result.Error -> {
                        _loginResult.postValue(LoginResult(error = R.string.login_failed))
                    }
                }
            }
        }
    }

    //todo move code inside to repo
    private lateinit var auth: FirebaseAuth
    fun loginAnonymous() {
        viewModelScope.launch {

            auth = Firebase.auth
            auth.signInAnonymously()
            delay(1000L)
            _loginResult.postValue(
                LoginResult(success = LoggedInUserView(displayName = "Anonym"))
            )
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
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