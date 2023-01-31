package com.example.clothesshop.ui.more

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clothesshop.R


interface MoreItemActionListener {
    fun onMoreClick(moreItem: MoreItem)
}

class MoreRecyclerViewAdapter(private val actionListener: MoreItemActionListener):
    RecyclerView.Adapter<MoreRecyclerViewAdapter.ViewHolder>() {

    var moreList: List<MoreItem> = emptyList()
        set(newValue) {
            field = newValue
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_more_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemMore = moreList[position]

        itemMore.drawableId?.let { holder.image.setImageResource(it) }
        holder.text.text = itemMore.text
        //actionListener
        holder.layoutItem.setOnClickListener {
            actionListener.onMoreClick(itemMore)
        }
    }

    override fun getItemCount(): Int {
        return moreList.size
    }


    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)  {
          val image = itemView.findViewById<ImageView>(R.id.icon_more_item)
          val text  = itemView.findViewById<TextView>(R.id.text_more_item)
          val layoutItem  = itemView.findViewById<LinearLayout>(R.id.layout_item)
    }
}