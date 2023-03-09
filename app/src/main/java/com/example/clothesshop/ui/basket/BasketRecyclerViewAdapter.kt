package com.example.clothesshop.ui.basket

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.clothesshop.GlideApp
import com.example.clothesshop.R
import com.example.clothesshop.databinding.FragmentProductBasketBinding
import com.example.clothesshop.model.ProductBasket
import com.example.clothesshop.ui.basket.BasketRecyclerViewAdapter.ProductBasketViewHolder
import com.example.clothesshop.ui.tabs.TabsFragmentDirections


interface ProductBasketActionListener {

    fun onProductDelete(productBasket: ProductBasket)

    fun onProductDetails(productBasket: ProductBasket)
}

class BasketRecyclerViewAdapter(private val actionListener: ProductBasketActionListener) :
    ListAdapter<ProductBasket,BasketRecyclerViewAdapter.ProductBasketViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductBasketViewHolder {

        val viewHolder = ProductBasketViewHolder(
            FragmentProductBasketBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.absoluteAdapterPosition
            actionListener.onProductDetails(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ProductBasketViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductBasketViewHolder(private var binding: FragmentProductBasketBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(productBasket: ProductBasket){
                binding.productBasket = productBasket
                binding.buttonBasketDelete.setOnClickListener {
                    actionListener.onProductDelete(productBasket)
                }
            }
    }

    companion object{
        private val DiffCallback = object : DiffUtil.ItemCallback<ProductBasket>(){
            override fun areItemsTheSame(oldItem: ProductBasket, newItem: ProductBasket): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ProductBasket, newItem: ProductBasket): Boolean {
               // todo compare visible elements
                return  oldItem == newItem
            }

        }
    }



}

