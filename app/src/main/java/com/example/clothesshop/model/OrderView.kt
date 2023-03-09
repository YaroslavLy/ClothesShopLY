package com.example.clothesshop.model

import java.util.*


data class OrderView(
    val id: String? = "",
    val address: String? = "",
    val products: List<ProductBasket>,
    val sumPrice: String? = "",
    val ifPaid: Boolean= false,
    val dateOrder: Date

)