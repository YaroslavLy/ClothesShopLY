package com.example.clothesshop.model

import java.util.*

// todo #12 rename (use new class in data module)
data class OrderView(
    val id: String? = "",
    val address: String? = "",
    val products: List<ProductBasket>,
    val sumPrice: String? = "",
    val ifPaid: Boolean= false,
    val dateOrder: Date

)