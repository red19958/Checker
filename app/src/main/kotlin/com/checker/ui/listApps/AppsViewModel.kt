package com.checker.ui.listApps

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.checker.domain.AppInfo
import com.checker.domain.AppItem
import com.checker.domain.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppsViewModel(private val app: Application, private val repository: Repository) :
    ViewModel() {
    private val _apps = MutableStateFlow(emptyList<AppItem>())

    internal val apps = _apps.asStateFlow()


    private val _downloaded = MutableStateFlow(false)

    internal val downloaded = _downloaded.asStateFlow()

    init {
        initApps()
    }

    internal fun initApps() {
        _downloaded.value = false

        try {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getAllInstalledAppsAndEntryPointsWithIcons(app).collect {
                    _apps.emit(it.toAppItem())
                }
                _downloaded.value = true
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
                item.services.map { it.name },
                item.providers.map { it.name },
                item.receivers.map { it.name },
            )
        }
    }
}
