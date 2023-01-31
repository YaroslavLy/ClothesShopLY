package com.example.clothesshop.ui.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clothesshop.databinding.FragmentBasketBinding
import com.example.clothesshop.model.ProductBasket
import com.example.clothesshop.ui.tabs.TabsFragmentDirections
import com.example.clothesshop.utils.parsers.PriceParser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasketFragment : Fragment() {
    private var data = ArrayList<ProductBasket>()

    private lateinit var adapter: BasketRecyclerViewAdapter

    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!

    private var countProductsInBasketmy =0

    private val basketViewModel by viewModels<BasketViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        _binding = FragmentBasketBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listProductBasket.layoutManager =
            LinearLayoutManager(context)

        //todo #18 use the transfer of functions to the constructor
        adapter = BasketRecyclerViewAdapter(object : ProductBasketActionListener {
            override fun onProductDelete(productBasket: ProductBasket) {
                basketViewModel.removeProductFromBasket(productBasket)
                data.remove(productBasket)
                updateData()
            }

            override fun onProductDetails(productBasket: ProductBasket) {
                val action = productBasket.code?.let { it1 ->
                    BasketFragmentDirections.actionBasketFragment2ToProductDetailsFragment2(it1)
                }
                if (action != null) {
                    Navigation.findNavController(view).navigate(action)
                }
            }
        })

        binding.listProductBasket.adapter = adapter

        basketViewModel.getProductsFromBasket()
        basketViewModel.basketProductFormState.observe(
            viewLifecycleOwner,
            Observer { productBasket ->
                if (productBasket == null) {
                    return@Observer
                }
                updateUiWithProduct(productBasket)
            })


        basketViewModel.tabsFormState.observe(viewLifecycleOwner, Observer { countProductsInBasket ->
            if (countProductsInBasket == null) {
                return@Observer
            }
            countProductsInBasketmy = countProductsInBasket
            updateData()
        })

        //todo move check if anonymous in repo
        binding.order.setOnClickListener {


            val navHostFragment = parentFragment as NavHostFragment?
            val parent = navHostFragment!!.parentFragment
            if (parent != null) {
                parent.view?.let { it1 -> Navigation.findNavController(it1).navigate(TabsFragmentDirections.actionTabsFragmentGraphToOrderFragment()) }
            }

        }
    }


    private fun updateData(){
        updatePriceData()
        updateListData()
    }

    private fun updatePriceData() {
        if (countProductsInBasketmy == 0) {
            binding.sumPrice.text = "0,0 ZŁ"
            binding.order.isEnabled = false
            return
        }

        val listPrice = mutableListOf<String>()
        for (s in data) {
            s.price?.let { listPrice.add(it) }
        }
        binding.sumPrice.text = PriceParser.sumPrise(listPrice)
        binding.order.isEnabled = true
    }

    private fun updateListData() {
        if (countProductsInBasketmy == 0)
            data = ArrayList<ProductBasket>()

        adapter.productsBasket = data.toMutableList()
    }

    private fun updateUiWithProduct(product: ProductBasket) {
        if (!data.contains(product)) {
            data.add(product)
            updateData()
        }
        binding.order.isEnabled = countProductsInBasketmy != 0
        // todo replace change background (bind to countProductsInBasketmy )
        //binding.listProductBasket.background = Color.WHITE.toDrawable()
    }

}














