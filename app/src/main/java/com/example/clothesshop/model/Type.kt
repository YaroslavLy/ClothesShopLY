package com.example.clothesshop.model

// todo #17 rename (use new class in data module)
data class Type(
    val urlImage: String? = "",
    val name: String? = "",
    val nameFolder: String? = ""
){
    // todo #16 override hashCode
    override fun equals(other: Any?): Boolean {
        return if(other is Type){
            other.name == name
        }else {
            false
        }
    }
}