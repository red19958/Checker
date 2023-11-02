package com.checker.ui.listApps

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _apps = MutableStateFlow(
        listOf(
            AppItem(
                0,
                Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888),
                "",
                ""
            )
        )
    )

    val apps = _apps.asStateFlow()


    private fun initApps() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                repository.getAllInstalledAppsAndPermissionsWithIcons(app).collect {
                    _apps.emit(it.toAppItem())
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun Map<String, Pair<String, Bitmap>>.toAppItem(): List<AppItem> {
        var counter = 0

        return this.map {
            AppItem(
                counter++,
                it.value.second,
                it.key,
                it.value.first
            )
        }
    }
}
