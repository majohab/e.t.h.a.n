package com.example.ethan.api.connectors

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SteamFriendsConnectorTest{

    private lateinit var steamFriendsConnector: SteamFriendsConnector

    @Before
    fun setUp(){
        steamFriendsConnector = SteamFriendsConnector()
    }

    @Test
    fun get(){
        val response = steamFriendsConnector.get("76561198198615839")



        println(response)
        assertNotEquals(null,response)
    }

}