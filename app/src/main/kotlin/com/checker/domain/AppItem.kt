package com.checker.domain

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppItem(
    val id: Int,
    val image: Bitmap,
    val name: String,
    val services: String,
    val providers: String,
    val receivers: String,
): Parcelable