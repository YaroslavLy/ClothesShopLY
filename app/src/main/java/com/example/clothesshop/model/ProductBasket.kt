package com.example.clothesshop.model

import com.google.firebase.database.PropertyName

data class ProductBasket(
    val id: String? = "",
    val image: String? = "",
    val name: String? = "",
    val price: String? = "",
    val code: String? = "",
    @get:PropertyName("in_bascked") @set:PropertyName("in_bascked") var inBasked: Boolean? = false,
    val type: String? = "",
    val description: String? = ""
) {

    override fun equals(other: Any?): Boolean {
        return when(other) {
            is ProductBasket -> this.code == other.code
            is Product -> this.code == other.code
            else -> false
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}