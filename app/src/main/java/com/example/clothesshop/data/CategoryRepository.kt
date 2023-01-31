package com.example.clothesshop.data

import android.util.Log
import com.example.clothesshop.utils.Constants
import com.example.clothesshop.model.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

// todo #5 create repo and source
class CategoryRepository {

    fun getCategories() : Flow<Resource<Category>> = callbackFlow  {
        val fbDB = FirebaseDatabase.getInstance().getReference(Constants.COLLECTION_CATEGORY)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSn in dataSnapshot.children) {
                    var item = dataSn.getValue(Category::class.java)
                    if (item != null) {
                        val category=Category(
                            url_image = item.url_image,
                            name_folder = item.name_folder,
                            name_pl = item.name_pl
                        )
                        trySend(Resource.Success<Category>(category)).isSuccess
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
}