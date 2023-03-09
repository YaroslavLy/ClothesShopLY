package com.example.clothesshop.ui.product


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.clothesshop.GlideApp

import com.example.clothesshop.R
import com.example.clothesshop.model.Product
import com.example.clothesshop.model.ProductBasket
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

interface ProductItemActionListener {
    fun onProductClickDetails(product: Product)
    fun onProductClickBasket(product: Product,imageView: ImageView)
}

class ProductRecyclerViewAdapter(
    private val actionListener: ProductItemActionListener
    //private val mList: List<Product>,
    //private val mBasketList: MutableList<ProductBasket>,
    //private val view1: RecyclerView
) : RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder>() {

    var mList: List<Product> = emptyList()
        set(newValue) {
            field = newValue
        }

    lateinit var view: View
    var idProducts = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_product, parent, false)

        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val productItem = mList[position]

        holder.textName.text = productItem.name
        holder.textPrice.text = productItem.price

        GlideApp.with(holder.imageView.context)
            .load(productItem.image)
            .error(R.drawable.ic_baseline_autorenew_24)
            .into(holder.imageView)

        //click product details
        holder.linearLayout.setOnClickListener {
            actionListener.onProductClickDetails(productItem)
//            val action = productItem.code?.let { it1 ->
//                ProductFragmentDirections.actionProductFragment3ToProductDetailsFragment4(it1)
//            }
//            if (action != null) {
//                Navigation.findNavController(view = view1).navigate(action)
//            }
        }
        //if (ifElementInBasket(porductItem)) {
        if (productItem.inBasked == true) {
            holder.buttonBasket.setImageResource(R.drawable.ic_baseline_shopping_cart_24_2)
        }
//        else{
//            holder.buttonBasket.setImageResource(R.drawable.ic_baseline_add_shopping_cart_24)
//        }

        //click button button
        holder.buttonBasket.setOnClickListener {
            actionListener.onProductClickBasket(productItem,holder.buttonBasket)
//            //TODO move to Repo
//            auth = Firebase.auth
//            val user = auth.currentUser
//            val userId = user?.uid
//            val firebaseDatabase = FirebaseDatabase.getInstance().getReference("Basket/$userId")
//
//            //if (!ifElementInBasket(porductItem)) {
//            if (productItem.in_bascked == false) {
//                val t1 = productItem.copy()
//                firebaseDatabase.push().setValue(t1)
//                holder.buttonBasket.setImageResource(R.drawable.ic_baseline_shopping_cart_24_2)
//            } else {
//                val t1 = productItem.copy()
//                // the best code
//                t1.code?.let { it1 ->
//                    getItemByCode(it1).id?.let { it1 ->
//                        firebaseDatabase.child(it1).removeValue()
//                    }
//                }
//                ProductFragment.basketData.remove(getItemByCode(t1.code!!))
//                holder.buttonBasket.setImageResource(R.drawable.ic_baseline_add_shopping_cart_24)
//            }

        }

    }



//    private fun ifElementInBasket(product: Product): Boolean {
//        for (productBasket in mBasketList) {
//            Log.i("TAR", productBasket.code.toString())
//        }
//        for (productBasket in mBasketList) {
//            if (productBasket.code.equals(product.code))
//                return true
//        }
//        return false
//    }

    override fun getItemCount() = mList.size

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textName: TextView = itemView.findViewById(R.id.textName)
        val textPrice: TextView = itemView.findViewById(R.id.textPrice)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout)
        val buttonBasket: ImageView = itemView.findViewById(R.id.button_basket)

    }
}
