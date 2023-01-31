package com.example.clothesshop.ui.type


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.clothesshop.GlideApp
import com.example.clothesshop.R
import com.example.clothesshop.model.Type


interface TypeItemActionListener {
    fun onTypeClick(type: Type)
}

class TypeRecyclerViewAdapter(private val actionListener: TypeItemActionListener) :
    RecyclerView.Adapter<TypeRecyclerViewAdapter.ViewHolder>() {

    var typesList: List<Type> = emptyList()
        set(newValue) {
            field = newValue
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_type, parent, false)
        return TypeRecyclerViewAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val typeItem = typesList[position]

        holder.textName.text = typeItem.name
        //todo move code load image
        GlideApp.with(holder.imageView.context)
            .load(typeItem.urlImage)
            .error(R.drawable.ic_baseline_autorenew_24)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
            .into(holder.imageView)

        holder.linearLayout.setOnClickListener {
            actionListener.onTypeClick(typeItem)
        }
    }

    override fun getItemCount(): Int {
        return typesList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewType)
        val textName: TextView = itemView.findViewById(R.id.textViewType)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.linearType)

    }

}

