package com.example.clothesshop.data.login


import com.example.clothesshop.data.Result
import com.example.clothesshop.data.UserDataSource
import com.example.clothesshop.model.LoggedInUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import java.io.IOException
import javax.inject.Inject

class LoginDataSource @Inject constructor(private val userDataSource: UserDataSource) {
    private lateinit var auth: FirebaseAuth

    // todo #3 return flow
    suspend  fun  login(username: String, password: String): Result<LoggedInUser> {
        try {
            auth = Firebase.auth
            auth.signInWithEmailAndPassword(username,password)
            // todo #2 remove delay
            delay(1000L)
            val user= auth.currentUser
            if(user != null) {
                if(user.isEmailVerified) {
                    userDataSource.saveUid(user.uid)
                    return Result.Success(
                        LoggedInUser(
                            user.uid,
                            username
                        )
                    )
                }else
                {
                    return Result.Error(IOException("Poczta nie jest potwierdzona"))
                }
            }else
            {
                return Result.Error(IOException("Error logging in"))
            }
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    // todo #4 add logout use data
    fun logout() {
        // TODO: revoke authentication
    }
}