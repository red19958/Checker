package com.checker

import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Repository {
    fun getAllInstalledAppsAndEntryPointsWithIcons(app: Application): Flow<List<AppInfo>> =
        flow {
            val appInfoMutableList = mutableListOf<AppInfo>()
            val packageManager = app.applicationContext.packageManager
            val installedApps = packageManager.getInstalledPackages(
                PackageManager.GET_SERVICES or
                        PackageManager.GET_PROVIDERS or
                        PackageManager.GET_RECEIVERS
            )

            for (packageInfo in installedApps) {
                val appName =
                    packageManager.getApplicationLabel(packageInfo.applicationInfo).toString()
                val services = packageInfo.services ?: arrayOf()
                val providers = packageInfo.providers ?: arrayOf()
                val receivers = packageInfo.receivers ?: arrayOf()

                Log.d("123", if (services.isEmpty()) "" else services[0].toString())
                val appIcon = packageManager.getApplicationIcon(packageInfo.applicationInfo)

                if(services.isNotEmpty() || providers.isNotEmpty() || receivers.isNotEmpty()) {
                    appInfoMutableList.add(
                        AppInfo(
                            appName,
                            appIcon.toBitmap(),
                            services.toList(),
                            providers.toList(),
                            receivers.toList(),
                        )
                    )
                }
            }

            emit(appInfoMutableList)
        }.flowOn(Dispatchers.IO)
}