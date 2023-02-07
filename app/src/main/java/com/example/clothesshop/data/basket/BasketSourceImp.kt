package com.example.clothesshop.data.basket

import android.util.Log
import com.example.clothesshop.data.Result
import com.example.clothesshop.data.userdata.UserDataSource
import com.example.clothesshop.model.Product
import com.example.clothesshop.model.ProductBasket
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import javax.inject.Inject


class BasketSourceImp @Inject constructor(private val userDataSource : UserDataSource) :BasketSource{

     override fun getProducts(): Flow<Result<ProductBasket>> = callbackFlow {

        val userId = userDataSource.getUid()
        if (userId == null) trySend(Result.Error(Exception("User Data Exception"))).isFailure
        val fbDB = FirebaseDatabase.getInstance().getReference("Basket/$userId")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSn in dataSnapshot.children) {
                    var item = dataSn.getValue(Product::class.java)
                    if (item != null) {
                        val product = ProductBasket(
                            id = dataSn.key,
                            image = item.image,
                            name = item.name,
                            price = item.price,
                            code = item.code,
                            inBasked = item.inBasked,
                            type = item.type,
                            description = item.description
                        )
                        trySend(Result.Success<ProductBasket>(product)).isSuccess
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

    override fun getCountProductsInBasket(): Flow<Result<Int>> = callbackFlow {
        val userId = userDataSource.getUid()

        if (userId == null) trySend(Result.Error(Exception("User not valid"))).isFailure

        val fbDB = FirebaseDatabase.getInstance().getReference("Basket/$userId")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val count = dataSnapshot.childrenCount

                trySend(Result.Success<Int>(count.toInt())).isSuccess
            }

            override fun onCancelled(databaseError: DatabaseError) {
                trySend(Result.Error(databaseError.toException())).isFailure
            }

        }
        fbDB.addValueEventListener(postListener)
        awaitClose {
            close()
        }
    }

    override fun removeProductInBasket(productBasket: ProductBasket) {
        val userId = userDataSource.getUid()
        if (userId != null) {
            val firebaseDatabase = FirebaseDatabase.getInstance().getReference("Basket/$userId")
            productBasket.id?.let { firebaseDatabase.child(it).removeValue() }
        }
    }
}