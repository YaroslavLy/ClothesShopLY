package com.example.clothesshop.data.product

import android.util.Log
import com.example.clothesshop.data.Resource
import com.example.clothesshop.data.Result
import com.example.clothesshop.model.Product
import com.example.clothesshop.model.ProductBasket
import com.example.clothesshop.utils.Constants
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
import java.lang.Exception
import javax.inject.Inject

class ProductSourceImp @Inject constructor() :ProductSource {
    override fun getProducts(path: String): Flow<Resource<Product>> = callbackFlow  {
        val fbDB = FirebaseDatabase.getInstance().getReference(Constants.COLLECTION_PRODUCTS+"$path")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSn in dataSnapshot.children) {
                    var item = dataSn.getValue(Product::class.java)
                    if (item != null) {

                        val product = Product(
                            image = item.image,
                            name = item.name,
                            price = item.price,
                            code = item.code,
                            inBasked = item.inBasked,
                            type = item.type,
                            description = item.description
                        )
                        trySend(Resource.Success<Product>(product)).isSuccess
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                trySend(Resource.Error(databaseError.toException().toString())).isFailure
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        fbDB.addValueEventListener(postListener)

        awaitClose {
            close()
        }
    }

    override fun getProduct(id: String, path: String): Flow<Resource<Product>> = callbackFlow{
        val fbDB = FirebaseDatabase.getInstance().getReference(Constants.COLLECTION_PRODUCTS+"$path")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSn in dataSnapshot.children) {
                    var item = dataSn.getValue(Product::class.java)
                    if (item != null) {

                        val product = Product(
                            image = item.image,
                            name = item.name,
                            price = item.price,
                            code = item.code,
                            inBasked = item.inBasked,
                            type = item.type,
                            description = item.description
                        )
                        if(product.code.equals(id)){
                            trySend(Resource.Success<Product>(product)).isSuccess
                        }
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                trySend(Resource.Error(databaseError.toException().toString())).isFailure
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
            }
        }
        fbDB.addValueEventListener(postListener)
        awaitClose {
            close()
        }
    }

    //todo add UserDataSource in constructor
    private lateinit var auth: FirebaseAuth
    override fun getProductsBasket(): Flow<Result<ProductBasket>> = callbackFlow {
        auth = Firebase.auth
        val user= auth.currentUser
        val userId = user?.uid
        //val userId = userDataSource.getUid()
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
                            code = item.code,//get key
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
}