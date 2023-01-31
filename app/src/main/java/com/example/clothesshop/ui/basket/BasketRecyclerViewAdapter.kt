package com.example.clothesshop.ui.basket

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.clothesshop.GlideApp
import com.example.clothesshop.R
import com.example.clothesshop.model.ProductBasket
import com.example.clothesshop.ui.tabs.TabsFragmentDirections


interface ProductBasketActionListener {

    fun onProductDelete(productBasket: ProductBasket)

    fun onProductDetails(productBasket: ProductBasket)

}

//todo #19 use ListAdapters
class ProductBasketDiffCallback(
    private val oldList: List<ProductBasket>,
    private val newList: List<ProductBasket>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProductBasket = oldList[oldItemPosition]
        val newProductBasket = newList[newItemPosition]
        return oldProductBasket.id == newProductBasket.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProductBasket = oldList[oldItemPosition]
        val newProductBasket = newList[newItemPosition]
        return oldProductBasket == newProductBasket
    }

}

class BasketRecyclerViewAdapter(private val actionListener: ProductBasketActionListener) :
    RecyclerView.Adapter<BasketRecyclerViewAdapter.ViewHolder>() {

    var productsBasket: List<ProductBasket> = emptyList()
        set(newValue) {
            val diffCallback = ProductBasketDiffCallback(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_product_basket, parent, false)
        return BasketRecyclerViewAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productBasket = productsBasket[position]

        holder.textName.text=productBasket.name
        holder.textPrice.text=productBasket.price

        GlideApp.with(holder.imageView.context)
            .load(productBasket.image)
            .error(R.drawable.ic_baseline_autorenew_24)
            .apply(RequestOptions.bitmapTransform( RoundedCorners(14)))
            .into(holder.imageView)

        holder.buttonBasket.setOnClickListener {
            actionListener.onProductDelete(productBasket)
        }

        holder.linearLayout.setOnClickListener {
            //actionListener.onProductDetails(productBasket)
            val action = productBasket.code?.let { it1 ->
                BasketFragmentDirections.actionBasketFragment2ToProductDetailsFragment2(it1)
            }
            if (action != null) {
               // holder.itemView.findNavController().navigate(action)
            }
            //Navigation.findNavController(it1).navigate(TabsFragmentDirections.actionTabsFragmentGraphToOrderFragment())
        }

    }

    override fun getItemCount(): Int {
        return productsBasket.size
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)

        val textName: TextView = itemView.findViewById(R.id.textName)
        val textPrice: TextView = itemView.findViewById(R.id.textPrice)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout)
        val buttonBasket: ImageView = itemView.findViewById(R.id.button_basket_delete)

    }

}

