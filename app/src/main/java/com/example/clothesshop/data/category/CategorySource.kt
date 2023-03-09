package com.example.clothesshop.data.category

import com.example.clothesshop.data.Resource
import com.example.clothesshop.model.Category
import kotlinx.coroutines.flow.Flow

interface CategorySource {
    fun getCategories() : Flow<Resource<Category>>
}