package com.example.clothesshop.data

import android.util.Log
import com.example.clothesshop.model.Order
import com.example.clothesshop.model.OrderView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

// todo #6 create repo and source
class OrderRepository {

    private lateinit var auth: FirebaseAuth

    fun save(order: Order) {
        auth = Firebase.auth
        val user= auth.currentUser
        val userId = user?.uid
        val v = FirebaseDatabase.getInstance().getReference("Orders/$userId")
        v.push().setValue(order)

        val firebaseDatabase = FirebaseDatabase.getInstance().getReference("Basket/$userId")
        for (productBasket in order.products!!) {
            productBasket.id?.let { firebaseDatabase.child(it).removeValue() }
        }

    }

    fun getOrders(path: String): Flow<Result<OrderView>> = callbackFlow {
        auth = Firebase.auth
        val user= auth.currentUser
        val userId = user?.uid
        val fbDB = FirebaseDatabase.getInstance().getReference("Orders/$userId")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSn in dataSnapshot.children) {
                    var item = dataSn.getValue(Order::class.java)
                    if (item != null) {
                        val product = item.products?.let {
                            item.ifPaid?.let { it1 ->
                                item.dateOrder?.let { it2 ->
                                    OrderView(
                                        id = dataSn.key,
                                        address = item.address,
                                        products = it,
                                        sumPrice = item.sumPrice,
                                        ifPaid = it1,
                                        dateOrder = it2
                                    )
                                }
                            }
                        }
                        product?.let { Result.Success<OrderView>(it) }
                            ?.let { trySend(it).isSuccess }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                trySend(Result.Error(databaseError.toException())).isFailure
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        fbDB.addValueEventListener(postListener)

        awaitClose {
            close()
        }
    }


}