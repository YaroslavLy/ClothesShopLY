package com.example.clothesshop.data.order

import com.example.clothesshop.data.Result
import com.example.clothesshop.model.Order
import com.example.clothesshop.model.OrderView
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class OrderRepository @Inject constructor(private val orderSource: OrderSource) {

    fun save(order: Order) = orderSource.save(order)

    fun getOrders(path: String): Flow<Result<OrderView>> = orderSource.getOrders(path)

}