package com.example.clothesshop.model

import com.google.firebase.database.PropertyName

data class Category(
    @get:PropertyName("url_image") @set:PropertyName("url_image") var urlImage: String? = "",
    @get:PropertyName("name_folder") @set:PropertyName("name_folder") var nameFolder: String? = "",
    @get:PropertyName("name_pl") @set:PropertyName("name_pl") var namePl: String? = ""
)