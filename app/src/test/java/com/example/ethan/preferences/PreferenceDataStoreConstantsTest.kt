package com.example.ethan.preferences

import androidx.datastore.preferences.core.stringPreferencesKey
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PreferenceDataStoreConstantsTest{
    private lateinit var preferenceDataStoreConstants: PreferenceDataStoreConstants

    @Before
    fun setUp(){
        preferenceDataStoreConstants = PreferenceDataStoreConstants
    }

    @Test
    fun validation(){
        var a = preferenceDataStoreConstants.TRANSPORTATION
        assertEquals(stringPreferencesKey("TRANSPORTATION_KEY"), a)
    }
}