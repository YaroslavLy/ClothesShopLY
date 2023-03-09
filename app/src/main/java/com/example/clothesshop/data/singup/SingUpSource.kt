package com.example.clothesshop.data.singup

import kotlinx.coroutines.flow.Flow

interface SingUpSource {
    fun signUp(username: String, password: String): Flow<Boolean>
}