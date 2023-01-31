package com.example.clothesshop.ui.more

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clothesshop.R
import com.example.clothesshop.databinding.FragmentMoreBinding
import com.example.clothesshop.databinding.FragmentOrderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MoreFragment : Fragment() {

    private lateinit var adapter: MoreRecyclerViewAdapter

    private val moreList =
        mutableListOf(
            MoreItem(1, R.drawable.ic_more_orders, "Historia Zamówień"),
            MoreItem(2, R.drawable.ic_more_help, "Pomoc i Kontakt"),
            MoreItem(3, R.drawable.ic_more_privavy_and_policy, "Polityka Prywatności"),
            MoreItem(4, R.drawable.ic_more_terms, "Zasady i Warunki")
        )

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var auth: FirebaseAuth
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.image.setImageResource(R.drawable.ic_baseline_account_box_24)

        // todo move to repo
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            binding.emailAccount.text = currentUser.email
        }

        binding.moreList.layoutManager = LinearLayoutManager(context)
        adapter= MoreRecyclerViewAdapter(
            object : MoreItemActionListener{
                override fun onMoreClick(moreItem: MoreItem) {
                    if(moreItem.id == 1){
                           findNavController().navigate(R.id.action_moreFragment_to_orderListFragment)
                    }
                    if(moreItem.id == 2){
                        findNavController().navigate(R.id.action_moreFragment_to_helpFragment)
                    }
                    if(moreItem.id == 3){
                        findNavController().navigate(R.id.action_moreFragment_to_privacyPolicyFragment)
                    }
                    if(moreItem.id == 4){
                        findNavController().navigate(R.id.action_moreFragment_to_termsAndConditionsFragment)
                    }
                }

            }
        )
        binding.moreList.adapter=adapter
        adapter.moreList = moreList.toMutableList()

    }
}
