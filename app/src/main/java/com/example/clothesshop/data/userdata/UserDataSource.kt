package com.example.clothesshop.data.userdata

interface UserDataSource {

    fun saveUid(uid: String?)

    fun getUid(): String?

}