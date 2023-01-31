package com.example.clothesshop.ui.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.clothesshop.R
import com.example.clothesshop.data.OrderRepository
import com.example.clothesshop.model.OrderView

class OrderListFragment : Fragment() {

    private lateinit var orderViewModel: OrderViewModel
    private var data = ArrayList<OrderView>()

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_item_list, container, false)

        orderViewModel = OrderViewModel( OrderRepository())


        orderViewModel.getOrders()

        orderViewModel.ordersFormState.observe(viewLifecycleOwner, Observer { order ->
            if(order == null){
                return@Observer
            }
            data.add(order)
            updateData(view)
        })

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyOrderRecyclerViewAdapter(data)
            }
        }
        return view
    }

    private fun updateData(view: View) {
        if (view is RecyclerView) {
            with(view) {
                adapter?.notifyDataSetChanged()
            }
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            OrderListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}