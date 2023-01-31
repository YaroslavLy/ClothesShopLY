package com.example.clothesshop.model

// todo #14 rename (use new class in data module)
data class ProductBasket(
    val id: String? = "",
    val image: String? = "",
    val name: String? = "",
    val price: String? = "",
    val code: String? = "",
    val in_bascked: Boolean? = false,
    val type: String? = "",
    val description: String? = ""
) {
    // todo #15 override hashCode
    override fun equals(other: Any?): Boolean {
        return when(other) {
            is ProductBasket -> this.code == other.code
            is Product -> this.code == other.code
            else -> false
        }
    }
}