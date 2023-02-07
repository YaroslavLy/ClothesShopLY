package com.example.clothesshop

import android.view.View
import android.widget.Button
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun bindButton(button: Button, visible: Boolean?) {
    if(visible == true){
        button.visibility = View.VISIBLE
    }else{
        button.visibility = View.INVISIBLE
    }
}