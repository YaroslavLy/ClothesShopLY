package com.example.clothesshop.ui.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.clothesshop.GlideApp
import com.example.clothesshop.R
import com.example.clothesshop.data.product.ProductRepository
import com.example.clothesshop.databinding.FragmentProductDetailsBinding
import com.example.clothesshop.model.Product
import com.example.clothesshop.model.ProductBasket
import com.example.clothesshop.ui.tabs.TabsFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment:Fragment(R.layout.fragment_product_details) {

    //todo #3 create check use recycler view

    private var _binding:FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val viewModel: ProductViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProductDetailsBinding.inflate(inflater,container,false);
        val view = binding.root;
        return view;
    }

    lateinit var id: String
    lateinit var path: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ProductDetailsFragmentArgs by navArgs()
        id =  args.id
        path = args.path


        // TODO replace
        //var viewModel = ProductViewModel(productRepository = ProductRepository(""))



        viewModel.getProduct(id=id,path=path)
        viewModel.productDetailsFormState.observe(viewLifecycleOwner, Observer { typeFormState ->
            if(typeFormState==null){
                return@Observer
            }
            typeFormState?.let {
                updateUiWithProduct(it,view)
            }

        })

        //todo
        //binding.details.text=args.id


    }

    //todo #6 remove
    private fun updateUiWithProduct(product: Product, view: View) {
        Log.i("Ok22", product.code.toString())
        Log.i("TAG0101", product.toString())
        if (product.code.equals(id)) {
            GlideApp.with(binding.imageview.context)
                .load(product.image)
                .error(R.drawable.ic_baseline_autorenew_24)
                .into(binding.imageview)
            binding.textName.text = product.name
            var data: List<String> = product.description?.split(";")?.map { it.trim() } ?: listOf()
            var text = product.description?.replace(
                ";",
                "\n--------------------------------------------------------------\n"
            )

            //var data = text.
            //Log.i("0801", data.size.toString() + " main")
            if(data.size==9){
                binding.details1.text = data[0]
                binding.price.text = product.price

                binding.details2.text =data[1]
                binding.details3.text = data[2]//.replaceFirstChar { it.uppercase() }+"."
                binding.details4.text = data[3]//.replaceFirstChar { it.uppercase() }+"."
                binding.details5.text = data[4]//.replaceFirstChar { it.uppercase() }+"."
                binding.details6.text = data[5]//.replaceFirstChar { it.uppercase() }

                binding.details7.text = data[6].replaceFirstChar { it.uppercase() }+"."
                binding.details8.text = data[7].replaceFirstChar { it.uppercase() }+"."
                binding.details9.text = data[8].replaceFirstChar { it.uppercase() }+"."
            }
            //binding.details.text = text
        }

        if(product.let { it1 -> ProductFragment.basketData.contains(it1.toProductBasket()) }){
            binding.button.text="Usuń z koszyka"

        }

        binding.button.setOnClickListener {
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
                return@setOnClickListener
            }
            val userId = user?.uid
            val firebaseDatabase =
                FirebaseDatabase.getInstance().getReference("Basket/$userId")

            //if (!ifElementInBasket(product)) {
            if (!product.let { it1 -> ProductFragment.basketData.contains(it1.toProductBasket()) }) {
                val t1 = product.copy()
                firebaseDatabase.push().setValue(t1)
                findNavController().popBackStack()
                //imageView.setImageResource(R.drawable.ic_baseline_shopping_cart_24_2)
            } else {
                val t1 = product.copy()
                // the best code
                t1.code?.let { it1 ->
                    getItemByCode(it1).id?.let { it1 ->
                        firebaseDatabase.child(it1).removeValue()
                    }
                }
                ProductFragment.basketData.remove(getItemByCode(t1.code!!))
                binding.button.text="Dodaj Do Koszyka"
                //imageView.setImageResource(R.drawable.ic_baseline_add_shopping_cart_24)
            }
        }
    }

    private fun getItemByCode(code: String): ProductBasket {
        for (productBasket in ProductFragment.basketData) {
            if (productBasket.code.equals(code))
                return productBasket
        }
        return ProductBasket()
    }
}