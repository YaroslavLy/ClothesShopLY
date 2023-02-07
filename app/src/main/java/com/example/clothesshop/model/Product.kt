package com.example.clothesshop.model

import com.google.firebase.database.PropertyName



data class Product(
    val image: String? = "",
    val name: String? = "",
    val price: String? = "",
    val code: String? = "",
    @get:PropertyName("in_bascked") @set:PropertyName("in_bascked") var inBasked: Boolean? = false,
    val type: String? = "",
    val description: String? = ""
){
    fun toProductBasket(): ProductBasket{
       return ProductBasket("id",
            image,
            name,
            price,
            code,
           inBasked,
            type,
            description)
    }

    override fun equals(other: Any?): Boolean {
        if(other is Product){
            return this.code==other.code
        }else
            return false
    }
}
