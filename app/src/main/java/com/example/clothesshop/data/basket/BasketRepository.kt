package com.example.clothesshop.data.basket

import com.example.clothesshop.data.Result
import com.example.clothesshop.model.ProductBasket
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class BasketRepository @Inject constructor(private val basketSource: BasketSource) {

    fun getProducts(): Flow<Result<ProductBasket>> = basketSource.getProducts()

    fun getCountProductsInBasket(): Flow<Int> = basketSource.getCountProductsInBasket()

    fun removeProductInBasket(productBasket: ProductBasket) =
        basketSource.removeProductInBasket(productBasket)

}