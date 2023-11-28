package com.checker.ui.appPoints

import android.app.Application
import androidx.lifecycle.ViewModel
import com.checker.domain.AppItem
import com.checker.domain.PointItem
import com.checker.domain.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PointsViewModel(private val app: Application, private val repository: Repository) :
    ViewModel() {

    private val _points: MutableStateFlow<List<PointItem>> = MutableStateFlow(emptyList())

    internal val points = _points.asStateFlow()

    private var curAppItem: AppItem? = null

    internal fun extractPoints(appItem: AppItem) {
        if (curAppItem == appItem) {
            return
        }

        curAppItem = appItem

        _points.value =
            appItemContentToPointItems(appItem.services + appItem.providers + appItem.receivers)
    }

    private fun appItemContentToPointItems(content: List<String>): List<PointItem> {
        return content.map { PointItem(it, emptyList()) }
    }
}