package com.example.ethan.sharedprefs

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SharedPrefsTest{
    private lateinit var sharedPrefs: SharedPrefs
    @Before
    fun setUp(){
        sharedPrefs = SharedPrefs
    }

    @Test
    fun testGetTransportation(){
        val response = sharedPrefs.getTransportation()
        assertNotEquals(null, response)

    }

    @Test
    fun testGet(){
        val response = sharedPrefs.get("transportation")
        assertNotEquals(null, response)

    }

    @Test
    fun getString(){
        val response = sharedPrefs.getString("")
        assertNotEquals(null, response)
    }

    @Test
    fun contains(){
        val response = sharedPrefs.contains("")
        assertNotEquals(null, response)
    }

    @Test
    fun testGetBoolean(){
        val response = sharedPrefs.getBoolean("")
        assertNotEquals(null, response)
    }

    @Test
    fun testGetFloat(){
        val response = sharedPrefs.getFloat("")
        assertNotEquals(null, response)
    }
}