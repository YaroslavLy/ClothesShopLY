package com.example.clothesshop.data

import android.util.Log
import com.example.clothesshop.utils.Constants
import com.example.clothesshop.model.Type
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

// todo #9 create repo and source
class TypeRepository(val path: String) {

    fun getTypes(path: String): Flow<Resource<Type>> = callbackFlow {
        var fbDB: DatabaseReference
        Log.i("taaaag", path)
        if (path.equals("all")) {
            fbDB = FirebaseDatabase.getInstance()
                .getReference(
                    Constants.COLLECTION_CATEGORY +
                            Constants.COLLECTION_TYPES + "/" + "All"
                )

        } else {
            fbDB = FirebaseDatabase.getInstance()
                .getReference(
                    Constants.COLLECTION_CATEGORY +
                            Constants.COLLECTION_TYPES + "/" + path
                )
        }
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSn in dataSnapshot.children) {
                    var item = dataSn.getValue(Type::class.java)
                    if (item != null) {
                        val type = Type(
                            name = item.name, urlImage = item.urlImage, nameFolder = item.nameFolder
                        )
                        trySend(Resource.Success<Type>(type)).isSuccess
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
