package com.example.clothesshop.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clothesshop.data.order.OrderRepository
import com.example.clothesshop.data.Result
import com.example.clothesshop.model.Order
import com.example.clothesshop.model.OrderView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor (
    private var orderRepository: OrderRepository
) : ViewModel() {

    //todo #2 move state payment and use data binding

    private val _ordersForm = MutableLiveData<OrderView>()
    val ordersFormState: LiveData<OrderView> = _ordersForm

    fun saveOrder(order: Order) {
        orderRepository.save(order)
    }



    fun getOrders(){
        viewModelScope.launch {
            orderRepository.getOrders("").collect{ resource ->
                when (resource) {
                    is Result.Success -> {
                        _ordersForm.value = resource.data!! as OrderView
                    }
                    is Result.Error -> {
                        //Log.w(TAG, resource.error!!)
                    }
                }
            }
        }
    }

}