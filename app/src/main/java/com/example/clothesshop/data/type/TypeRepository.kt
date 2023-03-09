package com.example.clothesshop.data.type

import com.example.clothesshop.data.Resource
import com.example.clothesshop.model.Type
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class TypeRepository @Inject constructor (private val typeSource: TypeSource) {

    fun getTypes(path: String): Flow<Resource<Type>> = typeSource.getTypes(path)
}
