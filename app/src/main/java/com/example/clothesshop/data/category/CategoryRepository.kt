package com.example.clothesshop.data.category

import com.example.clothesshop.data.Resource
import com.example.clothesshop.model.Category
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class CategoryRepository @Inject constructor(private val categorySource: CategorySource) {

    fun getCategories() : Flow<Resource<Category>> = categorySource.getCategories()
}