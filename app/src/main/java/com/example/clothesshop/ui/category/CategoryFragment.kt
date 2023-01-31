package com.example.clothesshop.ui.category

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.clothesshop.utils.Constants.KEY_FOLDER
import com.example.clothesshop.utils.Constants.SHARED_PREFS_CATEGORY
import com.example.clothesshop.GlideApp
import com.example.clothesshop.MainViewModel
import com.example.clothesshop.R
import com.example.clothesshop.databinding.FragmentCategoryBinding
import com.example.clothesshop.model.Category


class CategoryFragment : Fragment() {

    private var count = 0
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var categoryViewModel: CategoryViewModel
    private var listCategory = mutableListOf<Category>()
    private var _binding: FragmentCategoryBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        count = 0

        categoryViewModel =
            ViewModelProvider(
                this,
                CategoryViewModelFactory()
            )[CategoryViewModel::class.java]

        if (listCategory.isNotEmpty()) {
            for (category in listCategory) {
                updateUiWithCategories()
            }
        }
        categoryViewModel.categoryFormState.observe(
            viewLifecycleOwner,
            Observer { categoryFromState ->
                if (categoryFromState == null)
                    return@Observer

                categoryFromState.let {
                    listCategory.add(it)
                    updateUiWithCategories()
                }
            })
    }

    private fun updateUiWithCategories() {
        var category: RelativeLayout? = null
        when (count) {
            0 -> category = binding.category1
            1 -> category = binding.category2
            2 -> category = binding.category3
            3 -> category = binding.category4
            4 -> category = binding.category5
        }
        if (category != null) {
            setCategory(category, listCategory[count])
        }
        count++
    }

    private fun setCategory(categoryRelativeLayout: RelativeLayout, category: Category) {
        for (element in categoryRelativeLayout) {
            if (element is ImageView) {
                //todo move code load image
                GlideApp.with(requireContext())
                    .load(category.url_image)
                    .error(R.drawable.ic_baseline_autorenew_24)
                    .into(element)
            }
            if (element is TextView) {
                element.text = category.name_pl
            }
        }
        categoryRelativeLayout.setOnClickListener {
            //todo pass argument to graph catalog_graph to the TypeFragment (replace sharedPreferences)
//            val sharedPreferences =
//                context?.getSharedPreferences(SHARED_PREFS_CATEGORY, Context.MODE_PRIVATE)
//            sharedPreferences?.edit()?.putString(KEY_FOLDER, category.name_folder)?.apply()
            category.name_folder?.let { it1 -> mainViewModel.setCategory(it1) }
            findNavController().navigate(R.id.action_categoryFragment2_to_catalog_graph)
        }
    }

}