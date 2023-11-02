package com.checker

import android.app.Application

class MyApplication : Application() {
    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        repository = Repository()
    }
}