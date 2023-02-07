package com.example.clothesshop.data.order

import com.example.clothesshop.data.Result
import com.example.clothesshop.model.Order
import com.example.clothesshop.model.OrderView
import kotlinx.coroutines.flow.Flow

interface OrderSource {

    fun getOrders(path: String): Flow<Result<OrderView>>

    fun save(order: Order)

}