package com.example.clothesshop.data.singup

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SingUpSourceImp @Inject constructor() : SingUpSource {

    private lateinit var auth: FirebaseAuth
    override fun signUp(username: String, password: String): Flow<Boolean> = callbackFlow {
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