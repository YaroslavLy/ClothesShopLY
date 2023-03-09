package com.example.clothesshop.ui.tabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothesshop.data.Result
import com.example.clothesshop.data.basket.BasketRepository
import com.example.clothesshop.data.login.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TabsViewModel @Inject constructor(basketRepository: BasketRepository,private val loginRepository: LoginRepository):ViewModel() {

    private val _tabsForm = MutableLiveData<Int>()
    val tabsFormState: LiveData<Int> = _tabsForm

    //todo move from init code add add state
    init {
        viewModelScope.launch {
            basketRepository.getCountProductsInBasket().collect{ resource ->
                when (resource) {
                    is Result.Success -> {
                        _tabsForm.value = resource.data as Int
                    }
                    is Result.Error -> {
                        //Log.w(TAG, resource.error!!)
                    }
                }
            }
        }
    }

    fun logOut(){
        loginRepository.logout()
    }
}