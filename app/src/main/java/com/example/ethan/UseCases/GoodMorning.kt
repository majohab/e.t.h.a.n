package com.example.ethan.UseCases

import com.example.ethan.patterns.APIPattern
import kotlinx.coroutines.launch

class GoodMorning {
    public fun execute(){
        val coroutine = MyCoroutine()

        coroutine.start()

        coroutine.launch {
            coroutine.doGet()
        }

        // You can also cancel the coroutine if needed
        //coroutine.cancel()
    }
}