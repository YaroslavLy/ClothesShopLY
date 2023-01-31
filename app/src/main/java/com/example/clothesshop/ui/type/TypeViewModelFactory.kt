package com.example.clothesshop.ui.type

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clothesshop.data.CategoryRepository
import com.example.clothesshop.data.TypeRepository
import com.example.clothesshop.ui.category.CategoryViewModel


class TypeViewModelFactory(private val path: String): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TypeViewModel(typeRepository = TypeRepository(path)) as T
    }
}