package com.example.clothesshop.ui.order

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.clothesshop.databinding.FragmentOrderItemBinding
import com.example.clothesshop.model.OrderView

class MyOrderRecyclerViewAdapter(
    private val values: List<OrderView>
) : RecyclerView.Adapter<MyOrderRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentOrderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.orderId.text = item.id
        holder.orderSum.text = item.sumPrice
        if(item.ifPaid){
            holder.orderState.text = "Tak"
            holder.orderState.setTextColor(Color.parseColor("#008000"));
        }else
        {
            holder.orderState.text = "Nie"
            holder.orderState.setTextColor(Color.parseColor("#FF0000"));
        }

        holder.orderDate.text = item.dateOrder.toString()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
         val orderId = binding.textNumberOrder
         val orderSum = binding.textSumOrder
         val orderState = binding.textStateOrder
         val orderDate = binding.textDateOrder

    }

}