package com.example.clothesshop.data

import android.util.Log
import android.widget.Toast
import com.example.clothesshop.model.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

// todo #8 create repo and source
class SignUpRepository {
    private lateinit var auth: FirebaseAuth

     fun signUp(username: String, password: String): Flow<Boolean> = callbackFlow {
        auth = Firebase.auth

        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //send email verification
                    if(auth.currentUser != null) {
                        auth.currentUser!!.sendEmailVerification()
                            .addOnSuccessListener {
                                Log.d("TAGGG", "Instructions Sent")
                                Firebase.auth.signOut()
                                trySend(true).isSuccess
                            }
                            .addOnFailureListener { e ->
                                //Toast.makeText(context, "Failed to send due to " + e.message, Toast.LENGTH_SHORT).show()
                            }
                    }
                    Firebase.auth.signOut()
                } else {
                    trySend(false).isFailure
                }
            }


         awaitClose {
             close()
         }
    }
}