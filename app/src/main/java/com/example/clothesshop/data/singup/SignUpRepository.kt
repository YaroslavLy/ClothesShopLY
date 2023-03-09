package com.example.clothesshop.data.singup

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SignUpRepository @Inject constructor(private val singUpSource: SingUpSource) {
    fun signUp(username: String, password: String): Flow<Boolean> =
        singUpSource.signUp(username, password)
}