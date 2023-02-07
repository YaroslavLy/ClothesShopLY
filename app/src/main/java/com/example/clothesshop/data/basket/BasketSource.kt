package com.example.clothesshop.data.basket

import com.example.clothesshop.data.Result
import com.example.clothesshop.model.ProductBasket
import kotlinx.coroutines.flow.Flow


interface BasketSource {

    fun getProducts(): Flow<Result<ProductBasket>>

    fun getCountProductsInBasket():Flow<Result<Int>>

    fun removeProductInBasket(productBasket: ProductBasket)
}