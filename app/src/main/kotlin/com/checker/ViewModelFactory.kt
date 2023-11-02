package com.checker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.checker.ui.listApps.AppsViewModel

class ViewModelFactory(private val app: MyApplication) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (modelClass) {
            AppsViewModel::class.java -> return AppsViewModel(app, app.repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
