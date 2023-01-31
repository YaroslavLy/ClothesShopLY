package com.example.clothesshop.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clothesshop.data.ProductRepository

class ProductViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductViewModel(
            productRepository = ProductRepository("path"),
        ) as T
    }
}