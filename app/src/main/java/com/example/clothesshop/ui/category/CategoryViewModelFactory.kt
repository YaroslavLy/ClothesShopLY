package com.example.clothesshop.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clothesshop.data.CategoryRepository

class CategoryViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(categoryRepository = CategoryRepository()) as T
    }
}

