package com.checker

import android.content.pm.ActivityInfo
import android.content.pm.ProviderInfo
import android.content.pm.ServiceInfo
import android.graphics.Bitmap

data class AppInfo(
    val name: String,
    val image: Bitmap,
    val services: List<ServiceInfo>,
    val providers: List<ProviderInfo>,
    val receivers: List<ActivityInfo>,
)