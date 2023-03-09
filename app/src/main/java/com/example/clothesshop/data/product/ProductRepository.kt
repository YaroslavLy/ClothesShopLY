package com.example.clothesshop.data.product

import com.example.clothesshop.data.Resource
import com.example.clothesshop.data.Result
import com.example.clothesshop.model.Product
import com.example.clothesshop.model.ProductBasket
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productSource: ProductSource) {

    fun getProducts(path: String) : Flow<Resource<Product>> = productSource.getProducts(path)

    fun getProduct(id: String,path: String): Flow<Resource<Product>> = productSource.getProduct(id,path)

    fun getProductsBasket(): Flow<Result<ProductBasket>> = productSource.getProductsBasket()
}