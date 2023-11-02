package com.checker

import android.app.Application
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Repository {
    fun getAllInstalledAppsAndPermissionsWithIcons(app: Application): Flow<Map<String, Pair<String, Bitmap>>> =
        flow {
            val appsAndPermissionsWithIconsMap = mutableMapOf<String, Pair<String, Bitmap>>()
            val packageManager = app.applicationContext.packageManager
            val installedApps = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)

            for (packageInfo in installedApps) {
                val appName =
                    packageManager.getApplicationLabel(packageInfo.applicationInfo).toString()
                val permissions = packageInfo.requestedPermissions
                val appIcon = packageManager.getApplicationIcon(packageInfo.applicationInfo)

                if (permissions != null) {
                    appsAndPermissionsWithIconsMap[appName] =
                        Pair(permissions.joinToString(), appIcon.toBitmap())
                }
            }

            emit(appsAndPermissionsWithIconsMap)
        }.flowOn(Dispatchers.IO)
}