package com.example.clothesshop.ui.tabs

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.clothesshop.R
import com.example.clothesshop.data.basket.BasketRepository
import com.example.clothesshop.data.basket.BasketSourceImp
import com.example.clothesshop.databinding.FragmentTabsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabsFragment : Fragment(R.layout.fragment_tabs) {

    private lateinit var binding: FragmentTabsBinding
    lateinit  var bottomNavigationView: BottomNavigationView


    private val tabsViewModel by viewModels<TabsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)


    }

    //todo if selected "Katalog" in bottom navigation view set in sharedprefs type to all
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTabsBinding.bind(view)
        val navHost = childFragmentManager.findFragmentById(R.id.tabsContainer) as NavHostFragment
        val navController = navHost.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        tabsViewModel.tabsFormState.observe(viewLifecycleOwner, Observer { countProductsInBasket ->
            if (countProductsInBasket == null) {
                return@Observer
            }
            updateBadge(countProductsInBasket)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_product_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== R.id.menu_logout)
        {
            Firebase.auth.signOut()
            findNavController().navigate(R.id.action_tabsFragment_to_loginFragment)
        }

        return super.onOptionsItemSelected(item)
    }


    fun updateBadge(count: Int) {
        bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)!!
        val badge = bottomNavigationView.getOrCreateBadge(R.id.basket_graph)
        badge.isVisible = true
        badge.number = count
        if(count == 0)
            badge.isVisible=false
        badge.backgroundColor = context?.let { ContextCompat.getColor(it, R.color.green) }!!
    }


}