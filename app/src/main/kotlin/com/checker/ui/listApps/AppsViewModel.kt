package com.checker.ui.listApps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.checker.AppInfo
import com.checker.AppItem
import com.checker.MyApplication
import com.checker.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppsViewModel(private val app: MyApplication, private val repository: Repository) :
    ViewModel() {

    init {
        initApps()
    }

    private val _apps = MutableStateFlow(emptyList<AppItem>())

    internal val apps = _apps.asStateFlow()

    internal fun initApps() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getAllInstalledAppsAndEntryPointsWithIcons(app).collect {
                    _apps.emit(it.toAppItem())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun List<AppInfo>.toAppItem(): List<AppItem> {
        var counter = 0

        return this.map { item ->
            AppItem(
                counter++,
                item.image,
                item.name,
                item.services.joinToString(",\n") { it.name },
                item.providers.joinToString(",\n") { it.name },
                item.receivers.joinToString(",\n") { it.name },
            )
        }
    }
}
