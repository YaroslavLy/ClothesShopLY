package com.example.clothesshop.data.login


import com.example.clothesshop.data.Result
import com.example.clothesshop.data.userdata.UserDataSource
import com.example.clothesshop.model.LoggedInUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import javax.inject.Inject

class LoginDataSource @Inject constructor(private val userDataSource: UserDataSource) {
    private lateinit var auth: FirebaseAuth

    fun login(username: String, password: String): Flow<Result<LoggedInUser>> = callbackFlow {

        auth = Firebase.auth
        auth.signInWithEmailAndPassword(username, password).addOnCompleteListener() { task ->
            if (!task.isSuccessful) {
                trySend(
                    Result.Error(Exception("Error login user data not valid"))
                ).isFailure
            }

            val user = auth.currentUser

            if ((user != null) && (user.isEmailVerified)) {
                userDataSource.saveUid(user.uid)
                val loggedInUser = LoggedInUser(user.uid, username)
                trySend(Result.Success<LoggedInUser>(loggedInUser)).isSuccess
            } else {
                trySend(
                    Result.Error(Exception("Error login not correct user data"))
                ).isFailure
            }
        }

        awaitClose {
            close()
        }

    }

    fun logout() {
        Firebase.auth.signOut()
    }
}