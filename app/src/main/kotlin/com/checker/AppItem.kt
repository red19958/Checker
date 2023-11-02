package com.checker

import android.graphics.Bitmap

data class AppItem(
    val id: Int,
    val image: Bitmap,
    val name: String,
    val services: String,
    val providers: String,
    val receivers: String,
)