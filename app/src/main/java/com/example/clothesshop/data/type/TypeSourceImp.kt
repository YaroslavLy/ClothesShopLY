package com.example.clothesshop.data.type

import android.util.Log
import com.example.clothesshop.data.Resource
import com.example.clothesshop.model.Type
import com.example.clothesshop.utils.Constants
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class TypeSourceImp @Inject constructor() :TypeSource {

    override fun getTypes(path: String): Flow<Resource<Type>> = callbackFlow {
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