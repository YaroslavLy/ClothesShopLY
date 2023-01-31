package com.example.clothesshop.data

interface UserDataSource {

    fun saveUid(uid: String?)

    fun getUid(): String?

}