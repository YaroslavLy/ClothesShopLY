package com.example.clothesshop.ui.basket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothesshop.data.basket.BasketRepository
import com.example.clothesshop.data.Result
import com.example.clothesshop.model.ProductBasket
import com.example.clothesshop.utils.parsers.PriceParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(private val basketRepository: BasketRepository) :
    ViewModel() {

    //todo #21 add state loading, error
    //todo #22 add databinding to list ProductBasket

//    private val _basketProductForm = MutableLiveData<ProductBasket>()
//    val basketProductFormState: LiveData<ProductBasket> = _basketProductForm

    private val _productsBasketStateFlow = MutableStateFlow<List<ProductBasket>>(emptyList())
    val productsBasketState = _productsBasketStateFlow.asStateFlow()

    private val _priceStateFlow  = MutableStateFlow<String>("0,0 ZŁ")
    val priceState = _priceStateFlow.asStateFlow()

    private val _visibleButton = MutableStateFlow(false)
    val visibleButton = _visibleButton.asStateFlow()


    init {
        viewModelScope.launch {
            basketRepository.getProducts().collect { resource ->
                when (resource) {
                    is Result.Success -> {
                        val productBasket = resource.data as ProductBasket
                        if(!_productsBasketStateFlow.value.contains(productBasket)) {
                            _productsBasketStateFlow.value += productBasket

                            _priceStateFlow.value = sumPricesBasketProducts()
                        }
                        _visibleButton.value = _productsBasketStateFlow.value.isNotEmpty()
                    }
                    is Result.Error -> {
                        //Log.w(TAG, resource.error!!)
                    }
                }
            }
        }
    }

    fun getProductsFromBasket()//: Flow<Result<ProductBasket>> //= basketRepository.getProducts()
    {
        viewModelScope.launch {
            basketRepository.getProducts().collect { resource ->
                when (resource) {
                    is Result.Success -> {
                        //val t = resource.data as ProductBasket
                        //val li = _productsBasketStateFlow.value

                        _productsBasketStateFlow.value += (resource.data as ProductBasket)
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
        val list = _productsBasketStateFlow.value.toMutableList()
        list.remove(productBasket)
        _productsBasketStateFlow.value = list
        _priceStateFlow.value = sumPricesBasketProducts()
        _visibleButton.value = _productsBasketStateFlow.value.isNotEmpty()

    }

    private fun sumPricesBasketProducts(): String{
        val listPrice = mutableListOf<String>()
        for (s in _productsBasketStateFlow.value) {
            s.price?.let { listPrice.add(it) }
        }
        return PriceParser.sumPrise(listPrice)
    }

}