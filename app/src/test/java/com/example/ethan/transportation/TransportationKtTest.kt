package com.example.ethan.transportation

import org.junit.Assert.*
import org.junit.Test

class TransportationKtTest{

    @Test
    fun getAllTranportationsKeysTest(){
        val response = getAllTransportationKeys()
        assertNotEquals(null, response)
    }

}