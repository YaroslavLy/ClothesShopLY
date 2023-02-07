package com.example.clothesshop.data.userdata

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataSourceSharedPrefs @Inject constructor
 (
    @ApplicationContext appContext: Context
): UserDataSource {

    private val sharedPreferences = appContext.getSharedPreferences("userData", Context.MODE_PRIVATE)

    override fun saveUid(uid: String?) {
        val editor = sharedPreferences.edit()
        if (uid == null)
            editor.remove(PREF_CURRENT_ACCOUNT_UID)
        else
            editor.putString(PREF_CURRENT_ACCOUNT_UID, uid)
        editor.apply()
    }

    override fun getUid(): String? {
        return sharedPreferences.getString(PREF_CURRENT_ACCOUNT_UID, null)
    }

    companion object {
        private const val PREF_CURRENT_ACCOUNT_UID = "currentUID"
    }

}