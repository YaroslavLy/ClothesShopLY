package com.example.clothesshop.data.product

import com.example.clothesshop.data.Resource
import com.example.clothesshop.data.Result
import com.example.clothesshop.model.Product
import com.example.clothesshop.model.ProductBasket
import kotlinx.coroutines.flow.Flow

interface ProductSource {
    fun getProducts(path: String) : Flow<Resource<Product>>
    fun getProduct(id: String,path: String): Flow<Resource<Product>>
    fun getProductsBasket(): Flow<Result<ProductBasket>>
}