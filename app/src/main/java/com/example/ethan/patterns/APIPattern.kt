package com.example.ethan.patterns

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class APIPattern : CoroutineScope{

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun start() {
        job = Job()
    }

    fun cancel() {
        if (::job.isInitialized) {
            job.cancel()
        }
    }
}