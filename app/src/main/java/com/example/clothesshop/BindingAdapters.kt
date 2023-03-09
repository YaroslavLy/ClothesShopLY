package com.example.clothesshop

import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("visible")
fun bindButton(button: Button, visible: Boolean?) {
    if(visible == true){
        button.visibility = View.VISIBLE
    }else{
        button.visibility = View.INVISIBLE
    }
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    GlideApp.with(imgView.context)
        .load(imgUrl)
        .error(R.drawable.ic_baseline_autorenew_24)
        .apply(RequestOptions.bitmapTransform( RoundedCorners(14)))
        .placeholder(R.drawable.loading_animation)
        .into(imgView)
}