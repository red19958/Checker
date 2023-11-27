package com.checker

import android.app.Application
import com.checker.domain.Repository

class MyApplication : Application() {
    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        repository = Repository()
    }
}