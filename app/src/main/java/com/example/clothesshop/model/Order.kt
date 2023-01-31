package com.example.clothesshop.model

import java.util.Date

// todo #11 rename (use new class in data module)
data class Order(
    val address: String? = "",
    val sumPrice: String? = "",
    val products: List<ProductBasket>? = null,
    val ifPaid: Boolean?= false,
    val dateOrder: Date? = Date()
)