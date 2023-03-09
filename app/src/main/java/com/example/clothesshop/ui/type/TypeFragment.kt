package com.example.clothesshop.ui.type

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clothesshop.R
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.clothesshop.MainViewModel
import com.example.clothesshop.databinding.FragmentTypeListBinding
import com.example.clothesshop.utils.Constants
import com.example.clothesshop.model.Type
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TypeFragment : Fragment() {

    //todo #4 add filter

    private var _binding: FragmentTypeListBinding? = null
    private val binding get() = _binding!!

    private lateinit var typeRecyclerViewAdapter: TypeRecyclerViewAdapter
    private val mainViewModel: MainViewModel by activityViewModels()

    private val typeViewModel: TypeViewModel by viewModels()
    private var data = ArrayList<Type>()
    private var checkedItem = 5
    private var checkItemName = "all"
    private var category = "All"
    private var isKom=false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)//
        _binding = FragmentTypeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // todo replace to true way read argument
//        val sharedPreferences = context?.getSharedPreferences(Constants.SHARED_PREFS_CATEGORY, Context.MODE_PRIVATE)
//        val type = sharedPreferences?.getString(KEY_FOLDER,"all") ?:"all"// "all"// args.arg
//        Log.i("Test0112",type.toString())
        //category = "All"



        mainViewModel.сategoryFormState.observe(viewLifecycleOwner, Observer { typeRes ->
            if (typeRes == null) {
                return@Observer

            }
            category = typeRes
            updateDialogData(category)
            data = mutableListOf<Type>() as ArrayList<Type>
            //


            //binding.list.adapter?.notifyDataSetChanged()
//            data = mutableListOf<Type>() as ArrayList<Type>
//            typeRecyclerViewAdapter.typesList = data.toMutableList()
//            binding.list.adapter?.notifyDataSetChanged()
            //Log.i("0801",data.toString()+"kom")
            isKom=true
            typeViewModel.getTypes(category)

        })
        binding.list.layoutManager = GridLayoutManager(context, 3)
        typeRecyclerViewAdapter = TypeRecyclerViewAdapter(object : TypeItemActionListener {
            override fun onTypeClick(type: Type) {
                val action = TypeFragmentDirections.actionTypeFragment2ToProductFragment3("/"+"$category"+"/"+"${type.nameFolder}")
                findNavController().navigate(action)
            }
        })
        binding.list.adapter = typeRecyclerViewAdapter

//        if(category=="All") {
//            //typeViewModel.getTypes("Women")
//            //showDialog()
//            //category="Women"
//        }
        typeViewModel.typeFormState.observe(viewLifecycleOwner, Observer { typeFormState ->
            if (typeFormState == null) {
                return@Observer
            }

            typeFormState?.let {
                updateUiWithProduct(it, binding.list)
            }

        })

        binding.toggleButton.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val array = arrayOf(
            Constants.WOMEN_PL, Constants.MEN_PL,
            Constants.BOY_PL, Constants.GIRL_PL,
            Constants.BABY_PL, Constants.ALL_PL
        )

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.title_filter_type))
            //.setTitle("Title")
            //.setMessage(resources.getString(R.string.supporting_text))
            .setSingleChoiceItems(array, checkedItem) { _, which ->
                // Get the dialog selected item index
                checkedItem = which
            }
            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to neutral button press
            }
            .setNegativeButton(resources.getString(R.string.decline)) { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                // Respond to positive button press
                updateUiFilter()

            }
            .show()
    }

    private fun updateUiFilter() {
        data = mutableListOf<Type>() as ArrayList<Type>
        typeRecyclerViewAdapter.typesList = data.toMutableList()
        binding.list.adapter?.notifyDataSetChanged()
        var myCategory="Women"
        when(checkedItem){
            0->{myCategory="Women"}
            1->{myCategory="Men"}
            2->{myCategory="Boy"}
            3->{myCategory="Girl"}
            4->{myCategory="Baby"}

        }
        typeViewModel.getTypes(myCategory)
        //
        category=myCategory
    }

    private fun updateDialogData(category: String) {
        when (category) {
            "Women" -> {
                checkedItem = 0
            }
            "Men" -> {
                checkedItem = 1
            }
            "Boy" -> {
                checkedItem = 2
            }
            "Girl" -> {
                checkedItem = 3
            }
            "Baby" -> {
                checkedItem = 4
            }
        }

    }

    // todo not product - type
    private fun updateUiWithProduct(product: Type, view: View) {
        Log.i("type23", product.name.toString())
        if (!data.contains(product))
            data.add(product)
        typeRecyclerViewAdapter.typesList = data.toMutableList()
        if (view is RecyclerView) {
            with(view) {
                adapter?.notifyDataSetChanged()
            }
        }
    }


}