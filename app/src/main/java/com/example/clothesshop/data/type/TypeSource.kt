package com.example.clothesshop.data.type

import com.example.clothesshop.data.Resource
import com.example.clothesshop.model.Type
import kotlinx.coroutines.flow.Flow

interface TypeSource {
    fun getTypes(path: String): Flow<Resource<Type>>
}