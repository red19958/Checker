package com.checker.domain

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppItem(
    val id: Int,
    val image: Bitmap,
    val name: String,
    val services: List<String>,
    val providers: List<String>,
    val receivers: List<String>,
): Parcelable