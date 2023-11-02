package com.checker

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

class AppListProvider {
    fun getInstalledApps(context: Context): List<ApplicationInfo> {
        val packageManager = context.packageManager
        val queryFlags = PackageManager.MATCH_ALL or PackageManager.GET_META_DATA

        return packageManager.getInstalledApplications(queryFlags)
    }
}