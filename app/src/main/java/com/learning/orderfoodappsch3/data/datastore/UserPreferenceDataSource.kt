package com.learning.orderfoodappsch3.data.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import com.learning.orderfoodappsch3.utils.PreferenceDataStoreHelper
import kotlinx.coroutines.flow.Flow


interface UserPreferenceDataSource {
    // Dark Mode Pref
    suspend fun getUserDarkModePref(): Boolean
    fun getUserDarkModePrefFlow(): Flow<Boolean>
    suspend fun setUserDarkModePref(isUsingDarkMode: Boolean)

    // Layout List Menu Grid Layout Pref
    suspend fun getListLayoutMenuPref(): Boolean
    fun getListLayoutMenuPrefFlow(): Flow<Boolean>
    suspend fun setListLayoutMenuPref(isLayoutGrid: Boolean)
}

class UserPreferenceDataSourceImpl(private val preferenceHelper: PreferenceDataStoreHelper) :
    UserPreferenceDataSource {

    // Dark Mode Pref
    override suspend fun getUserDarkModePref(): Boolean {
        return preferenceHelper.getFirstPreference(PREF_USER_DARK_MODE, false)
    }

    override fun getUserDarkModePrefFlow(): Flow<Boolean> {
        return preferenceHelper.getPreference(PREF_USER_DARK_MODE, false)
    }

    override suspend fun setUserDarkModePref(isUsingDarkMode: Boolean) {
        return preferenceHelper.putPreference(PREF_USER_DARK_MODE, isUsingDarkMode)
    }


    // Layout List Menu Grid Layout Pref
    override suspend fun getListLayoutMenuPref(): Boolean {
        return preferenceHelper.getFirstPreference(PREF_LIST_LAYOUT_MENU, false)
    }

    override fun getListLayoutMenuPrefFlow(): Flow<Boolean> {
        return preferenceHelper.getPreference(PREF_LIST_LAYOUT_MENU, false)
    }

    override suspend fun setListLayoutMenuPref(isLayoutGrid: Boolean) {
        return preferenceHelper.putPreference(PREF_LIST_LAYOUT_MENU, isLayoutGrid)
    }

    companion object {
        val PREF_USER_DARK_MODE = booleanPreferencesKey("PREF_USER_DARK_MODE")
        val PREF_LIST_LAYOUT_MENU = booleanPreferencesKey("PREF_LIST_LAYOUT_MENU")
    }
}
