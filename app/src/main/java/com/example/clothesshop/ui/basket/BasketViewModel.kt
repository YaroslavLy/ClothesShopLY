package com.example.clothesshop.ui.basket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothesshop.data.basket.BasketRepository
import com.example.clothesshop.data.Result
import com.example.clothesshop.model.ProductBasket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(private val basketRepository: BasketRepository) :
    ViewModel() {

    // todo #2 create base View Model
    private val _tabsForm = MutableLiveData<Int>()
    val tabsFormState: LiveData<Int> = _tabsForm

    //todo #20 replace use stateFlow
    //todo #21 add state loading, error
    //todo #22 use dataBinding

    init {
        viewModelScope.launch {
            basketRepository.getCountProductsInBasket().collect { count ->
                _tabsForm.value = count
            }
        }
    }

    private val _basketProductForm = MutableLiveData<ProductBasket>()
    val basketProductFormState: LiveData<ProductBasket> = _basketProductForm


    //
    fun getProductsFromBasket() {
        viewModelScope.launch {
            basketRepository.getProducts().collect { resource ->
                when (resource) {
                    is Result.Success -> {
                        _basketProductForm.value = resource.data as ProductBasket
                    }
                    is Result.Error -> {
                        //Log.w(TAG, resource.error!!)
                    }
                }
            }
        }

    }

    fun removeProductFromBasket(productBasket: ProductBasket) {
        basketRepository.removeProductInBasket(productBasket)
    }

}