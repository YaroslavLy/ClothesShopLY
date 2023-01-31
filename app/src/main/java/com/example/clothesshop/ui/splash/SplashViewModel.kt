package com.example.clothesshop.ui.splash


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * SplashViewModel checks whether user is signed-in or not.
 */
class SplashViewModel(
    //private val accountsRepository: AccountsRepository
) : ViewModel() {

    private val _launchMainScreenEvent = MutableLiveData<Boolean>()
    val launchMainScreenEvent = _launchMainScreenEvent
    private lateinit var auth: FirebaseAuth


    init {
        viewModelScope.launch {
            delay(1000)
            //_categoryForm.value = resource.data!!
            // todo use repo check login
            auth = Firebase.auth
            val currentUser = auth.currentUser
            //currentUser
            if(currentUser != null) {
                if(currentUser.isAnonymous) {
                    _launchMainScreenEvent.value = true
                }else _launchMainScreenEvent.value = currentUser.isEmailVerified
            }else {
                _launchMainScreenEvent.value = false
            }
            //_launchMainScreenEvent.value=false
        }
    }
}