package com.example.clothesshop.ui.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clothesshop.databinding.FragmentBasketBinding
import com.example.clothesshop.model.ProductBasket
import com.example.clothesshop.ui.tabs.TabsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BasketFragment : Fragment() {
    private lateinit var adapter: BasketRecyclerViewAdapter

    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!

    private val basketViewModel by viewModels<BasketViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = basketViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listProductBasket.layoutManager =
            LinearLayoutManager(context)


        adapter = BasketRecyclerViewAdapter(object : ProductBasketActionListener {
            override fun onProductDelete(productBasket: ProductBasket) {
                basketViewModel.removeProductFromBasket(productBasket)
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

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                basketViewModel.productsBasketState.collect(){
                    adapter.submitList(it)
                }
            }
        }

        binding.addProduct.setOnClickListener {
            // todo navigate to category graph
        }

        //todo move check if anonymous in repo
        binding.order.setOnClickListener {

            val navHostFragment = parentFragment as NavHostFragment?
            val parent = navHostFragment!!.parentFragment
            if (parent != null) {
                parent.view?.let { it1 -> Navigation.findNavController(it1).navigate(TabsFragmentDirections.actionTabsFragmentGraphToOrderFragment()) }
            }

        }
    }



}














