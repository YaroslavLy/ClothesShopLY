package com.example.clothesshop.ui.product

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clothesshop.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.clothesshop.databinding.FragmentCategoryBinding
import com.example.clothesshop.databinding.FragmentProductBinding
import com.example.clothesshop.databinding.FragmentProductListBinding
import com.example.clothesshop.model.Product
import com.example.clothesshop.model.ProductBasket
import com.example.clothesshop.ui.tabs.TabsFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProductFragment : Fragment() {

    private var columnCount = 2
    private lateinit var productViewModel: ProductViewModel
    private var data = ArrayList<Product>()

    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterCopy: ProductRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = 2
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.list.adapter?.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ProductFragmentArgs by navArgs()
        val type =  args.path
        productViewModel =
            ViewModelProvider(
                this,
                ProductViewModelFactory()
            )[ProductViewModel::class.java]


        val recyclerView = binding.list
        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapterCopy = ProductRecyclerViewAdapter(object : ProductItemActionListener {
                override fun onProductClickDetails(product: Product) {
                    val action = product.code?.let { it1 ->
                        ProductFragmentDirections.actionProductFragment3ToProductDetailsFragment4(
                            it1,type
                        )
                    }
                    if (action != null) {
                        findNavController().navigate(action)
                    }
                }

                override fun onProductClickBasket(product: Product,imageView: ImageView) { //TODO move to Repo
                    auth = Firebase.auth
                    val user = auth.currentUser
                    if(user!!.isAnonymous){
                        val text = "Nie Możesz dodać produkt do koszyka, ponieważ używasz konta anonimowego."
                        val duration = Toast.LENGTH_SHORT

                        val toast = Toast.makeText(context, text, duration)
                        toast.show()
                        PurchaseConfirmationDialogFragment(object : DialogActions{
                            override fun onClickOK() {
                                              Firebase.auth.signOut()
                                val navHostFragment = parentFragment as NavHostFragment?
                                val parent = navHostFragment!!.parentFragment
                                if (parent != null) {
                                    parent.view?.let { it1 -> Navigation.findNavController(it1).navigate(
                                        TabsFragmentDirections.actionTabsFragmentToLoginFragment()) }
                                }

                            }

                            override fun onClickNO() {
                                TODO("Not yet implemented")
                            }

                        })
                            .show(childFragmentManager, PurchaseConfirmationDialogFragment.TAG)
                        return
                    }
                    val userId = user?.uid
                    val firebaseDatabase =
                        FirebaseDatabase.getInstance().getReference("Basket/$userId")

                    //if (!ifElementInBasket(product)) {
                    if (!basketData.contains(product.toProductBasket())) {
                        val t1 = product.copy()
                        firebaseDatabase.push().setValue(t1)
                        imageView.setImageResource(R.drawable.ic_baseline_shopping_cart_24_2)
                    } else {
                        val t1 = product.copy()
                        // the best code
                        t1.code?.let { it1 ->
                            getItemByCode(it1).id?.let { it1 ->
                                firebaseDatabase.child(it1).removeValue()
                            }
                        }
                        basketData.remove(getItemByCode(t1.code!!))
                        imageView.setImageResource(R.drawable.ic_baseline_add_shopping_cart_24)
                    }
                }

            })
            adapterCopy.mList = data
            adapter = adapterCopy

        }

        binding.findButton.setOnClickListener {
            val text = binding.findProductEditText.text
            updateUiWithFilter(text.toString())
        }

        binding.findClearButton.setOnClickListener {
            adapterCopy.mList=data
            if (binding.list is RecyclerView) {
                with(binding.list) {
                    adapter?.notifyDataSetChanged()
                }
            }
        }


        productViewModel.getProduct(type)
        productViewModel.productFormState.observe(viewLifecycleOwner, Observer { productFormState ->
            if (productFormState == null) {
                return@Observer
            }
            productFormState?.let {
                updateUiWithProduct(it, view)
            }

        })
        productViewModel.getBasketProducts()
        productViewModel.productBasketFormState.observe(
            viewLifecycleOwner,
            Observer { productFormState ->
                if (productFormState == null) {
                    return@Observer
                }
                productFormState?.let {
                    updateUiWithProduct2(it, view)
                }

            })
    }

    private fun updateUiWithFilter(text: String) {
        if (text.isNullOrEmpty()) return
        val copy = data.filter { it.name!!.contains(text, ignoreCase = true) } as ArrayList<Product>
        Log.i("TAG0201",data.toString())
        adapterCopy.mList=copy

        if (binding.list is RecyclerView) {
            with(binding.list) {
                adapter?.notifyDataSetChanged()
            }
        }

    }

    private fun updateAdapter(view: View){
        if (view is RecyclerView) {
            with(view) {
                adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun updateUiWithProduct2(product: ProductBasket, view: View) {
        if (!basketData.contains(product)) {
            basketData.add(product)
            product.code?.let {
                Log.i("BasketTag", it)
            }
        }
        // todo binding.list
        if (view is RecyclerView) {
            with(view) {
                adapter?.notifyDataSetChanged()
            }
        }
    }


    private fun updateUiWithProduct(product: Product, view: View) {

        val productBasketm = ProductBasket(code = product.code)
        val productCopy: Product
        if (basketData.contains(productBasketm)) {
            productCopy = Product(
                product.image,
                product.name,
                product.price,
                product.code,
                true,
                product.type,
                product.description
            )
        } else {
            productCopy = Product(
                product.image,
                product.name,
                product.price,
                product.code,
                false,
                product.type,
                product.description
            )
        }
        if(!data.contains(productCopy)) {
            data.add(productCopy)

        }

        if (binding.list is RecyclerView) {
            with(binding.list) {
                adapter?.notifyDataSetChanged()
            }
        }
    }

    companion object {
        var basketData = ArrayList<ProductBasket>()
    }

    private fun getItemByCode(code: String): ProductBasket {
        for (productBasket in basketData) {
            if (productBasket.code.equals(code))
                return productBasket
        }
        return ProductBasket()
    }



}

interface DialogActions{
    fun onClickOK()
    fun onClickNO()
}

class PurchaseConfirmationDialogFragment(private val dialogActions:DialogActions) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage("Zaloguj się")
            .setPositiveButton("Ok") { _,_ ->
                dialogActions.onClickOK()
            }.setNegativeButton("Nie"){_,_->}
            .create()



    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }
}