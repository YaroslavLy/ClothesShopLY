package com.example.clothesshop.model


data class Type(
    val urlImage: String? = "",
    val name: String? = "",
    val nameFolder: String? = ""
){

    override fun equals(other: Any?): Boolean {
        return if(other is Type){
            other.name == name
        }else {
            false
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}