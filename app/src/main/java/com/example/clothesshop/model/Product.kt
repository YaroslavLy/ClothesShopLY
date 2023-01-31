package com.example.clothesshop.model


// todo #13 rename (use new class in data module)
data class Product(
    val image: String? = "",
    val name: String? = "",
    val price: String? = "",
    val code: String? = "",
    val in_bascked: Boolean? = false,
    val type: String? = "",
    val description: String? = ""
){
    fun toProductBasket(): ProductBasket{
       return ProductBasket("id",
            image,
            name,
            price,
            code,
            in_bascked,
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
